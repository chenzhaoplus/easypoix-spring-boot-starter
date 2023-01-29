package com.itguoguo.easypoix.starter.app;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.itguoguo.easypoix.starter")
@EnableConfigurationProperties({EasyPoiXProperties.class})
public class EasyPoiXAutoConfiguration {

}
