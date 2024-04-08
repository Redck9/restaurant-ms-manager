package com.redck.restaurantmsmanager.repository;

import com.redck.restaurantmsmanager.domain.Restaurant;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, Long> {

    public Optional<Restaurant> findByUid(String uid);

}

