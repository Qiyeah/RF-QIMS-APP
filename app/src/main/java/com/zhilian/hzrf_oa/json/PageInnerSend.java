package com.zhilian.hzrf_oa.json;

import java.util.List;

/**
 * Created by Administrator on 2017-9-14.
 */
public class PageInnerSend {
    private boolean lastPage;
    private int pageSize;
    private int pageNumber;
    private boolean firstPage;
    private List<T_InnerSend> list;
    private int totalRow;
    private int totalPage;
    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }
    public boolean getLastPage() {
        return lastPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageSize() {
        return pageSize;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    public int getPageNumber() {
        return pageNumber;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }
    public boolean getFirstPage() {
        return firstPage;
    }

    public void setList(List<T_InnerSend> list) {
        this.list = list;
    }
    public List<T_InnerSend> getList() {
        return list;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }
    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    @Override
    public String toString() {
        return "PageInnerSend{" +
            "lastPage=" + lastPage +
            ", pageSize=" + pageSize +
            ", pageNumber=" + pageNumber +
            ", firstPage=" + firstPage +
            ", list=" + list +
            ", totalRow=" + totalRow +
            ", totalPage=" + totalPage +
            '}';
    }
}
