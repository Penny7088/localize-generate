package org.py.localize.utils;

import java.net.URL;

/**
 * Created by yizhaorong on 2017/3/28.
 */
public class Constant {
    // 类路径URL
    public static final URL CLASS_URL = Constant.class.getResource("");
    // 类路径
    public static final String CLASS_PATH = CLASS_URL.getPath();

    public static final String LANGUAGE_EX = "([a-zA-Z])+[-]?([a-zA-Z])*";
    public static final String SIMPLIFIED_CHINESE = "zh-CN";
    public static final String START_KEY = "name";
    public static final String DESCRIPTION = "使用场景描述";
    public static final String END_KEY = "[end]";
    public static final String COMMENT_KEY = "[comment]";
    public static final String IOS_KEY = "iOS";
    public static final String ANDROID_KEY = "Android";
    public static final String SERVER_KEY = "JAVA";
    public static final String IGNORE_ENGLISH_SUFFIX = "ignoreEnglishSuffix";
    public static final String IGNORE_CHINESE = "ignoreChinese";
    public static final String FIX_ID_LANGUAGE = "fixIdLanguage";
    public static final String USE_NEW_ANDROID = "useNewAndroid";
    public static final String IOS_SWITCH = "iOSOpen";
    public static final String ANDROID_SWITCH = "AndroidOpen";
    public static final String SERVER_SWITCH = "serverOpen";
    public static final String USE_DEFAULT_VALUE = "useDefaultValue";
    public static final String DEFAULT_VALUE = "defaultValue";

    public static final String TRUE = "true";
    public static final String FALSE = "false";
    //对应国家的key
    public static final String ENGLISH = "English";
    public static final String SIMPLIFIED_CHINESE_1= "中文简体";
    public static final String TRADITIONAL_CHINESE = "中文繁体";
    public static final String Japanese = "日语\n" + "(Japanese)";
    public static final String French = "法语\n" + "(French)";
    public static final String German = "德语\n" + "(German)";
    public static final String Spanish = "西班牙语\n" + "(Spanish)";
    public static final String Arabic = "阿拉伯语\n" + "(Arabic)";
    public static final String Turkish = "土耳其语\n" + "(Turkish)";
    public static final String Italian = "意大利语\n" +
            "(Italian)";
    public static final String Russian = "俄语\n" + "(Russian)";
    public static final String Portuguese = "葡萄牙语\n" + "(Portuguese)";
    public static final String Korean = "韩语\n" + "(Korean)";
}
