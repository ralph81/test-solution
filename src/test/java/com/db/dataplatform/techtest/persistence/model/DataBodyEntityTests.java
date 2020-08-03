package com.db.dataplatform.techtest.persistence.model;

import static com.db.dataplatform.techtest.TestDataHelper.DUMMY_DATA;
import static com.db.dataplatform.techtest.TestDataHelper.TEST_NAME;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataBodyEntity;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;


@RunWith(MockitoJUnitRunner.class)
public class DataBodyEntityTests {

    @Test
    public void assignDataBodyEntityFieldsShouldWorkAsExpected() {
        Instant expectedTimestamp = Instant.now();

        DataHeaderEntity dataHeaderEntity = new DataHeaderEntity();
        dataHeaderEntity.setName(TEST_NAME);
        dataHeaderEntity.setBlocktype(BlockTypeEnum.BLOCKTYPEA);
        dataHeaderEntity.setCreatedTimestamp(expectedTimestamp);

        DataBodyEntity dataBodyEntity = createTestDataBodyEntity(dataHeaderEntity);

        assertThat(dataBodyEntity.getDataHeaderEntity()).isNotNull();
        assertThat(dataBodyEntity.getDataBody()).isNotNull();
        assertThat(dataBodyEntity.getDataHeaderEntity()).isEqualTo(dataHeaderEntity);
        assertThat(dataBodyEntity.getDataBody()).isEqualTo(DUMMY_DATA);
       
    }
}
