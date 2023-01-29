package com.itguoguo.easypoix.starter.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: cz
 * @Date: 2023/1/14
 * @Description:
 */
@Data
@Accessors(chain = true)
public class HandleDataType implements Serializable {

    private String value;
    private Class<?> clazz;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandleDataType that = (HandleDataType) o;
        return value.equals(that.value) && clazz.equals(that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, clazz);
    }

}
