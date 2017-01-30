package com.cognitio.cognitio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anshu on 03/12/16.
 */

public class ma_recycler_adapter extends RecyclerView.Adapter<ma_recycler_adapter.RecyclerViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<school> schools = new ArrayList<>();

    VolleySingleton volleySingleton;
    RequestQueue requestQueue;


    public ma_recycler_adapter(Context context) {
        this.context = context;
        layoutInflater = layoutInflater.from(context);
        volleySingleton = VolleySingleton.getinstance(context);
        requestQueue = volleySingleton.getrequestqueue();
    }

    public void addAll(ArrayList<school> list){
        schools.clear();
        schools.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_row,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
//        offer_details bookingHistory = History.get(position);
        final school current = schools.get(position);
        holder.city.setText(current.getCity());
        holder.school.setText(current.getName());
        Constants.ZONES_MAP2.put("N","North");
        Constants.ZONES_MAP2.put("S","South");
        Constants.ZONES_MAP2.put("E","East");
        Constants.ZONES_MAP2.put("W","West");
        Constants.ZONES_MAP2.put("C","Central");
        holder.zone.setText(Constants.ZONES_MAP2.get(current.getZone()).toString());



//        <item>Yet to meet the highest authority</item>
//        <item>In talks - Negative</item>
//        <item>In talks - Positive</item>
//        <item>Talks closed - MOU signed</item>
//        <item>Talks closed - Negative</item>
//        <item>Nothing can be said</item>
        HashMap<String,Integer> colormap = new HashMap<>(6);
        colormap.put("Talks closed - Negative", R.color.one);
        colormap.put("In talks - Negative", R.color.two);
        colormap.put("Nothing can be said", R.color.three);
        colormap.put("Yet to meet the highest authority", R.color.four);
        colormap.put("In talks - Positive", R.color.five);
        colormap.put("Talks closed - MOU signed", R.color.six);
        if(colormap.containsKey(current.getStatus()))
            holder.status.setBackgroundColor(context.getResources().getColor(colormap.get(current.getStatus())));
        else
            holder.status.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ViewSchool.class);
                intent.putExtra("id",current.getId());
                context.startActivity(intent);
            }
        });






    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView city,zone,school;
        LinearLayout status;
        CardView card;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            city = (TextView)itemView.findViewById(R.id.city_row);
            zone = (TextView)itemView.findViewById(R.id.zone_row);
            school = (TextView)itemView.findViewById(R.id.school_row);
            status = (LinearLayout)itemView.findViewById(R.id.status_row);
            card = (CardView) itemView.findViewById(R.id.card);
        }
    }
}
