package com.sm360.advertisement.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.sm360.advertisement.model.Dealer;

/**
 * 
 * @author Luis Bazan
 *
 */
@Repository
public class DealerCacheRepository implements DealerRepository {

	Map<UUID, Dealer> map = new HashMap<UUID, Dealer>();
	
	@Override
	public void save(Dealer dealer) {
		map.put(dealer.getId(), dealer);
	}

	@Override
	public List<Dealer> findAll() {
		return map.values().stream().collect(Collectors.toList());
	}

	@Override
	public Dealer findById(UUID dealerId) {
		return map.get(dealerId);
	}

}
