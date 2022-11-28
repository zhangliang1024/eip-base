package com.eip.common.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ClassName: CalculateUtil
 * Function: 手动实现分页
 * Date: 2021年12月08 13:22:19
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class PageUtils<T> {

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 当前页显示多少条数据
     */
    private Integer pageSize;

    /**
     * 数据集合
     */
    private List<T> list;


    /**
     * @param list     查询的数据
     * @param current  当前页
     * @param pageSize 当前页显示多少条数据
     */
    public PageUtils(List<T> list, Integer current, Integer pageSize) {
        this.current = current;
        this.pageSize = pageSize;
        this.total = list.size();
        // 总记录数和每页显示的记录数据之间是否可以凑成整数(pages)
        boolean full = total % pageSize == 0;
        // 分页 == 根据pageSize(每页显示的记录数) 计算pages
        if (!full) {
            this.pages = total / pageSize + 1;
        } else {
            // 如果凑成整数
            this.pages = total / pageSize;
        }
        int fromIndex = 0;
        int toIndex = 0;
        fromIndex = current * pageSize - pageSize;
        if (current == 0) {
            throw new ArithmeticException("第0页无法展示");
        } else if (current > pages) {
            // 如果查询的页码数大于总的页码数，list设置为[];
            list = new ArrayList<>();
        } else if (Objects.equals(current, pages)) {
            // 如果查询的当前页等于总页数，直接索引到total处;
            toIndex = total;
        } else {
            // 如果查询的页码数小于总数，不用担心切割List的时候toIndex索引会越界，这里就直接等.
            toIndex = current * pageSize;
        }
        if (list.size() == 0) {
            this.list = list;
        } else {
            this.list = list.subList(fromIndex, toIndex);
        }

    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}

