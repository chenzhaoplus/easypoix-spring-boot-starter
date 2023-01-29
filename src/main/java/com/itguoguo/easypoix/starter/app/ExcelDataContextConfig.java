package com.itguoguo.easypoix.starter.app;

import cn.hutool.core.util.ArrayUtil;
import com.itguoguo.easypoix.starter.annotation.ExcelDictDataType;
import com.itguoguo.easypoix.starter.annotation.ExcelHandleDataType;
import com.itguoguo.easypoix.starter.model.HandleDataType;
import com.itguoguo.easypoix.starter.service.ExcelDictDataService;
import com.itguoguo.easypoix.starter.service.ExcelHandleDataService;
import com.itguoguo.easypoix.starter.service.impl.SimpleExcelDataHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class ExcelDataContextConfig implements BeanPostProcessor {

    @Autowired
    private ExcelDataContext dictDataContext;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        loadExcelHandleDataType(bean, beanName);
        loadExcelDictDataType(bean, beanName);
        return bean;
    }

    private void loadExcelHandleDataType(Object bean, String beanName) {
        if (!bean.getClass().isAnnotationPresent(ExcelHandleDataType.class)) {
            return;
        }
        ExcelHandleDataType anno = bean.getClass().getAnnotation(ExcelHandleDataType.class);
        String[] values = anno.value();
        Class<?> clazz = anno.clazz();
        if (ArrayUtil.isNotEmpty(values)) {
            if (bean instanceof ExcelHandleDataService) {
                for (String value : values) {
                    dictDataContext.setHandleMap(new HandleDataType().setValue(value).setClazz(clazz),
                            (ExcelHandleDataService) bean);
                }
            }
            if (bean instanceof SimpleExcelDataHandler) {
                ((SimpleExcelDataHandler) bean).setNeedHandlerFields(values);
            }
        }
    }

    private void loadExcelDictDataType(Object bean, String beanName) {
        if (!bean.getClass().isAnnotationPresent(ExcelDictDataType.class)) {
            return;
        }
        ExcelDictDataType anno = bean.getClass().getAnnotation(ExcelDictDataType.class);
        String[] values = anno.value();
        if (ArrayUtil.isNotEmpty(values) && bean instanceof ExcelDictDataService) {
            for (String value : values) {
                dictDataContext.setHandleMap(value, (ExcelDictDataService) bean);
            }
        }
    }

}
