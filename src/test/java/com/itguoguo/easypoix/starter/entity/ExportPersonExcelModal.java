package com.itguoguo.easypoix.starter.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.itguoguo.easypoix.starter.annotation.ExcelFileAttr;
import com.itguoguo.easypoix.starter.service.BasicDictDataService;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ExcelFileAttr(fileName = "导出.xls")
public class ExportPersonExcelModal implements Serializable {

    /**
     * 姓名
     */
    @Excel(name = "姓名")
    private String name;

    /**
     * 性别-XB
     */
    @Excel(name = "性别", dict = BasicDictDataService.DICT_XB)
    private String sex;

    /**
     * 民族-MZ
     */
    @Excel(name = "民族", dict = BasicDictDataService.DICT_MZ)
    private String mz;

}
