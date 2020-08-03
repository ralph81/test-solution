package com.db.dataplatform.techtest.server.component.impl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.stereotype.Service;

import com.db.dataplatform.techtest.server.component.SignatureService;

@Service


public class MD5SignatureService implements SignatureService {
	
	/**
	 * @param string
	 * @throws NoSuchAlgorithmException
	 * @returns MD5 signature of the input
	 */
	
	@Override
	@Transactional(value = TxType.NOT_SUPPORTED)
	public String getSignature(String input) throws NoSuchAlgorithmException{
		MessageDigest messageDigest;
		messageDigest = MessageDigest.getInstance("MD5");
        byte[] digest = messageDigest.digest(input.getBytes());
        String checksum = new BigInteger(1, digest).toString(16);
        return checksum;
	}

}
