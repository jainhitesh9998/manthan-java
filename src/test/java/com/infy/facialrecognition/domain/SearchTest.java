package com.infy.facialrecognition.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.infy.facialrecognition.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Search.class);
        Search search1 = new Search();
        search1.setId(1L);
        Search search2 = new Search();
        search2.setId(search1.getId());
        assertThat(search1).isEqualTo(search2);
        search2.setId(2L);
        assertThat(search1).isNotEqualTo(search2);
        search1.setId(null);
        assertThat(search1).isNotEqualTo(search2);
    }
}
