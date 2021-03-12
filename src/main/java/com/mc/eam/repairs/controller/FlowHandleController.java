package com.mc.eam.repairs.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.web.bind.annotation.*;

/**
 * @author ymj
 * @Date：
 * @description: 流程处理
 */
@RestController
@RequestMapping("flowHandle")
public class FlowHandleController {

    /**
     * 新增审批流程 直接json
     * @param flowForm
     *            {cells··· ···},
     * @return
     */
    @PutMapping(value = "Json.do")
    public String insertAssetFlow(@RequestBody JSONObject flowForm) {
        // todo 校验 name 是否重复，name 重复-type 是否重复
        return null;
    }

    // todo 流程绑定

}
