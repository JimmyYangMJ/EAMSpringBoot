package com.mc.eam.repairs.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.common.ServerResponse;
import com.mc.eam.repairs.dao.MongoAssetDao;
import com.mc.eam.repairs.dao.impl.MongoUtilDaoImpl;
import com.mc.eam.repairs.service.impl.AssetBizImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bson.types.ObjectId;
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
@Api(tags = "资产操作")
@RestController
@RequestMapping("asset")
public class AssetController {

    @Autowired
    private AssetBizImpl assetBiz;
    @Autowired
    private MongoAssetDao mongoAssetDao;

    /**
     * 上传原始资产数据（创建 新数据库表：asset_，asset_statistics）
     * @param file 上传文件
     * @param assetSetName 数据资产名
     * @param requestParameter 请求体
     * @return 结果
     */
    @ApiOperation(value = "上传原始数据Set", notes = "上传 Excel 文档")
    @PostMapping("setExcel.do")
    public ServerResponse<String> uploadAssetData(MultipartFile file, String assetSetName,
                                  HttpServletRequest requestParameter) throws Exception {

        requestParameter.setAttribute("ContentType", "multipart/form-data");
        Map parameterMap = requestParameter.getParameterMap();
        if (file.isEmpty()) {
            return ServerResponse.createByNullMessage("上传失败，请选择文件");
        }
        // todo 保存文件 fileBiz.saveFile(file);

        /** 新增的数据表 document 是否存在 */
        if (assetBiz.containAssetSetName(assetSetName)){
            return ServerResponse.createByErrorMessage("资产数据表已存在");
        }else {
            /** 创建collection（不存在会自动创建）并 存入数据库*/
            assetBiz.excelToMongo(file, assetSetName, parameterMap);
            return  ServerResponse.createBySuccessMessage("已新建资产数据表");
        }
    }


    /**
     * 查询当前已有的资产集合名称
     * @return  没有被删的资产Set List
     */
    @ApiOperation(value = "资产集合名称", notes = "查询当前已有的资产集合名称列表")
    @GetMapping(value = "setNameList.do")
    public ServerResponse<List<String>> queryAssetSetNameList() {
        return ServerResponse.createBySuccess(assetBiz.queryAssetSetList(true));
    }

    /**
     * 删除 资产set 数据
     * @param assetSetName 删除的资产名称
     * @param isForever 是否永久删除
     * @return 结果状态
     */
    @ApiOperation(value = "删除资产集合Set数据", notes = "")
    @DeleteMapping(value = "set.do")
    public ServerResponse deleteAssetSet(String assetSetName, boolean isForever) {
        return assetBiz.deleteAssetSet(assetSetName, isForever);
    }

    /**
     * 维修资产 增加属性数据 data-kv
     * @param assetId 资产 id
     * @param assetSetName 资产类名称
     * @param request
     * @return todo 格式化
     */
    @ApiOperation(value = "维修资产 增加属性数据", notes = "data-kv")
    @PutMapping(value = "addAssetInfo.do")
    public ServerResponse<String> addAssetInfo(String assetId, String assetSetName,
                                 HttpServletRequest request) {
        System.out.println(assetId + assetSetName);
        Map<String,String[]> dateMap = new HashMap<>(request.getParameterMap());
        System.out.println(dateMap.toString());
        dateMap.remove("assetId");
        dateMap.remove("assetSetName");
        return ServerResponse.createBySuccessMessage(assetBiz.insertAssetData(dateMap, assetId,  assetSetName));

    }

    /**
     * 根据表名 查询 所有数据
     * @param collectionName 表名 eg:asset_647台阿里索电维修test
     * @return todo test
     */
    @ApiOperation(value = "查询某表所有数据", notes = "")
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
    @ApiOperation(value = "根据表名 分页查询", notes = "")
    @GetMapping(value = "queryPerPages.do")
    public ServerResponse<JSONObject> queryPerPages(String collectionName, Integer pageSize, Integer page) {
        return assetBiz.queryAssetPerPages(collectionName, pageSize,  page,  null);
    }

    /**
     * todo 资产流程发起
     * @param assetSetName  资产表名称 eg 647台阿里索电维修3
     * @param flowName 流程名称 eg 阿里索电维修
     * @return
     */
    @GetMapping(value = "flowBind.do")
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
    private MongoUtilDaoImpl mongoUtilDao;

    @PutMapping(value = "toNextStage.do")
    public ServerResponse<String> toNextStage(String assetSetName, String assetId,
                                                    HttpServletRequest request) {

        Map<String,String[]> httpDataMap = new HashMap<>(request.getParameterMap());

        httpDataMap.remove("assetId");
        httpDataMap.remove("assetSetName");

        //
        Map<String, Object> updateMap = new HashMap<>();
        Map<String, String> dataMap = new LinkedHashMap<>();
        Iterator<Map.Entry<String, String[]>> iter = httpDataMap.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<String, String[]> entry = iter.next();
            // 获取key
            String key = entry.getKey();
            // 获取value
            String value = entry.getValue()[0];
            dataMap.put(key, value);
        }
        updateMap.put("currentFlowStatus",dataMap);
        mongoUtilDao.updateDocumentValue(updateMap, assetId, "asset_" + assetSetName);
        return ServerResponse.createBySuccess();

    }
    /**
     *  测试用, 直接访问 dao 层
     */
    // todo 查询某资产 当前 资产阶段（所属流程 当前阶段。 ）
    // 根据当前阶段 查询  需要 增加的 资产表单数据 todo 填写资产数据
    // 更新当前阶段信息
    @GetMapping(value = "assetItem.do")
    public ServerResponse<JSONObject> assetItem(String assetSetName, String assetId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("_id", assetId);
        return ServerResponse.createBySuccess(
                mongoUtilDao.findValue("currentFlowStatus", hashMap , "asset_" + assetSetName));
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

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("stage.flowStepName", stageName);
        JSONObject jsonObject = mongoUtilDao.findValue("stage", hashMap, "flow");
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
