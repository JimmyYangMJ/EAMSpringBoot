package com.mc.eam.repairs.controller;

import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.dao.MongoFlowDao;
import com.mc.eam.repairs.dao.impl.MongoAssetDaoImpl;
import com.mc.eam.repairs.service.FlowBiz;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ymj
 * @Date： 2021/1/29 14:13
 * @description: 流程操作
 */
@RestController
@RequestMapping("flow")
public class FlowController {

    @Autowired
    private FlowBiz flowBiz;

    /**
     * 查询 某阶段需要增添的资产数据信息
     * @param stageName
     * @param flowName
     * @return 当前data, 下一阶段 名称
     */
    @GetMapping(value = "getStageForm.do")
    public String getStageInfo(@RequestParam("stage") String stageName,
                               @RequestParam("flow") String flowName ) {

        return flowBiz.getStageInfo(stageName, flowName);

    }
    @Autowired
    private MongoTemplate mongoTemplate;

    // todo 新增审批流程 / 修改流程（后期）
    @PutMapping(value = "asset.do")
    public String insertAssetFlow(@RequestBody JSONObject flowForm) {
        // todo 校验 name 是否重复，name 重复-type 是否重复
        JSONObject jsonObject =  mongoTemplate.insert(flowForm, "flow");
        System.out.println("***" + jsonObject.getString("_id"));
        System.out.println("***" + ConvertOperators.ToObjectId.toObjectId(jsonObject.getString("_id")));
        return jsonObject.getString("name");
    }

    // todo 查询所有流程，

    // todo 删除流程

    // todo 修改资产流程
}
