var exec = require('cordova/exec');

var Wifi = {

    // Working methods:

    connectWifi: function(ssid, pass, success, error) {
        return exec(success, error, 'Wifi', 'connectWifi', [ssid, pass]);
    },

    connectWifiOpen: function(ssid, success, error) {
        return exec(success, error, 'Wifi', 'connectWifiOpen', [ssid]);
    },
    
    /* boolean */ isWifiEnabled: function(success, error) {
        return exec(success, error, 'Wifi', 'isWifiEnabled', []);
    },

    setWifiEnabled: function(enabled, success, error) {
        return exec(success, error, 'Wifi', 'setWifiEnabled', [enabled]);        
    },

    /* string */ getMacAddress: function(success, error) {
        return exec(success, error, 'Wifi', 'getMacAddress', []);        
    },

    // In developpement methods:
        
    // search avalaible wifi network
    /* List<ScanResult> */ scanWifi: function(success, error) { }, 
    
    // return SSID and ohter informations of active connection
    /* WifiInfo */ getConnectionInfo: function(success, error) { },

    // return list of all the networks configured
    getConfiguredNetworks: function(success, error) { },

    disconnect: function(success, error) { },

    // ask for other wifi needs...
    
    
    // maybe : 
    //boolean isConnected
    //getCurrentWifi


    // Temporary test methods:

    successTestMethod: function(success, error) {
        return exec(success, error, 'Wifi', 'successTestMethod', []);
    },

    errorTestMethod: function(success, error) {
        return exec(success, error, 'Wifi', 'errorTestMethod', []);
    },
};

module.exports = Wifi;