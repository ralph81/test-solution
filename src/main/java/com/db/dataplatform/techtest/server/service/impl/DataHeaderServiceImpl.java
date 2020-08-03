package com.db.dataplatform.techtest.server.service.impl;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.persistence.repository.DataHeaderRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataHeaderServiceImpl implements com.db.dataplatform.techtest.server.service.DataHeaderService {

    private final DataHeaderRepository dataHeaderRepository;

    @Override
    public void saveHeader(DataHeaderEntity entity) {
        dataHeaderRepository.save(entity);
    }

	@Override
	public List<DataHeaderEntity> getHeaderByBlockType(BlockTypeEnum blockType) {
		return dataHeaderRepository.findByBlocktype(blockType);
	}

	@Override
	public DataHeaderEntity getHeaderByBlockName(String blockName) {
		return dataHeaderRepository.findByName(blockName);
	}
}
