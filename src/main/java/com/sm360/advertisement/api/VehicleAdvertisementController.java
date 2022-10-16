package com.sm360.advertisement.api;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.sm360.advertisement.service.VehicleAdvertisementService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * Rest Controller to provide the list of apis for vehicle advertisement
 * @author Luis Bazan
 *
 */
@RestController
@RequestMapping("vehicle-advertisement")
@Slf4j
@Validated
public class VehicleAdvertisementController {
	
	@Autowired
	public VehicleAdvertisementService vehicleAdvertisementService;
	
	@Operation(summary = "Save a listing but you must enter a valid dealer id.")
	@PostMapping("listing")
	public ResponseEntity<ListingCreateResponse> saveListing(@RequestBody @Valid final ListingCreateRequest request)  {
		log.info("VehicleAdvertisementController#saveListing start");
		val response = vehicleAdvertisementService.saveListing(request);
		log.info("VehicleAdvertisementController#saveListing end");
		return ResponseEntity.ok(response);
    }
	
	@Operation(summary = "Update a listing, you must enter a valid id in order to update.")
	@PutMapping("listing")
	public ResponseEntity<ListingUpdateResponse> updateListing(@RequestBody @Valid final ListingUpdateRequest request)  {
		log.info("VehicleAdvertisementController#updateListing start");
		val response = vehicleAdvertisementService.updateListing(request);
		log.info("VehicleAdvertisementController#updateListing end");
		return ResponseEntity.ok(response);
    }
	
	@Operation(summary = "Get a listing by dealerId and state.")
	@GetMapping("listing")
	public ResponseEntity<List<ListingGetResponse>> getListing(@RequestParam final UUID dealerId, @RequestParam final ListingState state)  {
		log.info("VehicleAdvertisementController#getListing start");
		val response = vehicleAdvertisementService.getListing(dealerId, state);
		log.info("VehicleAdvertisementController#getListing end");
		return ResponseEntity.ok(response);
    }
	
	@Operation(summary = "Publish a listing.")
	@PostMapping("listing/publish")
	public ResponseEntity<String> publishListing(@RequestBody @Valid final ListingPublishRequest request)  {
		log.info("VehicleAdvertisementController#publishListing start");
		vehicleAdvertisementService.publishListing(request);
		log.info("VehicleAdvertisementController#publishListing end");
		return ResponseEntity.ok("It was published");
    }
	
	@Operation(summary = "Unpublish a listing.")
	@PostMapping("listing/unpublish")
	public ResponseEntity<String> unpublishListing(@RequestBody @Valid final ListingUnpublishRequest request)  {
		log.info("VehicleAdvertisementController#unpublishListing start");
		vehicleAdvertisementService.unpublishListing(request);
		log.info("VehicleAdvertisementController#unpublishListing end");
		return ResponseEntity.ok("It was unpublished");
    }
	
	@Operation(summary = "Save a dealer.")
	@PostMapping("dealer")
	public ResponseEntity<DealerCreateResponse> saveDealer(@RequestBody @Valid final DealerCreateRequest request)  {
		log.info("VehicleAdvertisementController#saveDealer start");
		val response = vehicleAdvertisementService.saveDealer(request);
		log.info("VehicleAdvertisementController#saveDealer end");
		return ResponseEntity.ok(response);
    }
	
	@Operation(summary = "Get all dealers.")
	@GetMapping("dealer")
	public ResponseEntity<List<DealerGetResponse>> getAllDealers()  {
		log.info("VehicleAdvertisementController#getAllDealers start");
		val response = vehicleAdvertisementService.getAllDealers();
		log.info("VehicleAdvertisementController#getAllDealers end");
		return ResponseEntity.ok(response);
    }
}
