var Ws = function(url){

    this.event = new Evt();
    this.socket = new WebSocket(url);
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

Ws.prototype = {
    _count : 1000,

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

    task : function(name,args,callback){
        var count = this._getCount();
        var temp = {
            "count" : count,
            "name" : name,
            "args" : args,
            "type" : "task"
        };
        this.socket.send(JSON.stringify(temp));
        this.event.bind(count,callback);
    },

    _onmessage : function(evt){

        //console.log(evt.data);
        var temp = JSON.parse(evt.data);
        //console.log(temp);
        this.event.fire(temp.count,temp.data)
    },

    _getCount : function(){
        this._count = this._count + 1;
        return this._count;
    },
    disconnect : function(){
        this.socket.close();
    }
};