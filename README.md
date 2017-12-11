# cordova-plugin-wifi

Version 0.3.0

## API

`connectWifi`: function(ssid, pass, success, error)

`connectWifiOpen`: function(ssid, success, error)

/* boolean */ `isWifiEnabled`: function(success, error)

`setWifiEnabled`: function(enabled, success, error)

/* string */ `getMacAddress`: function(success, error)

**NOT IMPLEMENTED**<br>
// search avalaible wifi network<br>
/* List<ScanResult> */ `scanWifi`: function(success, error)

**NOT IMPLEMENTED**<br>
// return SSID and ohter informations of active connection<br>
/* WifiInfo */ `getConnectionInfo`: function(success, error)

**NOT IMPLEMENTED**<br>
// return list of all the networks configured<br>
`getConfiguredNetworks`: function(success, error)

**NOT IMPLEMENTED**<br>
`disconnect`: function(success, error)

## Notes

- `getMacAddress` won't work on Android >= 6.0
  - on Android >= the result will be "02:00:00:00:00:00"
  - for security reason Android change the rules: [see API changes](https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id)
  - I'll try to find a workaround

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

