package com.example.infiny.tracker_master.Interfaces;

import com.example.infiny.tracker_master.Models.LogHours;
import com.example.infiny.tracker_master.Models.Target;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by infiny on 9/3/17.
 */

public interface OnItemClickListener {

    void onTargetItemClick(Target targetItem);

    void onReportItemClick(LogHours ReportItem);

}
