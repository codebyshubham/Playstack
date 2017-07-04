var gui = window.require('nw.gui');


closeWithEscape(gui);

function closeWithEscape(gui){
    var option = {
        key : "Escape",
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
        gui.App.quit();
    });
}