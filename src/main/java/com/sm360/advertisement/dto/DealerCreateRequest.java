package com.sm360.advertisement.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealerCreateRequest {
	@NotEmpty
	private String name;
	@NotNull
	@Min(1)
	private Integer tierLimit;
}
