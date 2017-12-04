# cordova-plugin-wifi

Version 0.0.1

## API

### Working methods

`connectWifi`: function(ssid, pass, success, error)

`connectWifiOpen`: function(ssid, success, error)

### In developpement methods

/* boolean */ `isWifiEnabled`: function(success, error)

/* string */ `getMacAddress`: function(success, error)

`setWifiEnabled`: function(/* boolean */ enabled, success, error)

// search avalaible wifi network<br>
/* List<ScanResult> */ `scanWifi`: function(success, error)
    
// return SSID and ohter informations of active connection<br>
/* WifiInfo */ `getConnectionInfo`: function(success, error)

// return list of all the networks configured<br>
`getConfiguredNetworks`: function(success, error)

`disconnect`: function(success, error)

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

