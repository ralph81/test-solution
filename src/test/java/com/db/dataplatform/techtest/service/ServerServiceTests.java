package com.db.dataplatform.techtest.service;

import static com.db.dataplatform.techtest.TestDataHelper.TEST_NAME;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataBodyEntity;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataEnvelopeApiObject;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataHeaderEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.api.model.DataHeaderBlockType;
import com.db.dataplatform.techtest.server.component.HadoopClient;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.component.SignatureService;
import com.db.dataplatform.techtest.server.component.impl.ServerImpl;
import com.db.dataplatform.techtest.server.mapper.ServerMapperConfiguration;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.service.DataBodyService;



@RunWith(MockitoJUnitRunner.class)
public class ServerServiceTests {

    @Mock
    private DataBodyService dataBodyServiceImplMock;
    @Mock
    private SignatureService mD5SignatureServiceMock;  
    @Mock
    private HadoopClient hadoopClientMock;
  
    private ModelMapper modelMapper;

    private DataBodyEntity expectedDataBodyEntity;
    private DataEnvelope testDataEnvelope;

    private Server server;

    @Before
    public void setup() {
        ServerMapperConfiguration serverMapperConfiguration = new ServerMapperConfiguration();
        modelMapper = serverMapperConfiguration.createModelMapperBean();

        testDataEnvelope = createTestDataEnvelopeApiObject();
        expectedDataBodyEntity = modelMapper.map(testDataEnvelope.getDataBody(), DataBodyEntity.class);
        expectedDataBodyEntity.setDataHeaderEntity(modelMapper.map(testDataEnvelope.getDataHeader(), DataHeaderEntity.class));

        server = new ServerImpl(dataBodyServiceImplMock, modelMapper, mD5SignatureServiceMock, hadoopClientMock);
    }

    @Test
    public void shouldSaveDataEnvelopeAsExpected() throws NoSuchAlgorithmException, IOException {
    	when(mD5SignatureServiceMock.getSignature(testDataEnvelope.getDataBody().getDataBody()))
    	.thenReturn(testDataEnvelope.getDataHeader().getBodyMD5Signature());
    	boolean success = server.saveDataEnvelope(testDataEnvelope);
        assertThat(success).isTrue();
        verify(dataBodyServiceImplMock, times(1)).saveDataBody(any());
        verify(hadoopClientMock, times(1)).sendDataToHadoop(any(DataEnvelope.class));
       
    }
    @Test
    public void saveDataEnvelopeWithWrongMD5Signature() throws NoSuchAlgorithmException, IOException {
    	when(mD5SignatureServiceMock.getSignature(testDataEnvelope.getDataBody().getDataBody()))
    	.thenReturn(testDataEnvelope.getDataHeader().getBodyMD5Signature()+"X");
    	testDataEnvelope.getDataHeader().setBodyMD5Signature("");
        boolean success = server.saveDataEnvelope(testDataEnvelope);
        assertThat(success).isFalse();
        verify(hadoopClientMock, times(0)).sendDataToHadoop(any());
       
    }
    
    @Test
    public void shouldListDataBodyByBlockType() {
    	List<DataBodyEntity> dataBodyEntities = new ArrayList<>();
    	dataBodyEntities.add(
    			createTestDataBodyEntity(
    					createTestDataHeaderEntity(Instant.now())
    					)
    			);
    			
    	when(dataBodyServiceImplMock.getDataByBlockType(any(BlockTypeEnum.class)))
    			.thenReturn(dataBodyEntities);
    	List<DataEnvelope> dataEnvelopes  = server.getDataBodyByBlockType(BlockTypeEnum.BLOCKTYPEA);
    	assertThat(dataEnvelopes.size()).isEqualTo(1);
    	DataEnvelope dataEnvelope= dataEnvelopes.get(0);
    	DataBodyEntity dataBodyEntity = dataBodyEntities.get(0);
    	verify(dataBodyServiceImplMock, times(1)).getDataByBlockType(any(BlockTypeEnum.class));
    	assertThat(dataEnvelope.getDataBody()).isEqualToComparingFieldByField(dataBodyEntity);
    	assertThat(dataEnvelope.getDataHeader()).isEqualToComparingOnlyGivenFields(dataBodyEntity.getDataHeaderEntity(),
    			"name","bodyMD5Signature");

    }
    
    @Test
    public void shouldUpdateBlockTypeSucessfully()
    {
    	Optional<DataBodyEntity> dataBodyEntityBox = Optional.of(createTestDataBodyEntity(
    			createTestDataHeaderEntity(Instant.now())));
    	when(dataBodyServiceImplMock.getDataByBlockName(any(String.class))).thenReturn(dataBodyEntityBox);
    	DataHeaderBlockType dataHeaderBlockType = new DataHeaderBlockType(TEST_NAME
    									, BlockTypeEnum.BLOCKTYPEA);
    	boolean updated = server.updateBlockType(dataHeaderBlockType);
    	assertThat(updated).isTrue();
    	verify(dataBodyServiceImplMock, times(1)).getDataByBlockName(any(String.class));
    }
    
    
    @Test
    public void shouldFailUpdateBlockType()
    {
    	Optional<DataBodyEntity> dataBodyEntityBox = Optional.empty();
    	when(dataBodyServiceImplMock.getDataByBlockName(any(String.class))).thenReturn(dataBodyEntityBox);
    	DataHeaderBlockType dataHeaderBlockType = new DataHeaderBlockType(TEST_NAME
    									, BlockTypeEnum.BLOCKTYPEA);
    	boolean updated = server.updateBlockType(dataHeaderBlockType);
    	assertThat(updated).isFalse();
    	verify(dataBodyServiceImplMock, times(1)).getDataByBlockName(any(String.class));
    }
}
