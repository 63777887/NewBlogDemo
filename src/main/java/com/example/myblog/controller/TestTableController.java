package com.example.myblog.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.myblog.bean.TestTable;
import com.example.myblog.bean.TestTableWithChild;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
public class TestTableController {

    @GetMapping("/testTable")
    public String testTable(Model model, HttpServletRequest request){
        TestTable a = new TestTable(1001, "A", null, 1);
        TestTable b = new TestTable(1002, "B", 1001, 1);
        TestTable c = new TestTable(1003, "C", 1001, 2);
        TestTable d = new TestTable(1004, "D", 1002, 1);
        TestTable e = new TestTable(1005, "E", 1002, 2);
        ArrayList<TestTable> testTables = new ArrayList<>();
        testTables.add(a);
        testTables.add(b);
        testTables.add(c);
        testTables.add(d);
        testTables.add(e);
        ArrayList<TestTableWithChild> testTableWithChildren = new ArrayList<>();
        testTables.forEach(t->{
                TestTableWithChild testTableWithChild = new TestTableWithChild();
                BeanUtil.copyProperties(t, testTableWithChild);
            ArrayList<TestTableWithChild> testTables1 = new ArrayList<>();
            testTableWithChild.setChilds(testTables1);
                testTableWithChildren.add(testTableWithChild);

        });



        testTableWithChildren.forEach(parent->{
               testTableWithChildren.forEach(child->{
                   if (child.getParentId()!=null) {
                       if (parent.getId().equals(child.getParentId())) {
                           parent.getChilds().add(child);
                       }
                   }
               });

            Integer numRow = TestTableWithChild.findNumRow(parent);
            parent.setRow(numRow);
            parent.setCol((int) Math.pow(2, (numRow - 1)));
        });
        System.out.println(testTableWithChildren.get(0).toString());

        model.addAttribute("tableTest", testTableWithChildren);
        request.setAttribute("tableTest",testTableWithChildren);

        return "testTable";

    }
}
