package com.db.dataplatform.techtest.server.component;

import java.security.NoSuchAlgorithmException;

public interface SignatureService {

	String getSignature(String input) throws NoSuchAlgorithmException;

}