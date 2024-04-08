package com.redck.restaurantmsmanager.repository;

import com.redck.restaurantmsmanager.domain.Chair;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChairRepository extends MongoRepository<Chair, Long>
{
    public Optional<Chair> findByChairUid(String chairUid);

    public List<Chair> findChairByTableUid(String tableUid);

    public long countByTableUid(String tableUid);

}
