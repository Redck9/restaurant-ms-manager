package com.redck.restaurantmsmanager.service.mapper;

import com.redck.restaurantmsmanager.domain.Chair;
import com.redck.restaurantmsmanager.domain.Restaurant;
import com.redck.restaurantmsmanager.domain.Table;
import com.redck.restaurantmsmanager.service.model.ChairDTO;
import com.redck.restaurantmsmanager.service.model.RestaurantDTO;
import com.redck.restaurantmsmanager.service.model.TableDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TableMapper
{
    Table mapTableDTOToTable(TableDTO tableDTO);

    TableDTO mapTableToTableDTO(Table table);

    List<Table> mapTableDTOListToTableList(List<TableDTO> tableDTO);

    List<TableDTO> mapTableListToTableDTOList(List<Table> table);
}
