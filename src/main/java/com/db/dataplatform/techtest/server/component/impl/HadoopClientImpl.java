package com.db.dataplatform.techtest.server.component.impl;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.component.HadoopClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(value = TxType.NOT_SUPPORTED)

public class HadoopClientImpl implements HadoopClient {

	private final RestTemplate serverRestTemplate;

	@Value("${hadoop.datalake.url}")
	private String HADOOP_URL; 
	
	/**
	 * @param DataEnvelope object encapsulating the request
	 * 
	 * Method asynchronously sends data to hadoop data store
	 * Retries upto 4 times in case of failure
	 */

	@Override
	@Async
	@Retryable(value = { HttpServerErrorException.class }, maxAttempts = 4)
	public void sendDataToHadoop(DataEnvelope envelope) {
		log.info("Sending data to hadoop");
			serverRestTemplate.postForEntity(HADOOP_URL, envelope, String.class);
		log.info("Successfully sent data to hadoop");
	}


	@Recover()
	public void recover(HttpServerErrorException exception) {
		log.error("Error could not update hadoop data");
		//not further implemented as failure and recovery protocol not defined
		//could be written out to a queue for further processing
	}

}
