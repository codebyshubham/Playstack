var gui = window.require('nw.gui');
var platform = require('./platform');
var settings = require('./settings');

var win = gui.Window.get();

module.exports = {


    close : function(c){
        this.saveWindowState(win);
        win.close(c);
    },
    min : function(){
        win.minimize();
    },

    max : function(){
        win.maximize();
    },

    unmax : function(){
        win.unmaximize();
    },


    closeWithEscKey: function(win) {
        var option = {
            key : "Shift+Escape",
            active : function() {
                console.log("Global desktop keyboard shortcut: " + this.key + " active.");
            },
            failed : function(msg) {
                console.log(msg);
            }
        };

        var shortcut = new gui.Shortcut(option);
        gui.App.unregisterGlobalHotKey(shortcut);
        gui.App.registerGlobalHotKey(shortcut);
        shortcut.on('active', function() {
            win.hide();
        });
    },


    set : function(win){
        gui.App.removeAllListeners('reopen');
        gui.App.on('reopen', function() {
            win.show();
        });

        // Don't quit the app when the window is closed
        if (!platform.isLinux) {
            win.removeAllListeners('close');
            win.on('close', function() {
                    this.saveWindowState(win);
                    win.hide();
            }.bind(this));
        }
    },

    bindWindowStateEvents: function(win) {
        win.removeAllListeners('maximize');
        win.on('maximize', function() {
            win.sizeMode = 'maximized';
        });

        win.removeAllListeners('unmaximize');
        win.on('unmaximize', function() {
            win.sizeMode = 'normal';
        });

        win.removeAllListeners('minimize');
        win.on('minimize', function() {
            win.sizeMode = 'minimized';
        });

        win.removeAllListeners('restore');
        win.on('restore', function() {
            win.sizeMode = 'normal';
        });
    },


    saveWindowState : function(win){
        var state = {
            mode: win.sizeMode || 'normal'
        };

        if (state.mode == 'normal') {
            state.x = win.x;
            state.y = win.y;
            state.width = win.width;
            state.height = win.height;
        }

        settings.windowState = state;
    },
    restoreWindowState : function(win){

        var state = settings.windowState;

        if (state.mode == 'maximized') {
            win.maximize();
        } else {
            win.resizeTo(state.width, state.height);
            win.moveTo(state.x, state.y);
        }
        win.show();
    }
};