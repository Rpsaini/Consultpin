package com.web.consultpin.jitsi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.web.consultpin.R;
import com.web.consultpin.Utilclass;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.URL;

public class MAinJistsiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_ain_jistsi);


        findViewById(R.id.txt_startvideocall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoCall=getIntent().getStringExtra(Utilclass.videocall);
                int lastindex=videoCall.lastIndexOf("/");
                int length=videoCall.length();
                String roomid= videoCall.substring(lastindex,length);
                System.out.println("room_id==="+roomid);
                try {
                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(new URL("https://jitsi.piehash.online/"))
                            .setRoom(roomid)
                            .setAudioMuted(false)
                            .setVideoMuted(false)
                            .setAudioOnly(false)
                            .setWelcomePageEnabled(false)
                            .build();
                    JitsiMeetActivity.launch(MAinJistsiActivity.this, options);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });



    }
}