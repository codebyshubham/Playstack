//list of all task
var TASK = {
    "login" : "login",
    "module" : {
        "main" :{
            "selective" : "selective"
        },

        "app" : {
            "app_list" : "app-list"
        },
        "sms" : {
            "thread_list" :"sms-thread-list",
            "sms_thread" : "sms-thread",
            "sms_send" : "sms-send"
        },
        "call" :{
            "call_log_list" :"call-log-list",
            "call_log_delete":"call-log-delete"
        },
        "gallery" :{
            "gallery_album_list" :"gallery-album-list",
            "gallery_album" :"gallery-album"
        },

        "file":{
            "file_storage_list" : "file-storage-list",
            "file_file_list":"file-file-list",
            "file_file_rename":"file-file-rename",
            "file_file_move" :"file-file-move",
            "file_file_copy":"file-file-copy",
            "file_file_delete":"file-file-delete"
        },
        "contact" :{
            "contact_contact_list":"contact-contact-list",
            "contact_group_list":"contact-group-list",
            "contact_group_contact_list":"contact-group-contact-list",
            "contact_contact":"contact-contact"
        }
    }
};


//all item storage for re-use
var ITEM = {
    "main":{
        "windows" :"",
        "home_windows":"",
        "search_window":"",
        "login_window" : "",
        "home" :"",
        "modules" :""
    },

    "login" :{
        "username" :"",
        "password" :""
    },
    "search" : {
        "device_view_container" :"",
        "device_view_item"  :"",
        "container" :""
    },
    "module" :{
        "title_bar" :{
            "title" :""
        },
        "app" : {
            "view_item" : "",
            "user_container" :"",
            "system_container" :"",
            "status_user" :"",
            "status_system" :""
        },
        "call_log" :{
            "item" :"",
            "item_outgoing" :"",
            "item_incoming" :"",
            "item_missed" :"",
            "all_container":"",
            "outgoing_container" :"",
            "incoming_container" :"",
            "missed_container" :""
        },
        "music" : {
            "list_album_container" :"",
            "list_album_item" :"",

            "album_view_detail" :"",
            "album_view_item_container" :"",
            "album_view_item" : ""
        },
        "sms" : {
            "thread_container" :"",
            "thread_item" :"",
            "thread_view_title":"",
            "thread_view_body":"",
            "thread_view_item_you" :"",
            "thread_view_item_me":"",
            "thread_view" : "",
            "thread_view_actions":""
        },
        "gallery" :{
            "left_item" :"",
            "left_container" :"",
            "gallery_item" :"",
            "gallery_container" :"",
            "gallery_title" :"",
            "gallery_body":""
        },
        "file":{
            "item_folder" :"",
            "item_file" :"",
            "item_back":"",
            "item_drive":"",
            "title_path":"",
            "body":""
        },
        "contact" : {
            "list_container" :"",
            "list_item" :"",
            "title" : "",
            "body_container" :"",
            "body_item" :""
        },
        "download" : {
            "list":"",
            "item":""
        }
    }
};

//available device
var AVAILABLE_DEVICE = {}; //device online device
var CURRENT_DEVICE; //define live device
var SESSION; //login session create
var SELECTIVE; //selective stus
var SEARCH_INTERVAL;
var FILE = {
    stack:[]
};



//service connection
var service = new Service();
service.on({
    "connect" : function(){
        /*SEARCH_INTERVAL = setInterval(function(){
            service.send("_mobile_list_rq");
        },5000);*/
        service.send("_mobile_list_rq_start");
    },
    "disconnect" : function(){
        clearInterval(SEARCH_INTERVAL);
    }
});

//websocket connection to android phone
var ws; //live connected device


var player = new Player({
    "_step" : function(per){
        ITEM.module.music.player_status.css("width",per+"%");
    },
    "_stop" : function(){
        ITEM.module.music.player_controle.bindData({
            status:""
        });
    },
    "_play" : function(data){
        console.log(data);
        ITEM.module.music.player_controle.bindData({
            status:""
        });

        ITEM.module.music.player_title.bindData({
            title : data.data.title
        });
        ITEM.module.music.player_cover.bindData({
            cover:"url('"+ httpURL({
                "task" :"album-cover",
                "location" : data.album.albumArt
            }) +"')"
        });

    },
    "_pause" : function(){
        ITEM.module.music.player_controle.bindData({
            status:""
        });
    }
});





$(document).ready(function(){

    var $scrollbar = $(".scrollbar");
    $scrollbar.perfectScrollbar();





    init();


    home_module();



    search_module();//all stuff about search page
    login_module();//all stuff about login page
    modules(); //all module stuff desing

    //debug_mode();//debug mde
});


function init(){
    var $windows = $("body > .platform > .window > .container");
    var $search_window = $("body > .platform > .window > .main > .container > .container");
    var $search  = $("body > .platform > .window > .main > .container > .search");
    var $login = $("body > .platform > .window > .main > .container > .login");

    var $modules = $("body > .platform > .window > .module");
    var $home = $("body > .platform > .window > .main");


    ITEM.main.windows = $windows;
    ITEM.main.home_windows = $search_window;

    ITEM.main.search_window = $search;
    ITEM.main.login_window = $login;

    ITEM.main.home = $home;
    ITEM.main.modules = $modules;
}


function home_module(){
    var $home_title_action = $("body > .platform > .window > .main > .container > .search > .title-bar > .actions");
    $home_title_action.bindData({
        "close" : function(){
            console.log("ok");
            windowBehaviour.close();
        },
        "min" : function(){
            windowBehaviour.min();
        },
        "max" : function(){
            windowBehaviour.max();
        }
    });


    var $social = $("body > .platform > .window > .main > .container > .search > .bottom-bar > .social");
    $social.bindData({
        "facebook" : function(){
            opener.open("https://www.facebook.com/pentestershubham");
        },
        "twitter" : function(){
            opener.open("https://twitter.com/pentestershubh");
        },
        "github" : function(){
            opener.open("https://github.com/pentestershubham");
        }

    });


}

function open_search_module(){
    player.stop();
    player.clear();
    ITEM.main.modules.removeClass('active');
    ITEM.main.home.addClass('active');

    ITEM.main.login_window.removeClass('active');
    ITEM.main.search_window.addClass('active');

}


function debug_mode(){
    CURRENT_DEVICE = {
        "ip" : "192.168.0.100",
        "name" : "shubham-mobile"
    };

    var username = "admin";
    var password = "admin";

    ws = new Ws("ws://" + CURRENT_DEVICE.ip +":8080/main");
    ws.on({
        "connect" : function(){
            //after connect open login module
            ws.task("login",[username,password],function(data){
                if(data.isLogin){
                    SESSION = data.session;

                    ws.task("selective",[],function(d){
                        console.log(d);
                        SELECTIVE = d;
                    });
                    //open modules layout && hide search layout
                    open_module_stuff();


                }else{
                    //handle login error
                    console.log("username or password wrong!!")
                }

            });
        },
        "disconnect": function(){
            //handle erroer
            console.log("some erroe...");
        }
    });

}






function modules(){
    module_tab_init(); //tab init
    module_title();//module title bar



    module_app(); //all stuff about app module
    module_call_log();//all stuff about call-log
    module_music();//all stuff about music
    module_sms();//all stuff about sms

    module_gallery();//all stuff about gallery
    module_file();//all stuff about file manager
    module_contact();
    module_download();
}

function module_download(){
    var $download_list = $("body > .platform > .window > .module > .modules > .container > .download > .container > .body > .main");
    var $download_item = $("body > .platform > .window > .module > .modules > .container > .download > .container > .body > .main > .item");

    ITEM.module.download.list = $download_list;
    ITEM.module.download.item = $download_item.clone();

    $download_list.empty();

    /*module_download_add({
        url :"http://127.0.0.1/a.zip",
        filename :"shubham.zip"
    });*/
}



function module_download_add(data){
    var $item = ITEM.module.download.item.clone();
    var $stats = $item.find('.status');
    var main_path = path.join(downloader.path,data.folder != undefined? data.folder:"")

    $item.bindData({
        "filename" : data.filename,
        "path" : main_path,
        "openFolder" : function(){
            opener.open(main_path);
        },
        "status" : "pending"
    });

    $item.addClass('pending');
    ITEM.module.download.list.prepend($item);
    downloader.add({
        "url" : data.url,
        "location" :main_path,
        "filename" : data.filename,
        "start":function(){
            $item.removeClass('pending');
            $item.removeClass('complete');
            $item.addClass('downloading');

            $item.bindData({
                "status":"downloading"
            })
        },
        "end" : function(){
            $stats.css("width","0%");
            $item.removeClass('pending');
            $item.removeClass('downloading');
            $item.addClass('complete');
            $item.bindData({
                "status":"complete",
                "openFile" : function(){
                    opener.open(path.join(main_path,data.filename));
                }
            })
        },
        "error" : function(){

        },
        "progress" : function(per){
            $stats.css("width", per+"%");
        }
    });


}



function module_contact(){
    var $list_container = $("body > .platform > .window > .module > .modules > .container > .contact > .container > .left > .threads");
    var $list_item = $("body > .platform > .window > .module > .modules > .container > .contact > .container > .left > .threads > .item");
    var $title = $("body > .platform > .window > .module > .modules > .container > .contact > .container > .right > .title");


    var $body_container = $("body > .platform > .window > .module > .modules > .container > .contact > .container > .right > .body > .container");
    var $body_item = $("body > .platform > .window > .module > .modules > .container > .contact > .container > .right > .body > .container > .item");



    ITEM.module.contact.list_container = $list_container;
    ITEM.module.contact.list_item = $list_item.clone();
    ITEM.module.contact.title = $title;
    ITEM.module.contact.body_container = $body_container;
    ITEM.module.contact.body_item = $body_item.clone();

    $body_container.empty();
    $list_container.empty();

}





function module_file(){
    var $item_folder = $("body > .platform > .window > .module > .modules > .container > .file > .container > .body > .folder");
    var $item_file = $("body > .platform > .window > .module > .modules > .container > .file > .container > .body > .file");
    var $item_back = $("body > .platform > .window > .module > .modules > .container > .file > .container > .body > .back");
    var $item_drive = $("body > .platform > .window > .module > .modules > .container > .file > .container > .body > .drive");
    var $title_path = $("body > .platform > .window > .module > .modules > .container > .file > .container > .title-bar > .path");
    var $body = $("body > .platform > .window > .module > .modules > .container > .file > .container > .body");
    var $back = $("body > .platform > .window > .module > .modules > .container > .file > .container > .title-bar > .back");

    var $actions = $("body > .platform > .window > .module > .modules > .container > .file > .container > .title-bar > .actions");
    $actions.bindData({
        download : function(){
            if(CURRENT_FILE != undefined){
                module_download_add({
                    filename : CURRENT_FILE.name,
                    "folder" : "file",
                    url : httpURL({
                        task :"file-download",
                        "location" : CURRENT_FILE.path
                    })
                });
            }
        }
    });

    $back.on({
        "click" : function(){
            ws_module_file_back();
        }
    });

    ITEM.module.file.item_folder = $item_folder.clone();
    ITEM.module.file.item_file= $item_file.clone();
    ITEM.module.file.item_back = $item_back.clone();
    ITEM.module.file.item_drive = $item_drive.clone();
    ITEM.module.file.title_path = $title_path;
    ITEM.module.file.body = $body;

    $body.empty();

}


function module_gallery(){
    var $left_item = $("body > .platform > .window > .module > .modules > .container > .gallery > .container > .left > .container > .item");
    var $gallery_item = $("body > .platform > .window > .module > .modules > .container > .gallery > .container > .right > .body > .container > .item");
    var $gallery_title = $("body > .platform > .window > .module > .modules > .container > .gallery > .container > .right > .top-bar > .title");


    var $left_container = $("body > .platform > .window > .module > .modules > .container > .gallery > .container > .left > .container");
    var $gallery_container = $("body > .platform > .window > .module > .modules > .container > .gallery > .container > .right > .body > .container");
    var $gallery_body = $("body > .platform > .window > .module > .modules > .container > .gallery > .container > .right > .body");


    ITEM.module.gallery.left_item = $left_item.clone();
    ITEM.module.gallery.left_container = $left_container;
    ITEM.module.gallery.gallery_item = $gallery_item.clone();
    ITEM.module.gallery.gallery_container = $gallery_container;
    ITEM.module.gallery.gallery_title = $gallery_title;
    ITEM.module.gallery.gallery_body = $gallery_body;

    ITEM.module.gallery.gallery_container.empty();
    ITEM.module.gallery.left_container.empty();

}


function module_title(){
    var $title = $("body > .platform > .window > .module > .title-bar > .title");
    var $actions = $("body > .platform > .window > .module > .title-bar > .actions");
    var $logout = $("body > .platform > .window > .module > .title-bar > .logout");


    $logout.on({
        "click" : function(){
            if(CURRENT_DEVICE != undefined){
                ws_close();
            }
            open_search_module();
        }
    });

    $actions.bindData({
        "close" : function(){
            console.log("ok");
            windowBehaviour.close();
        },
        "min" : function(){
            windowBehaviour.min();
        },
        "max" : function(){
            windowBehaviour.max();
        }
    });


    ITEM.module.title_bar.title = $title;


}


function module_sms(){
    var $thread_container = $("body > .platform > .window > .module > .modules > .container > .sms > .container > .left > .threads");
    var $thread_item = $("body > .platform > .window > .module > .modules > .container > .sms > .container > .left > .threads > .item");


    ITEM.module.sms.thread_item = $thread_item.clone();
    ITEM.module.sms.thread_container= $thread_container;
    ITEM.module.sms.thread_container.empty();



    var $thread_view_title = $("body > .platform > .window > .module > .modules > .container > .sms > .container > .right > .title");
    var $thread_view_body = $("body > .platform > .window > .module > .modules > .container > .sms > .container > .right > .body > .container");
    var $thread_view_item_you = $("body > .platform > .window > .module > .modules > .container > .sms > .container > .right > .body > .container > .item-you");
    var $thread_view_item_me = $("body > .platform > .window > .module > .modules > .container > .sms > .container > .right > .body > .container > .item-me");
    var $thread_view = $("body > .platform > .window > .module > .modules > .container > .sms > .container > .right > .body");
    var $thread_view_action = $("body > .platform > .window > .module > .modules > .container > .sms > .container > .right > .action");
    var $thread_view_send = $("body > .platform > .window > .module > .modules > .container > .sms > .container > .right > .action > .send");





    ITEM.module.sms.thread_view_title = $thread_view_title;
    ITEM.module.sms.thread_view_body = $thread_view_body;
    ITEM.module.sms.thread_view_item_you = $thread_view_item_you.clone();
    ITEM.module.sms.thread_view_item_me = $thread_view_item_me.clone();
    ITEM.module.sms.thread_view = $thread_view;
    ITEM.module.sms.thread_view_actions = $thread_view_action;

    ITEM.module.sms.thread_view_body.empty();


    $thread_view_send.on({
        "click" : function(){
            var data = $thread_view_action.data('data-flysync');
            var text = $("body > .platform > .window > .module > .modules > .container > .sms > .container > .right > .action > .input > input").val();

            console.log(data && (text != ""));
            if(data && text != ""){
                ws.task(TASK.module.sms.sms_send,[data.address,text],function(data){
                    console.log(data);
                });
            }
        }
    });

}



function module_music(){
    var $list_album = $("body > .platform > .window > .module > .modules > .container > .music > .container > .list-album > .container");
    var $item_album = $("body > .platform > .window > .module > .modules > .container > .music > .container > .list-album > .container > .item");

    ITEM.module.music.list_album_item = $item_album.clone();
    $list_album.empty();
    ITEM.module.music.list_album_container = $list_album;



    //for right side album view
    var $album_view_detail = $("body > .platform > .window > .module > .modules > .container > .music > .container > .view-album > .container > .details");
    var $album_view_item_container = $("body > .platform > .window > .module > .modules > .container > .music > .container > .view-album > .container > .song-list");
    var $album_view_item = $("body > .platform > .window > .module > .modules > .container > .music > .container > .view-album > .container > .song-list > .item");


    ITEM.module.music.album_view_detail = $album_view_detail;
    ITEM.module.music.album_view_item = $album_view_item.clone();
    $album_view_item_container.empty();
    ITEM.module.music.album_view_item_container = $album_view_item_container;


    //player controle
    var $controle = $("body > .platform > .window > .module > .modules > .container > .music > .container > .player > .container > .actions");
    var $status = $("body > .platform > .window > .module > .modules > .container > .music > .container > .player > .status");
    var $title = $("body > .platform > .window > .module > .modules > .container > .music > .container > .player > .container > .title");
    var $cover = $("body > .platform > .window > .module > .modules > .container > .music > .container > .player > .container > .cover");





    ITEM.module.music.player_controle = $controle;
    ITEM.module.music.player_status = $status;
    ITEM.module.music.player_title = $title;
    ITEM.module.music.player_cover  = $cover;


    $controle.bindData({
        random : function(){

        },
        prev : function(){
            player.prev();
        },
        next : function(){
            player.next();
        },
        play : function(){
            if(!player.isPlaying()){
                player.play();
            }else{
                player.pause();
            }

        }
    });

}




function module_call_log(){
    //tab init
    module_call_log_tab_init();

    var $item = $("body > .platform > .window > .module > .modules > .container > .call-log > .container > .body > .container > .view > .item");
    var $item_outgoing = $item.clone().addClass('outgoing');
    var $item_incoming = $item.clone().addClass('incoming');
    var $item_missed = $item.clone().addClass('missed');


    var $all_container = $("body > .platform > .window > .module > .modules > .container > .call-log > .container > .body > .container > .all");
    var $outgoing_container = $("body > .platform > .window > .module > .modules > .container > .call-log > .container > .body > .container > .outgoing");
    var $incoming_container = $("body > .platform > .window > .module > .modules > .container > .call-log > .container > .body > .container > .incoming");
    var $missed_container = $("body > .platform > .window > .module > .modules > .container > .call-log > .container > .body > .container > .missed");

    $all_container.empty();

    ITEM.module.call_log.all_container = $all_container;
    ITEM.module.call_log.outgoing_container = $outgoing_container;
    ITEM.module.call_log.incoming_container = $incoming_container;
    ITEM.module.call_log.missed_container = $missed_container;

    ITEM.module.call_log.item = $item.clone();
    ITEM.module.call_log.item_outgoing = $item_outgoing.clone();
    ITEM.module.call_log.item_incoming = $item_incoming.clone();
    ITEM.module.call_log.item_missed = $item_missed.clone();

}


function doCall(number){
    ws.task('call-call',[number],function(){

    });
}























function httpURL(url,parms){

    var URL;
    if(typeof url != "string"){
        parms = url;
        url = "";
    }


    URL = "http://" + CURRENT_DEVICE.ip +":8080/" + url;
    if(parms){
        URL = URL +"?";
        $.each(parms,function(key,value){
            URL = URL + key +"=" + encodeURIComponent(value) + "&";
        });
    }
    return URL;
}


function module_app(){
    //tab
    //get item and store

    module_app_tab_init(); // app tab

    var $user_container= $("body > .platform > .window > .module > .modules > .container > .app > .container > .body > .container > .user");
    var $system_container= $("body > .platform > .window > .module > .modules > .container > .app > .container > .body > .container > .system");
    var $item = $("body > .platform > .window > .module > .modules > .container > .app > .container > .body > .container > .view > .item");
    var $status_user = $("body > .platform > .window > .module > .modules > .container > .app > .container > .status > .user");
    var $status_system = $("body > .platform > .window > .module > .modules > .container > .app > .container > .status > .system");



    ITEM.module.app.view_item = $item.clone();
    ITEM.module.app.user_container = $user_container;
    ITEM.module.app.system_container = $system_container;
    ITEM.module.app.status_user = $status_user;
    ITEM.module.app.status_system  = $status_system;



    //free all container
    $user_container.empty();
    $system_container.empty();

}













function login_module(){
    var $login = $("body > .platform > .window > .main > .container > .login");
    var $login_close = $("body > .platform > .window > .main > .container > .login > .container > .box > .close");
    var $login_btn = $("body > .platform > .window > .main > .container > .login > .container > .box > .stuff > .container > .login > .icon");


    var $username = $("body > .platform > .window > .main > .container > .login > .container > .box > .stuff > .container > .username");
    var $password = $("body > .platform > .window > .main > .container > .login > .container > .box > .stuff > .container > .password");


    ITEM.login.password = $password;
    ITEM.login.username = $username;

    $login_close.on({
        "click" : function(){
            $login.removeClass('active');
            //close connection
            ws_close();
        }
    });




    $login_btn.on({
        "click" : function(){
            doLogin($username,$password);
        }
    });

    $password.keypress(function(e) {
        if(e.which == 13) {
            doLogin($username,$password);
        }
    });
}

function doLogin($username,$password){
    if(CURRENT_DEVICE != undefined){
        console.log(CURRENT_DEVICE);
        ws.task("login",[$username.val(),$password.val()],function(data){
            if(data.isLogin){
                SESSION = data.session;

                //selective
                ws.task("selective",[],function(d){
                    console.log(d);
                    SELECTIVE = d;
                });

                //open modules layout && hide search layout
                open_module_stuff();


            }else{
                //handle login error
                console.log("username or password wrong!!")
            }

        });
    }
}


function open_module_stuff(){

    //remove login class
    ITEM.main.login_window.removeClass('active');

    //hide search and open module
    var $container = $("body > .platform > .window > .container");
    $container.removeClass('active');

    var $module  = $("body > .platform > .window > .module");
    $module.addClass('active');




    ws_module_title();
    ws_module_app();
    ws_module_call_log();
    ws_module_music();
    ws_module_sms();
    ws_module_gallery();
    ws_module_file();

    ws_module_contact();
}

function ws_module_contact(){
    ws.task(TASK.module.contact.contact_contact_list,[],function(data){
        ITEM.module.contact.list_container.empty();
        $.each(data,function(i,val){
            var $item = ITEM.module.contact.list_item.clone();
            $item.bindData({
                name :val.name,
                star : val.star == false ? "" : "",
                "click" : function(){
                    if(!$(this).hasClass('active')){
                        var items = $("body > .platform > .window > .module > .modules > .container > .contact > .container > .left > .threads > .item");
                        items.removeClass('active');
                        $(this).addClass('active');
                    }

                    ws_module_contact_view(val);
                },
                "data-original" : httpURL({
                    task :"contact-photo",
                    id: val.id,
                    lookup : val.lookup
                })
            });

            ITEM.module.contact.list_container.append($item);
        });
        var images = $("body > .platform > .window > .module > .modules > .container > .contact > .container > .left > .threads > .item > .cover > .img");
        images.lazyload({
            container : ITEM.module.contact.list_container,
            fadIn :200
        });

    });
}


function ws_module_contact_view(contact){
    ws.task(TASK.module.contact.contact_contact,[contact.id],function(data){
        ITEM.module.contact.title.bindData({
            "name" : data.name,
            "number" : data.isHasNumber == true ? data.number : "",
            src:httpURL({
                task :"contact-photo",
                large : true,
                id : data.id,
                lookup : data.lookup
            }),
            "group" :ws_module_contact_view_groups(data)
        });

        console.log(data);

        ITEM.module.contact.body_container.empty();
        $.each(data.ans.raws[0].items.phone,function(i,val){
            var $item = ITEM.module.contact.body_item.clone();
            $item.bindData({
                value : val.item.number,
                "type" : val.item.lalbe ? val.item.lalbe :"Mobile",
                "doCall" : function(){
                    doCall(val.item.number);
                }
            });
            ITEM.module.contact.body_container.append($item);
        });

        $.each(data.ans.raws[0].items.email,function(i,val){
            var $item = ITEM.module.contact.body_item.clone();
            $item.bindData({
                value : val.item.address,
                "type" : val.item.lalbe ? val.item.lalbe :"EMail",
                "call" : 0,
                "dpEmail" : function(){
                    opener.open(val.item.address);
                }

            });
            ITEM.module.contact.body_container.append($item);
        });
    });
}

function ws_module_contact_view_groups(data){
    console.log(data);
    var ans = "";
    $.each(data.gropusIn,function(i,val){
        var temp = data.groups[val +""];
        console.log(temp);
        if(temp != undefined){
            ans = ans + temp +", ";
        }
    });
    return ans;
}



var CURRENT_FILE;
function ws_module_file(){
    ws.task(TASK.module.file.file_storage_list,[],function(data){
        ITEM.module.file.body.empty();
        ITEM.module.file.title_path.text("Storages");
        $.each(data,function(i,val){
            var $item = ITEM.module.file.item_drive.clone();
            $item.bindData({
                name : val.title,
                info : "(" + val.used +"/" + val.total+")",
                "dblclick" : function(e){
                    FILE.root = val.root;
                    File.stack =[];
                    ws_module_file_update();
                }

            });

            ITEM.module.file.body.append($item);
        });
    });
}

function ws_module_file_back(){
    FILE.root = FILE.stack.pop();
    if(FILE.root == undefined){
        ws_module_file();
        return;
    }
    ws_module_file_update();
}

function ws_module_file_update(){
    ws.task(TASK.module.file.file_file_list,[FILE.root],function(data){

        var $back = ITEM.module.file.item_back.clone();
        $back.bindData({
            "dblclick" : function(){
                ws_module_file_back();
            }
        });
        ITEM.module.file.body.empty();
        ITEM.module.file.title_path.text(FILE.root);
        ITEM.module.file.body.append($back);

        $.each(data.directory,function(i,val){
            var addBack = false;
            var $item = ITEM.module.file.item_folder.clone();
            $item.bindData({
                name : val.name,
                "click" : function(){
                    if($(this).hasClass('active')){
                        return;
                    }
                    var $items = $("body > .platform > .window > .module > .modules > .container > .file > .container > .body > .item");
                    $items.removeClass('active');
                    $(this).addClass('active');
                    CURRENT_FILE == undefined;

                },
                "dblclick" : function(){
                    if(addBack){
                        return;
                    }


                    FILE.stack.push(FILE.root);
                    FILE.root = val.path;
                    ws_module_file_update();
                    addBack = true;
                }
            });
            ITEM.module.file.body.append($item);
        });

        $.each(data.file,function(i,val){
            var $item = ITEM.module.file.item_file.clone();
            $item.bindData({
                name : val.name,
                "click" : function(){
                    if($(this).hasClass('active')){
                        return;
                    }
                    var $items = $("body > .platform > .window > .module > .modules > .container > .file > .container > .body > .item");
                    $items.removeClass('active');
                    $(this).addClass('active');
                    CURRENT_FILE = val;
                }
            });
            ITEM.module.file.body.append($item);
        });
    });
}


function ws_module_gallery(){
    ws.task(TASK.module.gallery.gallery_album_list,[],function(data){
        ITEM.module.gallery.left_container.empty();
        $.each(data,function(i,val){
            var $item = ITEM.module.gallery.left_item.clone();
            $item.bindData({
                "name" : val.name,
                "item" :"115 items",
                "data-original" : httpURL({
                    task :"gallery-thumb",
                    id :val.id
                }),
                "click" : function(){
                    if($(this).hasClass('active')){
                        return;
                    }




                    var $items = $("body > .platform > .window > .module > .modules > .container > .gallery > .container > .left > .container > .item");
                    $items.removeClass('active');
                    $(this).addClass('active');

                    ws_module_gallery_view(val);
                }

            });
            ITEM.module.gallery.left_container.append($item);
        });

        ITEM.module.gallery.left_container.find("img").lazyload({
            container: ITEM.module.gallery.left_container,
            effect : "fadeIn",
            threshold : 200
        });
    });
}

function  ws_module_gallery_view(item){
    ITEM.module.gallery.gallery_title.bindData({
        "name" : item.name,
        "item" :""
    });

    ws.task(TASK.module.gallery.gallery_album,[item.b_id],function(data){
        ITEM.module.gallery.gallery_container.empty();

        ITEM.module.gallery.gallery_title.bindData({
            "item" : data.length +" items"
        });
        $.each(data,function(i,val){
            var $item = ITEM.module.gallery.gallery_item.clone();
            $item.bindData({
                "data-original" : httpURL({
                    task :"gallery-thumb",
                    "id" : val.id
                }),
                download : function(){
                    module_download_add({
                        filename : val.fname,
                        folder :"gallery",
                        "url" : httpURL({
                            task : "file-download",
                            location : val.data
                        })
                    });

                    console.log(val);

                }
            });
            ITEM.module.gallery.gallery_container.append($item);
        });

        ITEM.module.gallery.gallery_container.find("img").lazyload({
            container: ITEM.module.gallery.gallery_body,
            effect : "fadeIn",
            threshold : 300
        });
    });
}


function ws_module_title(){
    ITEM.module.title_bar.title.bindData({
        "device" : CURRENT_DEVICE.name,
        "module" : "APP"
    });
}

function ws_module_sms(){
    ws.task(TASK.module.sms.thread_list,[],function(data){
        ITEM.module.sms.thread_container.empty();
        $.each(data,function(i,thread){
            var $item = ITEM.module.sms.thread_item.clone();

            $item.bindData({
                "name" : thread.name || thread.address,
                "last" : thread.snippet,
                "click":function(){
                    ws_module_sms_show_thread(thread);
                }
            });
            if(thread.unread)
                $item.addClass("unread");

            ITEM.module.sms.thread_container.append($item);
        });


    });
}


function ws_module_sms_show_thread(thread){
    ITEM.module.sms.thread_view_title.bindData({
        title : thread.name || thread.address
    });

    ITEM.module.sms.thread_view_actions.data('data-flysync',thread);

    ws.task(TASK.module.sms.sms_thread,[thread.id],function(data){


        ITEM.module.sms.thread_view_body.empty();
        ITEM.module.sms.thread_view.scrollTop(-ITEM.module.sms.thread_view_body[0].scrollHeight);

        $.each(data.sms,function(i,sms){
            var $item;
            if(sms.you){
                $item = ITEM.module.sms.thread_view_item_you.clone();
            }else{
                $item = ITEM.module.sms.thread_view_item_me.clone();
            }

            $item.bindData({
                "data" : sms.body
            });


            ITEM.module.sms.thread_view_body.append($item);
        });
        ITEM.module.sms.thread_view.scrollTop(ITEM.module.sms.thread_view_body[0].scrollHeight);

    });

}


function ws_module_music(){
    ws.task("album-list",[],function(data){
        ITEM.module.music.list_album_container.empty();
        $.each(data.albums,function(i,album){
            var $item = ITEM.module.music.list_album_item.clone();
            $item.bindData(album);
            ITEM.module.music.list_album_container.append($item);

            $item.on({
                "click" : function(){
                    var $items = $("body > .platform > .window > .module > .modules > .container > .music > .container > .list-album > .container > .item");
                    $items.removeClass('active');
                    $(this).addClass('active');

                    ws_module_music_show_album(album);
                }
            });
        });
    });
}



function ws_module_music_show_album(album){
    ITEM.module.music.album_view_detail.bindData({
        album : album.album,
        artist : album.artist,
        //cover : "url('"+ httpURL("?task=album-cover&location=" + album.albumArt) +"')"
        cover : "url('"+ httpURL({
            "task" :"album-cover",
            "location" : album.albumArt
        }) +"')"
    });


    ws.task("album-song-list",[album.id],function(data){
        ITEM.module.music.album_view_item_container.empty();
        $.each(data,function(i,song){
            console.log(song);
            var $item = ITEM.module.music.album_view_item.clone();
            $item.bindData({
                "index" : i + 1,
                "title" : song.title,
                "duration" : song.duration,
                "download" : function(){
                    module_download_add({
                        filename : song.fname,
                        "url" : httpURL({
                            task :"file-download",
                            location : song.data
                        }),
                        "folder" : "music"
                    });
                    console.log(httpURL({
                        task :"file-download",
                        location : song.data
                    }));
                },
                "delete" : function(){
                    console.log("delete");
                },
                "click" : function(){
                    //httpURL("song.mp3?task=song&location="+song.data)
                    //console.log(httpURL("song.mp3?task=song&location="+song.data));
                    //player.clear();
                    player.add({
                        id : song.id,
                        url : httpURL({
                            "task" : "song",
                            "location" : song.data
                        }),
                        data:song,
                        album : album
                    });
                    // player.play();
                }
            });
            ITEM.module.music.album_view_item_container.append($item);
        });
    });
}


function ws_module_call_log(){
    ws.task(TASK.module.call.call_log_list,[],function(data){
        $.each(data.reverse(),function(i,log){
            var $item;

            switch (log.type){
                case 1: //incomimng
                    $item = ITEM.module.call_log.item_incoming.clone();
                    break;
                case 2: //outgoing
                    $item = ITEM.module.call_log.item_outgoing.clone();
                    break;
                case 3://missed
                    $item = ITEM.module.call_log.item_missed.clone();
                    break;
            }

            $item.bindData({
                index : i + 1,
                name : log.name,
                duration : log.duration,
                number : log.number,
                date : log.date,
                delete : function(){
                    ws_module_call_log_delete(log,function(is_delete){
                        if(is_delete){
                            $item.remove();
                        }
                    });
                },
                call : function(){
                    doCall(log.number);
                }
            });

            ITEM.module.call_log.all_container.append($item);
        });

    });
}

function ws_module_call_log_delete(log,callback){
    ws.task(TASK.module.call.call_log_delete,[log.id],function(i){
        if(callback){
            callback(i > 0);
        }
    });
}


function ws_module_app(){

    //get app list
    ws.task("app-list",[],function(data){

        //for user app
        ITEM.module.app.user_container.empty();
        ITEM.module.app.status_user.text(data.user.length + " user app installed");
        $.each(data.user,function(i,val){
            var $item = ITEM.module.app.view_item.clone();
            //var url = "url('"+httpURL("?task=app-icon&package="+ val.package)+"')";
            var url = "url('"+httpURL({
                    "task" : "app-icon",
                    "package" : val.package
                })+"')";

            $item.bindData({
                "index" : i+1,
                "name" : val.name,
                "size" : formatBytes(val.size,1),
                "icon" : url,
                "uninstall" : function(e){
                    e.stopPropagation();
                    ws.task("app-uninstall",[val.package],function(data){
                        console.log(data);
                    });
                },
                "download" :function(e){
                    e.stopPropagation();

                    //$.fileDownload(httpURL("app.apk?task=app-download&location=" + val.dir));
                    /*$.fileDownload(httpURL("app.apk",{
                        "task" : "app-download",
                        "location" : val.dir
                    }));*/
                    module_download_add({
                        filename : val.name+".apk",
                        url : httpURL("app.apk",{
                            "task" : "app-download",
                            "location" : val.dir
                        }),
                        folder:"app"
                    });


                },
                "click" : function(e){
                    e.stopPropagation();
                }
            });
            ITEM.module.app.user_container.append($item);

        });


        //system app
        ITEM.module.app.system_container.empty();
        ITEM.module.app.status_system.text(data.system.length + " system app installed");
        $.each(data.system,function(i,val){
            var $item = ITEM.module.app.view_item.clone();
            //var url = "url('"+httpURL("?task=app-icon&package="+ val.package)+"')";
            var url = "url('"+httpURL({
                    "task" : "app-icon",
                    "package" : val.package
                })+"')";
            $item.bindData({
                "index" : i+1,
                "name" : val.name,
                "size" : formatBytes(val.size,1),
                "icon" : url,
                "uninstall" : function(e){
                    e.stopPropagation();
                    ws.task("app-uninstall",[val.package],function(data){
                        console.log(data);
                    });
                },
                "download" :function(e){
                    e.stopPropagation();
                    //$.fileDownload(httpURL("app.apk?task=app-download&location=" + val.dir));
                    $.fileDownload(httpURL("app.apk",{
                        "task" : "app-download",
                        "location" : val.dir
                    }));
                },
                "click" : function(e){
                    e.stopPropagation();
                }
            });
            ITEM.module.app.system_container.append($item);

        });

    });


}
function formatBytes(bytes,decimals) {
    if(bytes == 0) return '0 Byte';
    var k = 1024; // or 1024 for binary
    var dm = decimals + 1 || 3;
    var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    var i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}




function search_module(){
    //1.device view item
    //add device AVAILABLE_DEVICE change


    var $device_list = $("body > .platform > .window > .main > .container > .search > .body > .list > .container");
    var $device_view_item = $("body > .platform > .window > .main > .container > .search > .body > .list > .container > .item");

    ITEM.search.device_view_item = $device_view_item.clone();
    ITEM.search.device_view_container = $device_list;

    $device_view_item.remove();

    service.onMsg("_mobile_list_rp",function(data){
        //ITEM.search.device_view_container.empty();
        AVAILABLE_DEVICE = {};
        //console.log(data);

        $.each(data,function(i,val){
            if(!AVAILABLE_DEVICE[val.mac]){
                /*var $item = ITEM.search.device_view_item.clone();
                $item.bindData({
                    "name" : val.name,
                    "data" : val,
                    "mac" : val.mac,
                    "click" : function(e){
                        //stuff login create connection to mobile websocket
                        ws_module(e.data.data);
                    }
                });
                ITEM.search.device_view_container.append($item);*/
                AVAILABLE_DEVICE[val.mac] = val;
            }
        });

        var $items = $("body > .platform > .window > .main > .container > .search > .body > .list > .container > .item");

        $items.each(function(i,item){
            var $item = $(item);
            var mac = $item.attr("mac");


            if(AVAILABLE_DEVICE[mac]){
                AVAILABLE_DEVICE[mac].show = true;
            }else {
                $item.remove();
            }
        });

        //console.log("a",AVAILABLE_DEVICE);

        $.each(AVAILABLE_DEVICE,function(i,val){
            if(!val.show){
                AVAILABLE_DEVICE[val.mac].show = true;
                var $item = ITEM.search.device_view_item.clone();
                $item.bindData({
                    "name" : val.name,
                    "data" : val,
                    "mac" : val.mac,
                    "click" : function(e){
                        //stuff login create connection to mobile websocket
                        ws_module(e.data.data);
                    }
                });
                ITEM.search.device_view_container.append($item);
            }
        });

    });
}




function ws_module(data){
    //1 connection webscoket android phone
    ws_close();

    ws = new Ws("ws://" + data.ip +":8080/main");
    ws.on({
        "connect" : function(){
            //after connect open login module
            CURRENT_DEVICE = data;
            //clearInterval(SEARCH_INTERVAL);
            service.send("_mobile_list_rq_stop");
            open_login_module();

        },
        "disconnect": function(){
            //handle erroer
            CURRENT_DEVICE = undefined;
            SELECTIVE = undefined;
            console.log(data,"some erroe...");
            /*SEARCH_INTERVAL = setInterval(function(){
                service.send("_mobile_list_rq");
            },5000);*/
            service.send("_mobile_list_rq_start");
        }
    });
}

//close current phone connections
function ws_close(){
    if(ws != null){
        ws.disconnect();
        ws = null;
    }
}

function open_login_module(){
    //add active class login module
    var $login = $("body > .platform > .window > .main > .container > .login");
    $login.addClass('active');

    ITEM.login.username.val("");
    ITEM.login.password.val("");
    ITEM.login.username.focus();
}











function module_app_tab_init(){
    var $items = $("body > .platform > .window > .module > .modules > .container > .app > .container > .actions > .container > .action");
    var $views = $("body > .platform > .window > .module > .modules > .container > .app > .container > .body > .container > .view");
    var $status = $("body > .platform > .window > .module > .modules > .container > .app > .container > .status > .text");


    $items.on({
        "click" : function(e){
            $items.removeClass('active');
            $(this).addClass('active');

            var action_id = $(this).attr("action-id");
            var $view = $views.closest(".view[action-id='"+action_id+"']");

            $views.removeClass('active');
            $view.addClass('active');


            var $s = $status.closest(".text[action-id='"+action_id+"']");
            $status.removeClass('active');
            $s.addClass('active');

        }
    });
}


function module_tab_init(){
    var $actions = $("body > .platform > .window > .module > .left-bar > .container > .action");
    var $modules = $("body > .platform > .window > .module > .modules > .container > .module");


    $actions.on({
        "click" : function(e){
            $actions.removeClass('active');
            $(this).addClass('active');

            var action_id = $(this).attr("action-id");
            var $module ;


            if(SELECTIVE[action_id] == true){
                $module = $modules.closest(".module[action-id='"+action_id+"']");
            }else {
                action_id = "selective";
                $module = $modules.closest(".module[action-id='"+action_id+"']");
            }




            $modules.removeClass('active');
            $module.addClass('active');

            ITEM.module.title_bar.title.bindData({
                "module" : action_id.toUpperCase()
            });
        }
    });
}

function module_call_log_tab_init(){
    /*var $actions = $("body > .platform > .window > .module > .modules > .container > .call-log > .container > .actions > .container > .action");
    var $views = $("body > .platform > .window > .module > .modules > .container > .call-log > .container > .body > .container > .view");

    $actions.on({
        "click" : function(){
            $actions.removeClass('active');
            $(this).addClass('active');

            var action_id = $(this).attr("action-id");
            var $view = $views.closest(".view[action-id='"+action_id+"']");

            $views.removeClass('active');
            $view.addClass('active');
        }
    });*/
    var $actions = $("body > .platform > .window > .module > .modules > .container > .call-log > .container > .actions > .container > .action");


    $actions.on({
        "click" : function(){

            var $items = $("body > .platform > .window > .module > .modules > .container > .call-log > .container > .body > .container > .view > .item");

            $actions.removeClass('active');
            $(this).addClass('active');

            var action_id = $(this).attr("action-id");
            $items.removeClass("active");

            var i = $("body > .platform > .window > .module > .modules > .container > .call-log > .container > .body > .container > .view > .item"+ (action_id == "all" ? "" : ("."+action_id)));
            i.addClass('active');

        }
    });

}




//bind-data plugin
(function(){

    var data_type = [
        "bind-data",
        "bind-attr",
        "bind-event",
        "bind-css"
    ];

    $.fn.bindData = function(data) {
        var $this = this;

        $.each(data_type,function(i,type){
            var items = $this.find2("[" + type +"]");


            switch (type){
                case "bind-data" :
                    bind_data(items,data);
                    break;

                case "bind-attr" :
                    bind_attr(items,data);
                    break;
                case "bind-event" :
                    bind_event(items,data);
                    break;
                case  "bind-css" :
                    bind_css(items,data);
                    break;
            }
        });

    };


    function bind_css(items,data){
        $.each(items,function(i,item){
            var $item = $(item);
            var contain = $item.attr("bind-css");

            var main_css = {};
            var css_list = contain.split(";");
            $.each(css_list,function(i,css){
                var c = css.split(":");
                var css_name = c[0];
                var css_value = data[c[1]];

                if(css_value != undefined){
                    main_css[css_name] = css_value;
                }
            });
            $item.css(main_css);
        });
    }

    function bind_data(items,data){
        $.each(items,function(i,item){
            var $item = $(item);
            var var_name = $item.attr("bind-data");
            var value = data[var_name];
            if(value != undefined){
                $item.text(value);
            }
        });
    }


    function bind_attr(items,data){
        $.each(items,function(i,item){
            var $item = $(item);
            var contain = $item.attr("bind-attr");

            var attrs = contain.split(":");

            $.each(attrs,function(i,attr){
                var value = data[attr];
                if(value != undefined){
                    $item.attr(attr,value);
                }
            });
        });
    }

    function bind_event(items,data){
        $.each(items,function(i,item){
            var $item = $(item);
            var contain = $item.attr("bind-event");
            var events = contain.split(";");

            $.each(events,function(i,event){
                var e = event.split(":");

                var event_name = e[0];
                var var_value = data[e[1]];


                if(var_value != undefined){
                    $item.bind(event_name,data,var_value);
                }
            });
        });
    }

    $.fn.find2 = function(selector) {
        return this.filter(selector).add(this.find(selector));
    };
})();