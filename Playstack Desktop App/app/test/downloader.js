var request = require("request");
var fs = require("fs");
var path = require("path");


var task = [];
var taskRuning= false;
var defaultLocation = path.join(process.env.USERPROFILE+"","Downloads");
/*var data = {
    "url" : "",
    "filename" :"",
    "store" :""
}*/


function downloadTask(data,callback){
    var total =0;
    var get = 0;
    var req = request({
        method: 'GET',
        url: data.url
    });
    var out = fs.createWriteStream(data.location == undefined ? path.join(defaultLocation,data.filename) : path.join(data.location,data.filename));

    req.on('data', function (chunk)
    {
        get += chunk.length;
        if(data.progress){
            data.progress(parseFloat((get/total)*100).toFixed(2));
        }
    });

    req.on('end', function()
    {
        if(data.end){
            data.end();
        }
        if(callback){
            callback("req end");
        }
    });

    req.on('response', function ( data ) {
        total = data.headers['content-length'];
    });

    req.on('error',function(){
        if(data.error){
            data.error();
        }
        if(callback){
            callback("req error");
        }
    });

    var stream = req.pipe(out);
    stream.on('finish', function () {
        if(callback){
            callback("stream finsih");
        }
    });

    stream.on('error', function(err) {

        req.abort();
        if(callback){
            req.ab
            callback("stream error");
        }
    });
}



function downloadHandler(){
    if(!taskRuning && task.length > 0){
        taskRuning = true;
        var temp = task.shift();
        downloadTask(temp,function(c){
            console.log("data:"+c);
            taskRuning = false;
            downloadHandler();
        });
    }
}


module.exports = {
    add : function(data){
        task.push(data);
        downloadHandler();
    }
};












