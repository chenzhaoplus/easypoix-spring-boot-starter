package com.itguoguo.easypoix.starter.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ExportPersonParam implements Serializable {

    private long id;

}
