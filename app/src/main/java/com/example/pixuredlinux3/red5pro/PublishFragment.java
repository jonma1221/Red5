package com.example.pixuredlinux3.red5pro;

import android.content.Context;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;

public class PublishFragment extends Fragment implements SurfaceHolder.Callback{

    public R5Configuration configuration;
    protected Camera camera;
    protected boolean isPublishing = false;
    protected R5Stream stream;

    public PublishFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PublishFragment newInstance() {
        PublishFragment fragment = new PublishFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//127.0.0.1     1935
        configuration = new R5Configuration(R5StreamProtocol.RTSP, "10.0.0.61",  8554, "live", 1.0f);
    }

    private void preview() {
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        SurfaceView surface = (SurfaceView) getActivity().findViewById(R.id.surfaceView);
        surface.getHolder().addCallback(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button publishButton = (Button) getActivity().findViewById(R.id.publishButton);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPublishToggle();
            }
        });
    }

    private void onPublishToggle() {
        Button publishButton = (Button) getActivity().findViewById(R.id.publishButton);
        if(isPublishing) {
            stop();
        }
        else {
            start();
        }
        isPublishing = !isPublishing;
        publishButton.setText(isPublishing ? "stop" : "start");
    }

    public void start() {
        camera.stopPreview();
        stream = new R5Stream(new R5Connection(configuration));
        stream.setView((SurfaceView) getActivity().findViewById(R.id.surfaceView));

        R5Camera r5Camera = new R5Camera(camera, 320, 240);
        R5Microphone r5Microphone = new R5Microphone();

        stream.attachCamera(r5Camera);
        stream.attachMic(r5Microphone);

        stream.publish("red5prostream", R5Stream.RecordType.Live);
        camera.startPreview();
    }

    @Override
    public void onResume() {
        super.onResume();
        preview();
    }


    public void stop(){
        if(stream != null){
            stream.stop();
            camera.startPreview();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isPublishing){
            onPublishToggle();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        return view;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
