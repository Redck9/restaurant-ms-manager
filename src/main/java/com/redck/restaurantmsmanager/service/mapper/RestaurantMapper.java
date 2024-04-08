package com.redck.restaurantmsmanager.service.mapper;

import com.redck.restaurantmsmanager.domain.Restaurant;
import com.redck.restaurantmsmanager.domain.Table;
import com.redck.restaurantmsmanager.service.model.RestaurantDTO;
import com.redck.restaurantmsmanager.service.model.TableDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    Restaurant mapRestaurantDTOToRestaurant(RestaurantDTO restaurantDTO);

    RestaurantDTO mapRestaurantToRestaurantDTO(Restaurant restaurant);

    List<Restaurant> mapRestaurantDTOListToRestaurantList(List<RestaurantDTO> restaurantDTO);

    List<RestaurantDTO> mapRestaurantListToRestaurantDTOList(List<Restaurant> restaurant);

}
