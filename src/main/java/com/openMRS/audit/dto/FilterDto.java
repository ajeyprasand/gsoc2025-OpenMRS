package com.openMRS.audit.dto;

import lombok.Data;

@Data
public class FilterDto {
    private String user;
    private String revType;
    private String starDateTime;
    private String endDateTime;
}
