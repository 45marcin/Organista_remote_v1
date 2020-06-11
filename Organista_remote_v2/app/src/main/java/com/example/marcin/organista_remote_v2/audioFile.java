package com.example.marcin.organista_remote_v2;


/**
 * Created by marcin on 12.01.18.
 */

public class audioFile {
    public String Title;
    public String Album;
    public String Artist;
    public int No;
    public String path;
    audioFile(String Title, String Artist, String Album, String patch){
        this.Title = Title;
        this.Album = Album;
        this.Artist = Artist;
        this.path = patch;
    }

    String getTitle(){
        return Title;
    }
    String getArtist(){
        return Artist;
    }
    String getAlbum(){
        return Album;
    }
    int getNo(){
        return No;
    }
}
