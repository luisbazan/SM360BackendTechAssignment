package com.sm360.advertisement.repository;

import java.util.List;
import java.util.UUID;

import com.sm360.advertisement.model.Listing;

public interface ListingRepository {

	void save(Listing listing);

	List<Listing> findAll();

	Listing findById(UUID id);
	
}
