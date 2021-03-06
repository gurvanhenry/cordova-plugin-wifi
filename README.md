# cordova-plugin-wifi

Version 1.0.3

## API

`connectWifi`: function(ssid, pass, success, error)

`connectWifiOpen`: function(ssid, success, error)

/* boolean */ `isWifiEnabled`: function(success, error)

`setWifiEnabled`: function(enabled, success, error)

/* boolean */ `isWifiConnected`: function(success, error)

/* string */ `getCurrentSSID`: function(success, error)

/* string */ `getMacAddress`: function(success, error)

/* Array\<Object\> */ `listWifiNetworks`: function(success, error)

/* Array\<string\> */ `getConfiguredNetworks`: function(success, error)

## Installation

### Add plugin in cordova app

```bash
cordova plugin add com.gurvanhenry.cordova-plugin-wifi
```

### Basic Usage

```javascript
var success = function(message) { alert(message); };
var error = function(message) { alert("Error"); };

Wifi.isWifiEnabled(this.success, this.error);
Wifi.setWifiEnabled(true, this.success, this.error);
Wifi.connectWifi("wifispot", "megapass", success, error);
Wifi.getMacAddress(this.success, this.error);
```

## Check out more with this sample

[https://github.com/gurvanhenry/cordova-plugin-wifi-sample](https://github.com/gurvanhenry/cordova-plugin-wifi-sample)

## Notes

- `getMacAddress` complexity
  - Android do not provide mac address with `WifiInfo` on Android >= 6
    - new security rules: [see API 6 changes](https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id)
    - see code Wifi.java getMacAddress_sol1()
    - the result will be "02:00:00:00:00:00"
  - This plugin include a workaround that use network information
    - see code Wifi.java getMacAddress_sol2()
  - Android 7 changed the rule again
    - DevicePolicyManager can be used [see API 7 changes](https://developer.android.com/about/versions/nougat/android-7.0-changes.html#afw)
    - this plugin do not handle that case

- `listWifiNetworks` permission issue
  - Since Android 6 (again) `ACCESS_COARSE_LOCATION` location permission as to be asked on runtime (popup)
