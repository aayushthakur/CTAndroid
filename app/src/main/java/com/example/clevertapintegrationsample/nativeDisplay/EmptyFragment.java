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

import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.example.clevertapintegrationsample.MyApplication;
import com.example.clevertapintegrationsample.R;

import java.util.ArrayList;
import java.util.Map;

public class EmptyFragment extends Fragment {

    private String TAG = EmptyFragment.class.getSimpleName();
    public EmptyFragment() {
        // Required empty public constructor
    }

    public static EmptyFragment newInstance() {
        return new EmptyFragment();
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

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
