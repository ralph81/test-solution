package com.db.dataplatform.techtest.server.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.persistence.repository.DataStoreRepository;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import com.db.dataplatform.techtest.server.service.DataHeaderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class DataBodyServiceImpl implements DataBodyService {

    private final DataStoreRepository dataStoreRepository;
    private final DataHeaderService dataHeaderService;
    
    
    /**
     * @param DataBodyEntity
     * 
     * saves the DataBodyEntity to the database
     */
    @Override
    public void saveDataBody(DataBodyEntity dataBody) {
        dataStoreRepository.save(dataBody);
    }
    
    /**
     * @param BlockTypeEnum
     * @return List<DataBodyEntity>
     * 
     * return list of DataBodyEntity with the given blocktype 
     */

    @Override
    public List<DataBodyEntity> getDataByBlockType(BlockTypeEnum blockType) {
    	List<DataHeaderEntity> dataHeaderEntities = dataHeaderService.getHeaderByBlockType(blockType);
    	List<DataBodyEntity> dataBodyEntities = dataStoreRepository.findByDataHeaderEntityIn(dataHeaderEntities);
        return dataBodyEntities;
    }
    
    /**
     * @param String
     * @return Optional<DataBodyEntity>
     * 
     * returns DataBodyEntity for the given blockName
     * 
     */

    @Override
    public Optional<DataBodyEntity> getDataByBlockName(String blockName) {
    	DataBodyEntity dataBodyEntity = null;
    	DataHeaderEntity dataHeaderEntity = dataHeaderService.getHeaderByBlockName(blockName);
    	if(dataHeaderEntity!=null) {
    		dataBodyEntity = dataStoreRepository.findByDataHeaderEntity(dataHeaderEntity);
    	}
        return Optional.ofNullable(dataBodyEntity);
    }
    
    
}
