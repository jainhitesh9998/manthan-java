package com.infy.facialrecognition.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchMapperTest {

    private SearchMapper searchMapper;

    @BeforeEach
    public void setUp() {
        searchMapper = new SearchMapperImpl();
    }
}
