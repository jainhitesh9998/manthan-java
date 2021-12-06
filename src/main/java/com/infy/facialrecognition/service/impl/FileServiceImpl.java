package com.infy.facialrecognition.service.impl;

import com.infy.facialrecognition.domain.File;
import com.infy.facialrecognition.repository.FileRepository;
import com.infy.facialrecognition.service.FileService;
import com.infy.facialrecognition.service.dto.FileDTO;
import com.infy.facialrecognition.service.mapper.FileMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link File}.
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

    private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    public FileServiceImpl(FileRepository fileRepository, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    @Override
    public FileDTO save(FileDTO fileDTO) {
        log.debug("Request to save File : {}", fileDTO);
        File file = fileMapper.toEntity(fileDTO);
        file = fileRepository.save(file);
        return fileMapper.toDto(file);
    }

    @Override
    public Optional<FileDTO> partialUpdate(FileDTO fileDTO) {
        log.debug("Request to partially update File : {}", fileDTO);

        return fileRepository
            .findById(fileDTO.getId())
            .map(existingFile -> {
                fileMapper.partialUpdate(existingFile, fileDTO);

                return existingFile;
            })
            .map(fileRepository::save)
            .map(fileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> findAll() {
        log.debug("Request to get all Files");
        return fileRepository.findAll().stream().map(fileMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileDTO> findOne(Long id) {
        log.debug("Request to get File : {}", id);
        return fileRepository.findById(id).map(fileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete File : {}", id);
        fileRepository.deleteById(id);
    }
}
