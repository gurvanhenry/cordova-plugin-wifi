package com.gurvanhenry.cordova;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

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
        } else if (action.equals("setWifiEnabled")) {
            boolean enabled = args.getBoolean(0);
            this.executeSetWifiEnabled(enabled, callbackContext);
            return true;
        } else if (action.equals("getMacAddress")) {
            this.executeGetMacAddress(callbackContext);
            return true;
        } 
        // Test methods:
        else if (action.equals("successTestMethod")) {
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
        Log.v(TAG, "====== executeConnectWifi ====== ssid=" + ssid + " pass=" + pass);
        this.connectWifi(ssid, pass);
        callbackContext.success();
    }

    private void executeIsWifiEnabled(CallbackContext callbackContext) {
        Log.v(TAG, "====== executeIsWifiEnabled ======");
        boolean wifiEnabled = this.isWifiEnabled();
        callbackContext.sendPluginResult(new PluginResult(Status.OK, wifiEnabled));
    }
    
    private void executeSetWifiEnabled(boolean enabled, CallbackContext callbackContext) {
        Log.v(TAG, "====== executeSetWifiEnabled ====== enabled=" + enabled);
        this.setWifiEnable(enabled);
        callbackContext.success();
    }

    private void executeGetMacAddress(CallbackContext callbackContext) {
        Log.v(TAG, "====== executeGetMacAddress ======");
        String macAddress = this.getMacAddress();
        callbackContext.sendPluginResult(new PluginResult(Status.OK, macAddress));
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

    private boolean isWifiEnabled() {
        WifiManager wifiManager = (WifiManager) this.cordova.getActivity().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    private void setWifiEnable(boolean enabled) {
        WifiManager wifiManager = (WifiManager) this.cordova.getActivity().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enabled);
    }

    final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";

    private String getMacAddress() {
        // sol1:
        String macAddress = this.getMacAddress_sol1();
        // sol1 failed, try sol2:
        if(macAddress.equals(DEFAULT_MAC_ADDRESS)) {
            macAddress = this.getMacAddress_sol2();
        }
        return macAddress;
    }
    
    /**
     * Do not work anymore on Android >= 6
     */
    private String getMacAddress_sol1() {
        WifiManager wifiManager = (WifiManager) this.cordova.getActivity().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * Workaround for Android 6
     * Need android.permission.INTERNET
     */
    private String getMacAddress_sol2() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return DEFAULT_MAC_ADDRESS;
    }
    
}
