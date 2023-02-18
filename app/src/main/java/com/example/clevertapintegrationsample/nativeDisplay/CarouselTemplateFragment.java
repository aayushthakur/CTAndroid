package com.example.clevertapintegrationsample.nativeDisplay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.example.clevertapintegrationsample.MyApplication;
import com.example.clevertapintegrationsample.R;
import com.example.clevertapintegrationsample.appinbox.AppInboxModel;
import com.example.clevertapintegrationsample.appinbox.AppInboxRecyclerviewAdapter;

import java.lang.annotation.Native;
import java.util.ArrayList;
import java.util.Map;

public class CarouselTemplateFragment extends Fragment  implements FragmentCommunicator{

    private String TAG = CarouselTemplateFragment.class.getSimpleName();
    private ArrayList<CarouselModel> carouselModels = new ArrayList<>();

    CarouselRecyclerviewAdapter carouselRecyclerviewAdapter;

    public CarouselTemplateFragment() {
        // Required empty public constructor
    }

    public static CarouselTemplateFragment newInstance() {
        return new CarouselTemplateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.all_inbox_fragment, container, false);

        MyApplication.getInstance().getClevertapDefaultInstance().pushEvent("ND Carousel Banner");

        RecyclerView recyclerView = rootView.findViewById(R.id.inboxRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        carouselRecyclerviewAdapter = new CarouselRecyclerviewAdapter(carouselModels,getContext());
        recyclerView.setAdapter(carouselRecyclerviewAdapter);

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
                    customMap.get("native_display_type").equals("carousel_banner")){
                /*for (Map.Entry<String,String> entry : customMap.entrySet()) {
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                }*/
                String customImage1 = customMap.get("carousel_image_1");
                String customImage2 = customMap.get("carousel_image_2");
                String customImage3 = customMap.get("carousel_image_3");
                CarouselModel carouselModel = new CarouselModel();
                carouselModel.imageUrl = customImage1;
                carouselModels.add(carouselModel);
                 carouselModel = new CarouselModel();
                carouselModel.imageUrl = customImage2;
                carouselModels.add(carouselModel);
                 carouselModel = new CarouselModel();
                carouselModel.imageUrl = customImage3;
                carouselModels.add(carouselModel);
                Log.d(TAG, "onDisplayUnitsLoaded() called with: carousel models = [" + carouselModels.size() + "]");
                carouselRecyclerviewAdapter.notifyDataSetChanged();
            }else{
                //backend
            }
        }

    }
}
