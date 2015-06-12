package com.yuanzifu.helloworld;


import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnicodeUtil {


    public String chineseToUnicode(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int chr1 = (char) str.charAt(i);
            result += "\\u" + Integer.toHexString(chr1);
        }
        return result;
    }


    public String UnicodeTochinese(String unicodeStr) {
        try {
            String gbkStr = new String(unicodeStr.getBytes("ISO8859-1"), "GBK");
            return gbkStr;
        } catch (UnsupportedEncodingException e) {
            return unicodeStr;
        }
    }


    public static String decodeUnicode(String string) {
        try {
            // Convert from Unicode to UTF-8
            byte[] utf8 = string.getBytes("UTF-8");
            // Convert from UTF-8 to Unicode
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(MainActivity.TAG, string);
        }
        return string;
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
// TODO Auto-generated method stub


    }

}
