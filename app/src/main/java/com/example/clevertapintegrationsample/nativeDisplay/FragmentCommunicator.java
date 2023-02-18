package com.example.clevertapintegrationsample.nativeDisplay;

import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;

import java.util.ArrayList;

public interface FragmentCommunicator {

    void loadData(ArrayList<CleverTapDisplayUnit> units);
}
