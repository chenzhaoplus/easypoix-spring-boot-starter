package com.itguoguo.easypoix.starter.service.impl;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class DefaultExcelDictHandler implements IExcelDictHandler {

    @Autowired
    private ExcelDictResolver excelDictResolver;

    @Override
    public String toName(String dict, Object obj, String name, Object value) {
        if (Objects.isNull(value) || StrUtil.isBlank(dict)) {
            return null;
        }
        return excelDictResolver.getDictName(dict, obj, name, value);
    }

    @Override
    public String toValue(String dict, Object obj, String name, Object value) {
        if (Objects.isNull(value) || StrUtil.isBlank(dict)) {
            return null;
        }
        return excelDictResolver.getDictId(dict, obj, name, value);
    }

    public void removeMap() {
        ExcelDictResolver.removeMap();
    }

}
