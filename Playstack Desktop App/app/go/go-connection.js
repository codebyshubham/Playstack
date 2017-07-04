

/*var ws = new window.WebSocket("ws://127.0.0.1:8080/ws");
var mode = "connecting";


ws.onopen = function(){
    mode = "open";
    dispatchWatcher(mode);
};

ws.onmessage = function(e){
    var msg = e.data.split("@@");
    dispatchOns(msg[0],msg[1]);
};

ws.onclose = function(){
    mode = "close";
    dispatchWatcher(mode);
};

ws.onerror = function(){
    mode = "close";
    dispatchWatcher(mode);
};

var manager ={};
var watchers = {};
var ons = {};


manager.on = function(name,callback){
    if (!Array.isArray(ons[name])) {
        ons[name] = [];
    }
    ons[name].push(callback);
};

manager.send = function(msg){
    if(mode = "open"){
        ws.send(msg);
    }
};


function dispatchOns(name,data){
    var keyOns = ons[name];
    if (keyOns && keyOns.length) {
        for (var i = 0; i < keyOns.length; i++) {
            try {
                keyOns[i](data);
            } catch(ex) {
                console.error(ex);
                keyOns.splice(i--, 1);
            }
        }
    }
}




manager.watch = function(name,callback){
    if(name == mode){
        try{
            callback();
        }catch (ex){

        }
    }else{
        if (!Array.isArray(watchers[name])) {
            watchers[name] = [];
        }
        watchers[name].push(callback);
    }
};

Object.observe(watchers,function(changes){
    changes.forEach(function(change){
        if(change.name == mode){
            dispatchWatcher(change.name);
        }
    });

});

function dispatchWatcher(name){
    var keyWatchers = watchers[name];
    if (keyWatchers && keyWatchers.length) {
        for (var i = 0; i < keyWatchers.length; i++) {
            try {
                keyWatchers[i]();
            } catch(ex) {
                console.error(ex);
            }
            keyWatchers.splice(i--, 1);
        }
    }
}

module.exports = manager;
*/