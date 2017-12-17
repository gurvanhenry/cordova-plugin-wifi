package com.gurvanhenry.cordova;

import android.Manifest;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
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
        } else if (action.equals("isWifiConnected")) {
            this.executeIsWifiConnected(callbackContext);
            return true;
        } else if (action.equals("getCurrentSSID")) {
            this.executeGetCurrentSSID(callbackContext);
            return true;
        }  else if (action.equals("getMacAddress")) {
            this.executeGetMacAddress(callbackContext);
            return true;
        } else if (action.equals("listWifiNetworks")) {
            this.executeListWifiNetworks(callbackContext);
            return true;
        }
        return false;
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
    
    private void executeIsWifiConnected(CallbackContext callbackContext) {
        Log.v(TAG, "====== executeIsWifiConnected ======");
        boolean wifiConnected = this.isWifiConnected();
        callbackContext.sendPluginResult(new PluginResult(Status.OK, wifiConnected));
    }
    
    private void executeGetCurrentSSID(CallbackContext callbackContext) {
        Log.v(TAG, "====== executeGetCurrentSSID ======");
        String ssid = this.getCurrentSSID();
        callbackContext.sendPluginResult(new PluginResult(Status.OK, ssid));
    }

    private void executeGetMacAddress(CallbackContext callbackContext) {
        Log.v(TAG, "====== executeGetMacAddress ======");
        String macAddress = this.getMacAddress();
        callbackContext.sendPluginResult(new PluginResult(Status.OK, macAddress));
    }
    
    CallbackContext listWifiNetworkCallbackContext = null;

    private void executeListWifiNetworks(CallbackContext callbackContext) {
        Log.v(TAG, "====== executeListWifiNetworks ======");
        listWifiNetworkCallbackContext = callbackContext;
        this.listWifiNetworks();
        // result return when android send the result
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

    private String getCurrentSSID() {
        WifiManager wifiManager = (WifiManager) this.cordova.getActivity().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
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
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
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

    private static final int ACCESS_COARSE_LOCATION_REQ_CODE = 1;

    private void listWifiNetworks() {
        Log.d(TAG, "listWifiNetworks");
        // TODO : add a test : permission already ok
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, ">= M");
            this.cordova.requestPermission(this, ACCESS_COARSE_LOCATION_REQ_CODE, Manifest.permission.ACCESS_COARSE_LOCATION);
            // Check permission for android >= 6
            // Scan will start from "onRequestPermissionsResult" method
        } else{
            Log.d(TAG, "else");
            scanWifiAndGetResult();
            // permission was previously granted; or legacy device => Scan now
        }
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        // TODO : check resquest code and result
        Log.d(TAG, "onRequestPermissionsResult");
        scanWifiAndGetResult();
    }
    
    private void scanWifiAndGetResult() {
        Log.d(TAG, "scanWifiAndGetResult");
        final WifiManager wifiManager = (WifiManager) this.cordova.getActivity().getSystemService(Context.WIFI_SERVICE);

        this.cordova.getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<ScanResult> scanResults = wifiManager.getScanResults();
                try {
                    JSONArray results = new JSONArray();
                    if (scanResults != null && scanResults.size() > 0) {
                        for (ScanResult scanResult : scanResults) {
                            JSONObject result = new JSONObject();
                            result.put("BSSID", scanResult.BSSID);
                            result.put("SSID", scanResult.SSID);
                            result.put("capabilities", scanResult.capabilities);
                            result.put("frequency", scanResult.frequency);
                            result.put("level", scanResult.level);
                            result.put("timestamp", String.valueOf(scanResult.timestamp));
                            results.put(result);
                        }
                    } else {
                        Log.i(TAG, "scan result empty");
                    }

                    if(listWifiNetworkCallbackContext != null) {
                        listWifiNetworkCallbackContext.success(results);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Wifi scan failed", e);
                    listWifiNetworkCallbackContext.error("Wifi scan failed.");
                }
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifiManager.startScan();
    }
    
    private boolean isWifiConnected() {
        WifiManager wifiManager = (WifiManager) this.cordova.getActivity().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) { // Wi-Fi adapter is ON
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    }
    
}
