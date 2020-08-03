package com.db.dataplatform.techtest.client.component.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.db.dataplatform.techtest.client.api.model.DataEnvelope;
import com.db.dataplatform.techtest.client.component.Client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Client code does not require any test coverage
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientImpl implements Client {

    public static final String URI_PUSHDATA = "http://localhost:8090/dataserver/pushdata";
    public static final UriTemplate URI_GETDATA = new UriTemplate("http://localhost:8090/dataserver/data/{blockType}");
    public static final UriTemplate URI_PATCHDATA = new UriTemplate("http://localhost:8090/dataserver/update/{name}/{newBlockType}");
    
    private final RestTemplate clientRestTemplate;

    @Override
    public void pushData(DataEnvelope dataEnvelope) {
        log.info("Pushing data {} to {}", dataEnvelope.getDataHeader().getName(), URI_PUSHDATA);
        
        try {
        	Boolean result = clientRestTemplate.postForObject(URI_PUSHDATA, dataEnvelope, Boolean.class);
        	log.info("got response "+result);
        }catch(RestClientException e) {
        	//Exception caught as Exception here will stop server from starting
        	log.error("Error while pushing data {}",e.getMessage());
        }
    }

    @Override
    public List<DataEnvelope> getData(String blockType) {
        log.info("Query for data with header block type {}", blockType);
        
        Map<String, String> params = new HashMap<>();
        
        params.put("blockType", blockType);
        HttpHeaders headers = new HttpHeaders();
        List <MediaType> accepts = new LinkedList<>();
        accepts.add(MediaType.APPLICATION_JSON);
        headers.setAccept(accepts);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<List<DataEnvelope>> response = clientRestTemplate.exchange(URI_GETDATA.expand(params)
        		,HttpMethod.GET, entity, new ParameterizedTypeReference<List<DataEnvelope>>() {
        });
        return response.getBody();
        
        	
        
    }

    @Override
    public boolean updateData(String blockName, String newBlockType) {
        log.info("Updating blocktype to {} for block with name {}", newBlockType, blockName);
        Map<String, String> params = new HashMap<>();
        params.put("name", blockName);
        params.put("newBlockType", newBlockType);
        Boolean result = clientRestTemplate.patchForObject(URI_PATCHDATA.expand(params),null, Boolean.class);
        return result;
    }




}
