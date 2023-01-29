package com.itguoguo.easypoix.starter.service;

import com.itguoguo.easypoix.starter.model.DataParam;

import java.util.Map;

/**
 * @Author: cz
 * @Date: 2023/1/14
 * @Description:
 */
public interface ExcelDataService<K, V> {

    Map<K, V> getData(DataParam params);

}
