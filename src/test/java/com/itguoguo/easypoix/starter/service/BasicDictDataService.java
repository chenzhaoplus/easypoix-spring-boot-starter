package com.itguoguo.easypoix.starter.service;

import cn.hutool.core.map.MapUtil;
import com.itguoguo.easypoix.starter.annotation.ExcelDictDataType;
import com.itguoguo.easypoix.starter.model.DataParam;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.itguoguo.easypoix.starter.service.BasicDictDataService.*;

@ExcelDictDataType({DICT_XB, DICT_MZ, DICT_COMMUNITY})
@Component
public class BasicDictDataService implements ExcelDictDataService<String, String> {

    public static final String DICT_XB = "XB";
    public static final String DICT_MZ = "MZ";
    public static final String DICT_COMMUNITY = "DICT_COMMUNITY";
    private static final Map<String, Map<String, String>> dictMap = new HashMap<>();

    static {
        dictMap.put("basic:dict:" + DICT_XB, MapUtil.builder("1", "男").put("2", "女").build());
        dictMap.put("basic:dict:" + DICT_MZ, MapUtil.builder("1", "汉族").put("2", "维吾尔族").build());
        dictMap.put("basic:dict:" + DICT_COMMUNITY, MapUtil.builder("communityId1", "恒大小区").build());
    }

    @Override
    public Map<String, String> getData(DataParam params) {
        return dictMap.get(params.getKey());
    }

}
