package com.example.clevertapintegrationsample.appinbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clevertap.android.sdk.inbox.CTInboxMessage;
import com.example.clevertapintegrationsample.R;
import com.google.android.exoplayer2.C;

import java.util.ArrayList;

public class AllInboxTabFragment extends Fragment {

    public static final String MESSAGES = "messages";
    private ArrayList<AppInboxModel> allInboxMessages;

    public AllInboxTabFragment() {
        // Required empty public constructor
    }

    public static AllInboxTabFragment newInstance(ArrayList<AppInboxModel> inboxMessages) {
        AllInboxTabFragment fragment = new AllInboxTabFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.all_inbox_fragment, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.inboxRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AppInboxRecyclerviewAdapter appInboxRecyclerviewAdapter = new AppInboxRecyclerviewAdapter(allInboxMessages,getContext());
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
