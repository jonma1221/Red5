package com.example.pixuredlinux3.red5pro;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.view.R5VideoView;

public class SubscribeFragment extends Fragment {

    public R5Configuration configuration;
    public R5Stream stream;
    private boolean isSubscribing;
    
    public SubscribeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SubscribeFragment newInstance() {
        SubscribeFragment fragment = new SubscribeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configuration = new R5Configuration(R5StreamProtocol.RTSP, "10.0.0.61",  8554, "live", 1.0f);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button publishButton = (Button) getActivity().findViewById(R.id.subscribeButton);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubscribeToggle();
            }
        });
    }

    private void onSubscribeToggle() {
        Button subscribeButton = (Button) getActivity().findViewById(R.id.subscribeButton);
        if(isSubscribing) {
            stop();
        }
        else {
            start();
        }
        isSubscribing = !isSubscribing;
        subscribeButton.setText(isSubscribing ? "stop" : "start");
    }

    private void start() {
        R5VideoView videoView = (R5VideoView) getActivity().findViewById(R.id.subscribeView);
        stream = new R5Stream(new R5Connection((configuration)));
        videoView.attachStream(stream);
        stream.play("red5prostream");

    }

    private void stop() {
        if(stream != null){
            stream.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isSubscribing) {
            onSubscribeToggle();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);
        // Inflate the layout for this fragment
        return view;
    }

}
