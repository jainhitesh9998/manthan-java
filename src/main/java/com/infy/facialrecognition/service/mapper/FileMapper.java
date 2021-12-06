package com.infy.facialrecognition.service.mapper;

import com.infy.facialrecognition.domain.File;
import com.infy.facialrecognition.service.dto.FileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link File} and its DTO {@link FileDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FileMapper extends EntityMapper<FileDTO, File> {}
