package com.truck.portal.gateway.servlet;

import com.truck.utils.gateway.utils.define.ConstField;
import com.truck.utils.gateway.utils.util.HexStringUtil;
import com.truck.utils.gateway.utils.util.Md5Util;

import java.util.Arrays;

/**
 * Created by Administrator on 2016-05-19.
 */
public class Test {
    public static void main(String[] args) {
        String sig = "387b33e98b1dab8cdf57f8fa7e927ae0";//页面传过来的_sig，加密串
        String key = "abcd";//签名字符串
        StringBuilder sb = new StringBuilder("a=11&c=9&m=11");//按字母排序的参数串
        byte[] expect = HexStringUtil.toByteArray(sig);
        byte[] actual = Md5Util.compute(sb.append(key).toString().getBytes(ConstField.UTF8));
        System.out.println(Arrays.equals(expect, actual));
    }
}
