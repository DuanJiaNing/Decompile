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

    //groupCount() == 4 group(0) 不被计入
    //如：invoke-static {v6, v7}, Landroid/widget/View;->getXXX(Ljava/lang/String;I)Ljava/lang/Integer;
    //group(0)为开始到结束的整个字符串   invoke-static {v6, v7}, Landroid/widget/View;->getXXX(Ljava/lang/String;I)Ljava/lang/Integer;
    //group(1)为方法所在类    android/widget/View
    //group(2)为方法名  getXXX
    //group(3)为方法所有参数   Ljava/lang/String;I
    //group(4)为方法返回值    Ljava/lang/Integer; 或 I
    public final static String REGEX_FUNCTIONS = "invoke-.*}, L(android/.*?);->(.*?)\\((.*?)\\)(.*)";

}
