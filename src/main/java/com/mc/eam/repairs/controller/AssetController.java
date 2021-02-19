package com.mc.eam.repairs.controller;

import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.common.ServerResponse;
import com.mc.eam.repairs.dao.MongoAssetDao;
import com.mc.eam.repairs.service.impl.AssetBizImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 上传原始资产数据（创建 新数据库表：asset_，asset_statistics）
     * @param file 上传文件
     * @param assetSetName 数据资产名
     * @param requestParameter 请求体
     * @return 结果 todo 格式化
     */
    @PostMapping("uploadAssetData.do")
    public String uploadAssetData(MultipartFile file, String assetSetName,
                                  HttpServletRequest requestParameter) throws Exception {

        requestParameter.setAttribute("ContentType", "multipart/form-data");
        // todo response 设置
        Map parameterMap = requestParameter.getParameterMap();
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        // todo 保存文件 fileBiz.saveFile(file);

        /**  新增的数据表 document 是否存在 */
        if (mongoAssetDao.selectContainCollection("asset").contains(assetSetName)){
            return "资产数据表已存在";
        }else {
            /** 创建collection （不存在会自动创建）并 存入数据库*/
            assetBiz.excelToMongo(file, assetSetName, parameterMap);
            return  "success";
        }
    }

    /**
     * 维修资产 增加 data-kv
     * @param assertId 资产 id
     * @param assertSetName 资产类名称
     * @param request
     * @return todo 格式化
     */
    @PutMapping(value = "addAssertInfo.do")
    public String addAssertInfo(String assertId, String assertSetName,
                                 HttpServletRequest request) {
        System.out.println(assertId + assertSetName);
        Map<String,String[]> dateMap = new HashMap<>(request.getParameterMap());
        System.out.println(dateMap.size());
        dateMap.remove("assertId");
        dateMap.remove("assertSetName");
        // todo 更新资产统计表
        System.out.println(dateMap.size());
        return mongoAssetDao.updateAssetData(dateMap, assertId, "asset_" + assertSetName);
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
     * @param collectionName 表名
     * @return
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
        return assetBiz.queryAssetPerPages(collectionName, pageSize,  page);
    }
    /**
     * todo 查询当前已有的资产数据
     */
    @GetMapping(value = "queryAssetNameList.do")
    public String queryAssetNameList() {
        JSONObject formObject = new JSONObject();
        formObject.put("assetList", mongoAssetDao.selectContainCollection("asset"));
        return formObject.toJSONString();
    }



}
