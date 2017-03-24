package com.example.infiny.tracker_master.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.infiny.tracker_master.Helpers.SessionManager;
import com.example.infiny.tracker_master.Interfaces.OnItemClickListener;
import com.example.infiny.tracker_master.Models.LogHours;
import com.example.infiny.tracker_master.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by infiny on 22/3/17.
 */

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.MyViewHolder> {
    public ArrayList<LogHours> logHourList;
    Context context;
    private final OnItemClickListener listener;
    SessionManager sessionManager;

    public ReportListAdapter(Context context, ArrayList<LogHours> logHourList, OnItemClickListener listener) {
        this.logHourList = logHourList;
        this.context = context;
        this.listener = listener;
        sessionManager = new SessionManager(context);

    }

    @Override
    public ReportListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item, parent, false);
        return new ReportListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReportListAdapter.MyViewHolder holder, int position) {
        LogHours data = logHourList.get(position);

        String[] split = data.getHours().split(":");

        String hr,min;

        if(Integer.valueOf(split[0]) > 1 && Integer.valueOf(split[1]) > 1 ){
            holder.tvHours.setText(split[0]+ " Hours "+split[1]+" Minutes");
        } else if(Integer.valueOf(split[0]) <= 1 && Integer.valueOf(split[1]) > 1 ){
            holder.tvHours.setText(split[0]+ " Hour "+split[1]+" Minutes");
        } else if(Integer.valueOf(split[0]) <= 1 && Integer.valueOf(split[1]) <= 1 ){
            holder.tvHours.setText(split[0]+ " Hour "+split[1]+" Minute");
        } else if(Integer.valueOf(split[0]) > 1 && Integer.valueOf(split[1]) <= 1 ){
            holder.tvHours.setText(split[0]+ " Hours "+split[1]+" Minute");
        }


        try {
            holder.tvDate.setText(new SimpleDateFormat("dd MMMM yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(data.getDate())));
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.bind(logHourList.get(position), listener);


    }

    public void removeAt(int position) {
        logHourList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, logHourList.size());
    }

    @Override
    public int getItemCount() {
        return logHourList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ////implements View.OnCreateContextMenuListener
        TextView tvDate, tvHours;
        RelativeLayout rel_view;
        View seperator;

        public MyViewHolder(View view) {
            super(view);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvHours = (TextView) view.findViewById(R.id.tvHours);

            rel_view = (RelativeLayout) view.findViewById(R.id.rel_view);

        }

        public void bind(final LogHours item, final OnItemClickListener listener) {

            rel_view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    listener.onReportItemClick(item);
                }

            });

//            relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//
//                    new AlertDialog.Builder(context)
//                            .setMessage("Delete Target?")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
////                                    deleteChat(item, getPosition());
//                                }
//                            }).setNegativeButton("No", null).show();
//
//                    return false;
//                }
//            });
        }
    }
}
