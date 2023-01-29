package com.itguoguo.easypoix.starter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.itguoguo.easypoix.starter.app.EasyPoiXProperties;
import com.itguoguo.easypoix.starter.app.ExcelDataContext;
import com.itguoguo.easypoix.starter.model.DataParam;
import com.itguoguo.easypoix.starter.model.HandleDataType;
import com.itguoguo.easypoix.starter.service.ExcelDataService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class ExcelDictResolver {

    protected static final String DICT_PREFIX = "dict:";
    protected static final String REVERSE = "reverse";
    protected static ThreadLocal<Map<String, Map<Object, Object>>> dictMap = ThreadLocal.withInitial(HashMap::new);

    @Autowired
    private ExcelDataContext dictDataContext;
    @Autowired
    private EasyPoiXProperties easyPoiXProperties;
    private static ExcelDictResolver self;

    //abstract protected Map<Object, Object> getDictData(DictDataParam params);

    @PostConstruct
    public void init() {
        self = this;
        self.easyPoiXProperties = this.easyPoiXProperties;
    }

    private static String getDictPrefix() {
        return Objects.isNull(self.easyPoiXProperties) || StrUtil.isBlank(self.easyPoiXProperties.getDictPrefix()) ?
                DICT_PREFIX : self.easyPoiXProperties.getDictPrefix();
    }

    public String getDictId(String dict, Object obj, String name, Object value) {
        return getDict(dict, obj, name, value, (key) -> getDictId(dict, obj, name, value, key));
    }

    private Map<Object, Object> getDictId(String dict, Object obj, String name, Object value, String key) {
        putDictMap(key, () -> getDictData(dict, obj, key));
        Map<Object, Object> map = dictMap.get().get(key + ":" + REVERSE);
        if (Objects.isNull(map) || Objects.isNull(map.get(value))) {
            Object errorMsg = BeanUtil.getProperty(obj, "errorMsg");
            String errorMsgStr = Objects.isNull(errorMsg) ? "" : errorMsg.toString();
            BeanUtil.setProperty(obj, "errorMsg",
                    StrUtil.format("{} {}取值失败", errorMsgStr, name.replaceAll("\\*", "")));
        }
        return map;
    }

    public String getDictName(String dict, Object obj, String name, Object value) {
        return getDict(dict, obj, name, value, (key) -> getDictName(dict, obj, key));
    }

    private Map<Object, Object> getDictName(String dict, Object obj, String key) {
        putDictMap(key, () -> getDictData(dict, obj, key));
        Map<Object, Object> map = dictMap.get().get(key);
        return map;
    }

    private Map<Object, Object> getDictData(String dict, Object obj, String key) {
        return getDictDataService(dict, obj).getData(new DataParam().setDict(dict).setKey(key).setRow(obj));
    }

    private ExcelDataService getDictDataService(String dict, Object obj) {
        ExcelDataService ser = dictDataContext.getHandle(dict);
        if (Objects.isNull(ser)) {
            ser = dictDataContext.getHandle(new HandleDataType().setValue(dict).setClazz(obj.getClass()));
        }
        Validate.notNull(ser, "Not found implement class of ExcelDictDataService, type is : " + dict);
        return ser;
    }

    private String getDict(String dict, Object obj, String name, Object value, Function<String, Map<Object, Object>> func) {
        if (Objects.isNull(value) || StrUtil.isBlank(value.toString())) {
            return null;
        }
        String key = getDictPrefix() + dict;
        Map<Object, Object> map = func.apply(key);
        if (Objects.isNull(map) || Objects.isNull(map.get(value))) {
            return value.toString();
        }
        return map.get(value).toString();
    }

    private void putDictMap(String key, Supplier<Map<Object, Object>> supplyDictVal) {
        Map<Object, Object> dictValMap = dictMap.get().get(key);
        if (MapUtil.isNotEmpty(dictValMap)) {
            return;
        }
        Map<Object, Object> dictVal = supplyDictVal.get();
        if (MapUtil.isNotEmpty(dictVal)) {
            dictMap.get().put(key, dictVal);
            dictValMap = MapUtil.reverse(dictVal);
            dictMap.get().put(key + ":" + REVERSE, dictValMap);
        }
    }

    public static void removeMap() {
        dictMap.remove();
    }

}
