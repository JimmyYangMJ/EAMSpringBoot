package com.mc.eam.repairs.dao;

import java.util.List;
import java.util.Map;

/**
 * @author ymj
 * @Date： 2021/1/29 14:27
 * @description: flow 流程表 操作
 */
public interface MongoFlowDao {

    /**
     * 查询 某流程信息
     * @param flow 流程名称
     * @return 数据表单
     */
    List<Map> findFlow(String flow);
}
