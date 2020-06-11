package com.example.marcin.organista_remote_v2;
import android.app.Application;
import android.content.Context;
import android.content.Context;
import android.graphics.Color;
import android.location.OnNmeaMessageListener;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.gson.Gson;

public class MainScreen extends AppCompatActivity implements ButtonClickNotify, messageReceivedNotify {


    public static MainScreen instance;


    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
    public static MainScreen getInstance() {
        return instance;
    }



    public Toolbar mToolbar;

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<audioFile>> listHash;
    private Boolean volume = true;

    ImageButton minus;
    ImageButton volbal;
    ImageButton refresh;
    ImageButton stop;
    ImageButton stopNow;
    ImageButton plus;
    ImageButton connect;
    Context context;

    TextInputEditText text;
    private SingletonIO singleton;
    Vibrator vibe;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = this.getApplicationContext();

        


        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        initializeButton();


        listHash = new HashMap<String,List<audioFile>>();
        listHash.put("moja lista", new ArrayList<audioFile>());

        listDataHeader = new ArrayList<>();
        listDataHeader.add("moja lista");
        mToolbar = (Toolbar)findViewById(R.id.toolbar);

        singleton = SingletonIO.getInstance();
        singleton.io.registerListener(this);
        listView = (ExpandableListView)findViewById(R.id.lvExp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // keep screen on

        text = (TextInputEditText)findViewById(R.id.textInput);








    }



    private void initializeButton(){
        volbal = (ImageButton)findViewById(R.id.volbal);
        volbal.setImageResource(R.drawable.icons8_speaker_48);


        refresh = (ImageButton)findViewById(R.id.refresh) ;
        refresh.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override

                            public void run() {

                                clearList();

                            }

                        });
                        singleton.io.send("SendMeSomeMusic");
                        runOnUiThread(new Runnable() {

                            @Override

                            public void run() {

                                vibe.vibrate(75);

                            }

                        });
                    }

                }).start();
                return  true;
            }
        });

        connect = (ImageButton)findViewById(R.id.connect) ;
        connect.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                new Thread(new Runnable() {

                    @Override

                    public void run() {

                        //wifiState();
                        singleton.connect();


                        runOnUiThread(new Runnable() {

                            @Override

                            public void run() {
                                vibe.vibrate(75);

                            }

                        });
                    }

                }).start();
                return  true;
            }
        });







        plus = (ImageButton)findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (volume) {
                    new Thread(new Runnable() {

                        @Override

                        public void run() {
                            singleton.io.send("volumeUp");
                        }

                    }).start();
                }

                else{
                    new Thread(new Runnable() {

                        @Override

                        public void run() {
                            singleton.io.send("balanceRight");
                        }

                    }).start();


                }

                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override

                            public void run() {

                                vibe.vibrate(35);

                            }

                        });
                    }

                }).start();
            }
        });



        minus = findViewById(R.id.minus);
        minus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (volume) {
                    new Thread(new Runnable() {

                        @Override

                        public void run() {
                            singleton.io.send("volumeDown");
                        }

                    }).start();
                }

                else{
                    new Thread(new Runnable() {

                        @Override

                        public void run() {
                            singleton.io.send("balanceLeft");
                        }

                    }).start();


                }
                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override

                            public void run() {

                                vibe.vibrate(35);

                            }

                        });
                    }

                }).start();
            }
        });


        stop = (ImageButton)findViewById(R.id.stop);
        stop.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        singleton.io.send("stopWhenYouCan");
                        runOnUiThread(new Runnable() {

                            @Override

                            public void run() {

                                vibe.vibrate(100);

                            }

                        });
                    }

                }).start();
                return  true;
            }
        });



        stopNow = (ImageButton)findViewById(R.id.stopnow);
        stopNow.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        singleton.io.send("stopNow");
                        runOnUiThread(new Runnable() {

                            @Override

                            public void run() {

                                vibe.vibrate(100);

                            }

                        });
                    }

                }).start();
                return  true;
            }
        });


        volbal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (volume) {
                    volume = false;
                    volbal.setImageResource(R.drawable.icons8_scales_48);
                    minus.setImageResource(R.drawable.baseline_mic_black_48);
                    plus.setImageResource(R.drawable.baseline_music_note_black_48);
                } else {
                    volume = true;
                    volbal.setImageResource(R.drawable.icons8_speaker_48);
                    minus.setImageResource(R.drawable.icons8_minus_48);
                    plus.setImageResource(R.drawable.icons8_plus_math_48);
                }
                new Thread(new Runnable() {

                    @Override

                    public void run() {

                        runOnUiThread(new Runnable() {

                            @Override

                            public void run() {

                                vibe.vibrate(100);

                            }

                        });



                    }

                }).start();
                return false;
            }
        });
    }

    private void initData() {


        addAudioFileToList(new audioFile("Title1", "String Artist4", "String Album", "patch"));
        addAudioFileToList(new audioFile("Title1", "String Artist4", "String Album", "patch"));
        addAudioFileToList(new audioFile("Title1", "String Artist4", "String Album", "patch"));
        addAudioFileToList(new audioFile("Kabaret na żywo według Paranienormalnych - Kabaret", "String Artist4", "String Album", "patch"));
        addAudioFileToList(new audioFile("Title1", "String Artist4", "String Album", "patch"));
        addAudioFileToList(new audioFile("Title1", "String Artist4", "String Album", "patch"));
        addAudioFileToList(new audioFile("Title1", "String Artist4", "String Album", "patch"));
        addAudioFileToList(new audioFile("Kabaret na żywo według Paranienormalnych - Kabaret", "String Artist4", "String Album", "patch"));
        addAudioFileToList(new audioFile("Title1", "String Artist4", "String Album", "patch"));
        addAudioFileToList(new audioFile("Title1", "String Artist4", "String Album", "patch"));
        addAudioFileToList(new audioFile("Title1", "String Artist4", "Kabaret na żywo według Paranienormalnych - Kabaret", "patch"));
        addAudioFileToList(new audioFile("Title1", "String Artist4", "String Album", "patch"));

        new Thread(new Runnable() {

            @Override

            public void run() {

                runOnUiThread(new Runnable() {

                    @Override

                    public void run() {

                        updateFileList();
                        showMessage("Odebrano wszystko");

                    }

                });



            }

        }).start();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void  updateFileList(){

            sortList();
            listAdapter = new ExpandableListAdapter(this, listDataHeader, listHash);
            listView.setAdapter(listAdapter);
    }

    private void addAudioFileToList(audioFile file){

        try {
            if (listHash.containsKey(file.Album)) {
                listHash.get(file.Album).add(file);
                //showMessage(file.Title);
            } else {
                listHash.put(file.Album, new ArrayList<audioFile>());
                listHash.get(file.Album).add(file);
                listDataHeader.add(file.Album);
            }
        }
        catch(Exception e){

        }

    }




    public void showMessage(String msg){
        mToolbar.setTitle(msg);


    }

    @Override
    public void onButtonClick(int position, final String value, final audioFile file, int button) {
        new Thread(new Runnable() {

            @Override

            public void run() {
                runOnUiThread(new Runnable() {

                    @Override

                    public void run() {

                        vibe.vibrate(30);

                    }

                });
            }

        }).start();
        if (button == 0){
            new Thread(new Runnable() {

                @Override

                public void run() {

                    //singleton.io.send("PlayThis" + Integer.toString(file.No));
                    runOnUiThread(new Runnable() {

                        @Override

                        public void run() {
                            if (file.Album.contains("moja lista")){
                                new Thread(new Runnable() {

                                    @Override

                                    public void run() {

                                        singleton.io.send("removeThisFromTop" + file.path);
                                     }

                                }).start();
                                listHash.get("moja lista").remove(file);

                                //listView.setAdapter(listAdapter);
                                listAdapter.notifyDataSetChanged();
                            }
                            else {
                                addAudioFileToList(new audioFile(file.Title, file.Artist, "moja lista", file.path));

                                listAdapter.notifyDataSetChanged();
                                //showMessage(file.Album);
                                new Thread(new Runnable() {

                                    @Override

                                    public void run() {

                                        singleton.io.send("addThisToTop" + file.path);
                                        for (String x: listDataHeader){
                                            Collections.sort(listHash.get(x), new CustomComparator());
                                        }
                                    }

                                }).start();
                            }
                        }

                    });

                }

            }).start();
        }
        else if (button == 1){
            new Thread(new Runnable() {

                @Override

                public void run() {
                    singleton.io.send("PlayThis" + file.path);
                    runOnUiThread(new Runnable() {

                        @Override

                        public void run() {
                            showMessage(file.Title);
                        }

                    });

                }

            }).start();
        }


    }

    @Override
    public void onMessageReceivedNotify(final String message) {
        new Thread(new Runnable() {



            @Override

            public void run() {



                if (message.contains("audioFile:")){
                    new Thread(new Runnable() {

                        @Override

                        public void run() {
                            final int one = message.indexOf("{");
                            if (one != -1) {
                                try {
                                    Gson gson = new Gson();


                                    final audioFile file = gson.fromJson(message.substring(one), audioFile.class);
                                    if (file.Album.contains("ulubione")) {
                                        runOnUiThread(new Runnable() {

                                            @Override

                                            public void run() {

                                                addAudioFileToList(new audioFile(file.Title, file.Artist, "moja lista", file.path));
                                                showMessage("Odbieram");

                                            }

                                        });

                                    }
                                    else
                                    {
                                        runOnUiThread(new Runnable() {

                                            @Override

                                            public void run() {

                                                addAudioFileToList(file);
                                                showMessage("Odbieram");

                                            }

                                        });
                                    }
                                } catch (Exception e) {
                                    Log.e("Json", message);
                                    //showMessage(e.toString());
                                }
                            }

                        }

                    }).start();
                }


                else if (message.contains("TimeStopON")){
                    new Thread(new Runnable() {

                        @Override

                        public void run() {

                            runOnUiThread(new Runnable() {

                                @Override

                                public void run() {

                                    showMessage("Zaplanowano zatrzymanie");
                                    int color = Color.parseColor("#FFCC001F");
                                    stop.setBackgroundColor(color);
                                    int color2 = Color.parseColor("#FF029500");
                                    connect.setBackgroundColor(color2);

                                }

                            });



                        }

                    }).start();
                }

                else if (message.contains("Stopped")){
                    new Thread(new Runnable() {

                        @Override

                        public void run() {

                            runOnUiThread(new Runnable() {

                                @Override

                                public void run() {

                                    showMessage("Zatrzymano"); //zmien kolor przycisku
                                    int color = Color.parseColor("#FF0099CC");
                                    stop.setBackgroundColor(color);
                                    int color2 = Color.parseColor("#FF029500");
                                    connect.setBackgroundColor(color2);

                                }

                            });



                        }

                    }).start();
                }


                else if (message.contains("CanceledStopByUser")){
                    new Thread(new Runnable() {

                        @Override

                        public void run() {

                            runOnUiThread(new Runnable() {

                                @Override

                                public void run() {

                                    showMessage("Anulowano zaplanowane zatrzymanie"); //zmien kolor przycisku
                                    int color = Color.parseColor("#FF0099CC");
                                    stop.setBackgroundColor(color);
                                    int color2 = Color.parseColor("#FF029500");
                                    connect.setBackgroundColor(color2);

                                }

                            });



                        }

                    }).start();
                }

                else if (message.contains("CanceledStop")){
                    new Thread(new Runnable() {

                        @Override

                        public void run() {

                            runOnUiThread(new Runnable() {

                                @Override

                                public void run() {

                                    int color = Color.parseColor("#FF0099CC");
                                    stop.setBackgroundColor(color);
                                    int color2 = Color.parseColor("#FF029500");
                                    connect.setBackgroundColor(color2);

                                }

                            });



                        }

                    }).start();
                }

                else if (message.contains("updateIOsingleton")){
                    new Thread(new Runnable() {

                        @Override

                        public void run() {


                            runOnUiThread(new Runnable() {

                                @Override

                                public void run() {

                                    showMessage("Rozłączono"); //zmien kolor przycisku
                                    int color = Color.parseColor("#FFCC001F");
                                    connect.setBackgroundColor(color);

                                }

                            });




                        }

                    }).start();
                }

                else if (message.contains("SendedAll")){
                    new Thread(new Runnable() {

                        @Override

                        public void run() {

                                    runOnUiThread(new Runnable() {

                                        @Override

                                        public void run() {

                                            updateFileList();
                                            showMessage("Odebrano wszystko");
                                            int color2 = Color.parseColor("#FF029500");
                                            connect.setBackgroundColor(color2);

                                        }

                                    });



                        }

                    }).start();
                }

                else if (message.contains("connected")){

                    new Thread(new Runnable() {

                        @Override

                        public void run() {

                            runOnUiThread(new Runnable() {

                                @Override

                                public void run() {

                                    showMessage("Connected");
                                    //IOThread.connected = true;
                                    int color = Color.parseColor("#FF029500");
                                    connect.setBackgroundColor(color);

                                }

                            });



                        }

                    }).start();


                }



                else if (message.startsWith("now")){

                    new Thread(new Runnable() {

                        @Override

                        public void run() {

                            runOnUiThread(new Runnable() {

                                @Override

                                public void run() {

                                    showMessage(message.substring(3));
                                    int color = Color.parseColor("#FF029500");
                                    connect.setBackgroundColor(color);

                                }

                            });



                        }

                    }).start();


                }



                else if (message.contains("vol")){

                    new Thread(new Runnable() {

                        @Override

                        public void run() {

                            runOnUiThread(new Runnable() {

                                @Override

                                public void run() {

                                    showMessage(message.substring(3, message.length()));
                                    int color = Color.parseColor("#FF029500");
                                    connect.setBackgroundColor(color);

                                }

                            });



                        }

                    }).start();


                }




            }

        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (volume) {
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                //showMessage("volume down");

                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        singleton.io.send("volumeDown");
                    }

                }).start();
            }
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
                //showMessage("volume up");
                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        singleton.io.send("volumeUp");
                    }

                }).start();
            }
        }
        else{
            if (!volume) {
                if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                    //showMessage("volume down");
                    new Thread(new Runnable() {

                        @Override

                        public void run() {
                            singleton.io.send("balanceLeft");
                        }

                    }).start();
                }
                if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
                    //showMessage("volume up");
                    new Thread(new Runnable() {

                        @Override

                        public void run() {
                            singleton.io.send("balanceRight");
                        }

                    }).start();
                }
            }
        }
        return true;
    }

    public void sortList(){
        Collections.sort(listDataHeader, new CustomComparatorString());
        for (String x: listDataHeader){
            Collections.sort(listHash.get(x), new CustomComparator());
        }
    }

    public void wifiState(){
        new Thread(new Runnable() {

                        @Override

                        public void run() {


                            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                            if (!wifiManager.isWifiEnabled()) {
                                wifiManager.setWifiEnabled(true);
                            }
                            if (!wifiManager.getConnectionInfo().getSSID().toString().contains("OrganistaAP")) {
                                wifiManager.disconnect();

                            }
                            runOnUiThread(new Runnable() {

                                @Override

                                public void run() {

                                    showMessage("Ustanawianie połączenia wifi");

                                }

                            });
                            android.net.wifi.SupplicantState s = wifiManager.getConnectionInfo().getSupplicantState();
                            NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(s);
                            while (!wifiManager.getConnectionInfo().getSSID().toString().contains("OrganistaAP")) {
                                List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                                for (WifiConfiguration i : list) {
                                    if (i.SSID != null && i.SSID.equals("\"" + "OrganistaAP" + "\"")) {
                                        while (!wifiManager.getConnectionInfo().getSSID().toString().contains("OrganistaAP"))
                                            wifiManager.disconnect();
                                        wifiManager.enableNetwork(i.networkId, true);
                                        wifiManager.reconnect();

                                        break;
                                    }

                                }
                                s = wifiManager.getConnectionInfo().getSupplicantState();
                                state = WifiInfo.getDetailedStateOf(s);
                                final String info = state.toString();

                                runOnUiThread(new Runnable() {

                                    @Override

                                    public void run() {

                                        showMessage(info);

                                    }

                                });
                                try {
                                    Thread.sleep(2000);
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {

                                        @Override

                                        public void run() {

                                            showMessage("error");

                                        }

                                    });
                                }
                            }




                        }

                    }).start();



    }



    public void clearList(){
        for (String x: listDataHeader){
            listHash.get(x).clear();
        }
    }
}
