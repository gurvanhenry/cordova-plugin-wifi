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
    
    /* boolean */ isWifiConnected: function(success, error) {
        return exec(success, error, 'Wifi', 'isWifiConnected', []);
    },
    
    /* string */ getCurrentSSID: function(success, error) {
        return exec(success, error, 'Wifi', 'getCurrentSSID', []);
    },

    /* string */ getMacAddress: function(success, error) {
        return exec(success, error, 'Wifi', 'getMacAddress', []);
    },
    
    /* Array<Object> */ listWifiNetworks: function(success, error) {
        return exec(success, error, 'Wifi', 'listWifiNetworks', []);
    },
    
    /* Array<string> */ getConfiguredNetworks: function(success, error) {
        return exec(success, error, 'Wifi', 'getConfiguredNetworks', []);
    },

};

module.exports = Wifi;