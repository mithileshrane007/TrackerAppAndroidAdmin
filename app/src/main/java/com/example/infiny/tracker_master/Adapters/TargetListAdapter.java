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
        ImageView ivProfPic;
        TextView tvName, tvEmail;
        RelativeLayout relativeLayout;
        View seperator;

        public MyViewHolder(View view) {
            super(view);
            ivProfPic = (ImageView) view.findViewById(R.id.ivProfPic);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);

            relativeLayout = (RelativeLayout) view.findViewById(R.id.targetList);

        }

        public void bind(final Target item, final OnItemClickListener listener) {

            relativeLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    listener.onChatItemClick(item);
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


//    public void deleteChat(final Target item, final int position) {
//
//        String url = Config.BASE_URL + "";
//
//            final ProgressDialog pDialog = new ProgressDialog(context);
//            pDialog.setMessage("Deleting target...");
//            pDialog.setCancelable(false);
//            pDialog.setCanceledOnTouchOutside(false);
//            pDialog.show();
//
//            StringRequest request = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                boolean error = jsonObject.getBoolean("error");
//                                if (error == false){
//                                    removeAt(position);
//                                }else{
//                                    Toast.makeText(context,"Unable to delete target please try agian",Toast.LENGTH_SHORT).show();
//                                }
//                                pDialog.dismiss();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                pDialog.dismiss();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Config.errorResponse(error, context);
//                            error.printStackTrace();
//                            pDialog.dismiss();
//                        }
//                    }) {
//
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("from_user_id", sessionManager.getUserID());
////                    params.put("to_user_id", item.getId());
//
//
//                    return params;
//                }
//
//                @Override
//                public String getBodyContentType() {
//                    return "application/x-www-form-urlencoded; charset=UTF-8";
//                }
//
//                @Override
//                public Map<String, String> getHeaders() {
//                    Map<String, String> mHeaders = new android.util.ArrayMap<String, String>();
//                    mHeaders.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//                    mHeaders.put("Accept", "application/json");
//                    return mHeaders;
//                }
//
//            };
//            RequestQueue requestQueue = Volley.newRequestQueue(context);
//            requestQueue.add(request);
//    }

}
