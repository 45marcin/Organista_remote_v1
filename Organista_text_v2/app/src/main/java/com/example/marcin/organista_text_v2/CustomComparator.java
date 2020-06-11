package com.example.marcin.organista_text_v2;

import java.util.Comparator;

public class CustomComparator implements Comparator<AudioText> {
    @Override
    public int compare(AudioText file1, AudioText file2) {
        return file1.tytul.compareTo(file2.tytul);
    }
}


