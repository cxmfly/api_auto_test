package com.util;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.pojo.CaseData;

import java.io.File;
import java.util.List;

public class ExcelUtil {
    public static final String EXCEL_FILE_PATH ="src\\test\\resources\\caseData.xlsx";
    //读取外部Excel文件中的数据
    public static List<CaseData> readExcel(int sheetNum){
        //1、读取Excel？？Easypoi
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheetNum);
        //读取的文件src\test\resources\caseData.xlsx
        List<CaseData> datas = ExcelImportUtil.importExcel(new File(EXCEL_FILE_PATH),
                CaseData.class,importParams);
        return datas;
    }
}
