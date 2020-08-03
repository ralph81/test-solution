package com.db.dataplatform.techtest.server.component.impl;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.db.dataplatform.techtest.server.api.model.DataBody;
import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.api.model.DataHeader;
import com.db.dataplatform.techtest.server.api.model.DataHeaderBlockType;
import com.db.dataplatform.techtest.server.component.HadoopClient;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.component.SignatureService;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.service.DataBodyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerImpl implements Server {

    private final DataBodyService dataBodyServiceImpl;
    private final ModelMapper modelMapper;
    private final SignatureService mD5SignatureService;  
    private final HadoopClient hadoopClient;
    

    /**
     * @param envelope
     * @return true if there is a match with the client provided checksum.
     * @throws NoSuchAlgorithmException 
     */
    @Override
    public boolean saveDataEnvelope(DataEnvelope envelope) throws NoSuchAlgorithmException{

        // Save to persistence.
        String checksum = mD5SignatureService.getSignature(envelope.getDataBody().getDataBody());
        boolean checksumPass = checksum.equals(envelope.getDataHeader().getBodyMD5Signature());
        if(checksumPass) {
	        persist(envelope);
	        hadoopClient.sendDataToHadoop(envelope);
	        log.info("Data persisted successfully, data name: {}", envelope.getDataHeader().getName());
        }else {
        	log.info("Data not persisted due to invalid checksum, data name: {}"
        			, envelope.getDataHeader().getName());
        }
        return checksumPass;
    }
    
    /**
     * @param blockType
     * @return list of dataenvelopes with the given blocktype
     */

	@Override
	public List<DataEnvelope> getDataBodyByBlockType(BlockTypeEnum blockType) {
		List<DataBodyEntity> dataBodyEntities =  dataBodyServiceImpl.getDataByBlockType(blockType);
		return dataBodyEntities.stream().map(dataBodyEntity-> new DataEnvelope(
				modelMapper.map(dataBodyEntity.getDataHeaderEntity(),DataHeader.class),
				modelMapper.map(dataBodyEntity, DataBody.class))).collect(Collectors.toList());
	}

	
	/**
	 * @param DataHeaderBlockType contains the block name and new blocktype
	 * @returns if an entity was updated successfully
	 */
	@Override
	public boolean updateBlockType(DataHeaderBlockType dataHeaderBlockType) {
		Optional<DataBodyEntity> dataBodyEntityBox = dataBodyServiceImpl.getDataByBlockName(dataHeaderBlockType.getName());
		if(!dataBodyEntityBox.isPresent()) return false;
		DataBodyEntity dataBodyEntity = dataBodyEntityBox.get();
		dataBodyEntity.getDataHeaderEntity().setBlocktype(dataHeaderBlockType.getBlockType());
		saveData(dataBodyEntity);
		return true;
	}
	

    private void persist(DataEnvelope envelope) {
        log.info("Persisting data with attribute name: {}", envelope.getDataHeader().getName());
        DataHeaderEntity dataHeaderEntity = modelMapper.map(envelope.getDataHeader(), DataHeaderEntity.class);

        DataBodyEntity dataBodyEntity = modelMapper.map(envelope.getDataBody(), DataBodyEntity.class);
        dataBodyEntity.setDataHeaderEntity(dataHeaderEntity);

        saveData(dataBodyEntity);
    }

    private void saveData(DataBodyEntity dataBodyEntity) {
        dataBodyServiceImpl.saveDataBody(dataBodyEntity);
    }

}
