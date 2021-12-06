package com.infy.facialrecognition.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.infy.facialrecognition.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SearchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchDTO.class);
        SearchDTO searchDTO1 = new SearchDTO();
        searchDTO1.setId(1L);
        SearchDTO searchDTO2 = new SearchDTO();
        assertThat(searchDTO1).isNotEqualTo(searchDTO2);
        searchDTO2.setId(searchDTO1.getId());
        assertThat(searchDTO1).isEqualTo(searchDTO2);
        searchDTO2.setId(2L);
        assertThat(searchDTO1).isNotEqualTo(searchDTO2);
        searchDTO1.setId(null);
        assertThat(searchDTO1).isNotEqualTo(searchDTO2);
    }
}
