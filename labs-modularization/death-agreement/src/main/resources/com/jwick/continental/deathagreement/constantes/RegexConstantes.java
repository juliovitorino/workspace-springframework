/*
Copyright <YEAR> <COPYRIGHT HOLDER>

This software is Open Source and is under MIT license agreement

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions
of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
OR OTHER DEALINGS IN THE SOFTWARE.
*/


package br.com.jcv.commons.library.commodities.constantes;

public class RegexConstantes {
    public static String REGEX_EMAIL = "([A-Za-z0-9_.\\-])+@([A-Za-z0-9_])+\\.([A-Za-z])+\\.?([A-Za-z]){2}";
    public static String REGEX_CPF = "[0-9]{3}(\\.[0-9]{3}){2}-[0-9]{2}";
    public static String REGEX_ANYTHING = ".*";
    public static String REGEX_ANYTHING_NOT_EMPTY = ".+";
    public static String REGEX_INTEGER_POSITIVE = "[0-9]+";
    public static String REGEX_INTEGER_NEGATIVE = "[-][0-9]+";
    public static String REGEX_REAL_ONLY_POSITIVE = "[+\\s]?[0-9]+\\[.,]?[0-9]*";
    public static String REGEX_REAL_ONLY_NEGATIVE = "[-][0-9]+\\[.,]?[0-9]*";
    public static String REGEX_REAL_POSITIVE_OR_NEGATIVE = "[-+\\s]?[0-9]+\\[.,]?[0-9]*";
    public static String REGEX_URL = "(http[s]?|s3|ftp|www\\.)?(://)?[a-z_/0-9\\-#=&.]+:?[0-9]{0,5}/?.*";
}
