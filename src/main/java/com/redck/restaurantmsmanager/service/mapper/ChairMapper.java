package com.redck.restaurantmsmanager.service.mapper;

import com.redck.restaurantmsmanager.domain.Chair;
import com.redck.restaurantmsmanager.domain.Table;
import com.redck.restaurantmsmanager.service.model.ChairDTO;
import com.redck.restaurantmsmanager.service.model.TableDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChairMapper
{
    Chair mapChairDTOToChair(ChairDTO chairDTO);

    ChairDTO mapChairToChairDTO(Chair chair);

    List<Chair> mapChairDTOListToChairList(List<ChairDTO> chairDTO);

    List<ChairDTO> mapChairListToChairDTOList(List<Chair> chair);

}
