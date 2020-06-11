package com.example.marcin.organista_text_v2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TextViewAdapter extends RecyclerView.Adapter<TextViewAdapter.ViewHolder>{
    @NonNull
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> texts = new ArrayList<>();
    private Context mContext;

    ButtonClickNotify buttonNotify;

    public TextViewAdapter(Context context, ArrayList<String> textss){
         mContext = context;
         texts = textss;
        try{
            buttonNotify=(ButtonClickNotify) context;
        }catch(Throwable e){

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "OnCreateViewHolder: called.");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text_view_item, viewGroup, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.text.setText(texts.get(i));

        viewHolder.text.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
                buttonNotify.onButtonClick(texts.get(i), 2, null, i);
                return true;
            }
        });
    }




    @Override
    public int getItemCount() {
        return texts != null ? texts.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.text);

        }
    }

}
