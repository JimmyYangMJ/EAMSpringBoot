package com.mc.eam.repairs.dao;

import com.alibaba.fastjson.JSONArray;
import org.bson.Document;

import java.util.Map;
import java.util.Set;

/**
 * @author ymj
 * @Date： 2021/1/29 9:51
 * @description:
 */
public interface MongoAssetDao {

    /**
     * 插入 动态 Object （新增）
     * @param objectBean 要存储的 Javabean
     * @param objKey 需要包装为嵌套 Object 的 中的 key，当key 为 null 及非嵌套
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    <T> boolean insert(Object objectBean, String objKey) throws IllegalAccessException;

    /**
     *  查看当前数据库 某类型 数据表/ collection
     * @param type 类型 assert：资产
     * @return
     */
    Set selectContainCollection(String type);

    /**
     *  指定资产 插入 data 数据 (key 相同时 更新  value)
     * @param map data 数据参数
     * @param id  资产 id
     * @param collectionName  集合名称（数据表）
     * @return
     */
    String updateAssetData(Map map, String id, String collectionName);

    /**
     * 更新资产统计表
     * @param parameter 更新参数
     * @param assetName 资产名称
     * @param id 资产id
     * @return
     */
    String updateAssetStatistics(Document parameter, String assetName, String id);
    /**
     * 查询collection document数量
     * @param parameterMap 查询条件参数
     * @param collectionName 数据表名称
     * @return document count
     */
    long selectDocumentCount(Map parameterMap, String collectionName);

    /**
     * 根据表名查询所有 document
     * @param collectionName
     * @return
     */
    String queryAll(String collectionName);

    /**
     * todo 分页查询
     * 根据表明
     */
    JSONArray queryPerPages(String collectionName, Integer pageSize, Integer page);

}
