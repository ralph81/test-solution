package com.db.dataplatform.techtest.server.component;


import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.api.model.DataHeaderBlockType;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;


public interface Server {
	boolean saveDataEnvelope(DataEnvelope envelope) throws NoSuchAlgorithmException;

	List<DataEnvelope> getDataBodyByBlockType(BlockTypeEnum blockType);

	boolean updateBlockType(DataHeaderBlockType dataHeaderBlockType);

}
