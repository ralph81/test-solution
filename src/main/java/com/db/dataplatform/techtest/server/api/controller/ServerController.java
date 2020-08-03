package com.db.dataplatform.techtest.server.api.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.api.model.DataHeaderBlockType;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/dataserver")
@RequiredArgsConstructor
@Validated
public class ServerController {

    private final Server server;
    

    @PostMapping(value = "/pushdata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> pushData(@Valid @RequestBody DataEnvelope dataEnvelope) throws NoSuchAlgorithmException{

        log.info("Data envelope received: {}", dataEnvelope.getDataHeader().getName());
        
        boolean checksumPass = server.saveDataEnvelope(dataEnvelope);

        log.info("Data envelope {} persisted. Attribute name: {}", checksumPass?"":"not",dataEnvelope.getDataHeader().getName());
        return ResponseEntity.ok(checksumPass);
    }
    

    @GetMapping(value = "/data/{blockType}")
    public ResponseEntity<List<DataEnvelope>> getData(@NotNull @PathVariable("blockType") BlockTypeEnum blockType){
    	log.info("Request for data receieved");
    	List<DataEnvelope> body = server.getDataBodyByBlockType(blockType);
    	return ResponseEntity.ok(body);
    }

    
    @PatchMapping(value = "/update/{name}/{blockType}")
    public ResponseEntity<Boolean> updateBlockType(@Valid DataHeaderBlockType dataHeaderBlockType){
    	log.info("Request for update receieved");
    	boolean updated= server.updateBlockType(dataHeaderBlockType);
    	return ResponseEntity.ok(updated);
    	
    }
    
}
