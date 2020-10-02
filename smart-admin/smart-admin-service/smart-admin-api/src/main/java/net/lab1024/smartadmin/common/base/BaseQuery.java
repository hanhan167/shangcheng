package net.lab1024.smartadmin.common.base;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private transient Integer rowSrt;// 开始索引
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private transient Integer endIndex;// 结束索引
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private transient String sortFields;// 排序字段
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private transient Map<String, Object> queryData;// 查询扩展
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private transient String keywords;// 关键字查询
    private transient Integer pageSize = 10; // 每页多少项
    private transient Integer pageNo = 1;   // 请求页码

    public Integer getRowSrt() {
        return rowSrt;
    }

    public void setRowSrt(Integer rowSrt) {
        this.rowSrt = rowSrt;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return this.pageNo;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Integer getPageSize() {
        if(endIndex != null && rowSrt != null) {
            return endIndex - rowSrt;
        }
        return null;
    }

    public String getSortFields() {
        return sortFields;
    }

    public void setSortFields(String sortFields) {
        this.sortFields = sortFields;
    }

    public Map<String, Object> getQueryData() {
        if(queryData != null && queryData.size() > 0) {
            return queryData;
        }
        return null;
    }

    //添加其它查询数据
    public void addQueryData(String key,Object value) {
        if(queryData == null) {
            queryData = new HashMap<String, Object>();
        }
        queryData.put(key, value);
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getkeywords() {
        return keywords;
    }
}
