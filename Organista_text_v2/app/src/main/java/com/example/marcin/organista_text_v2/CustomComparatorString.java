package com.example.marcin.organista_text_v2;

import java.util.Comparator;


public class CustomComparatorString implements Comparator<String> {
    @Override
    public int compare(String file1, String  file2) {
        return file1.compareTo(file2);
    }
}