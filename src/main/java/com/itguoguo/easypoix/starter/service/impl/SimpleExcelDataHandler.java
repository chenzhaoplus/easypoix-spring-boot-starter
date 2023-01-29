package com.itguoguo.easypoix.starter.service.impl;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import com.itguoguo.easypoix.starter.service.ExcelHandleDataService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public abstract class SimpleExcelDataHandler<T> extends ExcelDataHandlerDefaultImpl<T>
        implements ExcelHandleDataService {

    @Autowired
    private ExcelDictResolver excelDictResolver;

    @Override
    public Object importHandler(T obj, String name, Object value) {
        if (Objects.isNull(value)) {
            return value;
        }
        return excelDictResolver.getDictId(name, obj, name, value);
    }

    @Override
    public Object exportHandler(T obj, String name, Object value) {
        if (Objects.isNull(value)) {
            return value;
        }
        return excelDictResolver.getDictName(name, obj, name, value);
    }

}
