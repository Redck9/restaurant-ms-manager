package com.redck.restaurantmsmanager.service;

import com.redck.restaurantmsmanager.domain.Chair;
import com.redck.restaurantmsmanager.domain.Restaurant;
import com.redck.restaurantmsmanager.domain.Table;
import com.redck.restaurantmsmanager.logging.enumeration.LogTag;
import com.redck.restaurantmsmanager.repository.ChairRepository;
import com.redck.restaurantmsmanager.repository.RestaurantRepository;
import com.redck.restaurantmsmanager.repository.TableRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.redck.restaurantmsmanager.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantService
{
    private static final Logger LOGGER = new Logger(RestaurantService.class);
    private static final int DEFAULT_TABLES_CAPACITY = 1;

    private static final String DEFAULT_LOCATION = "Not Provided";
    private final RestaurantRepository restaurantRepository;



    @Autowired
    public RestaurantService(final RestaurantRepository restaurantRepository)
    {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Create Restaurant
     * @param restaurant restaurant
     * @return restaurant created
     */
    public Restaurant createRestaurant(final Restaurant restaurant)
    {
        final Optional<Restaurant> restaurantOptional = restaurantRepository.findByUid(restaurant.getUid());

        if(restaurantOptional.isEmpty())
        {
            LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.RESTAURANTS, LogTag.PERSISTED),
                        "Create Restaurant: " + restaurant.toString());

            restaurant.uid(UUID.randomUUID().toString());
            final Restaurant restaurantBuildEnriched = buildRestaurantInfo(restaurant);
            return restaurantRepository.save(restaurant);
        }
        throw new NullPointerException("Restaurant already exists!!");
    }

    private Restaurant buildRestaurantInfo(final Restaurant restaurant)
    {
        if (restaurant.getTablesCapacity() <= 0) {
            restaurant.tablesCapacity(DEFAULT_TABLES_CAPACITY);
        }

        if(restaurant.getLocation().isEmpty())
        {
            restaurant.location(DEFAULT_LOCATION);
        }

        return restaurant;

    }

    /**
     * Get Restaurant
     * @param restaurantId restaurant id.
     * @return restaurant retrieved
     */
    public Restaurant getRestaurant(final String restaurantId)
    {
        final Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId, "Restaurant does not exists!!");

        LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.RESTAURANTS, LogTag.RETRIEVED),
                "Get Restaurant by id: " + restaurantId + ": " + restaurantOptional.get().toString());

        return restaurantOptional.get();
    }




    /**
     * Edit Restaurant.
     * @param restaurantId restaurant id
     * @param restaurantNew restaurant new
     * @return restaurant edited
     */
    public Restaurant editRestaurant(final String restaurantId, final Restaurant restaurantNew)
    {
        final Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId,
                "Restaurant to be edited not exists!!!");

        LOGGER.info(MDC.get("correlationId"),  Arrays.asList(LogTag.RESTAURANTS, LogTag.EDITED),
                "Edit Restaurant by id " + restaurantId);

        restaurantOptional.get()
                .name(restaurantNew.getName())
                .location(restaurantNew.getLocation())
                .tablesCapacity(restaurantNew.getTablesCapacity());

        return restaurantRepository.save(restaurantOptional.get());
    }

    /**
     * Delete Restaurant
     * @param restaurantId restaurant id
     * @return restaurant deleted
     */
    public Restaurant deleteRestaurant(final String restaurantId)
    {
        final Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId, "Restaurant to be deleted not exists!!");

        restaurantRepository.delete(restaurantOptional.get());

        LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.RESTAURANTS, LogTag.DELETED),
                "Delete Restaurant by id: " + restaurantId);

        return restaurantOptional.get();
    }



    /**
     * Get All Restaurants.
     * @return List of Restaurants.
     */
    public List<Restaurant> getAllRestaurants()
    {
        final List<Restaurant> restaurantList = restaurantRepository.findAll();
        if(restaurantList.isEmpty())
        {
            throw new NullPointerException("No Restaurants persisted!!");
        }
        LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.RESTAURANTS, LogTag.RETRIEVED),
                    "Get All Restaurants from database");
        return restaurantList;

    }

    /**
     * Find Restaurant on Repository
     * @param restaurantId restaurant Id.
     * @param exceptionMessage exception Message
     * @return Optional of Restaurant
     */
    private Optional<Restaurant> getRestaurantById(final String restaurantId, final String exceptionMessage)
    {
        final Optional<Restaurant> restaurantOptional = restaurantRepository.findByUid(restaurantId);
        if(restaurantOptional.isEmpty())
        {
            throw new NullPointerException(exceptionMessage);
        }
        return restaurantOptional;
    }



}
