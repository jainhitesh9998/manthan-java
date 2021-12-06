package com.infy.facialrecognition.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.infy.facialrecognition.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmbeddingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Embeddings.class);
        Embeddings embeddings1 = new Embeddings();
        embeddings1.setId(1L);
        Embeddings embeddings2 = new Embeddings();
        embeddings2.setId(embeddings1.getId());
        assertThat(embeddings1).isEqualTo(embeddings2);
        embeddings2.setId(2L);
        assertThat(embeddings1).isNotEqualTo(embeddings2);
        embeddings1.setId(null);
        assertThat(embeddings1).isNotEqualTo(embeddings2);
    }
}
