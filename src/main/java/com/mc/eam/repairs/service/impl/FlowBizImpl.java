package com.mc.eam.repairs.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.common.ServerResponse;
import com.mc.eam.repairs.dao.MongoFlowDao;
import com.mc.eam.repairs.service.BO.FlowInfoBO;
import com.mc.eam.repairs.service.FlowBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author ymj
 * @Date： 2021/1/29 16:16
 * @description: 流程
 */
@Service("FlowBiz")
public class FlowBizImpl implements FlowBiz {

    @Autowired
    private MongoFlowDao mongoFlowDao;

    /**
     * 获取 某流程 某阶段 form 信息
     * @param stageName
     * @param flowName
     * @return
     */
    public String getStageInfo(String stageName, String flowName) {
        List<Map> documents = mongoFlowDao.findFlow(flowName);
        Iterator<Map> iterator2 = documents.iterator();
        Map map = null;
        while (iterator2.hasNext()) {
            map = iterator2.next();
//            json = new JSONObject(map);
        }
        JSONObject formObject = new JSONObject();
        /* 添加当前阶段的 form 信息*/
        List<Map> list = (List<Map>) map.get("stage");
        Iterator<Map> iterator = list.iterator();
        while (iterator.hasNext()) {
            Map mapTemp = iterator.next();
            if(mapTemp.get("name").equals(stageName)) {
                System.out.println(new JSONObject((Map) mapTemp.get("form")));

                formObject.put("form", new JSONObject((Map) mapTemp.get("form")));
                /* 添加下一阶段名称*/
                if(iterator.hasNext()){
                    formObject.put("nextStage", iterator.next().get("name"));
                } else {
                    formObject.put("nextStage", "end");
                }
            }
        }

        return formObject.toJSONString();
    }

    @Override
    public ServerResponse<String> insertFlow(String document) {
        return null;
    }

    @Override
    public ServerResponse<List<FlowInfoBO>> queryFlowList(String document) {
        return null;
    }

}
