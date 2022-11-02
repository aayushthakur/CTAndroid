package com.example.clevertapintegrationsample.appinbox;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clevertapintegrationsample.R;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PromotionsTabFragment extends Fragment {

    public static final String MESSAGES = "messages";
    private ArrayList<AppInboxModel> allInboxMessages;

    public PromotionsTabFragment() {
        // Required empty public constructor
    }

    public static PromotionsTabFragment newInstance(ArrayList<AppInboxModel> inboxMessages) {
        PromotionsTabFragment fragment = new PromotionsTabFragment();
        Bundle args = new Bundle();
        args.putSerializable(MESSAGES, inboxMessages);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            allInboxMessages = getArguments().getParcelableArrayList(MESSAGES);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.all_inbox_fragment, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.inboxRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<AppInboxModel> filteredModels
                = (ArrayList<AppInboxModel>) allInboxMessages.stream().filter
                (inbox -> inbox.getTags().contains("Promotions")).collect(Collectors.toList());
        AppInboxRecyclerviewAdapter appInboxRecyclerviewAdapter = new AppInboxRecyclerviewAdapter(filteredModels,getContext());
        recyclerView.setAdapter(appInboxRecyclerviewAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        view.setBackgroundColor(ContextCompat.getColor(getContext(), COLOR_MAP[counter]));
//        TextView textViewCounter = view.findViewById(R.id.tv_counter);
//        textViewCounter.setText("Fragment No " + (counter+1));
    }

}
