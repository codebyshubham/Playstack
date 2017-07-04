var gui = require("nw.gui");
var win = gui.Window.get();
var path = require("path");



var windowBehaviour = require("./componets/window-behaviour");
var tray = require("./componets/tray");
var downloader = require("./componets/downloader");
var opener = require("./componets/opener");


win.show();
//win.showDevTools();
win.title = "Playstack";

tray.loadTray(win);

windowBehaviour.restoreWindowState(win);
windowBehaviour.bindWindowStateEvents(win);
windowBehaviour.set(win);


