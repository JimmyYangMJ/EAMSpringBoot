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
public interface MongoUtilDao {
    // 查询某表所有 document
    JSONArray queryDocuments(String collectionName);
    // 查询数据表collection 某名称列表


    /**
     * 查询 某 document 列表 中 某 key 对应  value(array类型) 的集合
     * @param collectionName
     * @param keyName 用 “ . ” 分割， 倒数第二个 key 对应的 value 为 array，再此之前 都不可为 array
     * @param id
     * @return 流程列表名
     */
    List<String> findNameList(String collectionName, String keyName, String id);

    /**
     * 查询所有document 列表 中 某 key 对应  value 的集合
     * @param collectionName
     * @param keyName
     * @return
     */
    List<String> findNameList(String collectionName, String keyName);

    JSONObject findValue(String keyName,  String queryKey, String queryValue, String collectionName);

    String updateDocumentValue(Map map, String id, String collectionName, String jsonKey);

    int deleteCollection(String collectionName);

    boolean deleteDocument(String collectionName, Map filters);

    // 根据 条件查询 对应 某表项（document） 的 键值（ _id）

    /**
     * 某表中 根据条件查询 对应 唯一索引
     * @param tableName 表名
     * @param filter 查询条件
     * @return  对应 关键字/主键/唯一索引
     */
    List findUniqueIndex (String tableName, Map filter);


}
