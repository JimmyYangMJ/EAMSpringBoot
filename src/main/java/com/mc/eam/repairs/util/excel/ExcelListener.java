package com.mc.eam.repairs.util.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.mc.eam.repairs.util.GenerateObjectUtil;
import com.mc.eam.repairs.util.excel.dictionary.ExcelDictionaryListener;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import com.mc.eam.repairs.dao.impl.MongoAssetDaoImpl;

/**
 * @author ymj
 * @Date： 2021/1/12 14:04
 * @description:
 */
@Component
public class ExcelListener extends AnalysisEventListener<Map<Integer, String>> {

    @Autowired
    public MongoAssetDaoImpl mongoCRUDDao;

    /**
     * 动态：bean
     */
    public static Object dynamicBean = null;

    /**
     * 数据字典：0，1，2
     */
    public static HashMap<String, ArrayList<String>> dictionaryMap = null;

    /**
     * 表头数据
     */
    public static Map headMap = null;

    /** 1. 读取表头 */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头数据："+headMap);
        dictionaryMap = ExcelDictionaryListener.dictionaryMap;
        this.headMap = headMap;

        /** 动态 Bean 初始化 */
        try {
            Map properties = new HashMap();
            Iterator<Map.Entry<Integer, String>> iter = headMap.entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry<Integer, String> entry = iter.next();
                String value = entry.getValue();
                ArrayList<String> list = dictionaryMap.get(value);
                String EnglishValue = list.get(0);
                String type = list.get(1);
                properties.put(EnglishValue, Class.forName("java.lang." + type));
            }
            System.out.println(properties.toString());
            // 生成 JavaBean
            Object stu = GenerateObjectUtil.generateObject(properties);
            this.dynamicBean = stu;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取表内容
     * 逐行读取excel内容
     * @param map
     * @param analysisContext
     */
    @SneakyThrows
    @Override
    public void invoke(Map<Integer, String> map, AnalysisContext analysisContext) throws ExcelAnalysisException {
        Object Bean = dynamicBean;
        /* 生成 Obj */
        Iterator<Map.Entry<Integer, String>> iter = map.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<Integer, String> entry = iter.next();
            // 获取key
            int key = entry.getKey();
            // 获取对应编号，表属性值
            String keyTemp = headMap.get(key).toString();
            // 获取中文表头 对应 英文表头
            // list0:English, list1:数据类型
            ArrayList<String> attribution = dictionaryMap.get(keyTemp);
            // 获取 value
            String value = entry.getValue();
            // 属性值
            String contribution = attribution.get(0);
            // Type
            String type = attribution.get(1);

            if(type.equals("Integer")) {
                GenerateObjectUtil.setValue(Bean, contribution, Integer.parseInt(value));
            } else{
                GenerateObjectUtil.setValue(Bean, contribution, value);
            }
//            System.out.print("<" + value +  ">");
//            System.out.println(">> " + GenerateObjectUtil.getValue(Bean, attribution.get(0)));
        }

        /* 资产date 创建*/
        mongoCRUDDao.insert(Bean, "data");
        /* 更新 资产统计表*/
    }

    //读取完成之后
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("存储完成");
    }

}
