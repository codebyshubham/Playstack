//event handler
var Evt = function(){
    this._listeners = {};
    return this;
};
Evt.prototype = {
    bind : function(event,listener){
        if(event == "undefined" || listener == "undefined")
            return;


        if(typeof this._listeners[event] == "undefined"){
            this._listeners[event] = [];
        }
        this._listeners[event].push(listener);
    },
    unbind : function(event){
        this._listeners[event] = [];
    },
    fire : function(event,data){
        if(typeof event == "string"){
            event = { type : event};
        }else if(typeof event == "number"){
            event = { type : event.toString()};
        }
        if(!event.target){
            event.target = this;
        }

        if(!event.type){
            throw new Error("Event type not valid..");
        }
        event.data = data;

        if(this._listeners[event.type] instanceof Array){
            var listeners = this._listeners[event.type];
            for(var i = 0 ;i < listeners.length ; i++){
                listeners[i].call(this,data);
            }
        }
    }
};
