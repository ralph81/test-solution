package com.db.dataplatform.techtest.component;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.db.dataplatform.techtest.TestDataHelper;
import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.component.HadoopClient;
import com.db.dataplatform.techtest.server.component.impl.HadoopClientImpl;

@RunWith(MockitoJUnitRunner.class)
public class HadoopClientTests {
	
	@Mock
	private RestTemplate serverRestTemplate;
	private HadoopClient hadoopClient;
	
	private static final String HADOOP_URL = "hadoop.datalake.url";
	
	@Before
	public void setup() {
		hadoopClient = new HadoopClientImpl(serverRestTemplate) ;
		ReflectionTestUtils.setField(hadoopClient, "HADOOP_URL", HADOOP_URL);
	}
	
	@Test
	public void testHadoopClientCalled() {
		DataEnvelope dataEnvelope = TestDataHelper.createTestDataEnvelopeApiObject();
		hadoopClient.sendDataToHadoop(dataEnvelope);
		verify(serverRestTemplate,times(1)).postForEntity(HADOOP_URL, dataEnvelope, String.class);
	}
	

}
