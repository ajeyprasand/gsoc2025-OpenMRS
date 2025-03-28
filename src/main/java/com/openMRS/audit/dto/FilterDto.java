package com.openMRS.audit.dto;



import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilterDto {
    @NotNull(message = "Entity is mandatory")
    private String entity;
    private String fieldName;
    private String fieldValue;
    private String user;
    private String revType;
    private String starDateTime;
    private String endDateTime;
}
