package com.itguoguo.easypoix.starter.app;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = EasyPoiXProperties.PREFIX)
@Data
public class EasyPoiXProperties {

    public static final String PREFIX = "easypoix";

    private String dictPrefix;

}
