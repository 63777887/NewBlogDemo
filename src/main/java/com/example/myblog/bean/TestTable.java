package com.example.myblog.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

@Data
@AllArgsConstructor
public class TestTable implements Serializable {
    private Integer id;
    private String text;
    private Integer parentId;
    private Integer order;


    public static void main(String[] args) throws UnsupportedEncodingException {
        //编码转换，怎样实现将GB2312编码的字符串转换为ISO-8859-1编码的字符串。
        String s=new String("季伟宽".getBytes("gb2312"));
        System.out.println(s);

        String s1=new String(s.getBytes(),"iso-8859-1");
        System.out.println(s1);
        String a1=new String("季伟宽".getBytes("gb2312"));
        String a=new String(a1.getBytes(),"iso-8859-1");
        System.out.println(a);

    }
}
