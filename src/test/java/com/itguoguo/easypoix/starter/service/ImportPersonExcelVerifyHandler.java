package com.itguoguo.easypoix.starter.service;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.util.StrUtil;
import com.itguoguo.easypoix.starter.entity.ImportPersonExcelModal;
import com.itguoguo.easypoix.starter.service.impl.DefaultExcelVerifyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class ImportPersonExcelVerifyHandler extends DefaultExcelVerifyHandler<ImportPersonExcelModal> {

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportPersonExcelModal obj) {
        Set<Object> idCards = getRepeatTmp("idCard");
        if (StrUtil.isBlank(obj.getIdCard())) {
            return new ExcelVerifyHandlerResult(false, "证件号必填");
        } else if (idCards.contains(obj.getIdCard())) {
            return new ExcelVerifyHandlerResult(false, "证件号重复");
        } else if (StrUtil.isBlank(obj.getName())) {
            return new ExcelVerifyHandlerResult(false, "姓名必填");
        } else if (StrUtil.isBlank(obj.getSex())) {
            return new ExcelVerifyHandlerResult(false, "性别必填");
        }
        idCards.add(obj.getIdCard());
        return new ExcelVerifyHandlerResult(true);
    }

}
