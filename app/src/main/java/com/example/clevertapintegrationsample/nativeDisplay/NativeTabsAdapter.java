package com.example.clevertapintegrationsample.nativeDisplay;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.example.clevertapintegrationsample.appinbox.AppInboxModel;

import java.util.ArrayList;

public class NativeTabsAdapter extends FragmentStateAdapter {
    int mNumOfTabs = 4;

    public NativeTabsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
            default:
                return  ImageTextTemplateFragment.newInstance();
            case 1:
                return EmptyFragment.newInstance();
            case 2:
                return SimpleBannerTemplateFragment.newInstance();
            case 3:
                return CarouselTemplateFragment.newInstance();

        }
    }

    @Override
    public int getItemCount() {
        return mNumOfTabs;
    }
}
