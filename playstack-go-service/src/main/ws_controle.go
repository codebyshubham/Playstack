package main

import (
	"github.com/matishsiao/goInfo"
	"encoding/json"

	"time"
)

const (
	SPLIT = "@@"
	_PC_NAME_RQ = "_pc_name_rq"
	_MOBILE_LIST_RQ_START = "_mobile_list_rq_start"
	_MOBILE_LIST_RQ_STOP = "_mobile_list_rq_stop"
)


var isListStart bool

func wsControle(conn *WsConnection,action string)  {
	var msg string
	switch action {
		case _PC_NAME_RQ:
			msg = _pc_name_rq()
		case _MOBILE_LIST_RQ_START:
			_mobile_list_rq_start(conn)
		case _MOBILE_LIST_RQ_STOP:
			_mobile_list_rq_stop(conn)
	}
	if msg != ""{
		conn.send <- []byte(msg)
	}
}

func _pc_name_rq() string {
	gi := goInfo.GetInfo()
	pc_name := gi.Hostname + "-" +gi.OS
	return "_pc_name_rp" + SPLIT +pc_name
}

func _mobile_list_rq_start(conn *WsConnection){
	if !isListStart{
		isListStart = true
		go ws_findMobile()
	}
}

func _mobile_list_rq_stop(conn *WsConnection){
	isListStart = false
}

func ws_findMobile()  {
	for isListStart{
		go findMobiles();
		time.Sleep(time.Second * 1)
		_mobile_list_rp();
	}
}


func _mobile_list_rp()  {
	data,err := json.Marshal(availableMobile)
	if err != nil{
		dev("WS_CONTROLE",err)
	}else {
		if!currentWs.closeWs{
			currentWs.send <- []byte("_mobile_list_rp" + SPLIT + string(data));
		}
	}
}


