package com.itguoguo.easypoix.starter.app;

import com.itguoguo.easypoix.starter.model.HandleDataType;
import com.itguoguo.easypoix.starter.service.ExcelDataService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExcelDataContext {

    private static Map<String, ExcelDataService> dictMap = new ConcurrentHashMap<>(10);
    private static Map<HandleDataType, ExcelDataService> handleMap = new ConcurrentHashMap<>(10);

    public void setHandleMap(String handleType, ExcelDataService handle) {
        if (!dictMap.containsKey(handleType)) {
            dictMap.put(handleType, handle);
        }
    }

    public ExcelDataService getHandle(String handleType) {
        return dictMap.containsKey(handleType) ? dictMap.get(handleType) : null;
    }

    public void setHandleMap(HandleDataType handleType, ExcelDataService handle) {
        if (!handleMap.containsKey(handleType)) {
            handleMap.put(handleType, handle);
        }
    }

    public ExcelDataService getHandle(HandleDataType handleType) {
        return handleMap.containsKey(handleType) ? handleMap.get(handleType) : null;
    }

}
