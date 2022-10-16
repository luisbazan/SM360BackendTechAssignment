package com.sm360.advertisement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import com.sm360.advertisement.dto.DealerCreateRequest;
import com.sm360.advertisement.dto.ListingCreateRequest;
import com.sm360.advertisement.dto.ListingPublishRequest;
import com.sm360.advertisement.dto.ListingUnpublishRequest;
import com.sm360.advertisement.dto.ListingUpdateRequest;
import com.sm360.advertisement.exception.TierLimitHasBeenExceededException;
import com.sm360.advertisement.model.Dealer;
import com.sm360.advertisement.model.Listing;
import com.sm360.advertisement.model.ListingState;
import com.sm360.advertisement.repository.DealerRepository;
import com.sm360.advertisement.repository.ListingRepository;

import lombok.val;

@ExtendWith(MockitoExtension.class)
class VehicleAdvertisementServiceImplTest {

	@InjectMocks
	VehicleAdvertisementServiceImpl vehicleAdvertisementServiceImpl;
	
	@Mock
	public DealerRepository dealerRepository;
	
	@Mock
	public ListingRepository listingRepository;
	
	@Captor
	ArgumentCaptor<Dealer> dealerCaptor;
	
	@Captor
	ArgumentCaptor<Listing> listingCaptor;
	
	ModelMapper modelMapper;
	
	@BeforeEach
	void setUp() {
		this.modelMapper = new ModelMapper();
		ReflectionTestUtils.setField(vehicleAdvertisementServiceImpl, "modelMapper", modelMapper);
	}
	
	@Test
	void saveDealerTest() {
		val request = DealerCreateRequest.builder().name("Toyota Group").tierLimit(1).build();
		val response = vehicleAdvertisementServiceImpl.saveDealer(request);
		assertNotNull(response);
		verify(dealerRepository).save(dealerCaptor.capture());
		val dealerToSave = dealerCaptor.getValue();
		assertNotNull(dealerToSave);
		assertNotNull(dealerToSave.getId());
	}
	
	@Test
	void saveListingTest() {
		val uuid = UUID.randomUUID();
		val dealer = getDealers(uuid).get(0);
		val request = ListingCreateRequest.builder().dealerId(dealer.getId()).vehicle("Toyota").price(100.02).build();
		
		when(dealerRepository.findById(dealer.getId())).thenReturn(dealer);
		
		val response = vehicleAdvertisementServiceImpl.saveListing(request);
		assertNotNull(response);
		verify(listingRepository).save(listingCaptor.capture());
		val listingToSave = listingCaptor.getValue();
		assertNotNull(listingToSave);
		assertNotNull(listingToSave.getId());
		assertEquals(ListingState.draft, listingToSave.getState());
	}
	
	@Test
	void getAllDealersTest() {
		val uuid = UUID.randomUUID();
		when(dealerRepository.findAll()).thenReturn(getDealers(uuid));
		
		val response = vehicleAdvertisementServiceImpl.getAllDealers();
		assertNotNull(response);
		assertEquals(1, response.size());
	}
	
	@Test
	void updateListingTest() {
		val uuid = UUID.randomUUID();
		val dealer = getDealers(uuid).get(0);
		val listing = getListings(uuid, ListingState.draft).get(0);
		val request = ListingUpdateRequest.builder().id(listing.getId()).dealerId(dealer.getId()).vehicle("Toyota").price(100.00).build();
		
		when(listingRepository.findById(listing.getId())).thenReturn(listing);
		when(dealerRepository.findById(dealer.getId())).thenReturn(dealer);
		
		val response = vehicleAdvertisementServiceImpl.updateListing(request);
		assertNotNull(response);
		verify(listingRepository).save(listingCaptor.capture());
		val listingToSave = listingCaptor.getValue();
		assertNotNull(listingToSave);
	}
	
	@Test
	void getListingTest() {
		val uuid = UUID.randomUUID();
		val dealer = getDealers(uuid).get(0);
		when(listingRepository.findAll()).thenReturn(getListings(uuid, ListingState.draft));
		val response = vehicleAdvertisementServiceImpl.getListing(dealer.getId(), ListingState.draft);
		assertNotNull(response);
		assertEquals(1, response.size());
	}

	@ParameterizedTest
	@CsvSource({"true, true", "false, true", "true, false", "false, false"})
	void publishListingTest(boolean showError , boolean limitWasReached) {
		val uuid = UUID.randomUUID();
		val dealer = getDealers(uuid).get(0);
		val listing = getListings(uuid, limitWasReached ? ListingState.published : ListingState.draft).get(0);
		val request = ListingPublishRequest.builder().id(listing.getId()).showErrorLimitIsReached(showError).build();
		
		if(limitWasReached) {
			when(listingRepository.findAll()).thenReturn(getListings(uuid, ListingState.published));
		}
		when(listingRepository.findById(listing.getId())).thenReturn(listing);
		when(dealerRepository.findById(dealer.getId())).thenReturn(dealer);
		
		if(showError && limitWasReached) {
			assertThrows(TierLimitHasBeenExceededException.class, ()-> vehicleAdvertisementServiceImpl.publishListing(request));
		} else if(!showError && limitWasReached) {
			vehicleAdvertisementServiceImpl.publishListing(request);
			verify(listingRepository, atLeast(2)).save(any(Listing.class));
			
		} else {
			vehicleAdvertisementServiceImpl.publishListing(request);
			verify(listingRepository).save(listingCaptor.capture());
			val listingToSave = listingCaptor.getValue();
			assertNotNull(listingToSave);
		}
	}
	
	@Test
	void unpublishListingTest() {
		val uuid = UUID.randomUUID();
		val listing = getListings(uuid, ListingState.draft).get(0);
		val request = ListingUnpublishRequest.builder().id(listing.getId()).build();
		
		when(listingRepository.findById(listing.getId())).thenReturn(listing);
		
		vehicleAdvertisementServiceImpl.unpublishListing(request);
		verify(listingRepository).save(listingCaptor.capture());
		val listingToSave = listingCaptor.getValue();
		assertNotNull(listingToSave);
		assertEquals(ListingState.draft, listingToSave.getState());
	}
	
	private List<Dealer> getDealers(UUID id){
		return List.of(
				Dealer.builder().id(id).name("Toyota Group").tierLimit(1).build()
				);
	}
	
	private List<Listing> getListings(UUID dealerId, ListingState listingState){
		val dealer = getDealers(dealerId).get(0);
		return List.of(
				Listing.builder().id(UUID.randomUUID()).dealer(dealer).vehicle("Toyota").price(100.0).state(listingState).publishedAt(LocalDateTime.now()).build()
				);
	}
}
