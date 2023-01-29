package com.itguoguo.easypoix.starter.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DataParam {
    private String dict;
    private String key;
    private Object row;
}
