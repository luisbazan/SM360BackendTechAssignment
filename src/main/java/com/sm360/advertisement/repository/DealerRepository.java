package com.sm360.advertisement.repository;

import java.util.List;
import java.util.UUID;

import com.sm360.advertisement.model.Dealer;

public interface DealerRepository {

	void save(Dealer dealer);

	List<Dealer> findAll();

	Dealer findById(UUID dealerId);

}
