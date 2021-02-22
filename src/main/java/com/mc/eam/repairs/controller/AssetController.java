package com.mc.eam.repairs.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.common.ServerResponse;
import com.mc.eam.repairs.dao.MongoAssetDao;
import com.mc.eam.repairs.dao.impl.MongoUtilImpl;
import com.mc.eam.repairs.service.impl.AssetBizImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author ymj
 * @Date： 2021/1/25 16:53
 * @description: 填表,CRUD
 */
@RestController
@RequestMapping("table")
public class AssetController {

    @Autowired
    private AssetBizImpl assetBiz;
    @Autowired
    private MongoAssetDao mongoAssetDao;

    @GetMapping("test.do")
    public  String test(){
        return null;
    }
    /**
     * 上传原始资产数据（创建 新数据库表：asset_，asset_statistics）
     * @param file 上传文件
     * @param assetSetName 数据资产名
     * @param requestParameter 请求体
     * @return 结果
     */
    @PostMapping("uploadAssetData.do")
    public ServerResponse<String> uploadAssetData(MultipartFile file, String assetSetName,
                                  HttpServletRequest requestParameter) throws Exception {

        requestParameter.setAttribute("ContentType", "multipart/form-data");
        // todo response 设置
        Map parameterMap = requestParameter.getParameterMap();
        if (file.isEmpty()) {
            return ServerResponse.createByErrorMessage("上传失败，请选择文件");
        }
        // todo 保存文件 fileBiz.saveFile(file);

        /**  新增的数据表 document 是否存在 */
        if (assetBiz.containAssetName(assetSetName)){
            return ServerResponse.createByErrorMessage("资产数据表已存在");
        }else {
            /** 创建collection （不存在会自动创建）并 存入数据库*/
            assetBiz.excelToMongo(file, assetSetName, parameterMap);
            return  ServerResponse.createBySuccessMessage("已新建资产数据表");
        }
    }

    /**
     * 维修资产 增加属性数据 data-kv
     * @param assertId 资产 id
     * @param assertSetName 资产类名称
     * @param request
     * @return todo 格式化
     */
    @PutMapping(value = "addAssertInfo.do")
    public String addAssetInfo(String assertId, String assertSetName,
                                 HttpServletRequest request) {
        System.out.println(assertId + assertSetName);
        Map<String,String[]> dateMap = new HashMap<>(request.getParameterMap());
        System.out.println(dateMap.toString());
        dateMap.remove("assertId");
        dateMap.remove("assertSetName");
        // todo 更新资产统计表
        return assetBiz.insertAssetData(dateMap, assertId,  assertSetName);
    }

    // todo 资产绑定默认流程
    @PutMapping(value = "assertBind.do")
    public String assertBind( @RequestParam("assertId") String assertId,
                                 @RequestParam("flowId") String flowId,
                                 HttpServletRequest request) {
        return null;
    }

    /**
     * 根据表名 查询
     * @param collectionName 表名 eg:asset_647台阿里索电维修test
     * @return todo test
     */
    @GetMapping(value = "query.do")
    public String query(String collectionName) {
        return mongoAssetDao.queryAll(collectionName);
    }

    /**
     * 根据表名 分页查询
     * @param collectionName 表名称
     * @param pageSize 页大小
     * @param page 某页
     * @return
     */
    @GetMapping(value = "queryPerPages.do")
    public ServerResponse<JSONObject> queryPerPages(String collectionName, Integer pageSize, Integer page) {
        return assetBiz.queryAssetPerPages(collectionName, pageSize,  page,  null);
    }

    /**
     * 查询当前已有的资产数据
     * @return todo
     */
    @GetMapping(value = "AssetNameList.do")
    public ServerResponse<List<String>> queryAssetNameList() {
        return ServerResponse.createBySuccess(mongoAssetDao.selectAssetSetNameList());
    }

    /**
     * todo 资产流程发起
     * @param assetSetName  资产表名称 eg 647台阿里索电维修3
     * @param flowName 流程名称 eg 阿里索电维修
     * @return
     */
    @GetMapping(value = "assetFlowStart.do")
    public ServerResponse<String> assetFlowStart(String assetSetName, String flowName) {
        return ServerResponse.createBySuccess("success",assetBiz.AssetFlowStart(assetSetName, flowName));
    }

    // todo 查询 个人 待处理事件 currentFlowStatus.pendingStaff ==
    @GetMapping(value = "individualHandle.do")
    public ServerResponse<JSONObject> individualHandle(String assetSetName, Integer pageSize, Integer page, String staffName) {
        Map map = new HashMap();
        map.put("currentFlowStatus.pendingStaff", staffName);
        return assetBiz.queryAssetPerPages("asset_" + assetSetName, pageSize, page, map);
    }


    @Autowired
    private MongoUtilImpl mongoUtil;
    /**
     *  测试用, 直接访问 dao 层
     */
    // todo 查询某资产 当前 资产阶段（所属流程 当前阶段。 ）
    // 根据当前阶段 查询  需要 增加的 资产表单数据 todo 填写资产数据
    // 更新当前阶段信息
    @GetMapping(value = "assetItem.do")
    public ServerResponse<JSONObject> assetItem(String assetSetName, String assetId) {
        return ServerResponse.createBySuccess(
                mongoUtil.findValue("currentFlowStatus", "_id", assetId, "asset_" + assetSetName));
    }

    // todo 查询 需要填写 的表项
    /**
     * 查询 某阶段需要增添的资产数据信息
     * @param stageName 阶段名
     * @param flowName 流程名
     * @return 当前data, 下一阶段 名称 todo 格式
     */
    @GetMapping(value = "getStageForm.do")
    public ServerResponse<JSONObject> getStageInfo(@RequestParam("stage") String stageName,
                               @RequestParam("flow") String flowName ) {

        JSONObject jsonObject = mongoUtil.findValue("stage", "stage.flowStepName", stageName, "flow");
        JSONArray jsonArray = jsonObject.getJSONArray("stage");
        for (Object j: jsonArray) {
            JSONObject jo = (JSONObject) j;
            System.out.println(jo.toJSONString());

            if (jo.containsKey("flowStepName") && jo.getString("flowStepName").equals(stageName)) {
                return ServerResponse.createBySuccess( jo );
            }
        }

        return ServerResponse.createByError();



    }

    // todo 删除资产表

    // todo 删除资产数据item


}
