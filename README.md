# cordova-plugin-wifi

Version 0.0.1

## API

### Working methods:

`connectWifi`: function(ssid, pass, success, error)

`connectWifiOpen`: function(ssid, success, error)

### In developpement methods:

/* boolean */ `isWifiEnabled`: function(success, error)

/* string */ `getMacAddress`: function(success, error)

`setWifiEnabled`: function(/* boolean */ enabled, success, error)

// search avalaible wifi network
/* List<ScanResult> */ `scanWifi`: function(success, error) 
    
// return SSID and ohter informations of active connection
/* WifiInfo */ `getConnectionInfo`: function(success, error)

// return list of all the networks configured
`getConfiguredNetworks`: function(success, error)

`disconnect`: function(success, error)
