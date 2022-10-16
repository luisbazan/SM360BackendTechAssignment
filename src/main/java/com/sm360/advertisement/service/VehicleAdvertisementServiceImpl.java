package com.sm360.advertisement.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sm360.advertisement.dto.DealerCreateRequest;
import com.sm360.advertisement.dto.DealerCreateResponse;
import com.sm360.advertisement.dto.DealerGetResponse;
import com.sm360.advertisement.dto.ListingCreateRequest;
import com.sm360.advertisement.dto.ListingCreateResponse;
import com.sm360.advertisement.dto.ListingGetResponse;
import com.sm360.advertisement.dto.ListingPublishRequest;
import com.sm360.advertisement.dto.ListingUnpublishRequest;
import com.sm360.advertisement.dto.ListingUpdateRequest;
import com.sm360.advertisement.dto.ListingUpdateResponse;
import com.sm360.advertisement.exception.DealerAlreadyExistsException;
import com.sm360.advertisement.exception.DealerNotFoundException;
import com.sm360.advertisement.exception.ListingAlreadyExistsException;
import com.sm360.advertisement.exception.ListingNotFoundException;
import com.sm360.advertisement.exception.TierLimitHasBeenExceededException;
import com.sm360.advertisement.model.Dealer;
import com.sm360.advertisement.model.Listing;
import com.sm360.advertisement.model.ListingState;
import com.sm360.advertisement.repository.DealerRepository;
import com.sm360.advertisement.repository.ListingRepository;

import lombok.val;
import lombok.extern.slf4j.Slf4j;


/**
 * {@link VehicleAdvertisementService} implementation for managing listings for online advertising services.
 * @author Luis Bazan
 *
 */
@Service
@Slf4j
public class VehicleAdvertisementServiceImpl implements VehicleAdvertisementService {

	@Autowired
	public ListingRepository listingRepository;
	
	@Autowired
	public DealerRepository dealerRepository;
	
	@Autowired
	public ModelMapper modelMapper;
	
	/**
	 * Save listing, the following steps are executed:
	 * - Dealer must exists
	 * - valid if request exist (same dealerId, vehicle and price)
	 * - Assign ID (Random UUID)
	 * - Assign by default DRAFT status.
	 */
	@Override
	public ListingCreateResponse saveListing(ListingCreateRequest request) {
		val listing = modelMapper.map(request, Listing.class);
		val dealer = dealerRepository.findById(request.getDealerId());
		if(dealer == null) {
			throw new DealerNotFoundException(request.getDealerId());
		}
		validateIfRequestExist(request);
		listing.setCreatedAt(LocalDateTime.now());
		listing.setId(UUID.randomUUID());
		listing.setState(ListingState.draft);
		listing.setDealer(dealer);
		listingRepository.save(listing);
		val listingResponse = modelMapper.map(listing, ListingCreateResponse.class);
		return listingResponse;
	}

	/**
	 * Save dealer, the following steps are executed:
	 * - valid if dealer exist (same name)
	 * - Assign ID (Random UUID)
	 * - TierLimit is required in order to know if the listing can be published.
	 */
	@Override
	public DealerCreateResponse saveDealer(DealerCreateRequest request) {
		validateIfExistDealer(request);
		val dealer = modelMapper.map(request, Dealer.class);
		dealer.setId(UUID.randomUUID());
		dealerRepository.save(dealer);
		val dealerResponse = modelMapper.map(dealer, DealerCreateResponse.class);
		return dealerResponse;
	}

	@Override
	public List<DealerGetResponse> getAllDealers() {
		val dealers = dealerRepository.findAll();
		List<DealerGetResponse> dealersResponse = modelMapper.map(dealers, new TypeToken<List<DealerGetResponse>>() {}.getType());
		return dealersResponse;
	}

	@Override
	public ListingUpdateResponse updateListing(@Valid ListingUpdateRequest request) {
		val listing = listingRepository.findById(request.getId());
		if(listing == null) {
			throw new ListingNotFoundException(request.getId());
		}
		val dealer = dealerRepository.findById(request.getDealerId());
		if(dealer == null) {
			throw new DealerNotFoundException(request.getDealerId());
		}
		listing.setUpdatedAt(LocalDateTime.now());
		listing.setState(ListingState.draft);
		listing.setDealer(dealer);
		listing.setVehicle(request.getVehicle());
		listing.setPrice(request.getPrice());
		listingRepository.save(listing);
		val listingResponse = modelMapper.map(listing, ListingUpdateResponse.class);
		return listingResponse;
	}

	@Override
	public List<ListingGetResponse> getListing(UUID dealerId, ListingState state) {
		val response = listingRepository.findAll().stream().filter(item-> dealerId.equals(item.getDealer().getId())
				&& state.equals(item.getState())).collect(Collectors.toList());
		
		return modelMapper.map(response, new TypeToken<List<ListingGetResponse>>() {}.getType());
	}

	/**
	 * Publish listing, the following steps are executed:
	 * - valid if listing id exist
	 * - valid if leader id exist
	 * - if showErrorLimitIsReached is true and dealer's tier limit has been exceeded
	 * throw an error to client opposite case if only dealer's tier limit has been exceeded
	 * publish a listing, but unpublish the oldest listing of a dealer.
	 */
	@Override
	public void publishListing(ListingPublishRequest request) {
		Listing listing = listingRepository.findById(request.getId());
		if(listing == null) {
			throw new ListingNotFoundException(request.getId());
		}
		if(listing.getDealer() == null) {
			throw new DealerNotFoundException();
		}
		
		val dealer = dealerRepository.findById(listing.getDealer().getId());
		if(dealer == null) {
			throw new DealerNotFoundException(listing.getDealer().getId());
		}
		
		val total = totalListingPublished(listing.getDealer().getId(), listing.getId());
		
		if(total >= dealer.getTierLimit()) {
			if(request.isShowErrorLimitIsReached()) {
				throw new TierLimitHasBeenExceededException(dealer.getTierLimit());
			} else {
				val lastListing = getLastListingPublished(listing.getDealer().getId());
				lastListing.setState(ListingState.draft);
				listingRepository.save(lastListing);
				log.info("The previous listing was unpublished and the new was published");
			}
		}
		listing.setPublishedAt(LocalDateTime.now());
		listing.setState(ListingState.published);
		listingRepository.save(listing);
	}

	@Override
	public void unpublishListing(ListingUnpublishRequest request) {
		val listing = listingRepository.findById(request.getId());
		if(listing == null) {
			throw new ListingNotFoundException(request.getId());
		}
		listing.setPublishedAt(null);
		listing.setState(ListingState.draft);
		listingRepository.save(listing);
	}
	
	private void validateIfRequestExist(ListingCreateRequest request) {
		val listing = listingRepository.findAll();
		val result = listing.stream().anyMatch(item-> request.getDealerId().equals(item.getDealer().getId())
				&& request.getVehicle().equalsIgnoreCase(item.getVehicle())
				&& request.getPrice().equals(item.getPrice()));
		if(result) {
			throw new ListingAlreadyExistsException(request.getDealerId(), request.getVehicle(), request.getPrice());
		}
	}
	
	private long totalListingPublished(UUID dealerId, UUID id) {
		return listingRepository.findAll().stream().filter(item-> dealerId.equals(item.getDealer().getId())
				&& ListingState.published.equals(item.getState())
				&& !id.equals(item.getId())).count();
	}
	
	private Listing getLastListingPublished(UUID dealerId) {
		return listingRepository.findAll().stream().filter(item-> dealerId.equals(item.getDealer().getId())
				&& ListingState.published.equals(item.getState()) && item.getPublishedAt() != null)
				.sorted(Comparator.comparing(Listing::getPublishedAt).reversed())
				.findFirst().orElseThrow();
	}

	private void validateIfExistDealer(DealerCreateRequest request) {
		val exists = dealerRepository.findAll().stream().anyMatch(item-> item.getName().equalsIgnoreCase(request.getName()));
		if(exists) {
			throw new DealerAlreadyExistsException(request.getName());
		}
	}
}
