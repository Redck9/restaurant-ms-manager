package com.redck.restaurantmsmanager.service.mapper;

import com.redck.restaurantmsmanager.domain.Restaurant;
import com.redck.restaurantmsmanager.domain.Table;
import com.redck.restaurantmsmanager.service.model.RestaurantDTO;
import com.redck.restaurantmsmanager.service.model.TableDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MenuItemMapper.class, ScheduleMapper.class})
public interface RestaurantMapper {

    @Mapping(target = "menuItem", source = "menuItem")
    @Mapping(target = "schedule", source = "schedule")
    Restaurant mapRestaurantDTOToRestaurant(RestaurantDTO restaurantDTO);

    @Mapping(target = "menuItem", source = "menuItem")
    @Mapping(target = "schedule", source = "schedule")
    RestaurantDTO mapRestaurantToRestaurantDTO(Restaurant restaurant);

    @Mapping(target = "menuItem", source = "menuItem")
    @Mapping(target = "schedule", source = "schedule")
    List<Restaurant> mapRestaurantDTOListToRestaurantList(List<RestaurantDTO> restaurantDTO);

    @Mapping(target = "menuItem", source = "menuItem")
    @Mapping(target = "schedule", source = "schedule")
    List<RestaurantDTO> mapRestaurantListToRestaurantDTOList(List<Restaurant> restaurant);

}
