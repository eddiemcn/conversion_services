package com.demo.backend.util;

import java.text.DecimalFormat;

public class NumberUtils {
  public String floatToString(float f) {
    DecimalFormat decimalFormat = new DecimalFormat("0.##");
    return decimalFormat.format(f);
}

public float stringToFloat(String s) {
    float result;
    try {
        result = Float.parseFloat(s);
    } catch (NumberFormatException nfe) {
        result = -1;
    }
    return result;
}
}