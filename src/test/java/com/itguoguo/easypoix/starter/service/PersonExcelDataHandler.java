package com.itguoguo.easypoix.starter.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.itguoguo.easypoix.starter.annotation.ExcelHandleDataType;
import com.itguoguo.easypoix.starter.entity.ImportPersonExcelModal;
import com.itguoguo.easypoix.starter.model.DataParam;
import com.itguoguo.easypoix.starter.service.impl.SimpleExcelDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.itguoguo.easypoix.starter.service.PersonExcelDataHandler.BUILDING_NAME;
import static com.itguoguo.easypoix.starter.service.PersonExcelDataHandler.UNIT_NAME;

@Slf4j
@Component
@ExcelHandleDataType(clazz = ImportPersonExcelModal.class, value = {BUILDING_NAME, UNIT_NAME})
public class PersonExcelDataHandler extends SimpleExcelDataHandler<ImportPersonExcelModal> {

    public static final String BUILDING_NAME = "楼栋";
    public static final String UNIT_NAME = "单元";
    private static final Map<String, Map<String, String>> dictMap = new HashMap<>();

    static {
        dictMap.put(BUILDING_NAME + ":communityId1",
                MapUtil.builder("buildingId1", "1栋").put("buildingId2", "2栋").build());
        dictMap.put(UNIT_NAME + ":buildingId1", MapUtil.builder("1", "1单元").put("2", "2单元").build());
        dictMap.put(UNIT_NAME + ":buildingId2", MapUtil.builder("1", "1单元").put("2", "2单元").build());
    }

    @Override
    public Map<String, String> getData(DataParam params) {
        switch (params.getDict()) {
            case BUILDING_NAME:
                return getBuildingData(params);
            case UNIT_NAME:
                return getUnitData(params);
            default:
                return null;
        }
    }

    private Map<String, String> getBuildingData(DataParam params) {
        Object row = params.getRow();
        String communityId = BeanUtil.getProperty(row, "communityId");
        return dictMap.get(BUILDING_NAME + ":" + communityId);
    }

    private Map<String, String> getUnitData(DataParam params) {
        Object row = params.getRow();
        String buildingId = BeanUtil.getProperty(row, "buildingId");
        return dictMap.get(UNIT_NAME + ":" + buildingId);
    }

}
