package com.sm360.advertisement.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Listing {
	private UUID id;
	private Dealer dealer;
	private String vehicle;
	private Double price;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime publishedAt;
	private ListingState state;
}
