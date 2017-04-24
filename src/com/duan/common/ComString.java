package com.duan.common;

/**
 * Created by DuanJiaNing on 2017/4/21.
 */
public class ComString {

    public final static String APKTOOL_PATH = "D:\\Decompile\\tools\\apktool\\apktool.bat";

    public final static String APKTOOL_OPTION_F = " d -f ";

    public final static String APKTOOL_OPTION_O = " -o ";

    public final static String DECOMPILE_TEMP_PATH = "D:\\Decompile\\temp\\";

    public final static String DECOMPILE_PATH = "D:\\Decompile\\decompile\\";

    public final static String ANDROIDMANIFEST = "AndroidManifest.xml";

    public final static String REGEX_PERMISSION = "<uses-permission android:name=\"(.*?)\"/>";

    public final static String REGEX_FUNCTIONS = "invoke-.*?},L(android/.*?);->(.*?)\\((.*?)\\)(.*?)\n";

}
