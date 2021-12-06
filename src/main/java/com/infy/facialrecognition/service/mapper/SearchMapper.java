package com.infy.facialrecognition.service.mapper;

import com.infy.facialrecognition.domain.Search;
import com.infy.facialrecognition.service.dto.SearchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Search} and its DTO {@link SearchDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SearchMapper extends EntityMapper<SearchDTO, Search> {}
