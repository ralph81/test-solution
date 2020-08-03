package com.db.dataplatform.techtest.service;

import com.db.dataplatform.techtest.TestDataHelper;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.persistence.repository.DataStoreRepository;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import com.db.dataplatform.techtest.server.service.DataHeaderService;
import com.db.dataplatform.techtest.server.service.impl.DataBodyServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.db.dataplatform.techtest.TestDataHelper.createTestDataBodyEntity;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataHeaderEntity;
import static com.db.dataplatform.techtest.TestDataHelper.createDataHeaderEntityList;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataBodyServiceTests {

    public static final String TEST_NAME_NO_RESULT = "TestNoResult";
    private static final String DUMMY_HEADER_NAME = "dummy";

    @Mock
    private DataStoreRepository dataStoreRepositoryMock;

    private DataBodyService dataBodyService;
    
    @Mock
    private DataHeaderService dataHeaderService;
    private DataBodyEntity expectedDataBodyEntity;

    @Before
    public void setup() {
        DataHeaderEntity testDataHeaderEntity = createTestDataHeaderEntity(Instant.now());
        expectedDataBodyEntity = createTestDataBodyEntity(testDataHeaderEntity);

        dataBodyService = new DataBodyServiceImpl(dataStoreRepositoryMock, dataHeaderService);
    }

    @Test
    public void shouldSaveDataBodyEntityAsExpected(){
        dataBodyService.saveDataBody(expectedDataBodyEntity);

        verify(dataStoreRepositoryMock, times(1))
                .save(eq(expectedDataBodyEntity));
    }
    
    @Test
    public void returnsEntitiesForBlockType() {
    	List<DataHeaderEntity> dataHeaderEntities = createDataHeaderEntityList();

    	when(dataHeaderService.getHeaderByBlockType(any(BlockTypeEnum.class))).thenReturn(dataHeaderEntities);
    	
    	List<DataBodyEntity> dataBodyEntities = dataHeaderEntities.stream()
    										.map(dataHeaderEntity->
    												TestDataHelper.createTestDataBodyEntity(dataHeaderEntity))
    										.collect(Collectors.toList());
    	
    	when(dataStoreRepositoryMock.findByDataHeaderEntityIn(dataHeaderEntities)).thenReturn(dataBodyEntities);
    	
    	List<DataBodyEntity> dataBodyEntitiesResponse = dataBodyService.getDataByBlockType(BlockTypeEnum.BLOCKTYPEA);
    	verify(dataHeaderService,times(1)).getHeaderByBlockType(BlockTypeEnum.BLOCKTYPEA);
    	verify(dataStoreRepositoryMock, times(1)).findByDataHeaderEntityIn(eq(dataHeaderEntities));
    	
    	assertThat(dataBodyEntities).isEqualTo(dataBodyEntitiesResponse);
    	
    }
    
    @Test
    public void returnEntityForBlockName() {
    	
    	DataHeaderEntity headerEntity = createTestDataHeaderEntity(Instant.now());
    	DataBodyEntity dataBodyEntity = createTestDataBodyEntity(headerEntity);
    	when(dataHeaderService.getHeaderByBlockName(DUMMY_HEADER_NAME)).thenReturn(headerEntity);
    	when(dataStoreRepositoryMock.findByDataHeaderEntity(headerEntity)).thenReturn(dataBodyEntity);
    	Optional<DataBodyEntity> dataBodyEntityBox = dataBodyService.getDataByBlockName(DUMMY_HEADER_NAME);
    	verify(dataHeaderService, times(1)).getHeaderByBlockName(eq(DUMMY_HEADER_NAME));
    	verify(dataStoreRepositoryMock, times(1)).findByDataHeaderEntity(eq(headerEntity));
    	
    	assertThat(dataBodyEntityBox.isPresent()).isTrue();
    	assertThat(dataBodyEntityBox.get()).isEqualTo(dataBodyEntity);
    	
    }
    
    @Test
    public void returnEmptyForBlockName() {
    	when(dataHeaderService.getHeaderByBlockName(DUMMY_HEADER_NAME)).thenReturn(null);
    	Optional<DataBodyEntity> dataBodyEntityBox = dataBodyService.getDataByBlockName(DUMMY_HEADER_NAME);
    	verify(dataHeaderService, times(1)).getHeaderByBlockName(eq(DUMMY_HEADER_NAME));
    	verify(dataStoreRepositoryMock, times(0));
    	assertThat(dataBodyEntityBox.isPresent()).isFalse();
    	
    }
    

	

}
