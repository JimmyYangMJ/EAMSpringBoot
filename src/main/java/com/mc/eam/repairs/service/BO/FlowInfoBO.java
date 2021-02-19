package com.mc.eam.repairs.service.BO;

import jdk.nashorn.internal.runtime.regexp.joni.constants.StringType;

import java.util.Date;

/**
 * @author ymj
 * @Date： 2021/2/5 15:45
 * @description: 流程信息
 */
public class FlowInfoBO {
    /** 流程名称*/
    private String name;
    private String _id;
    private String type;
    private Date createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
