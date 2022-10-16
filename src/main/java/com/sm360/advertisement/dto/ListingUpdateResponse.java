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
public class ListingUpdateResponse {
	private UUID id;
	private UUID dealerId;
	private String vehicle;
	private Double price;
	private LocalDateTime updatedAt;
	private ListingState state;
}
