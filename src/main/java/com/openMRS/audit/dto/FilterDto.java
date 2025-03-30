package com.openMRS.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilterDto {
    private String entity;
    private String field;
    private String user;
    private String revType;
    private String starDateTime;
    private String endDateTime;
}
