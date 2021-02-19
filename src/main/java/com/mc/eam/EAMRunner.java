package com.mc.eam;

import com.alibaba.excel.EasyExcel;
import com.mc.eam.repairs.util.excel.dictionary.ExcelDictionaryListener;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author ymj
 * @Date： 2021/1/26 10:54
 * @description: 初始化 程序
 */
@Component
@Order(value = 1)
public class EAMRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        // todo 加载数据字典， 后期 可采用 redis 存储
        /** 导入数据 */
        String dictionary = "D:/数据字典.xls";
        EasyExcel.read(dictionary,new ExcelDictionaryListener()).sheet("data").doRead();
    }



}
