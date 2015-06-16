package com.energysistem.energyretailmode;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.energysistem.energyretailmode.Services.ServiceSettings;


public class LoopVideo extends Activity {

    Intent intentService;
    Button b_closeService;
    Context mContext;
    Window mWindow;
    BroadcastReceiver mbroadcast = new BroadcastReceiver() {

        //When Event is published, onReceive method is called
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.i("[BroadcastReceiver]", "MyReceiver");

            if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                Log.i("[BroadcastReceiver]", "Screen ON");

            }
            else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                Log.i("[BroadcastReceiver]", "Screen OFF");
                encenderPantalla();
                desbloquearPatron();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_video);

        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName demoDeviceAdmin = new ComponentName(this, "LoopVideo");
        devicePolicyManager.lockNow();*/

        mWindow=this.getWindow();
        mContext=getApplication();
        /*
        *   Inicializamos el servicioo y lo encendemos
         */
        intentService = new Intent(this, ServiceSettings.class);
        startService(intentService);

        b_closeService = (Button) findViewById(R.id.b_closeService);

        b_closeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stopService(intentService);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loop_video, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mbroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mbroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mbroadcast);
    }

    public void encenderPantalla() {
        mWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mWindow.setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        mWindow.setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        mWindow.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mWindow.setFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
    }

    public void desbloquearPatron() {
        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

    }


}
