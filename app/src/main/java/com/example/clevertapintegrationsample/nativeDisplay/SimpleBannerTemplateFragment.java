package com.example.clevertapintegrationsample.nativeDisplay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.example.clevertapintegrationsample.MyApplication;
import com.example.clevertapintegrationsample.R;
import com.example.clevertapintegrationsample.appinbox.AppInboxModel;
import com.example.clevertapintegrationsample.appinbox.AppInboxRecyclerviewAdapter;
import com.example.clevertapintegrationsample.appinbox.PromotionsTabFragment;

import java.util.ArrayList;
import java.util.Map;

public class SimpleBannerTemplateFragment extends Fragment implements FragmentCommunicator{

    private String TAG = SimpleBannerTemplateFragment.class.getSimpleName();


    ImageView nativeImage1;

    public SimpleBannerTemplateFragment() {
        // Required empty public constructor
    }

    public static SimpleBannerTemplateFragment newInstance() {
        return new SimpleBannerTemplateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.simple_banner_native_display_fragment, container, false);
        MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("ND Simple Banner");
        nativeImage1 = rootView.findViewById(R.id.nativeImage);

        if (getActivity() instanceof NativeDisplayActivity){
            ((NativeDisplayActivity) getActivity()).setFragmentListener(this);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void loadData(ArrayList<CleverTapDisplayUnit> units){
        Log.d(TAG, "loadData() called with: units = [" + units.toString() + "]");
        for (CleverTapDisplayUnit cleverTapDisplayUnit : units) {
            //CustomKV
            Map<String, String> customMap = cleverTapDisplayUnit.getCustomExtras();
            if (customMap==null){
                return;
            }
            if (customMap.containsKey("native_display_type") &&
                    customMap.get("native_display_type").equals("image_only")){
                /*for (Map.Entry<String,String> entry : customMap.entrySet()) {
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                }*/
                String customImage1 = customMap.get("simple_banner_image");
                Glide.with(this).load(customImage1).into(nativeImage1);
                nativeImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyApplication.getInstance().getClevertapDefaultInstance().pushDisplayUnitClickedEventForID(cleverTapDisplayUnit.getUnitID());
                    }
                });
            }else{
                //backend
            }
        }
    }
}
