// Copyright 2015 Daniel Theophanes.
// Use of this source code is governed by a zlib-style
// license that can be found in the LICENSE file.

// simple does nothing except block while running the service.
package main

import (
	"log"
	"os"
	"github.com/kardianos/service"
)

type program struct{}


var udp UDPserver
var ws WSserver

func (p *program) Start(s service.Service) error {

	//start UDP TCP server
	udp = UDPserver{
		address: CONST_IP_ADDRESS,
	}

	ws = WSserver{
		address:CONST_IP_ADDRESS,
	}

	go udp.start()
	go ws.start()

	go p.run()
	return nil
}
func (p *program) run() {

}
func (p *program) Stop(s service.Service) error {

	udp.stop()
	return nil
}

func main() {
	svcConfig := &service.Config{
		Name:        "Shubham",
		DisplayName: "Playstack - The pc suite",
		Description: "android application",
	}

	prg := &program{}
	s, err := service.New(prg, svcConfig)
	if err != nil {
		log.Fatal(err)
	}
	if len(os.Args) > 1 {
		switch os.Args[1] {
			case "install":
				service.Control(s,"install")
				service.Control(s,"start")
				return
			case "uninstall":
				service.Control(s,"stop")
				service.Control(s,"uninstall")
			return
		}
		return
	}
	err = s.Run()
}
