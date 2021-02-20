package com.mc.eam.repairs.service;

import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.common.ServerResponse;
import com.mc.eam.repairs.service.BO.FlowInfoBO;
import org.json.JSONException;

import java.util.List;

/**
 * @author ymj
 * @Date： 2021/1/29 16:17
 * @description:
 */
public interface FlowBiz {

    String getStageInfo(String stageName, String flowName);

    ServerResponse<String> insertFlow(String document);

    // todo 查询流程列表
    ServerResponse<List<String>> queryFlowNameList();

}
