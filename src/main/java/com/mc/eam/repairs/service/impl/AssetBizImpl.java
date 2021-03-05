package com.mc.eam.repairs.service.impl;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mc.eam.repairs.common.ResponseCode;
import com.mc.eam.repairs.common.ServerResponse;
import com.mc.eam.repairs.dao.impl.MongoAssetDaoImpl;
import com.mc.eam.repairs.dao.impl.MongoFlowDaoImpl;
import com.mc.eam.repairs.dao.impl.MongoUtilDaoImpl;
import com.mc.eam.repairs.service.AssetBiz;
import com.mc.eam.repairs.util.FileUtil;
import com.mc.eam.repairs.util.excel.*;
import com.mongodb.client.ClientSession;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * 资产相关操作
 * @author ymj
 * @Date： 2021/1/25 17:37
 * @description: 文件处理
 */
@Service
public class AssetBizImpl implements AssetBiz {

    @Autowired
    private ExcelListener excelListener;

    @Autowired
    private MongoAssetDaoImpl mongoAssetDao;

    @Autowired
    private MongoFlowDaoImpl mongoFlowDao;

    @Autowired
    private MongoUtilDaoImpl mongoUtilDao;

    /**
     * 保存文件
     * @param file
     * @return 是否成功
     */
    @Override
    public boolean saveFile(MultipartFile file, String filePath) {
        String fileName = file.getOriginalFilename(); // 获取文件名
        fileName = "原始文件.xlsx";
        filePath = "C:\\Users\\dell\\Desktop\\MC资产管理系统EAM\\test2\\";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            System.out.println("success");
            return true;
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return false;
    }


    /**
     * excel 文件数据 保存 至 Mongo DB 中
     * @param mFile 上传的 excel 文件
     * @param assetSetName 资产表名称
     * @param requestMap 请求体 参数
     * @return 是否插入成功
     * @throws Exception
     */
    @Override
    public boolean excelToMongo(MultipartFile mFile, String assetSetName, Map requestMap) throws Exception {
        // 更新 数据表名/document

        File file = FileUtil.multipartFileToFile(mFile);

        excelListener.setCollectionName("asset_" + assetSetName);
        EasyExcel.read(file, excelListener).sheet("Sheet1").doRead();

        /** 更新资产统计表 asset_statistics
         *   assetSetName : 647台阿里索电维修
         *   isDelete ： false / null
             department：部门
             charge：负责人
             count：数量
             default_flow： 默认流程
            todo 增加资产属性 数组 ：dataDictionary
         */
        Document document = new Document();
        document.put("count", mongoAssetDao.selectDocumentCount(null, "asset_" + assetSetName));
        Iterator<Map.Entry<String, String[]>> iter = requestMap.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<String, String[]> entry = iter.next();
            document.put(entry.getKey(), entry.getValue()[0]);
        }
        mongoAssetDao.updateAssetStatistics(document, assetSetName, null);

        // 更新 asset_statistics ，数据字典数组数据
        HashMap<String, Object> filters = new HashMap<>();
        filters.put("assetSetName", assetSetName);
        System.out.println(mongoUtilDao.updateDocumentValue("dataDictionary",
                excelListener.getDataDictionaryArray(), filters,
                "asset_statistics"));

        return true;
    }

    /**
     * 资产数据 分页查询
     * @param collectionName
     * @param pageSize
     * @param page
     * @return
     */
    @Override
    public ServerResponse<JSONObject> queryAssetPerPages(String collectionName, Integer pageSize, Integer page, Map queryMap)  {
        JSONArray dataList ;
        long dataSum = 0;

        dataList = mongoAssetDao.queryPerPages(collectionName, pageSize, page, queryMap);
        dataSum = mongoAssetDao.selectDocumentCount(queryMap, collectionName);

//        if(dataSum == 0 ){
//            return ServerResponse.createByErrorMessage("没有相应记录");
//        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sum", dataSum);
        jsonObject.put("dataList", dataList);
        System.out.println(jsonObject.toJSONString());
        System.out.println(jsonObject.toString());
        return  ServerResponse.createBySuccess("查询成功", jsonObject);
    }

    @Override
    public ServerResponse<Arrays> allAssetNameList() {
        return null;
    }

    /**
     * 查询 某 资产表 是否存在, 包括 isDelete = true
     * @param assetSetName
     * @return
     */
    @Override
    public boolean containAssetSetName(String assetSetName){
        return  mongoAssetDao.selectAssetSetNameList(true).contains(assetSetName);
    }

    @Override
    public String insertAssetData(Map httpDateMap, String assertId, String assertSetName) {
        Map<String, String> dataMap = new LinkedHashMap<>();
        Iterator<Map.Entry<String, String[]>> iter = httpDateMap.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<String, String[]> entry = iter.next();
            // 获取key
            String key = entry.getKey();
            // 获取value
            String value = entry.getValue()[0];
            dataMap.put(key, value);
        }
        return  mongoAssetDao.updateAssetData(dataMap, assertId, "asset_" + assertSetName, "data");
    }


    @Override
    public String AssetFlowStart(String assetSetName, String flowName) {
        //  查询 流程信息
        List<Map> documents = mongoFlowDao.findFlow(flowName);
        if (documents.size() != 1) {
            System.out.println("查询流程出错： " + documents.size());
            return "error";
        }
        List<Map> stages = (List<Map>) documents.get(0).get("stage");
        System.out.println(stages);
        Map<String, String> updateDataMap = new LinkedHashMap(7);
        updateDataMap.put("flow", flowName);
        updateDataMap.put("currentStage",stages.get(1).get("flowStepName").toString() );
        updateDataMap.put("nextStage", stages.get(2).get("flowStepName").toString());
        updateDataMap.put("pendingStaff",stages.get(1).get("handler_id").toString() );
        return  mongoAssetDao.updateAssetData(updateDataMap, null , "asset_" +assetSetName, "currentFlowStatus");

    }

    // todo 删除某 资产集合 数据
    @Override
    public ServerResponse deleteAssetSet(String assetSetName, boolean isForever) {
        HashMap hashMap = new HashMap<String, String>();
        hashMap.put("assetSetName", assetSetName);
        List<String> idList = mongoUtilDao.findUniqueIndex("asset_statistics", hashMap);
        String id;
        if(idList.size() == 1) {
            id =  idList.get(0);
        }else {
            return ServerResponse.createByErrorMessage("资产名异常");
        }
        if (isForever) {
            // 删除 数据库
            // todo 需要原子操作，或者 事务 。 增加某些机制
            /**
             *  ClientSession clientSession = null;
             *         clientSession.startTransaction();
             *  import com.mongodb.client.ClientSession;
             */
//            ClientSession clientSession = null;
//            clientSession.startTransaction();
            int answerCode = mongoUtilDao.deleteCollection("asset_" + assetSetName);
            if(answerCode == ResponseCode.SUCCESS.getCode()) {
                // 删除 asset_statistics 中 对应 document 信息
                Map map = new HashMap<String, Object>();
                map.put("_id", new ObjectId(id));
                mongoUtilDao.deleteDocument("asset_statistics", map);
            } else if (answerCode == ResponseCode.NULL.getCode()){
                return ServerResponse.createByErrorMessage("已删除");
            }
//            clientSession.abortTransaction();
        } else {
            System.out.println("暂时删除");
            Map map = new HashMap<String, String>(5);
            map.put("isDelete", "true");
            // 更新 asset_statistics 数据表 信息
            mongoUtilDao.updateDocumentValue(map, id,"asset_statistics");

        }
        return ServerResponse.createBySuccess(ResponseCode.SUCCESS.getDesc());
    }


    /**
     * 查询资产set数据名 List
     * @param notDelete 是否删除，一般为 true
     * @return
     */
    @Override
    public List<String> queryAssetSetList(boolean notDelete) {

        return  mongoAssetDao.selectAssetSetNameList(notDelete);
    }

}
