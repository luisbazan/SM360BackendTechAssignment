package com.sm360.advertisement.dto;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingUpdateRequest {
	@NotNull
	private UUID id;
	@NotNull
	private UUID dealerId;
	@NotEmpty
	private String vehicle;
	@NotNull
	private Double price;
}
