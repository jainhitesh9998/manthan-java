package com.infy.facialrecognition.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmbeddingsMapperTest {

    private EmbeddingsMapper embeddingsMapper;

    @BeforeEach
    public void setUp() {
        embeddingsMapper = new EmbeddingsMapperImpl();
    }
}
