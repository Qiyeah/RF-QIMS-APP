package com.zhilian.api;

/**
 * @Author Andersen
 * mail: yawen199@163.com
 * Date: 2016-9-12 14:57
 */
public class StrKit {
    public StrKit() {}

    public static String firstCharToLowerCase(String str)
    {
        char firstChar = str.charAt(0);
        if ((firstChar >= 'A') && (firstChar <= 'Z'))
        {
            char[] arr = str.toCharArray(); int
                tmp25_24 = 0; char[] tmp25_23 = arr;tmp25_23[tmp25_24] = ((char)(tmp25_23[tmp25_24] + ' '));
            return new String(arr);
        }
        return str;
    }

    public static String firstCharToUpperCase(String str)
    {
        char firstChar = str.charAt(0);
        if ((firstChar >= 'a') && (firstChar <= 'z'))
        {
            char[] arr = str.toCharArray(); int
                tmp25_24 = 0; char[] tmp25_23 = arr;tmp25_23[tmp25_24] = ((char)(tmp25_23[tmp25_24] - ' '));
            return new String(arr);
        }
        return str;
    }

    public static boolean isBlank(String str)
    {
        return (str == null) || ("".equals(str.trim()));
    }

    public static boolean notBlank(String str)
    {
        return (str != null) && (!"".equals(str.trim())) && (!"null".equals(str.trim()));
    }

    public static boolean notBlank(String... strings)
    {
        if (strings == null) {
            return false;
        }
        String[] arrayOfString = strings;int j = strings.length;
        for (int i = 0; i < j; i++)
        {
            String str = arrayOfString[i];
            if ((str == null) || ("".equals(str.trim()))) {
                return false;
            }
        }
        return true;
    }

    public static boolean notNull(Object... paras)
    {
        if (paras == null) {
            return false;
        }
        Object[] arrayOfObject = paras;int j = paras.length;
        for (int i = 0; i < j; i++)
        {
            Object obj = arrayOfObject[i];
            if (obj == null) {
                return false;
            }
        }
        return true;
    }

    public static String toCamelCase(String stringWithUnderline)
    {
        if (stringWithUnderline.indexOf('_') == -1) {
            return stringWithUnderline;
        }
        stringWithUnderline = stringWithUnderline.toLowerCase();
        char[] fromArray = stringWithUnderline.toCharArray();
        char[] toArray = new char[fromArray.length];
        int j = 0;
        for (int i = 0; i < fromArray.length; i++) {
            if (fromArray[i] == '_')
            {
                i++;
                if (i < fromArray.length) {
                    toArray[(j++)] = Character.toUpperCase(fromArray[i]);
                }
            }
            else
            {
                toArray[(j++)] = fromArray[i];
            }
        }
        return new String(toArray, 0, j);
    }

    public static String join(String[] stringArray)
    {
        StringBuilder sb = new StringBuilder();
        String[] arrayOfString = stringArray;int j = stringArray.length;
        for (int i = 0; i < j; i++)
        {
            String s = arrayOfString[i];
            sb.append(s);
        }
        return sb.toString();
    }

    public static String join(String[] stringArray, String separator)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringArray.length; i++)
        {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(stringArray[i]);
        }
        return sb.toString();
    }
}
