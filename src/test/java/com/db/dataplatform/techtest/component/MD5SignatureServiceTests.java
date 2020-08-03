package com.db.dataplatform.techtest.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.db.dataplatform.techtest.TestDataHelper;
import com.db.dataplatform.techtest.server.component.impl.MD5SignatureService;

@RunWith(MockitoJUnitRunner.class)
public class MD5SignatureServiceTests {
	
	MD5SignatureService signatureService;
	 
	@Before
	public void setup() {
		signatureService = new MD5SignatureService();
	}
	
	@Test
	public void testMD5Signaturehashing() throws NoSuchAlgorithmException {
		String result = signatureService.getSignature(TestDataHelper.DUMMY_DATA);
		assertThat(result).isEqualTo(TestDataHelper.MD5_CHECKSUM);
	}

}
