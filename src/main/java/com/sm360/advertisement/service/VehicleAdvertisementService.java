package com.sm360.advertisement.service;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

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
import com.sm360.advertisement.model.ListingState;

/**
 * Defines a contract to handle all services of a vehicle advertisement
 * @author Luis Bazan
 *
 */
public interface VehicleAdvertisementService {

	/**
	 * Create a listing as of {@link ListingCreateRequest}
	 * @param request
	 * @return a {@link DealerCreateResponse}
	 */
	ListingCreateResponse saveListing(ListingCreateRequest request);

	/**
	 * Create a dealer as of {@link DealerCreateRequest}
	 * @param request
	 * @return a {@link DealerCreateResponse}
	 */
	DealerCreateResponse saveDealer(DealerCreateRequest request);

	/**
	 * Get a list of dealers
	 * @return a list of {@link DealerGetResponse}
	 */
	List<DealerGetResponse> getAllDealers();

	/**
	 * Update a listing as of {@link ListingUpdateRequest}
	 * @param request
	 * @return
	 */
	ListingUpdateResponse updateListing(@Valid ListingUpdateRequest request);

	/**
	 * Get a list of listing by dealerId and state 
	 * @param dealerId {@link UUID}
	 * @param state {@link ListingState}
	 * @return a list of {@link ListingGetResponse}
	 */
	List<ListingGetResponse> getListing(UUID dealerId, ListingState state);

	/**
	 * Publish a listing as of {@link ListingPublishRequest}
	 * @param request
	 */
	void publishListing(ListingPublishRequest request);

	/**
	 * Unpublish a listing as of {@link ListingUnpublishRequest}
	 * @param request
	 */
	void unpublishListing(ListingUnpublishRequest request);
	
}
