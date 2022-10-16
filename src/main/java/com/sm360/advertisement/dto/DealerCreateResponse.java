package com.sm360.advertisement.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DealerCreateResponse {
	private UUID id;
	private String name;
	private int tierLimit;
}
