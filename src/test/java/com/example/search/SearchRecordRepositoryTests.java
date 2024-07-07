package com.example.search;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class SearchRecordRepositoryTests {

    @Autowired
    private SearchRecordRepository searchRecordRepository;

    @Test
    public void testSaveAndFindAll() {
        SearchRecord record = new SearchRecord();
        record.setUsername("TylerTest");
        record.setSearchTerm("TylerTest");
        record.setResultCount(10);

        searchRecordRepository.save(record);

        List<SearchRecord> records = searchRecordRepository.findAll();
        assertThat(records).hasSize(1);
        assertThat(records.get(0).getUsername()).isEqualTo("TylerTest");
        assertThat(records.get(0).getSearchTerm()).isEqualTo("TylerTest");
        assertThat(records.get(0).getResultCount()).isEqualTo(10);
    }
}