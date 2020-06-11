package com.example.marcin.organista_remote_v2;

import java.util.Comparator;

public class CustomComparator implements Comparator<audioFile> {
    @Override
    public int compare(audioFile file1, audioFile  file2) {
        return file1.getTitle().compareTo(file2.Title);
    }
}


