package com.openMRS.audit.dto;

import java.time.LocalDateTime;

import org.hibernate.envers.RevisionType;

import lombok.Data;

@Data
public class ResponseDto<T> {
    private String username;
    private Integer revision;
    private RevisionType type;
    private LocalDateTime timestamp;
    private T data;
}
