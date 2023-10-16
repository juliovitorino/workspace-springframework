package br.com.jcv.commons.library.utility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtility {

    private static final String number = "0123456789";
    private static final String stringLower = "abcdefghijklmnopqrstuvwxyz";
    private static final String stringUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String stringUpperLower = stringLower + stringUpper;
    private static final String stringNumberUpperLower = number + stringLower + stringUpper;

    public static int getRandomMonth() {
        return  (int) (Math.floor(Math.random() * 12)) + 1;
    }
    public static int getRandomDay() {
        return  (int) (Math.floor(Math.random() * 31)) + 1;
    }

    public static String getRandomCode(int length, String seed) {
        StringBuffer result = new StringBuffer();
        length = length <= 0 ? 8 : length;
        for (int idx = 0; idx < length; idx++) {
            int rndInt = (int) (Math.floor(Math.random() * seed.length())) - 1;
            if(rndInt < 0) rndInt = 0;
            result.append(seed.substring(rndInt,rndInt + 1));
        }
        return result.toString();
    }

    public static String getRandomCodeStringLower(int length) {
        return getRandomCode(length, stringLower);
    }
    public static String getRandomCodeStringUpper(int length) {
        return getRandomCode(length, stringUpper);
    }
    public static String getRandomCodeStringUpperLower(int length) {
        return getRandomCode(length, stringUpperLower);
    }
    public static String getRandomCodeNumber(int length) {
        return getRandomCode(length, number);
    }
    public static String getRandomCodeNumberUpperLower(int length) {
        return getRandomCode(length, stringNumberUpperLower);
    }
}
