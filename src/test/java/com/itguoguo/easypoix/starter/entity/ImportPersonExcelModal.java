package com.itguoguo.easypoix.starter.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.itguoguo.easypoix.starter.model.ImportExcelModel;
import com.itguoguo.easypoix.starter.service.BasicDictDataService;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ImportPersonExcelModal extends ImportExcelModel implements Serializable {

    /**
     * 姓名
     */
    @Excel(name = "*姓名")
    private String name;

    /**
     * 性别-lookup_type=XB
     */
    @Excel(name = "*性别", dict = BasicDictDataService.DICT_XB)
    private String sex;

    /**
     * 类型号，如身份证号码
     */
    @Excel(name = "*证件号")
    private String idCard;

    /**
     * 民族-MZ
     */
    @Excel(name = "民族", dict = BasicDictDataService.DICT_MZ)
    private String mz;

    /**
     * 所属小区
     */
    @Excel(name = "*所属小区", dict = BasicDictDataService.DICT_COMMUNITY)
    private String communityId;

    /**
     * 楼栋
     */
    @Excel(name = "楼栋")
    private String buildingId;

    /**
     * 单元
     */
    @Excel(name = "单元")
    private String unitId;

}
