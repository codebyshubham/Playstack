
//name,url




var Player = function(o){
    o = o || {};
    this.playlist = o.playlist || [];
    this.isLoop = o.loop || true;
    this.index = 0;
    this.id = [];


    //handler
    this._step = o._step || function(){};
    this._stop = o._stop || function(){};
    this._pause = o._pause || function(){};
    this._play = o._play || function(){};





    for(var i = 0 ; i < this.playlist.length; i++){
        var item = this.playlist[i];
        if(item.id){
            this.id.push(item);
        }
    }
};


Player.prototype = {
    play : function(index){
        var self = this;
        var sound;


        if(self.playlist.length < 1)
            return;

        index = typeof index === 'number' ? index : self.index;
        var data =  self.playlist[index];


            sound = data.howl = new Howl({
                src : [data.url],
                html5 : true,
                onplay : function(){
                    self._play(data);
                    sound.stepInterval = setInterval(function(sound){
                        var seek = sound.seek() || 0;
                        var progress = (((seek / sound.duration()) * 100) || 0);
                        self.step(progress);
                    }.bind(self,sound),500);
                },
                onload : function(){
                    self.step(0);
                },
                onend : function(){
                    clearInterval(sound.stepInterval);
                },
                onpause : function(){
                    self._pause(data);
                    clearInterval(sound.stepInterval);
                },
                onstop : function(){
                    self._stop(data);
                    clearInterval(sound.stepInterval);
                }
            });


        if(!sound.playing())
            sound.play();
        self.index = index;
    },

    isPlaying : function(){
        var self = this;
        var song = self.playlist[self.index];

        if(song && song.howl){
            return song.howl.playing();
        }
    },

    pause : function(){
        var self = this;
        var sound = self.playlist[self.index].howl;

        sound.pause();
    },

    stop : function(){
        var self = this;
        var song = self.playlist[self.index];
        self.index = 0;
        if(song && song.howl){
            song.howl.stop();
        }

    },

    next : function(){
        var self = this;
        var index = self.index + 1;
        if(index >= self.playlist.length){
            if(self.isLoop){
                index = 0;
            }else{
                index = -1;
            }
        }
        console.log(index);
        self.pos(index);
    },

    prev : function(){
        var self = this;
        var index = self.index - 1;

        if(index < 0){
            if(self.isLoop){
                index = self.playlist.length - 1;
            }
        }

        self.pos(index);
    },

    pos : function(index){
        var self = this;

        if(index > -1){
            if(self.playlist[self.index].howl){
                self.playlist[self.index].howl.stop();
            }
            self.play(index);
        }
    },

    volume : function(val){
        var self = this;
        var sound = self.playlist[self.index].howl;

        sound.volume(val);
    },


    step : function(per){
        var self = this;
        self._step(per);
    },
    seek : function(per){
        var self = this;
        var sound = self.playlist[self.index].howl;
        if (sound.playing()) {
            sound.seek(sound.duration() * per);
        }
    },

    formatTime: function (secs) {
        var minutes = Math.floor(secs / 60) || 0;
        var seconds = (secs - minutes * 60) || 0;

        return minutes + ':' + (seconds < 10 ? '0' : '') + seconds;
    },

    loop : function(loop){
        var self = this;
        self.isloop = loop;
    },


    add : function(data,force){
        var self = this;
        var idIndex = self.id.indexOf(data.id);

        if(idIndex  < 0){
            self.id.push(data.id);
            self.playlist.push(data);
        }
    },


    clear : function(){
        var self = this;

        self.stop();
        self.index = 0;
        self.playlist = [];
        self.id = [];
    },

    delete : function(index){
        var self = this;
        var sound = self.playlist[self.index].howl;
        var playing = sound.playing();


        if(self.index == index){
            //same song
            self.stop();
            self._remove(index);
            self.index = index;
            if(playing)
                self.play();

        }else if(self.index > index){
            self.pause();
            self._remove(index);
            self.index = self.index - 1;
        }else{
            self._remove(index);
        }
    },
    _remove : function(index){
        var self = this;
        var idIndex = self.id.indexOf(self.playlist[index].id);

        self.id.splice(idIndex,1);
        self.playlist.splice(index, 1);
    }
};