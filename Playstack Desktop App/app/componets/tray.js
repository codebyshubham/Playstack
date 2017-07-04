var gui = window.require('nw.gui');

var settings = require('./settings');
var platform = require('./platform');
var windowBehaviour = require("./window-behaviour");
var ws = require("../go/go-connection");

module.exports = {
    trayItems : function(win){
        return [
            {
                type: 'checkbox',
                label : "Lunch on Startup",
                setting: 'launchOnStartup',
                click : function(){
                    settings.launchOnStartup = this.checked;
                }
            },
            {
                type : 'checkbox',
                label : 'Check for Update on Launch',
                setting : 'checkUpdateOnLaunch',
                click : function(){
                    settings.checkUpdateOnLaunch = this.checked;
                }
            },
            {
                type : 'separator'
            },
            {
                label : 'Check Update',
                click : function(){
                    console.log("Check update");
                }
            },
            {
                label : 'Lunch Dev Tool',
                click : function(){
                    win.showDevTools();
                }
            }

        ].map(function(item){
            //for setting
            if (item.setting) {
                if (!item.hasOwnProperty('checked')) {
                    item.checked = settings[item.setting];
                }
                if (!item.hasOwnProperty('click')) {
                    item.click = function() {
                        settings[item.setting] = item.checked;
                    };
                }
            }
            return item;
        }).filter(function(item){
            //platform specific tray
            return !Array.isArray(item.platforms) || (item.platforms.indexOf(platform.type) != -1);
        }).map(function(item){
            var menuItem = new gui.MenuItem(item);
            menuItem.setting = item.setting;
            return menuItem;
        });
    },
    createTray : function(win){
        var menu = new gui.Menu();
        this.trayItems(win).forEach(function(item) {
            menu.append(item);
        });

        menu.append(new gui.MenuItem({
            type: 'separator'
        }));

        menu.append(new gui.MenuItem({
            label: 'Show Flysync',
            click: function() {
                win.show();
            }
        }));

        menu.append(new gui.MenuItem({
            label: 'Quit',
            click: function() {
                windowBehaviour.saveWindowState(win);
                win.close(true);
            }
        }));

        menu.items.forEach(function(item) {
            if (item.setting) {
                settings.watch(item.setting, function(value) {
                    item.checked = value;
                });
            }
        });

        return menu;
    },
    loadTray : function(win){
        if (win.tray) {
            win.tray.remove();
            win.tray = null;
        }

        var tray = new gui.Tray({
            icon: 'images/icon_tray.png'
        });

        tray.on('click', function() {
            win.show();
        });
        tray.tooltip = 'Flysync - The Android PC Suite';
        tray.menu = this.createTray(win);
    }
};
