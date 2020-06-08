package com.example.myblog.bean;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

import java.util.List;

public class TestTableWithChild {
    private Integer id;
    private String text;
    private Integer parentId;
    private Integer order;
    private Integer row;
    private Integer col;
    private List<TestTableWithChild> childs;

    public TestTableWithChild() {
        row=1;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }


    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<TestTableWithChild> getChilds() {
        return childs;
    }

    public void setChilds(List<TestTableWithChild> childs) {
        this.childs = childs;
    }

    @Override
    public String toString() {
        return "TestTableWithChild{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", parentId=" + parentId +
                ", order=" + order +
                ", row=" + row +
                ", col=" + col +
                ", childs=" + childs +
                '}';
    }

    private boolean hasChild(){
        return CollUtil.isNotEmpty(this.childs);

    }

    public static void main(String[] args) {

    }

    public static int findNumRow(TestTableWithChild table){
            int add=table.getRow();
        if (table.hasChild()){
            add=add+1;
            table.setRow(add);
            findNumRow(table.childs.get(0));
        }
        table.setRow(add);
        System.out.println(add);
        return add;
    }
}