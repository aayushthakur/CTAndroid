package com.example.clevertapintegrationsample.appinbox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class TabsAdapter extends FragmentStateAdapter {
    int mNumOfTabs = 3;
    private ArrayList<AppInboxModel> mAppInboxArrayList;

    public TabsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setData(ArrayList<AppInboxModel> appInboxModels){
        this.mAppInboxArrayList = appInboxModels;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return  PromotionsTabFragment.newInstance(mAppInboxArrayList);
            case 1:
                return TransactionsTabFragment.newInstance(mAppInboxArrayList);

        }
        return AllInboxTabFragment.newInstance(mAppInboxArrayList);

    }

    @Override
    public int getItemCount() {
        return mNumOfTabs;
    }
}
