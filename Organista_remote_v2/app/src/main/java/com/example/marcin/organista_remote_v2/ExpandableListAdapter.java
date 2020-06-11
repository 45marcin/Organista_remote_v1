package com.example.marcin.organista_remote_v2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import static java.security.AccessController.getContext;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<audioFile>>  listHashMap;

    ButtonClickNotify buttonNotify;



    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<audioFile>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
        try{
            buttonNotify=(ButtonClickNotify) context;
        }catch(Throwable e){

        }
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listHashMap.get(listDataHeader.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public class ViewHolder {
        TextView Title;
        //TextView Artist;
        TextView Album;
        ImageButton add;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String)getGroup(i);
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView)view.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        final ViewHolder holder = new ViewHolder();
        final audioFile child = (audioFile)getChild(i, i1);
        final String title = child.Title;
        //final String artist = child.Artist;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }


        holder.Title = (TextView)view.findViewById(R.id.lblListItemTitle);
        //holder.Artist = (TextView)view.findViewById(R.id.lblListItemAlbum);
        holder.Title.setText(title);
        //holder.Artist.setText(artist);



        ImageButton  add = (ImageButton)view.findViewById(R.id.lblListItemAdd);
        if (child.Album.contains("moja lista")){
            add.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icons8_minus_48));
        }
        else{
            add.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icons8_plus_math_48));
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    buttonNotify.onButtonClick(child.No, title, child, 0);
                    Toast.makeText(context, "Dodaję do ulubionych", Toast.LENGTH_SHORT).show();

            }
        });

        ImageButton play = (ImageButton)view.findViewById(R.id.lblListItemPlay);

        play.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                buttonNotify.onButtonClick(child.No, title, child, 1);
                Toast.makeText(context, "Odtwarzam: " + title, Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }



}
