package com.db.dataplatform.techtest.server.api.model;

import javax.validation.constraints.NotBlank;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonSerialize(as = DataHeader.class)
@JsonDeserialize(as = DataHeader.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataHeader {

    @NotBlank
    private String name;

    private BlockTypeEnum blockType;
    
    private String bodyMD5Signature;
    

}
