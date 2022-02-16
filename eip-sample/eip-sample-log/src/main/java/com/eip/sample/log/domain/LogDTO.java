package com.eip.sample.log.domain;

import lombok.Data;

import java.util.List;

@Data
public class LogDTO {

    private Long id;
    private String str;
    private List<String> list;

}
