package com.example.marcin.organista_remote_v2;

interface ButtonClickNotify {
    void onButtonClick(int position, String message, audioFile file, int button);
}
