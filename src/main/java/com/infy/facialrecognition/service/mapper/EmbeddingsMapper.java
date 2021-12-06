package com.infy.facialrecognition.service.mapper;

import com.infy.facialrecognition.domain.Embeddings;
import com.infy.facialrecognition.service.dto.EmbeddingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Embeddings} and its DTO {@link EmbeddingsDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonMapper.class })
public interface EmbeddingsMapper extends EntityMapper<EmbeddingsDTO, Embeddings> {
    @Mapping(target = "person", source = "person", qualifiedByName = "id")
    EmbeddingsDTO toDto(Embeddings s);
}
