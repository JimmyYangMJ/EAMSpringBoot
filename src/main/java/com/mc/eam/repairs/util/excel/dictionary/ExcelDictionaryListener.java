package com.mc.eam.repairs.util.excel.dictionary;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ymj
 * @Date： 2021/1/12 14:04
 * @description:
 */
@Component
public class ExcelDictionaryListener extends AnalysisEventListener<Map<Integer, String>> {

    public static HashMap<String, ArrayList<String>> dictionaryMap = new HashMap<>();

    static Map headMap;

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头："+headMap);
        this.headMap = headMap;
    }

    @Override
    public void invoke(Map<Integer, String> map, AnalysisContext analysisContext) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(map.get(1));
        arrayList.add(map.get(2));
        dictionaryMap.put(map.get(0), arrayList);
    }

    //读取完成之后
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("size:" + dictionaryMap.size());
    }

}
