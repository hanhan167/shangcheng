package net.lab1024.smartadmin.module.business.goods.domain.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenkai_yy
 * @dateTime 2019/1/16 10:59
 * @className com.migu.spms.webapi.data.UserInfoAllDTO
 * @description
 **/
@Data
public class PageQueryDTO {

    /**
     * 目标页
     **/
    @Min(value = 1,message = "页码不能小于1")
    private Integer pageNo;

    /**
     * 每页条数
     **/
    @Min(value = 10,message = "条数最少10条")
    private Integer pageSize;
    /**
     * 查询字段
     */
    private Map<String,Object> queryFields;



    /**
     * 获取当前页页码
     * @return
     */
    public int getCurrentPage() {
        if (pageNo == 0) {
            pageNo = 1;
        }
        return pageNo;
    }

    /**
     * 获取开始索引
     * @return
     */
    public int getRowSrt() {
        return (getCurrentPage() - 1) * this.pageSize;
    }


    /**
     * 获取结束索引
     * @return
     */
    public void putPage() {
        if(null==this.queryFields){
            this.queryFields=new HashMap<>();
        }
        this.queryFields.put("rowSrt",getRowSrt());
        this.queryFields.put("pageSize",this.pageSize);
    }








}
