package com.risklens.core.risk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

@Data
@AllArgsConstructor
public class ExportFileDto {
    private Resource resource;
    private MediaType mediaType;
    private String filename;
}
