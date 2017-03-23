package com.example.infiny.tracker_master.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.infiny.tracker_master.Helpers.Config;
import com.example.infiny.tracker_master.Helpers.SessionManager;
import com.example.infiny.tracker_master.Interfaces.OnItemClickListener;
import com.example.infiny.tracker_master.Models.Target;
import com.example.infiny.tracker_master.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by infiny on 9/3/17.
 */

public class TargetListAdapter extends RecyclerView.Adapter<TargetListAdapter.MyViewHolder>  {
    private ArrayList<Target> targetList;
    Context context;
    private final OnItemClickListener listener;
    SessionManager sessionManager;

    public TargetListAdapter(Context context, ArrayList<Target> targetList, OnItemClickListener listener) {
        this.targetList = targetList;
        this.context = context;
        this.listener = listener;
        sessionManager = new SessionManager(context);

    }

    @Override
    public TargetListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_target, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Target targetData = targetList.get(position);

        holder.bind(targetList.get(position), listener);

        if(targetData.getIsOnline()){
            holder.ivOnline.setVisibility(View.VISIBLE);
        }

        Picasso.with(context)
                .load(Config.BASE_URL + targetData.getProfilePic())
                .placeholder(R.drawable.ic_person_36dp)
                .into(holder.ivProfPic);
        holder.tvName.setText(targetData.getFirstName()+" "+targetData.getLastName());
        holder.tvEmail.setText(targetData.getEmail());

    }

    public void removeAt(int position) {
        targetList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, targetList.size());
    }

    @Override
    public int getItemCount() {
        return targetList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ////implements View.OnCreateContextMenuListener
        ImageView ivProfPic,ivOnline;
        TextView tvName, tvEmail;
        RelativeLayout relativeLayout;
        View seperator;

        public MyViewHolder(View view) {
            super(view);
            ivProfPic = (ImageView) view.findViewById(R.id.ivProfPic);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            ivOnline = (ImageView) view.findViewById(R.id.ivOnline);

            relativeLayout = (RelativeLayout) view.findViewById(R.id.targetList);

        }

        public void bind(final Target item, final OnItemClickListener listener) {

            relativeLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    listener.onTargetItemClick(item);
                }

            });

            relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    new AlertDialog.Builder(context)
                            .setMessage("Delete Target?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    deleteChat(item, getPosition());
                                }
                            }).setNegativeButton("No", null).show();

                    return false;
                }
            });
        }
    }
}
