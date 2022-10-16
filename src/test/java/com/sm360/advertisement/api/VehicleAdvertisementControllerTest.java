package com.sm360.advertisement.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sm360.advertisement.dto.DealerCreateRequest;
import com.sm360.advertisement.dto.ListingCreateRequest;
import com.sm360.advertisement.dto.ListingPublishRequest;
import com.sm360.advertisement.dto.ListingUnpublishRequest;
import com.sm360.advertisement.dto.ListingUpdateRequest;
import com.sm360.advertisement.model.ListingState;
import com.sm360.advertisement.service.VehicleAdvertisementService;

import lombok.val;

@ExtendWith(MockitoExtension.class)
class VehicleAdvertisementControllerTest {

	@InjectMocks
	private VehicleAdvertisementController vehicleAdvertisementController;
	
	@Mock
	private VehicleAdvertisementService vehicleAdvertisementService; 
	
	private MockMvc mockMvc;
	
	private ObjectMapper mapper;
	
	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(vehicleAdvertisementController).build();
		this.mapper = new ObjectMapper();
	}
	
	@Test
	void saveListingTest() throws Exception {
		val url = "/vehicle-advertisement/listing";
		val listingCreateRequest = ListingCreateRequest.builder()
				.dealerId(UUID.randomUUID())
				.vehicle("Toyota")
				.price(200.0)
				.build();
		
		val json = mapper.writeValueAsString(listingCreateRequest);
		
		val request = MockMvcRequestBuilders
				.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON);
				
		val response = mockMvc.perform(request)
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	void updateListingTest() throws Exception {
		val url = "/vehicle-advertisement/listing";
		val listingUpdateRequest = ListingUpdateRequest.builder()
				.id(UUID.randomUUID())
				.dealerId(UUID.randomUUID())
				.vehicle("Toyota")
				.price(200.0)
				.build();
		
		val json = mapper.writeValueAsString(listingUpdateRequest);
		
		val request = MockMvcRequestBuilders
				.put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON);
				
		val response = mockMvc.perform(request)
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	void getListingTest() throws Exception {
		val url = String.format("/vehicle-advertisement/listing?dealerId=%s&state=%s", UUID.randomUUID(), ListingState.draft);
		
		val request = MockMvcRequestBuilders
				.get(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
				
		val response = mockMvc.perform(request)
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	void publishListingTest() throws Exception {
		val url = "/vehicle-advertisement/listing/publish";
		val listingPublishRequest = ListingPublishRequest.builder()
				.id(UUID.randomUUID())
				.showErrorLimitIsReached(true)
				.build();
		
		val json = mapper.writeValueAsString(listingPublishRequest);
		
		val request = MockMvcRequestBuilders
				.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON);
				
		val response = mockMvc.perform(request)
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	void unpublishListingTest() throws Exception {
		val url = "/vehicle-advertisement/listing/unpublish";
		val listingUnpublishRequest = ListingUnpublishRequest.builder()
				.id(UUID.randomUUID())
				.build();
		
		val json = mapper.writeValueAsString(listingUnpublishRequest);
		
		val request = MockMvcRequestBuilders
				.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON);
				
		val response = mockMvc.perform(request)
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	void saveDealerTest() throws Exception {
		val url = "/vehicle-advertisement/dealer";
		val dealerCreateRequest = DealerCreateRequest.builder()
				.name("Toyota Group")
				.tierLimit(2)
				.build();
		
		val json = mapper.writeValueAsString(dealerCreateRequest);
		
		val request = MockMvcRequestBuilders
				.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON);
				
		val response = mockMvc.perform(request)
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	void getAllDealersTest() throws Exception {
		val url = "/vehicle-advertisement/dealer";
		
		val request = MockMvcRequestBuilders
				.get(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
				
		val response = mockMvc.perform(request)
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
}
