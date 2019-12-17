package com.topband.bluetooth.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {

    //读取文件 所有内容
    public static String readFileToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }
    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null){
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static byte[] hexStrToByteArray(String str)
    {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < byteArray.length; i++){
            String subStr = str.substring(2 * i, 2 * i + 2);
            byteArray[i] = ((byte)Integer.parseInt(subStr, 16));
        }
        return byteArray;
    }
    public static String byteArrayToStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = new String(byteArray);
        return str;
    }
    public static byte[] strToByteArray(String str) {
        if (str == null) {
            return null;
        }
        byte[] byteArray = str.getBytes();
        return byteArray;
    }

    public  static  boolean isEmpty(String str)
    {
        if (str == null || str.trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    static Pattern DatePattern = Pattern
            .compile("^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$");
    /**
     * 功能：判断字符串是否为日期格式 验证时间格式为：2012-01-31 09:00:22
     *
     * @param strDate
     * @return
     */
    public static boolean isDate(String strDate) {

        Matcher m = DatePattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将utf-8编码的汉字转为中文
     * @author zhaoqiang
     * @param str
     * @return
     */
    public static String urlDecoding(String str){
        String result = str;
        try
        {
            result = URLDecoder.decode(str, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return result;
    }




    /**正则表达式**/
    private static String Sqlreg = "(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
    // select 不通过   1select则是可以的
    private static Pattern sqlPattern = Pattern.compile(Sqlreg, Pattern.CASE_INSENSITIVE);
    /**
     * 防SQL注入  判断
     * @param str
     * @return true验证通过, false不通过，有注入风险
     */
    public static boolean strSqlValid(String str)
    {
        if (sqlPattern.matcher(str).find())
        {
            // logger.error("未能通过过滤器：str=" + str);
            return false;
        }
        return true;
    }

    /**
     * 将字符串转化成unicode码
     * @author shuai.ding
     * @param string
     * @return
     */
    public static String string2Unicode(String string) {

        if (StringUtils.isBlank(string)) {
            return "";
        }

        char[] bytes = string.toCharArray();
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            char c = bytes[i];

            // 标准ASCII范围内的字符，直接输出
            if (c >= 0 && c <= 127) {
                unicode.append(c);
                continue;
            }
            String hexString = Integer.toHexString(bytes[i]);

            unicode.append("\\u");

            // 不够四位进行补0操作
            if (hexString.length() < 4) {
                unicode.append("0000".substring(hexString.length(), 4));
            }
            unicode.append(hexString);
        }
        return unicode.toString();
    }


    /**
     * 将unicode码转化成字符串
     * @author shuai.ding
     * @param unicode
     * @return
     */
    public static String unicode2String(String unicode) {
        if (StringUtils.isBlank(unicode)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = unicode.indexOf("\\u", pos)) != -1) {
            sb.append(unicode.substring(pos, i));
            if (i + 5 < unicode.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
            }
        }
        //如果pos位置后，有非中文字符，直接添加
        sb.append(unicode.substring(pos));

        return sb.toString();
    }
    /**
     * 下划线转换为驼峰
     * @param underscoreName
     * @param firstUpper 首字母是否大写
     * @return
     */
    public static String camelCaseName(String underscoreName,boolean  firstUpper) {
        return camelCaseName(underscoreName,firstUpper,false);
    }

    /**
     * 驼峰 转换为下划线
     * @param camelCaseName
     * @return
     */
    public static String underscoreName(String camelCaseName)
    {

        return underscoreName(camelCaseName,"");
    }

    /**
     * 驼峰 转换为下划线
     * @param camelCaseName
     * @param prefix 添加前缀
     * @return
     */
    public static String underscoreName(String camelCaseName,String prefix)
    {
        StringBuilder result = new StringBuilder();
        if (camelCaseName != null && camelCaseName.length()> 0)
        {
            result.append(camelCaseName.substring(0, 1).toLowerCase());
            for (int i = 1; i < camelCaseName.length(); i++)
            {
                char ch = camelCaseName.charAt(i);
                if (Character.isUpperCase(ch))
                {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                }
                else
                {
                    result.append(ch);
                }
            }
        }
        if (!StringHelper.isEmpty(prefix))//添加前缀
        {
            result.insert(0, prefix);
        }
        return result.toString();
    }

    /**
     * 下划线转换为驼峰
     * @param underscoreName
     * @param firstUpper 首字母大写
     * @param SubPrefix 是否减去前缀
     * @return
     */
    public static String camelCaseName(String underscoreName,boolean firstUpper, boolean SubPrefix)
    {
        if (SubPrefix)
        {
            int idx= underscoreName.indexOf("_");
            if (idx > 0)
            {
                underscoreName = underscoreName.substring(idx+1, underscoreName.length());
            }
        }

        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0)
        {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++)
            {
                char ch = underscoreName.charAt(i);

                if ('_' == ch)
                {
                    flag = true;
                }
                else
                {
                    if (flag||(i==0&& firstUpper))
                    {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    }
                    else
                    {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }
}
