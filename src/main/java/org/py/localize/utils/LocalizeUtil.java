package org.py.localize.utils;

/**
 * @author penny
 */
public class LocalizeUtil {

    public static String filterCountry(String country) {
        switch (country) {
            case Constant.ENGLISH:
                return "en";
            case Constant.SIMPLIFIED_CHINESE_1:
                return "zh-rCN";
            case Constant.TRADITIONAL_CHINESE:
                return "zh-rTW";
            case Constant.Japanese:
                return "ja";
            case Constant.French:
                return "fr";
            case Constant.German:
                return "de";
            case Constant.Spanish:
                return "es";
            case Constant.Arabic:
                return "ar";
            case Constant.Turkish:
                return "tr";
            case Constant.Italian:
                return "it";
            case Constant.Russian:
                return "ru";
            case Constant.Portuguese:
                return "pt";
            case Constant.Korean:
                return "ko";
        }
        return "default";
    }

}
