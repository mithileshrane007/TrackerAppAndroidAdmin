package com.example.infiny.tracker_master.Adapters;

import android.widget.Filter;

import com.example.infiny.tracker_master.Models.Target;

import java.util.ArrayList;

/**
 * Created by infiny on 27/3/17.
 */

public class FilterAdapter extends Filter{
    TargetListAdapter adapter;
    ArrayList<Target> filterList;

    public FilterAdapter(ArrayList<Target> filterList,TargetListAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;
    }
    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toLowerCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Target> filteredPlayers=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                String name = filterList.get(i).getFirstName().toLowerCase()+" "+filterList.get(i).getLastName().toLowerCase();
                //CHECK
                if(name.contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }
            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.targetList = (ArrayList<Target>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}