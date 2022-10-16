package com.sm360.advertisement.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sm360.advertisement.model.ListingState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingGetResponse {
	private UUID id;
	private DealerResponse dealer;
	private String vehicle;
	private Double price;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private ListingState state;
	
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class DealerResponse {
		private UUID id;
		private String name; 
	}
}
