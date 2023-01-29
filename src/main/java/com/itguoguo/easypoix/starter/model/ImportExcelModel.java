package com.itguoguo.easypoix.starter.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;

import java.io.Serializable;

/**
 * @Author: cz
 * @Date: 2023/1/13
 * @Description:
 */
public class ImportExcelModel implements Serializable, IExcelDataModel, IExcelModel {

    /**
     * 行号
     */
    private int rowNum;

    /**
     * 错误消息
     */
    @Excel(name = "错误信息")
    private String errorMsg;

    @Override
    public int getRowNum() {
        return rowNum;
    }

    @Override
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
