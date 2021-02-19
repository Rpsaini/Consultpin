package com.web.consultpin.jitsi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.react.modules.core.PermissionListener;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;
import org.jitsi.meet.sdk.JitsiMeetViewListener;

import java.net.URL;
import java.util.Map;

import okhttp3.internal.Util;

public class MAinJistsiActivity extends AppCompatActivity  implements JitsiMeetActivityInterface, JitsiMeetViewListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_ain_jistsi);
        getSupportActionBar().hide();

        ImageView img_showimage =findViewById(R.id.img_showimage);
        TextView txt_name =findViewById(R.id.txt_name);
        ImageView toolbar_back_arrow =findViewById(R.id.toolbar_back_arrow);
        TextView title =findViewById(R.id.toolbar_title);

        showImage(getIntent().getStringExtra(Utilclass.imageUrl),img_showimage);
        txt_name.setText(getIntent().getStringExtra(Utilclass.first_name));
        title.setText(getResources().getString(R.string.videochat));

        findViewById(R.id.txt_startvideocall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoCall=getIntent().getStringExtra(Utilclass.videocall);
                System.out.println("Video Call url---"+videoCall);
                int lastindex=videoCall.lastIndexOf("/");
                int length=videoCall.length();
                String roomid= videoCall.substring(lastindex,length);
                System.out.println("room_id==="+roomid);

                Bundle bundle=new Bundle();
                bundle.putString("displayName",getIntent().getStringExtra(Utilclass.first_name));
                bundle.putString("email","");
                bundle.putString("avatarURL",getIntent().getStringExtra(Utilclass.imageUrl));

                System.out.println("User name====>"+getIntent().getStringExtra(Utilclass.first_name));
                try {
                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(new URL("https://jitsi.piehash.online/"))
                            .setRoom(roomid)
                            .setUserInfo(new JitsiMeetUserInfo(bundle))
                            .setAudioMuted(false)
                            .setSubject(getIntent().getStringExtra(Utilclass.first_name))
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

        toolbar_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showImage(final String url, final ImageView header_img) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(MAinJistsiActivity.this)
                        .load(url)
                        .placeholder(R.drawable.profileavtar)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(header_img);
            }
        });


    }

    @Override
    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {
        System.out.println("Conferencing request permission===="+strings);
    }

    @Override
    public void onConferenceJoined(Map<String, Object> map) {
        System.out.println("Conferencing joined data===="+map);
    }

    @Override
    public void onConferenceTerminated(Map<String, Object> map)
    {
        System.out.println("Conferencing end data===="+map);

    }

    @Override
    public void onConferenceWillJoin(Map<String, Object> map) {
        System.out.println("Conferencing will join===="+map);
    }
}