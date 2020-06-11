package com.example.marcin.organista_text_v2;

public class SingletonIO {
    public IOThread io;
    Thread ioThread;

    private static final SingletonIO ourInstance = new SingletonIO();

    public static SingletonIO getInstance() {
        return ourInstance;
    }

    private SingletonIO() {
        io = new IOThread("10.0.0.1", 1024);

    }

    public void connect(){
        try{
            io.interrupt();
        }
        catch(Exception e){

        }
        io.run();
    }
}
