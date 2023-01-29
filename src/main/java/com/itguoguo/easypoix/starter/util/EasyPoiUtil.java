package com.itguoguo.easypoix.starter.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.excel.export.ExcelBatchExportService;
import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.itguoguo.easypoix.starter.annotation.ExcelFileAttr;
import com.itguoguo.easypoix.starter.service.impl.DefaultExcelDictHandler;
import com.itguoguo.easypoix.starter.service.impl.DefaultExcelVerifyHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Component
public class EasyPoiUtil {

    private static EasyPoiUtil self;
    @Autowired
    private DefaultExcelDictHandler dictHandler;

    @PostConstruct
    public void init() {
        self = this;
        self.dictHandler = this.dictHandler;
    }

    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, boolean isCreateHeader, IExcelDataHandler dataHandler, HttpServletResponse response) {
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        exportParams.setDictHandler(self.dictHandler);
        exportParams.setType(ExcelType.XSSF);
        if (Objects.nonNull(dataHandler) && Objects.nonNull(dataHandler.getNeedHandlerFields())) {
            exportParams.setDataHandler(dataHandler);
        }
        defaultExport(list, pojoClass, fileName, response, exportParams);
        self.dictHandler.removeMap();
    }

    public static void exportExcel(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response) {
        exportExcel(list, null, pojoClass, fileName, response);
    }

    public static void exportExcel(List<?> list, String sheetName, Class<?> pojoClass, String fileName,
                                   HttpServletResponse response) {
        exportExcel(list, null, sheetName, pojoClass, fileName, response);
    }

    public static void exportExcel(List<?> list, HttpServletResponse response) {
        Class<?> cls = list.get(0).getClass();
        ExcelFileAttr fileAttr = cls.getDeclaredAnnotation(ExcelFileAttr.class);
        exportExcel(list, null, fileAttr.sheetName(), cls, fileAttr.fileName(), response);
    }

    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) {
        exportExcel(list, title, sheetName, pojoClass, fileName, true, null, response);
    }

    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, IExcelDataHandler dataHandler, HttpServletResponse response) {
        exportExcel(list, title, sheetName, pojoClass, fileName, true, dataHandler, response);
    }

    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
        defaultExport(list, fileName, response);
    }

    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    public static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try {
            //response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/octet-stream");
            String name = URLEncoder.encode(fileName, "UTF-8");
            //String name = fileName;
            response.setHeader("Content-Disposition", "attachment;filename=" + name);
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        if (workbook == null) ;
        downLoadExcel(fileName, response, workbook);
    }

    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        //判断文件是否存在
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("模板不能为空");
        } catch (Exception e) {
        }
        return list;
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("excel文件不能为空");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    public static <T> ExcelImportResult<T> importExcelMore(MultipartFile file, Integer titleRows,
                                                           Integer headerRows, Class<T> pojoClass) {
        return importExcelMore(file, titleRows, headerRows, pojoClass, null, null, null);
    }

    public static <T> ExcelImportResult<T> importExcelMore(MultipartFile file, Integer titleRows,
                                                           Integer headerRows, Class<T> pojoClass, IExcelDataHandler dataHandler) {
        return importExcelMore(file, titleRows, headerRows, pojoClass, null, null, dataHandler);
    }

    public static <T> ExcelImportResult<T> importExcelMore(MultipartFile file, Integer titleRows,
                                                           Integer headerRows, Class<T> pojoClass, IExcelDataHandler dataHandler, IExcelVerifyHandler verifyHandler) {
        return importExcelMore(file, titleRows, headerRows, pojoClass, verifyHandler, null, dataHandler);
    }

    public static <T> ExcelImportResult<T> importExcelMore(MultipartFile file, Integer titleRows,
                                                           Integer headerRows, Class<T> pojoClass, IExcelVerifyHandler verifyHandler) {
        return importExcelMore(file, titleRows, headerRows, pojoClass, verifyHandler, null, null);
    }

    public static <T> ExcelImportResult<T> importExcelMore(MultipartFile file, Integer titleRows,
                                                           Integer headerRows, Class<T> pojoClass, IExcelVerifyHandler verifyHandler,
                                                           String[] importFields, IExcelDataHandler dataHandler) {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setVerifyHandler(verifyHandler);
        params.setNeedVerify(true);
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setImportFields(importFields);
        params.setDictHandler(self.dictHandler);
        if (Objects.nonNull(dataHandler) && Objects.nonNull(dataHandler.getNeedHandlerFields())) {
            params.setDataHandler(dataHandler);
        }
        try {
            ExcelImportResult<T> result = ExcelImportUtil.importExcelMore(file.getInputStream(), pojoClass, params);
            return result;
        } catch (NoSuchElementException e) {
            throw new RuntimeException("excel文件不能为空");
        } catch (Exception e) {
            throw new RuntimeException("文件解析失败，原因：" + e.getMessage());
        } finally {
            self.dictHandler.removeMap();
            Optional.ofNullable(verifyHandler).ifPresent((h) -> DefaultExcelVerifyHandler.removeRepeatTmp());
        }
    }

    public static Workbook exportBigExcel(ExportParams exportParams, Class<?> pojoClass, IExcelExportServer server,
                                          Object queryParams) {
        exportParams.setDictHandler(self.dictHandler);
        ExcelBatchExportService batchServer = new ExcelBatchExportService();
        batchServer.init(exportParams, pojoClass);
        return batchServer.exportBigExcel(server, queryParams);
    }

    public static void exportBigExcel(Class<?> pojoClass, IExcelExportServer server,
                                      Object queryParams, HttpServletResponse response) {
        ExcelFileAttr fileAttr = pojoClass.getDeclaredAnnotation(ExcelFileAttr.class);
        exportBigExcel(pojoClass, server, queryParams, fileAttr.fileName(), response);
    }

    public static void exportBigExcel(Class<?> pojoClass, IExcelExportServer server,
                                      Object queryParams, String exportName, HttpServletResponse response) {
        ExportParams exportParams = new ExportParams(null, exportName, ExcelType.XSSF);
        Workbook workbook = exportBigExcel(exportParams, pojoClass, server, queryParams);
        downLoadExcel(exportName, response, workbook);
    }

}
