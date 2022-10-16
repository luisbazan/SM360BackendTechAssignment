package com.sm360.advertisement.dto;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingPublishRequest {
	@NotNull
	private UUID id;
	@NotNull
	private boolean showErrorLimitIsReached;
}
