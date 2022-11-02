package com.example.clevertapintegrationsample.appinbox;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.inbox.CTInboxMessage;
import com.clevertap.android.sdk.inbox.CTInboxMessageContent;
import com.example.clevertapintegrationsample.MyApplication;
import com.example.clevertapintegrationsample.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CustomAppInboxActivity extends AppCompatActivity {

    private static final String TAG = CustomAppInboxActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_app_inbox);
//        Intent intent = getIntent();
        //Sample Data in sample-appinbox-data.json file
//        ArrayList<CTInboxMessage> inboxMessages = intent.getParcelableArrayListExtra("app_inbox_messages");
        CleverTapAPI cleverTapAPI = MyApplication.getInstance().getClevertapDefaultInstance();
        ArrayList<CTInboxMessage> ctInboxMessageArrayList = cleverTapAPI.getAllInboxMessages();
        ArrayList<AppInboxModel> appInboxModels = new ArrayList<>();
        if (ctInboxMessageArrayList != null && !ctInboxMessageArrayList.isEmpty()) {
            Log.d(TAG, "onCreate() called with: inboxMessages = [" + new Gson().toJson(ctInboxMessageArrayList) + "]");
            for (CTInboxMessage ctInboxMessage: ctInboxMessageArrayList ) {
                AppInboxModel appInboxModel = new AppInboxModel();
               ArrayList<CTInboxMessageContent> messageContents = ctInboxMessage.getInboxMessageContents();
               if (!messageContents.isEmpty()){
                   CTInboxMessageContent ctInboxMessageContent = messageContents.get(0);
                   appInboxModel.setTitle(ctInboxMessageContent.getTitle());
                   appInboxModel.setMessage(ctInboxMessageContent.getMessage());
                   appInboxModel.setReceivedDate(ctInboxMessage.getDate());
                   if (ctInboxMessageContent.getMedia()==null || ctInboxMessageContent.getMedia().isEmpty()) {
                       appInboxModel.setType(AppInboxModel.TEXT_TYPE);
                   }else {
                       appInboxModel.setType(AppInboxModel.IMAGE_TYPE);
                       appInboxModel.setImageUrl(ctInboxMessageContent.getMedia());
                   }
               }
                appInboxModel.setTags(ctInboxMessage.getTags());
                appInboxModels.add(appInboxModel);
            }
        }


        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        TabsAdapter tabsAdapter = new TabsAdapter(this);
        tabsAdapter.setData(appInboxModels);
        viewPager.setAdapter(tabsAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("Promotions");
                                tab.setIcon(R.drawable.promotions_inbox);
                                break;

                            default:
                            case 1:
                                tab.setText("Transactions");
                                tab.setIcon(R.drawable.transactions_inbox);
                                break;
                            case 2:
                                tab.setText("All");
                                tab.setIcon(R.drawable.all_inbox);
                                break;
                        }
//                        tab.setText("Tab " + (position + 1));
                    }
                }).attach();
    }
}