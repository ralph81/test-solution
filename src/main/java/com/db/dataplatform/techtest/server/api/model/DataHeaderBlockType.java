package com.db.dataplatform.techtest.server.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonDeserialize(as = DataHeaderBlockType.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DataHeaderBlockType {

    @NotBlank
    private String name;

    @NotNull
    private BlockTypeEnum blockType;
}
