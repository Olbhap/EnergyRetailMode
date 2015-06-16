package com.energysistem.energyretailmode.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.energysistem.energyretailmode.SettingsBlocked;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by pgc on 12/06/2015.
 */
public class ServiceSettings extends Service {

    private Timer timer = null;
    public static int SERVICE_PERIOD = 200;
    private boolean pararServicio;
    Context mContext;
    ActivityManager manager;
    ScheduledThreadPoolExecutor exec;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pararServicio=true;
        this.unregisterReceiver(wifiStatusReceiver);
        exec.shutdownNow();
//        timer.cancel();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("Servicio", "Entramos");
        mContext = this.getApplicationContext();
        pararServicio = false;

        manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        exec= new ScheduledThreadPoolExecutor(1);
        exec.scheduleAtFixedRate(new MonitoringTimerTask(),0,250, TimeUnit.MILLISECONDS);



        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        this.registerReceiver(wifiStatusReceiver, filter);


    }


    BroadcastReceiver wifiStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("WIFI-Pablo","checking wifi state...");

            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

            if(wifiManager.getWifiState()== WifiManager.WIFI_STATE_DISABLING)
            {

                Log.e("WIFI-Pablo","User está desactivando el wifi. ACTIVANDO WIFI. PIUM PIUM");
                Toast.makeText(context, "LUSER, no toques el Wi-Fi... ACTIVANDO WIFI! PIUM PIUM.",
                        Toast.LENGTH_LONG).show();
                wifiManager.setWifiEnabled(true);
            }

        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful







        return Service.START_NOT_STICKY;
    }


    private class MonitoringTimerTask implements Runnable {
        String className;
        @Override
        public void run() {
            //Log.e("Servicio","Run");
            /*ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> processes = am.getRunningTasks(1);
            ComponentName componentInfo = processes.get(0).topActivity;
            String className = componentInfo.getClassName();

            if (className.equals("com.android.settings.Settings")) {
                Intent dialogIntent = new Intent(getBaseContext(), SettingsBlocked.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);


            }*/


                Log.e("Loop","Loopeamos");


                List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
                className = tasks.get(0).processName;
                if (className.equals("com.android.settings")) {
                    Intent dialogIntent = new Intent(getBaseContext(), SettingsBlocked.class);
                    dialogIntent.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }


        }
    }
}

