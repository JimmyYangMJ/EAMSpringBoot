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

    JSONObject findValue(String keyName, Map filters, String collectionName);

    /**
     * 更新 数据库 中某 数据表项 信息
     * @param updateMap 需要更新的参数
     * @param id 唯一键值
     * @param tableName 数据表名称
     * @return count of changes
     */
    long updateDocumentValue(Map updateMap, String id, String tableName);


    /**
     * 某 tableName 中 某条数据（filters），某属性（key） 追加数据 （list）
     * @param arrayKey key
     * @param list 追加数据
     * @param filters
     * @param tableName 表
     * @return
     */
    long updateDocumentValue(String arrayKey, List list, Map filters, String tableName);

    /**
     * 删除 表
     * @param tableName 表名称
     * @return 响应码  ResponseCode
     */
    int deleteCollection(String tableName);

    /**
     * 删除 某 表 某符合条件的数据
     * @param tableName 数据表名称
     * @param filters 查询条件 <key, value>
     * @return
     */
    long deleteDocument(String tableName, Map filters);

    // 根据 条件查询 对应 某表项（document） 的 键值（ _id）

    /**
     * 某表中 根据条件查询 对应 唯一索引
     * @param tableName 表名
     * @param filters 查询条件
     * @return  对应 关键字/主键/唯一索引
     */
    List findUniqueIndex (String tableName, Map filters);



}
