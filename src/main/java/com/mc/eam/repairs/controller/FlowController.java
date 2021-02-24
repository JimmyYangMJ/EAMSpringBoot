package com.mc.eam.repairs.controller;

import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.common.ServerResponse;
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
import java.util.List;
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
     * @param stageName 阶段名
     * @param flowName 流程名
     * @return 当前data, 下一阶段 名称 todo 格式
     */
    @GetMapping(value = "getStageForm.do")
    public String getStageInfo(@RequestParam("stage") String stageName,
                               @RequestParam("flow") String flowName ) {

        return flowBiz.getStageInfo(stageName, flowName);

    }
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 新增审批流程 直接json / todo  修改流程（后期）
     * @param flowForm
     * @return
     */
    @PutMapping(value = "assetJson.do")
    public String insertAssetFlow(@RequestBody JSONObject flowForm) {
        // todo 校验 name 是否重复，name 重复-type 是否重复
        JSONObject jsonObject =  mongoTemplate.insert(flowForm, "flow");
        System.out.println("***" + jsonObject.getString("_id"));
        System.out.println("***" + ConvertOperators.ToObjectId.toObjectId(jsonObject.getString("_id")));
        return jsonObject.getString("name");
    }


    /**
     * todo 给 资产数据表 中的某数据项 绑定数据流程
     * @param assetId 资产 id
     * @param flowId 流程 id
     * @param remark 备注
     * @return
     */
    @PutMapping(value = "bindAsset.do")
    public ServerResponse<String> bindAssetFlow(String assetId, String flowId, String remark) {
        return null;
    }

    /**
     * 查询所有流程名称列表，
     * @return data list 返回 <流程id, 流程名称>
     */
    @GetMapping(value = "allName.do")
    public ServerResponse<List<String>> allName() {
        return flowBiz.queryFlowNameList();
    }


    // todo 删除流程

    // todo 修改资产流程
}
