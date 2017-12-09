package com.gurvanhenry.cordova;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Wifi extends CordovaPlugin {

    private static final String TAG = "Wifi";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        
        if (action.equals("connectWifi")) {
            String ssid = args.getString(0);
            String pass = args.getString(1);
            this.executeConnectWifi(ssid, pass, callbackContext);
            return true;
        } else if (action.equals("connectWifiOpen")) {
            String ssid = args.getString(0);
            this.executeConnectWifi(ssid, null, callbackContext);
            return true;
        } else if (action.equals("isWifiEnabled")) {
            this.executeIsWifiEnabled(callbackContext);
            return true;
        } else if (action.equals("successTestMethod")) {
            this.successTestMethod(callbackContext);
            return true;
        }
        else if (action.equals("errorTestMethod")) {
            this.errorTestMethod(callbackContext);
            return true;
        }
        return false;
    }
    
    private void successTestMethod(CallbackContext callbackContext) {
        Log.v(TAG, "====== successTestMethod ======");
        callbackContext.success("=> success callback");
    }

    private void errorTestMethod(CallbackContext callbackContext) {
        Log.v(TAG, "====== errorTestMethod ======");
        callbackContext.error("/!\\ ERROR callback /!\\");
    }

    private void executeConnectWifi(String ssid, String pass, CallbackContext callbackContext) {
        Log.v(TAG, "====== executeConnectWifi ======");
        this.connectWifi(ssid, pass);
        callbackContext.success();
    }
    
    private void executeIsWifiEnabled(CallbackContext callbackContext) {
        Log.v(TAG, "====== executeIsWifiEnabled ======");
        boolean wifiEnabled = this.isWifiEnabled();
        callbackContext.sendPluginResult(new PluginResult(Status.OK, wifiEnabled));
    }

    /**
     * If networkPass == null => open wifi
     * else (networkPass != null) => secured wifi (WPA ...)
     */
    private void connectWifi(String networkSSID, String networkPass) {

        WifiManager wifiManager = (WifiManager) this.cordova.getActivity().getSystemService(Context.WIFI_SERVICE);

        // 1 enable wifi (TODO :check wifi already enable)
        wifiManager.setWifiEnabled(true);

        // 2 add new wifi network
        WifiConfiguration wifiConf = new WifiConfiguration();
        wifiConf.SSID = "\"" + networkSSID + "\"";
        if (networkPass == null) {
            wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE); // Open network
        } else {
            wifiConf.preSharedKey = "\"" + networkPass + "\""; // WPA network
        }
        int networkID = wifiManager.addNetwork(wifiConf);

        // 3 connect
        wifiManager.disconnect();
        wifiManager.enableNetwork(networkID, true);
        wifiManager.reconnect();
    }

    public boolean isWifiEnabled() {
        WifiManager wifiManager = (WifiManager) this.cordova.getActivity().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }
}
