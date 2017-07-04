var request = require("request");
var fs = require("fs");
var path = require("path");


var task = [];
var taskRuning= false;
var defaultLocation = path.join(process.env.USERPROFILE+"","Downloads","Playstack");
if (!fs.existsSync(defaultLocation)){
    fs.mkdirSync(defaultLocation);
}



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
    var end = false;
    var location = data.location == undefined ? defaultLocation : data.location;
    if (!fs.existsSync(location)){
        fs.mkdirSync(location);
    }
    var p = path.join(location,data.filename);


    console.log(location);


    var out = fs.createWriteStream(p);

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
        if(!end){
            end = true;
            callback()
        }
    });

    req.on('response', function ( d ) {
        total = d.headers['content-length'];

        if(data.start){
            data.start();
        }
    });

    req.on('error',function(){
        if(data.error){
            data.error();
        }
        if(!end){
            end = true;
            callback()
        }
    });

    var stream = req.pipe(out);
    stream.on('finish', function () {
        if(!end){
            end = true;
            callback()
        }
    });

    stream.on('error', function(err) {

        req.abort();
        if(callback){
            req.abort();
            if(!end){
                end = true;
                callback()
            }
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
    },
    path : defaultLocation
};












