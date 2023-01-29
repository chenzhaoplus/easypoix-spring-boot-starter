package com.itguoguo.easypoix.starter.service.impl;

import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import com.itguoguo.easypoix.starter.service.BigExcelExportServer;

import java.util.List;

public class DefaultBigExcelExportServer implements IExcelExportServer {

    private BigExcelExportServer bigExcelExportServer;

    public DefaultBigExcelExportServer(BigExcelExportServer ser) {
        this.bigExcelExportServer = ser;
    }

    @Override
    public List<Object> selectListForExcelExport(Object queryParams, int page) {
        return bigExcelExportServer.findPage4BigExcelExport(queryParams, page);
    }

}
