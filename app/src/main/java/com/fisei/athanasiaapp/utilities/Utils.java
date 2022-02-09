package com.fisei.athanasiaapp.utilities;

public class Utils {

    public static String ConvertDate(String date){
        String dateConverted = "";
        dateConverted += date.charAt(8);
        dateConverted += date.charAt(9);
        dateConverted += "/";
        dateConverted += date.charAt(5);
        dateConverted += date.charAt(6);
        dateConverted += "/";
        dateConverted += date.charAt(0);
        dateConverted += date.charAt(1);
        dateConverted += date.charAt(2);
        dateConverted += date.charAt(3);
        dateConverted += "  ";
        dateConverted += date.charAt(11);
        dateConverted += date.charAt(12);
        dateConverted += date.charAt(13);
        dateConverted += date.charAt(14);
        dateConverted += date.charAt(15);
        return dateConverted;
    }
    public static String CleanString(String toClean){
        String a = "";
        a = toClean.replace("\"", "");
        a = a.replace("[", "");
        a = a.replace("]", "");
        return a;
    }
}
