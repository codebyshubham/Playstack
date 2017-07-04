var Store = require('jfs');
var path = require('path');
var gui = window.require('nw.gui');


var DEFAULT_SETTINGS = {
    launchOnStartup: false,
    checkUpdateOnLaunch : false,
    windowState: {}
};




var db = new Store(path.join(gui.App.dataPath, 'preferences.json'));

console.log(path.join(gui.App.dataPath, 'preferences.json'));

var settings = db.getSync('settings');
var watchers = {};
// Watch changes to the storage
settings.watch = function(name, callback) {
    if (!Array.isArray(watchers[name])) {
        watchers[name] = [];
    }
    watchers[name].push(callback);
};

Object.observe(settings,function(changes){
    db.save('settings', settings, function(err) {
        if (err) {
            console.error('Could not save settings', err);
        }
    });

    changes.forEach(function(change) {
        var newValue = change.object[change.name];
        var keyWatchers = watchers[change.name];

        if (keyWatchers && keyWatchers.length) {
            for (var i = 0; i < keyWatchers.length; i++) {
                try {
                    keyWatchers[i](newValue);
                } catch(ex) {
                    console.error(ex);
                    keyWatchers.splice(i--, 1);
                }
            }
        }
    });
});

Object.keys(DEFAULT_SETTINGS).forEach(function(key) {
    if (!settings.hasOwnProperty(key)) {
        settings[key] = DEFAULT_SETTINGS[key];
    }
});

module.exports = settings;