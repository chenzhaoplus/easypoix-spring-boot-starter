package com.itguoguo.easypoix.starter.examples;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.itguoguo.easypoix.starter.annotation.ExcelFileAttr;
import com.itguoguo.easypoix.starter.app.EasyPoiXAutoConfiguration;
import com.itguoguo.easypoix.starter.entity.ExportPersonExcelModal;
import com.itguoguo.easypoix.starter.entity.ExportPersonParam;
import com.itguoguo.easypoix.starter.entity.ImportPersonExcelModal;
import com.itguoguo.easypoix.starter.service.ImportPersonExcelVerifyHandler;
import com.itguoguo.easypoix.starter.service.PersonExcelDataHandler;
import com.itguoguo.easypoix.starter.service.impl.DefaultBigExcelExportServer;
import com.itguoguo.easypoix.starter.util.EasyPoiUtil;
import com.itguoguo.easypoix.starter.util.MockServletOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@Slf4j
//@ContextConfiguration(classes = EasyPoiXAutoConfiguration.class)
@SpringBootTest(classes = EasyPoiXAutoConfiguration.class)
public class DefaultExcelTest {

    private final static Logger logger = LoggerFactory.getLogger(DefaultExcelTest.class);
    public final static String TEST_DIR = "test/";

    @Autowired
    private ImportPersonExcelVerifyHandler verifyHandler;
    @Autowired
    private PersonExcelDataHandler dataHandler;

    @Test
    public void exportByExcelFileAttrAnnotation() throws IOException {
        ExcelFileAttr annotation = ExportPersonExcelModal.class.getAnnotation(ExcelFileAttr.class);
        Assert.assertNotNull(annotation);
        String exportName = annotation.fileName();
        File file = getExportFile(TEST_DIR, exportName);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        try (ServletOutputStream sos = new MockServletOutputStream(file)) {
            Mockito.when(response.getOutputStream()).thenReturn(sos);
            EasyPoiUtil.exportExcel(findList(), response);
            Assert.assertTrue(file.exists());
        }
    }

    private List<ExportPersonExcelModal> findList() {
        List<ExportPersonExcelModal> list = new ArrayList<>();
        list.add(new ExportPersonExcelModal().setName("姓名a").setSex("1").setMz("1"));
        list.add(new ExportPersonExcelModal().setName("姓名b").setSex("2").setMz("2"));
        return list;
    }

    private File getExportFile(String dir, String exportName) {
        FileUtil.mkdir(new File(dir));
        File file = new File(dir + exportName);
        file.delete();
        Assert.assertFalse(file.exists());
        return file;
    }

    @Test
    public void exportBigExcel() throws IOException {
        String exportName = "导出住户基础信息.xlsx";
        File file = getExportFile(TEST_DIR, exportName);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        try (ServletOutputStream sos = new MockServletOutputStream(file)) {
            Mockito.when(response.getOutputStream()).thenReturn(sos);
            exportBigExcel(response, new ExportPersonParam().setId(1), exportName);
            Assert.assertTrue(file.exists());
        }
    }

    private void exportBigExcel(HttpServletResponse response, Object param, String exportName) {
        DefaultBigExcelExportServer ser = new DefaultBigExcelExportServer((queryParams, page) -> findPage(queryParams, page));
        EasyPoiUtil.exportBigExcel(ExportPersonExcelModal.class, ser, param, exportName, response);
    }

    private List<ExportPersonExcelModal> findPage(Object queryParams, int page) {
        if (page > 10) {
            return null;
        }
        logger.info("测试exportBigExcel, page: {}, queryParams: {}", page, queryParams);
        List<ExportPersonExcelModal> list = new ArrayList<>();
        list.add(new ExportPersonExcelModal().setName("姓名a" + page).setSex("1").setMz("1"));
        list.add(new ExportPersonExcelModal().setName("姓名b" + page).setSex("2").setMz("2"));
        return list;
    }

    @Test
    public void importExcel() throws IOException {
        String fileName = "导入.xls";
        try (InputStream is = this.getClass().getResourceAsStream("/" + fileName)) {
            MockMultipartFile file = new MockMultipartFile(fileName, is);
            ExcelImportResult<ImportPersonExcelModal> result = EasyPoiUtil.importExcelMore(
                    file, 0, 1, ImportPersonExcelModal.class, dataHandler, verifyHandler);
            assert !(CollUtil.isEmpty(result.getList()) && CollUtil.isEmpty(result.getFailList()));
            exportFailedExcel(result);
        }
    }

    private void exportFailedExcel(ExcelImportResult<ImportPersonExcelModal> result) throws IOException {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        String fileName = "导入失败.xls";
        File file = getExportFile(TEST_DIR, fileName);
        try (ServletOutputStream sos = new MockServletOutputStream(file)) {
            Mockito.when(response.getOutputStream()).thenReturn(sos);
            EasyPoiUtil.exportExcel(result.getFailList(), null, null, ImportPersonExcelModal.class,
                    fileName, dataHandler, response);
            assert CollUtil.isEmpty(result.getFailList()) || file.exists();
        }
    }

}
