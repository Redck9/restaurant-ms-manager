package com.redck.restaurantmsmanager.repository;

import com.redck.restaurantmsmanager.domain.Chair;
import com.redck.restaurantmsmanager.domain.Table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends MongoRepository<Table, Long>
{
    public Optional<Table> findByTableUid(String tableUid);

    public List<Table> findByRestaurantUid(String restaurantUid);

    public long countByRestaurantUid(String restaurantUid);

}
