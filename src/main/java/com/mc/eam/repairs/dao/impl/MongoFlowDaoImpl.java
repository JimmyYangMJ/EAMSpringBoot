package com.mc.eam.repairs.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.dao.MongoFlowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author ymj
 * @Date： 2021/1/29 14:18
 * @description: flow（流程表） 操作
 */
@Component
public class MongoFlowDaoImpl implements MongoFlowDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询 某流程信息
     * @param flow 流程名称
     * @return 数据表单
     */
    public List<Map> findFlow(String flow) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(flow));

        /** 条件查询 */
        return mongoTemplate.find(query, Map.class, "flow");

    }
}
