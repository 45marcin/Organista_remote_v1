package com.example.marcin.organista_remote_v2;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Marcin on 08.02.2018.
 */

public class IOThread extends Thread {
    private Socket socket;
    private String ip;
    public  PrintWriter out;
    public BufferedReader mBufferIn;
    private String mServerMessage;
    private int port;
    public static boolean connected = false;

    private List<messageReceivedNotify> newMessageReceivedNotify;

    public IOThread( String ip, int port){
        this.ip = ip;
        this.port = port;
        newMessageReceivedNotify = new ArrayList<>();



    }
    public void send(String text){
        try{
            if (connected) {
                //if(socket != null) socket.getOutputStream().write(text.getBytes());
                out.println(text);
                out.flush();
            }

        } catch(Exception e){
            for (messageReceivedNotify listener : newMessageReceivedNotify)
                listener.onMessageReceivedNotify("updateIOsingleton");

        }


    }

    @Override
    public void run() {
        // Connect
            try{
                socket.close();
            }
            catch(Exception e) {

            }


            try {

                InetAddress addr = InetAddress.getByName(ip);
                connected = false;


                socket = new Socket(addr, port);


            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                for (messageReceivedNotify listener : newMessageReceivedNotify)
                    listener.onMessageReceivedNotify("updateIOsingleton");
                connected = true;

            } catch (Exception e) {
                e.printStackTrace();
                //newMessageReceivedNotify.onMessageReceivedNotify("Error");
            }


            while (connected)
                // Setup receiver
                try {
                    while (mBufferIn != null)
                        try {
                            if (mServerMessage != null)
                                mServerMessage = null;
                            try {
                                mServerMessage = mBufferIn.readLine();
                                int one = mServerMessage.indexOf("mgr");
                                if (one != -1) {
                                    for (messageReceivedNotify listener : newMessageReceivedNotify)
                                        listener.onMessageReceivedNotify(mServerMessage.substring(one + 3));
                                }
                                mServerMessage = null;
                            } catch (Throwable e) {
                                for (messageReceivedNotify listener : newMessageReceivedNotify)
                                    listener.onMessageReceivedNotify("updateIOsingleton");
                                break;
                            }
                            //Log.e("fromTCP/IP", mServerMessage);




                        } catch (Throwable e) {
                            for (messageReceivedNotify listener : newMessageReceivedNotify)
                                listener.onMessageReceivedNotify("updateIOsingleton");



                            //connected = false;
                            break;
                        }
                } catch (Exception e) {
                    for (messageReceivedNotify listener : newMessageReceivedNotify)
                        listener.onMessageReceivedNotify("updateIOsingleton");
                    break;
                }

    }





            //if(socket != null)





                    //InputStream is = socket.getInputStream();
                    //final int bufferSize = 4096;
                    //final char[] buffer = new char[bufferSize];
                    //final StringBuilder out = new StringBuilder();
                    //Reader in = new InputStreamReader(is);
                    //int rsz = in.read(buffer, 0, buffer.length-1);
                    //out.append(buffer, 0, rsz);
                    //String result = out.toString();
                    //int one = result.indexOf("mgr");
                    //if (one != -1){
                    //    for (OnMessageListener listener : listeners)
                    //        listener.onMessage(result.substring(one));
                    //}


         //}


    public void registerListener(messageReceivedNotify listener){
        this.newMessageReceivedNotify.clear();
        this.newMessageReceivedNotify.add(listener);
     }
    public void error(){
        try {

            socket.close();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}
