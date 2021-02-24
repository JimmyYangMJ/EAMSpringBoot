package com.mc.eam.repairs.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bson.Document;

import java.util.List;
import java.util.Map;

/**
 * @author ymj
 * @Date： 2021/2/5 15:37
 * @description: 通用操作
 */
public interface MongoUtil {
    // 查询某表所有 document
    JSONArray queryDocuments(String collectionName);
    // 查询数据表collection 某名称列表


    /**
     * 查询 某 document 列表
     * @param collectionName
     * @param keyName
     * @param id
     * @return 流程列表名
     */
    List<String> findNameList(String collectionName, String keyName, String id);

    /**
     * 查询所有document 列表
     * @param collectionName
     * @param keyName
     * @return
     */
    List<String> findNameList(String collectionName, String keyName);

    JSONObject findValue(String keyName,  String queryKey, String queryValue, String collectionName);

    String updateDocumentValue(Map map, String id, String collectionName, String jsonKey);

    // 查询 arrayList
    /**
     * 1. 查询每个 document中的 某 key 的value
     * 2. 查询 某 document中的 某 key 对应 json 的 某 key 的list
     */

}
