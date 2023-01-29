package com.itguoguo.easypoix.starter.service.impl;

import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.CollUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: cz
 * @Date: 2023/1/13
 * @Description:
 */
public abstract class DefaultExcelVerifyHandler<T> implements IExcelVerifyHandler<T> {

    private static final ThreadLocal<Map<String, Set<Object>>> repeatTmp = ThreadLocal.withInitial(HashMap::new);

    public static Set<Object> getRepeatTmp(String key) {
        Set<Object> set = repeatTmp.get().get(key);
        if (CollUtil.isEmpty(set)) {
            set = new HashSet<>();
        }
        repeatTmp.get().put(key, set);
        return set;
    }

    public static void removeRepeatTmp() {
        repeatTmp.remove();
    }

}
