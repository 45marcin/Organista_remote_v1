package com.example.marcin.organista_text_v2;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.media.AudioFocusHandler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Organista extends AppCompatActivity implements ButtonClickNotify, messageReceivedNotify  {

    //public static Organista instance;


    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
    //public static Organista getInstance() {
    //    return instance;
    //}

    public List<AudioText> teksty;

    public Toolbar mToolbar;

    LinearLayoutManager layoutManager;
    private ArrayList<String> texts = new ArrayList<>();
    //RecyclerView recyclerView;
    //TextViewAdapter TextAdapter;

    private SingletonIO singleton;

    Button swipeButton;
    ImageButton minus;
    ImageButton volbal;
    ImageButton refresh;
    ImageButton stop;
    ImageButton stopNow;
    ImageButton plus;
    ImageButton connect;
    Context context;
    Toolbar toolbar;
    private float x1,x2;
    static final int MIN_DISTANCE = 250;
    int onButton = 0;
    Vibrator vibe;

    private List<String> listDataHeader;
    private HashMap<String, List<AudioText>> listHash;

    private ExpandableListView listView;
    private ExpandableListAdapterText listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //context = this.getApplicationContext();
        super.onCreate(savedInstanceState);

        //instance = this;
        setContentView(R.layout.activity_organista);

        toolbar = (Toolbar) findViewById(R.id.Toolbar);
        toolbar.setTitle("hello world");
        listHash = new HashMap<String,List<AudioText>>();
        listDataHeader = new ArrayList<>();


        listView = (ExpandableListView)findViewById(R.id.lvExp);
        InitSampleData();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //recyclerView = findViewById(R.id.textViewDisplay);
        //recyclerView.setLayoutManager(layoutManager);


        initializeButton();
        singleton = SingletonIO.getInstance();
        singleton.io.registerListener(this);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void  updateFileList(){

        sortList();
        listAdapter = new ExpandableListAdapterText(this, listDataHeader, listHash);
        listView.setAdapter(listAdapter);
    }

    private void addAudioFileToList(AudioText file){

        try {
            if (listHash.containsKey(file.Album)) {
                listHash.get(file.Album).add(file);
                //showMessage(file.Title);
            } else {
                listHash.put(file.Album, new ArrayList<AudioText>());
                listHash.get(file.Album).add(file);
                listDataHeader.add(file.Album);
            }
        }
        catch(Exception e){

        }

    }

    public void showMessage(String msg){
        toolbar.setTitle(msg);


    }


    public void sortList(){
        Collections.sort(listDataHeader, new CustomComparatorString());
        for (String x: listDataHeader){
            Collections.sort(listHash.get(x), new CustomComparator());
        }
    }

    public void initTextView(){
        //TextAdapter = new TextViewAdapter(this, texts);
        //recyclerView.setAdapter(TextAdapter);
        onButton = 0;
        if (!texts.isEmpty()){
            swipeButton.setText(texts.get(0));
        }
    }

    public void InitSampleData(){

        //listDataHeader.add("przykładowe");
        //listHash.put("przykładowe", new ArrayList<AudioText>());
        AudioText piosenka = new AudioText();
        piosenka.refren = "";
        piosenka.tytul = "Cicha Noc";
        piosenka.zwrotki.add("Cicha noc, święta noc,\n" +
                "Pokój niesie ludziom wszem,\n" +
                "A u żłóbka Matka Święta\n" +
                "Czuwa sama uśmiechnięta\n" +
                "Nad dzieciątka snem,\n" +
                "Nad dzieciątka snem.");
        piosenka.zwrotki.add("Cicha noc, święta noc,\n" +
                "Pastuszkowie od swych trzód\n" +
                "Biegną wielce zadziwieni\n" +
                "Za anielskim głosem pieni\n" +
                "Gdzie się spełnił cud,\n" +
                "Gdzie się spełnił cud.");
        piosenka.zwrotki.add("Cicha noc, święta noc,\n" +
                "Narodzony Boży Syn\n" +
                "Pan Wielkiego majestatu\n" +
                "Niesie dziś całemu światu\n" +
                "Odkupienie win,\n" +
                "Odkupienie win. ");
        piosenka.zwrotki.add("Cicha noc, święta noc,\n" +
                "Jakiż w tobie dzisiaj czas\n" +
                "W Betlejem dziecina święta\n" +
                "Wznosi w górę swe rączęta, \n" +
                "Błogosławi nam,\n" +
                "Błogosławi nam.");
        piosenka.Album = "kolędy";
        addAudioFileToList(piosenka);


        piosenka = new AudioText();
        piosenka.refren = "";
        piosenka.tytul = "Betlejemska Miłość";
        piosenka.zwrotki.add("Radosny nastał dzień, dzień wielkiej miłości\n" +
                "Kłótnie milkną już w niepamięć idą złości\n" +
                "Radośnie bije dzwon już kolęd płyną dźwięki\n" +
                "W Betlejem na sianku śpi Jezus malusieńki");
        piosenka.zwrotki.add("Dziś mała Dziecina nam się zrodziła\n" +
                "Swą Bożą miłością świat otuliła\n" +
                "To Jezus maleńki już leży w żłobie\n" +
                "Unosi rączkę błogosławi Tobie");
        piosenka.zwrotki.add("Obrusa lśniąca biel, zasiądą tu bliscy\n" +
                "I opłatkiem znów podzielą się dziś wszyscy\n" +
                "Bo w ten radosny dzień życzenia płyną wszędzie\n" +
                "Więc wspomnij także o tych których tu nie będzie");
        piosenka.Album = "kolędy";
        addAudioFileToList(piosenka);

        piosenka = new AudioText();
        piosenka.refren = "";
        piosenka.tytul = "Dlaczego dzisiaj";
        piosenka.zwrotki.add("Dlaczego dzisiaj wśród nocy dnieje,\n" +
                "I jako słońce niebo jaśnieje?\n" +
                "\n" +
                "Chrystus, Chrystus nam się narodził,\n" +
                "Aby nas od piekła oswobodził.");
        piosenka.zwrotki.add("Dlaczego dzisiaj Boży Aniele,\n" +
                "Ogłaszasz ludziom wielkie wesele?\n" +
                "\n" +
                "Chrystus, Chrystus nam się narodził,\n" +
                "Aby nas od piekła oswobodził.");
        piosenka.zwrotki.add("Czemuż pasterze do szopy śpieszą?\n" +
                "I podarunki ze sobą niosą?\n" +
                "\n" +
                "Chrystus, Chrystus nam się narodził,\n" +
                "Aby nas od piekła oswobodził.");
        piosenka.zwrotki.add("Dlaczego gwiazda nad podziw świeci,\n" +
                "I przed Królami tak szybko leci?\n" +
                "\n" +
                "Chrystus, Chrystus nam się narodził,\n" +
                "Aby nas od piekła oswobodził.");
        piosenka.Album = "kolędy";
        addAudioFileToList(piosenka);



        piosenka = new AudioText();
        piosenka.refren = "Gloria, gloria, gloria,\n" +
                "in excelsis Deo";
        piosenka.tytul = "Gdy się Chrystus rodzi";
        piosenka.zwrotki.add("Gdy się Chrystus rodzi, \n" +
                "i na świat przychodz1. \n" +
                "Ciemna noc w jasności \n" +
                "promienistej brodzi \n" +
                "Aniołowie się radują, \n" +
                "Pod niebiosy wyśpiewują:");
        piosenka.zwrotki.add("Mówią do pasterzy,\n" +
                "którzy trzód swych strzegl1. \n" +
                "Aby do Betlejem,\n" +
                "czym prędzej pobiegl1.\n" +
                "Bo się narodził Zbawiciel, \n" +
                "Wszego świata Odkupiciel");
        piosenka.zwrotki.add("O niebieskie Duchy, \n" +
                "i posłowie nieba. \n" +
                "Powiedzcież wyraźniej \n" +
                "co nam czynić trzeba:\n" +
                "Bo my nic nie pojmujemy, \n" +
                "Ledwo od strachu żyjemy.");
        piosenka.zwrotki.add("Idźcież do Betlejem, \n" +
                "gdzie Dziecię zrodzone, \n" +
                "W pieluszki powite, \n" +
                "w żłobie położone: \n" +
                "Oddajcie Mu pokłon boski, \n" +
                "On osłodzi wasze troski.");
        piosenka.zwrotki.add("A gdy pastuszkowie \n" +
                "wszystko zrozumieli\n" +
                "Zaraz do Betlejem\n" +
                "śpieszno pobiezeli\n" +
                "I zupełnie tak zastali\n" +
                "Jak anieli im zeznali");
        piosenka.zwrotki.add("A stanąwszy na miejszu\n" +
                "pełni zdumienia\n" +
                "Iż sie Bóg tak zniżył\n" +
                "do swego storzenia\n" +
                "Padli przed Nim na kolana\n" +
                "I uczcili swego Pana");
        piosenka.zwrotki.add("Wreszcie kiedy pokłon\n" +
                "Panu już oddali\n" +
                "Z wielką wesołością\n" +
                "do swych trzó wracali\n" +
                "Że się stali być godnymi\n" +
                "Boga widzieć na tej ziemi");
        piosenka.Album = "kolędy";
        addAudioFileToList(piosenka);









        piosenka = new AudioText();
        piosenka.refren = "";
        piosenka.tytul = "Bracia patrzcie jeno";
        piosenka.zwrotki.add("Bracia patrzcie jeno\n" +
                "jak niebo goreje\n" +
                "znać, że coś dziwnego\n" +
                "w Betlejem się dzieje.\n" +
                "Rzućmy budy, warty, stada,\n" +
                "niechaj nimi Pan Bóg włąda.\n" +
                "A my do Betlejem, do Betlejem.");
        piosenka.zwrotki.add("Patrzcie, jak tam gwiazda \n" +
                "światłem swoim miga!\n" +
                "Pewnie do uczczenia \n" +
                "Pana swego ściga.\n" +
                "Krokiem śmiałym i wesołym\n" +
                "śpieszmy i uderzmy czołem;\n" +
                "przed Panem w Betlejem");
        piosenka.zwrotki.add("Wszakże powiedziałem,\n" +
                "że cuda ujrzymy\n" +
                "Dziecię, Boga świata,\n" +
                "w żłobie zobaczymy.\n" +
                "Patrzcie, jak biedne okryte,\n" +
                "w żłobku Panię znakomite.\n" +
                "W szopie przy Betlejem, przy Betlejem");
        piosenka.zwrotki.add("Jak prorok pwoiedział,\n" +
                "Panna zrodzi Syna.\n" +
                "Dla ludu całego\n" +
                "szczęśliwa nowina.\n" +
                "Nam zaś radość w tej tu chwili,\n" +
                "gdyśmy Pana zobaczyli\n" +
                "W szopie przy Betlejem, przy Betlejem.");
        piosenka.zwrotki.add("Betlejem miasteczko,\n" +
                "w Juda sławne będzie.\n" +
                "Pamiętnym się stanie, \n" +
                "w tym kraju i wszędzie.\n" +
                "Ucieszmy się więc ziomkowie,\n" +
                "Pana tegoż już uczniowie.\n" +
                "W szopie przy Betlejem, przy Betlejem.");

        piosenka.Album = "kolędy";
        addAudioFileToList(piosenka);






        piosenka = new AudioText();
        piosenka.refren = "A Słowo Ciałem się stało\n" +
                "i mieszkało między nami";
        piosenka.tytul = "Bóg się rodzi, moc truchleje";
        piosenka.zwrotki.add("Bóg się rodzi, moc truchleje,\n" +
                "Pan niebiosów obnażony;\n" +
                "Ogień krzepnie, blask ciemnieje,\n" +
                "Ma granice nieskończony.\n" +
                "Wzgardzony okryty chwałą,\n" +
                "Śmiertelny Król nad wiekami;");
        piosenka.zwrotki.add("Cóż masz niebo nad ziemiany?\n" +
                "Bóg porzucił szczęście twoje\n" +
                "Wszedł między lud ukochany,\n" +
                "Dzieląc z nim trudy i znoje;\n" +
                "Nie mało cierpiał, nie mało,\n" +
                "Żeśmy byli winni sami,");
        piosenka.zwrotki.add("W nędznej szopie urodzony,\n" +
                "Żłób mu za kolebkę dano?\n" +
                "Cóż jest, czem był otoczony?\n" +
                "Bydło, pasterze i siano.\n" +
                "Ubodzy, was to spotkało\n" +
                "Witać Go przed bogaczami,");
        piosenka.zwrotki.add("Potem i Króle widziani\n" +
                "Cisną się między prostotą,\n" +
                "Niosąc dary Panu w dani:\n" +
                "Mirrę, kadzidło i złoto;\n" +
                "Bóstwo to razem zmieszało\n" +
                "Z wieśniaczymi ofiarami;");
        piosenka.zwrotki.add("Podnieś rękę, Boże Dziecię!\n" +
                "Błogosław krainę miłą,\n" +
                "W dobrych radach, w dobrym bycie\n" +
                "Wspieraj jej siłę swą siłą.\n" +
                "Dom nasz i majętność całą,\n" +
                "I Twoje wioski z miastami!");
        piosenka.Album = "kolędy";
        addAudioFileToList(piosenka);



        updateFileList();
    }

    @Override
    public void onButtonClick(final String title, int ButtonId, final AudioText text, final int position) {
        if (ButtonId == 1) {

            texts.clear();
            if (text.refren.length() > 5)
                for (String text_ : text.zwrotki) {
                    texts.add(text_);
                    texts.add(text.refren);
                }
            else
                for (String text_ : text.zwrotki)
                    texts.add(text_);

            new Thread(new Runnable() {

                        @Override

                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                vibe.vibrate(80);
                                showMessage(text.tytul);
                                }
                            });
                        }
                    }).start();

            initTextView();

        }




    }



    @Override
    public void onMessageReceivedNotify(final String message) {
        new Thread(new Runnable() {





            public void run() {

                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        if (!message.contains("updateIOsingleton"))
                            runOnUiThread(new Runnable() {

                                @Override

                                public void run() {


                                        int color = Color.parseColor("#FF029500");
                                        connect.setBackgroundColor(color);


                                }

                            });
                        else
                            runOnUiThread(new Runnable() {

                                @Override

                                public void run() {

                                    showMessage("Rozłączono");
                                    int color = Color.parseColor("#FFCC001F");
                                    connect.setBackgroundColor(color);

                                }

                            });


                    }

                }).start();

                if (message.contains("connected")){

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
                                    vibe.vibrate(150);

                                }

                            });



                        }

                    }).start();

                }





            }

        }).start();
    }

    public void ButtonMoveToNext(){
        if (onButton < texts.size()-1) {
            onButton += 1;
            swipeButton.setText(texts.get(onButton));
        }
    }

    public void ButtonMovoToPrevious(){
        if (onButton > 0) {
            onButton -=1;
            swipeButton.setText(texts.get(onButton));
        }
    }

    public void initializeButton(){
        swipeButton = findViewById(R.id.swipeButton);


        swipeButton.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        String tmp = (String)swipeButton.getText();
                        tmp = tmp.replace("\n", "zxcv");
                        singleton.io.send("ShowThis" + tmp);

                        runOnUiThread(new Runnable() {



                            public void run() {

                                toolbar.setTitle("Wysłano");
                                vibe.vibrate(85);
                                ButtonMoveToNext();



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



                            public void run() {
                                vibe.vibrate(25);
                                int color = Color.parseColor("#cc001f");
                                connect.setBackgroundColor(color);

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
                        singleton.io.send("Hide");
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



    }



}
