var Service = function(){
    this.event = new Evt();
    this.socket = new WebSocket("ws://127.0.0.1:8080/ws");
    var $this = this;

    this.socket.onopen = function(){
        $this._onopen();
    };
    this.socket.onclose = function(){
        $this._onclose();
    };
    this.socket.onmessage = function(evt){
        $this._onmessage(evt);
    };
    this.socket.onerror = function(){
        $this._onerror();
    };

    return this;
};


Service.prototype = {
    _onopen : function(){
        this.event.fire("connect");
    },
    _onclose : function(){
        this.event.fire("disconnect");
    },

    _onerror : function(){
        this.event.fire("disconnect");
    },


    on : function(event){
        if(typeof  event.connect == "function"){
            this.event.bind("connect",event.connect);
        }
        if(typeof event.disconnect == "function"){
            this.event.bind("disconnect",event.disconnect);
        }
    },


    send : function(data){
        this.socket.send(data);
    },

    _onmessage : function(e){
        var msg = e.data.split("@@");
        this.event.fire(msg[0],JSON.parse(msg[1]));
    },

    onMsg : function(name,callback){
        this.event.bind(name,callback);
    }
};