package com.sample;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class AppProperties {
    public static final String OUTPUT_DIRECTORY;
    static {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("application");
        OUTPUT_DIRECTORY = bundle.getString("output_directory");
    }
}
