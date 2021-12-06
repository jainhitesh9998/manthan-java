package com.infy.facialrecognition.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.infy.facialrecognition.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmbeddingsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmbeddingsDTO.class);
        EmbeddingsDTO embeddingsDTO1 = new EmbeddingsDTO();
        embeddingsDTO1.setId(1L);
        EmbeddingsDTO embeddingsDTO2 = new EmbeddingsDTO();
        assertThat(embeddingsDTO1).isNotEqualTo(embeddingsDTO2);
        embeddingsDTO2.setId(embeddingsDTO1.getId());
        assertThat(embeddingsDTO1).isEqualTo(embeddingsDTO2);
        embeddingsDTO2.setId(2L);
        assertThat(embeddingsDTO1).isNotEqualTo(embeddingsDTO2);
        embeddingsDTO1.setId(null);
        assertThat(embeddingsDTO1).isNotEqualTo(embeddingsDTO2);
    }
}
