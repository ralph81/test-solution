package com.db.dataplatform.techtest.server.component;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;

public interface HadoopClient {

	void sendDataToHadoop(DataEnvelope envelope);

}
