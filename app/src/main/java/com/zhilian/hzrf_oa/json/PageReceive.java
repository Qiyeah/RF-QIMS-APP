package com.zhilian.hzrf_oa.json;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017-1-9.
 */
public class PageReceive implements Serializable {

    private static final long serialVersionUID = -5395997221963176643L;
    private List<T_Receive> list;
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private int totalRow;

    public PageReceive() {
    }

    public List<T_Receive> getList() {
        return list;
    }

    public void setList(List<T_Receive> list) {
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

    @Override
    public String toString() {
        return "PageReceive{" +
            "list=" + list +
            ", pageNumber=" + pageNumber +
            ", pageSize=" + pageSize +
            ", totalPage=" + totalPage +
            ", totalRow=" + totalRow +
            '}';
    }
}
