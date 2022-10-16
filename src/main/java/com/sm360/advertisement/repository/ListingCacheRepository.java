package com.sm360.advertisement.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.sm360.advertisement.model.Listing;

/**
 * 
 * @author Luis Bazan
 *
 */
@Repository
public class ListingCacheRepository implements ListingRepository {

	Map<UUID, Listing> map = new HashMap<UUID, Listing>();
	
	@Override
	public void save(Listing listing) {
		map.put(listing.getId(), listing);
	}

	@Override
	public List<Listing> findAll() {
		return map.values().stream().collect(Collectors.toList());
	}

	@Override
	public Listing findById(UUID id) {
		return map.get(id);
	}

}
