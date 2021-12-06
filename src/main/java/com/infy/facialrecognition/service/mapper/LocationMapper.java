package com.infy.facialrecognition.service.mapper;

import com.infy.facialrecognition.domain.Location;
import com.infy.facialrecognition.service.dto.LocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonMapper.class })
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Mapping(target = "person", source = "person", qualifiedByName = "id")
    LocationDTO toDto(Location s);
}
