package com.zhilian.hzrf_oa.json;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017-9-12.
 */
public class PageSend implements Serializable {
    private static final long serialVersionUID = -5395997221963176619L;
    private List<T_Send> list;
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private int totalRow;

    public PageSend() {
    }

    public PageSend(List<T_Send> list, int pageNumber, int pageSize, int totalPage, int totalRow) {
        this.list = list;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalRow = totalRow;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<T_Send> getList() {
        return list;
    }

    public void setList(List<T_Send> list) {
        this.list = list;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }
}
