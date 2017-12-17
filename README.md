# cordova-plugin-wifi

Version 0.3.0

## API

`connectWifi`: function(ssid, pass, success, error)

`connectWifiOpen`: function(ssid, success, error)

/* boolean */ `isWifiEnabled`: function(success, error)

`setWifiEnabled`: function(enabled, success, error)

/* string */ `getMacAddress`: function(success, error)

// search avalaible wifi network<br>
/* Array\<Object\> */ `listWifiNetworks`: function(success, error)

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

## Try a sample

[https://gitlab.com/gurvanhenry/cordova-plugin-wifi-sample](https://gitlab.com/gurvanhenry/cordova-plugin-wifi-sample)

## Installation

### Add plugin in cordova app

```bash
cordova plugin add https://gitlab.com/gurvanhenry/cordova-plugin-wifi
```

### Usage

```javascript
var success = function(message) { alert(message); };
var error = function(message) { alert("Error"); };

Wifi.connectWifi("wifispot", "megapass", success, error);
Wifi.connectWifiOpen("wifiopen", success, error);
```

## Futur development

Other methods can be added (`getConnectionInfo`, `disconnect` ... )

Check [www/Wifi.js](www/Wifi.js) for more details
