package com.db.dataplatform.techtest.service;

import static com.db.dataplatform.techtest.TestDataHelper.TEST_NAME;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataHeaderEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.persistence.repository.DataHeaderRepository;
import com.db.dataplatform.techtest.server.service.DataHeaderService;
import com.db.dataplatform.techtest.server.service.impl.DataHeaderServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class DataHeaderServiceTests {

    @Mock
    private DataHeaderRepository dataHeaderRepositoryMock;

    private DataHeaderService dataHeaderService;
    private DataHeaderEntity expectedDataHeaderEntity;

    @Before
    public void setup() {
        expectedDataHeaderEntity = createTestDataHeaderEntity(Instant.now());

        dataHeaderService = new DataHeaderServiceImpl(dataHeaderRepositoryMock);
    }

    @Test
    public void shouldSaveDataHeaderEntityAsExpected(){
        dataHeaderService.saveHeader(expectedDataHeaderEntity);

        verify(dataHeaderRepositoryMock, times(1))
                .save(eq(expectedDataHeaderEntity));
    }
    
    @Test
    public void shouldGetHeadersByBlockType() {
    	List<DataHeaderEntity> returnList = new ArrayList<>();
    	when(dataHeaderRepositoryMock.findByBlocktype(BlockTypeEnum.BLOCKTYPEA))
    		.thenReturn(returnList);
    	
    	List<DataHeaderEntity> result = dataHeaderService.getHeaderByBlockType(BlockTypeEnum.BLOCKTYPEA);
    	verify(dataHeaderRepositoryMock, times(1)).findByBlocktype(eq(BlockTypeEnum.BLOCKTYPEA));
    	assertThat(returnList).isEqualTo(result);
    }
    
    @Test
    public void shouldGetHeaderByBlockName() {
    	DataHeaderEntity returnDataHeaderEntity= createTestDataHeaderEntity(Instant.now()); 
    	when(dataHeaderRepositoryMock.findByName(TEST_NAME))
				.thenReturn(returnDataHeaderEntity);
    	DataHeaderEntity result = dataHeaderService.getHeaderByBlockName(TEST_NAME);
    	verify(dataHeaderRepositoryMock, times(1)).findByName(TEST_NAME);
    	assertThat(returnDataHeaderEntity).isEqualTo(result);
    }
    


}
