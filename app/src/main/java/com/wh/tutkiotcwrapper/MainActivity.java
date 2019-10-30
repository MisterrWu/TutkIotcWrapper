package com.wh.tutkiotcwrapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.wh.tutkwrapper.TuTkClient;
import com.wh.tutkwrapper.TuTkManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TuTkClient tuTkClient = new TuTkClient.Builder()
                .tag("tag")
                .needRecordData(false)
                .needRecvMsg(false)
                .build();

        int ret = TuTkManager.instance().initIOTC();
        Toast.makeText(this,"init: " + ret,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        TuTkManager.instance().deInitIOTC();
        super.onDestroy();
    }
}
