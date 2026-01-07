package com.example.clevertapintegrationsample.homescreen;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.variables.Var;
import com.clevertap.android.sdk.variables.callbacks.FetchVariablesCallback;
import com.clevertap.android.sdk.variables.callbacks.VariableCallback;
import com.example.clevertapintegrationsample.AutoCarousel.ItemImageSlider;
import com.example.clevertapintegrationsample.AutoCarousel.SliderAdapter;
import com.example.clevertapintegrationsample.MyApplication;
import com.example.clevertapintegrationsample.R;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductExperienceActivity extends AppCompatActivity {

    private static final String TAG = ProductExperienceActivity.class.getSimpleName();

    Var<String> PETheme, PEColor;
    ArrayList<ItemImageSlider> sliderDataArrayList = new ArrayList<>();
    SliderAdapter sliderAdapter;
    private final List<HorizontalListItemsModel> horizontalListItemsModels = new ArrayList<>();
    private RecyclerView horizontalItemsRecyclerView;
    private final View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO: Step 4 of 4: Finally call getTag() on the view.
            // This viewHolder will have all required values.
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getBindingAdapterPosition();
            // viewHolder.getItemId();
            // viewHolder.getItemViewType();
            // viewHolder.itemView;
            HorizontalListItemsModel thisItem = horizontalListItemsModels.get(position);
            Toast.makeText(ProductExperienceActivity.this, "You Clicked: " + thisItem.getImageText(), Toast.LENGTH_SHORT).show();
        }
    };
    private HorizontalItemsAdapter horizontalItemsAdapter;
    private ImageView greetingsImageView, thinBannerImageView;
    private ConstraintLayout main;
    private CleverTapAPI cleverTapAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_experience);

         cleverTapAPI = MyApplication.getInstance().getClevertapDefaultInstance();

//        Var<String> PEColor =  cleverTapAPI.defineVariable("themeColor", "white");
//        Var<String> PETheme = (Var<String>) cleverTapAPI.getVariableValue("festivalTheme");
//        cleverTapAPI.syncVariables();

//        PETheme = cleverTapAPI.defineVariable("festivalTheme", "diwali");
//        PEColor = cleverTapAPI.defineVariable("themeColor", "black");
//
//        cleverTapAPI.fetchVariables(new FetchVariablesCallback() {
//            @Override
//            public void onVariablesFetched(boolean isSuccess) {
//                // isSuccess is true when server request is successful, false otherwise
//                Log.d(TAG, "onVariablesFetched: " + isSuccess);
//                if (isSuccess) {
//                    PETheme = cleverTapAPI.getVariable("festivalTheme");
//                    PEColor = cleverTapAPI.getVariable("black");
//                    Log.d(TAG, "onVariablesFetched() called with:  = [" + PEColor.stringValue + "]");
//
//                }
//            }
//        });

//        // invoked on app start and whenever vars are fetched from the server
//        cleverTapAPI.addVariablesChangedCallback(new VariablesChangedCallback() {
//            @Override
//            public void variablesChanged() {
//                // implement
//                Log.d(TAG, "variablesChanged() called " + cleverTapAPI.getVariable("festivalTheme"));
//                PETheme = cleverTapAPI.getVariable("festivalTheme");
//                prepareSlidersData();
//            }
//        });

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.imageSlider);

        main = findViewById(R.id.main);

        // passing this array list inside our adapter class.
        sliderAdapter = new SliderAdapter(getApplicationContext(), sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(sliderAdapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();

        horizontalItemsRecyclerView = findViewById(R.id.itemRecyclerView);
        horizontalItemsAdapter = new HorizontalItemsAdapter(horizontalListItemsModels, getApplicationContext());
        // Setting the layout as Staggered Grid for vertical orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        horizontalItemsRecyclerView.setLayoutManager(layoutManager);
        horizontalItemsRecyclerView.setAdapter(horizontalItemsAdapter);
        horizontalItemsAdapter.setItemClickListener(onItemClickListener);

        greetingsImageView = findViewById(R.id.greetingsImage);
        thinBannerImageView = findViewById(R.id.thinBanner);


        if (PETheme != null) {
            Log.d(TAG, "PETheme called before callback with: = [" + PETheme.stringValue() + "]");
        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

       /* assert PETheme != null;
        PETheme.addValueChangedCallback(new VariableCallback() {
            @Override
            public void onValueChanged(Var varInstance) {
                // invoked on app start and whenever value is changed
                Log.d(TAG, "onValueChanged() called with: varInstance = [" + varInstance.stringValue() + "]");
                new Handler(getApplicationContext().getMainLooper()).post(() -> {
                    PETheme = varInstance;
                    // run code
                    renderData();
                });
            }
        });

        assert PEColor != null;
        PEColor.addValueChangedCallback(new VariableCallback() {
            @Override
            public void onValueChanged(Var varInstance) {
                // invoked on app start and whenever value is changed
                Log.d(TAG, "onValueChanged() called with: varInstance = [" + varInstance.stringValue() + "]");
                new Handler(getApplicationContext().getMainLooper()).post(() -> {
                    PEColor = varInstance;
                    // run code
                    Log.d(TAG, "onValueChanged() called with: varInstance = [" + varInstance.stringValue + "]");
                });
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        PETheme = cleverTapAPI.defineVariable("festivalTheme", "diwali");
        PEColor = cleverTapAPI.defineVariable("themeColor", "black");

        cleverTapAPI.fetchVariables(new FetchVariablesCallback() {
            @Override
            public void onVariablesFetched(boolean isSuccess) {
                // isSuccess is true when server request is successful, false otherwise
                Log.d(TAG, "onVariablesFetched: " + isSuccess);
                if (isSuccess) {
                    PETheme = cleverTapAPI.getVariable("festivalTheme");
                    PEColor = cleverTapAPI.getVariable("black");
                    Log.d(TAG, "onVariablesFetched() called with:  = [" + PEColor.stringValue + "]");

                }
            }
        });

        if (PETheme!=null) {
            PETheme.addValueChangedCallback(new VariableCallback() {
                @Override
                public void onValueChanged(Var varInstance) {
                    // invoked on app start and whenever value is changed
                    Log.d(TAG, "onValueChanged() called with: varInstance = [" + varInstance.stringValue() + "]");
                    new Handler(getApplicationContext().getMainLooper()).post(() -> {
                        PETheme = varInstance;
                        // run code
                        renderData();
                    });
                }
            });
        }

        if (PEColor!=null) {
            PEColor.addValueChangedCallback(new VariableCallback() {
                @Override
                public void onValueChanged(Var varInstance) {
                    // invoked on app start and whenever value is changed
                    Log.d(TAG, "onValueChanged() called with: varInstance = [" + varInstance.stringValue() + "]");
                    new Handler(getApplicationContext().getMainLooper()).post(() -> {
                        PEColor = varInstance;
                        // run code
                        Log.d(TAG, "onValueChanged() called with: varInstance = [" + varInstance.stringValue + "]");
                    });
                }
            });
        }

    }

    private void renderData() {
        horizontalItemsAdapter.modelList.clear();
        sliderAdapter.mSliderItems.clear();

        // adding the urls inside array list
        if (PETheme != null && PETheme.value().equals("diwali")) {
            sliderDataArrayList.add(new ItemImageSlider("https://iili.io/2COB2nf.jpg"));
            sliderDataArrayList.add(new ItemImageSlider("https://iili.io/2COBJZG.jpg"));
            sliderDataArrayList.add(new ItemImageSlider("https://iili.io/2COB3G4.jpg"));
        } else {

            sliderDataArrayList.add(new ItemImageSlider("https://iili.io/2A7B8xa.png"));
            sliderDataArrayList.add(new ItemImageSlider("https://iili.io/2A7CWIn.jpg"));
            sliderDataArrayList.add(new ItemImageSlider("https://iili.io/2A7n32p.png"));
        }

        if (PETheme != null && PETheme.value().equals("diwali")) {

            horizontalListItemsModels.add(new HorizontalListItemsModel("https://iili.io/2IoK4hF.jpg", "Indian Sweets"));
            horizontalListItemsModels.add(new HorizontalListItemsModel("https://iili.io/2IoK6Lg.jpg", "Pooja Needs & Idols"));
            horizontalListItemsModels.add(new HorizontalListItemsModel("https://iili.io/2IoKSkB.jpg", "Gifting Store"));
            horizontalListItemsModels.add(new HorizontalListItemsModel("https://iili.io/2IoKvdQ.jpg", "Fireworks"));
            horizontalListItemsModels.add(new HorizontalListItemsModel("https://iili.io/2IoKUmP.jpg", "Diwali Decor"));
            horizontalListItemsModels.add(new HorizontalListItemsModel("https://iili.io/2IoKiBa.jpg", "Rangoli"));
        } else {
            horizontalListItemsModels.add(new HorizontalListItemsModel("https://iili.io/2A7ISWX.png", "Christmas Sweets"));
            horizontalListItemsModels.add(new HorizontalListItemsModel("https://iili.io/2A7IiJ4.png", "Prayer Necessities"));
            horizontalListItemsModels.add(new HorizontalListItemsModel("https://iili.io/2A7Irfs.png", "Gifting"));
            horizontalListItemsModels.add(new HorizontalListItemsModel("https://iili.io/2A7I40G.png", "Fireworks"));
            horizontalListItemsModels.add(new HorizontalListItemsModel("https://iili.io/2A7ILe2.png", "Christmas Decor"));
        }

        horizontalItemsAdapter.notifyDataSetChanged();
        sliderAdapter.notifyDataSetChanged();

        Context context = getApplicationContext();

        if (PETheme != null && PETheme.value().equals("diwali")) {
            Glide.with(context).load("https://iili.io/2ulfk8b.png").into(greetingsImageView);
        } else {
            Glide.with(context).load("https://iili.io/2ulfk8b.png").into(greetingsImageView);
        }

        if (PETheme != null && PETheme.value().equals("diwali")) {
            Glide.with(context).load("https://iili.io/2A3FHQI.png").into(thinBannerImageView);
        } else {
            Glide.with(context).load("https://iili.io/2A7BuVf.png").into(thinBannerImageView);
        }

        if (PETheme != null && PETheme.value().equals("diwali")) {
            main.setBackgroundColor(Color.parseColor("#E6D6AA"));
        } else {
            main.setBackgroundColor(Color.parseColor("#ff7878"));
        }
    }
}