package com.shareqube.android.qlense;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.gsm.GsmCellLocation;

public class PhoneStateUpdater extends PhoneStateListener {

    Context context = null;
    static String cellID = "";


    public PhoneStateUpdater( Context context)
    {
        this.context = context;
    }


    public void onCellLocationChanged(CellLocation loc)
    {
        GsmCellLocation gsm = (GsmCellLocation)loc;



        cellID = "CID : " + gsm.getCid() + " , LAC : "+ gsm.getLac();


    }

}
