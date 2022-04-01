package com.example.mainactivity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DispInfo extends AppCompatActivity {

    private WifiReceiver wifiReceiver;
    private TextView Result;
    private TextView WiFi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Result = findViewById(R.id.result);
        Button btnTela2 = findViewById(R.id.btnTela2);
        WiFi = findViewById(R.id.WiFi_Camp);


        loadBatteryInfo();

        //Monitorar a tela em primeiro plano
        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("TELA", "Tela 1");
        startService(intent);

        btnTela2.setOnClickListener(view -> {
            Intent intent2 = new Intent(this, SecondActivity.class);
            startActivity(intent2);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        wifiReceiver = new WifiReceiver();
        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        wifiReceiver = new WifiReceiver();
        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, filter);
    }

    class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status;
            if (checkWifiOnAndConnected()) {
                status = "Habilitado";
                Log.d("GFQ", "O Status do Wi-Fi Mudou para: " + status);
                WiFi.setText(status);
            } else {
                status = "Desabilitado";
                Log.d("GFQ", "O Status do Wi-Fi Mudou para: " + status);
                WiFi.setText(status);
            }
        }
    }

    public boolean checkWifiOnAndConnected() {
        WifiManager Wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return Wifi.isWifiEnabled();
    }

    private void loadBatteryInfo() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(batteryInfoReceiver, intentFilter);
    }

    //Receive info when data about battery changes with intent ACTION_BATTERY_CHANGED
    // but also to be alerted when Battery is plugged or no
    private final BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBatteryData(intent);
        }
    };

    private void updateBatteryData(Intent intent) {
        boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        if (present) {
            StringBuilder batteryInfo = new StringBuilder();
            // display battery health
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);

            switch (health) {
                case BatteryManager.BATTERY_HEALTH_COLD:
                    batteryInfo.append("Health: Cold").append("\n");
                    break;

                case BatteryManager.BATTERY_HEALTH_DEAD:
                    batteryInfo.append("Health: Dead").append("\n");
                    break;

                case BatteryManager.BATTERY_HEALTH_GOOD:
                    batteryInfo.append("Health: Good").append("\n");
                    break;

                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    batteryInfo.append("Health: Over Voltage").append("\n");
                    break;

                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    batteryInfo.append("Health: Overheat").append("\n");
                    break;

                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    batteryInfo.append("Health: Unspecified Failure").append("\n");
                    break;

                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                    batteryInfo.append("Health: Unknown").append("\n");
                    break;
            }

            // Calculate Battery Pourcentage.
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            if (level != -1 && scale != -1) {
                int batteryPct = (int) ((level / (float) scale) * 100f);
                batteryInfo.append("Battery Ptc: " + batteryPct + " %").append("\n");
            }

            BatteryManager timeManager = (BatteryManager) getApplicationContext().getSystemService(Context.BATTERY_SERVICE);
            long chargingTime = 0L;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                chargingTime = timeManager.computeChargeTimeRemaining();
            }

            if (chargingTime != -1) {
                chargingTime = chargingTime / 60000;
                Log.d("GFQ", "Tempo para o carregamento: " + Math.round(chargingTime) + " min");
                batteryInfo.append("Tempo de carregamento: " + chargingTime + " min").append("\n");
            } else {
                batteryInfo.append("Tempo de carregamento: Unknown ").append("\n");
            }

            // display plugged status
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            //batteryInfo.append("Plugged: " + plugged).append("\n");

            switch (plugged) {
                case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                    batteryInfo.append("Plugged: Wireless").append("\n");
                    break;

                case BatteryManager.BATTERY_PLUGGED_USB:
                    batteryInfo.append("Plugged: USB").append("\n");
                    break;

                case BatteryManager.BATTERY_PLUGGED_AC:
                    batteryInfo.append("Plugged: AC charger").append("\n");
                    break;

                default:
                    batteryInfo.append("Plugged: None").append("\n");
                    break;
            }

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    batteryInfo.append("Charging Status: Charging").append("\n");
                    break;

                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    batteryInfo.append("Charging Status: Discharging").append("\n");
                    break;

                case BatteryManager.BATTERY_STATUS_FULL:
                    batteryInfo.append("Charging Status: Full").append("\n");
                    break;

                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    batteryInfo.append("Charging Status: Unknown").append("\n");
                    break;

                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                default:
                    batteryInfo.append("Charging Status: Not Charging").append("\n");
                    break;
            }

            if (intent.getExtras() != null) {
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
                batteryInfo.append("Technology: " + technology).append("\n");
            }

            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);

            if (temperature > 0) {
                batteryInfo.append("Temperature: " + ((float) temperature / 10f)).append("°C\n");
            }

            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            batteryInfo.append("Voltage: " + voltage).append(" mV\n");

            long capacity = getBatteryCapacity();
            batteryInfo.append("Capacity: " + capacity / 1000).append(" mAh\n");

            Result.setText(batteryInfo.toString());

        } else {
            Toast.makeText(DispInfo.this, "No Battery Present", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private long getBatteryCapacity() {
        if (Build.VERSION.SDK_INT > -Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager mBatteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
            Long chargeCounter = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            Long capacity = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if (chargeCounter != null && capacity != null) {
                long value = (long) (((float) chargeCounter / (float) capacity) * 100f);
                return value;
            }
        }
        return 0;
    }
}
