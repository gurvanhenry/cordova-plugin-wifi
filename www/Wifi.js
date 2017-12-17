var exec = require('cordova/exec');

var Wifi = {

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
    
    /* Array<Object> */ listWifiNetworks: function(success, error) {
        return exec(success, error, 'Wifi', 'listWifiNetworks', []);
    }, 

    // Futur developpement:
    
    // return SSID and ohter informations of active connection
    ///* WifiInfo */ getConnectionInfo: function(success, error) { },

    // return list of all the networks configured
    //getConfiguredNetworks: function(success, error) { },

    //disconnect: function(success, error) { },

    //boolean isConnected
    
    //getCurrentWifi
};

module.exports = Wifi;