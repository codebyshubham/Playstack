package main

import (
	"fmt"
)

var (
	SERVER_PORT = 8080
	CONST_IP_ADDRESS = ":8080"
	myIpList []string
	availableMobile map[string]Mobile
	connectedMobile map[string]Mobile


	currentWs *WsConnection
)

func dev(tag string,msg error)  {
	fmt.Println(tag,":",msg)
}


type Mobile struct {
	Ip string `json:"ip"`
	Mac string `json:"mac"`
	Name string `json:"name"`
}


