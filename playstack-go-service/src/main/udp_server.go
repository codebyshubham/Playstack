package main

import (
	"net"
	"fmt"
	"strings"
	"encoding/binary"
)

type UDPserver struct {
	address string
	conn *net.UDPConn
}

func (server *UDPserver) start()  {

	myIpList = getMyIpList()
	fmt.Println(myIpList)


	serverAddress,err := net.ResolveUDPAddr("udp",server.address)
	if err != nil {
		dev("UDP",err)
		return
	}

	serverConnection,err := net.ListenUDP("udp",serverAddress)
	if err != nil {
		dev("UDP",err)
		return
	}

	server.conn = serverConnection

	buf := make([]byte,10240)
	for{
		n,add,err := serverConnection.ReadFromUDP(buf)
		if err != nil {
			dev("UDP",err)
			return
		}

		if(!isMyIp(add.IP.String())){
			msg := strings.Split(string(buf[0:n]),"%%")
			switch msg[0] {
			case "PLAYSTACK_SERVER_FIND_REQUEST":
				add.Port = 8080
				Conn,_ := net.DialUDP("udp",serverAddress,add)
				Conn.Write([]byte("PLAYSTACK_SERVER_FIND_RESPONSE"))
				Conn.Close()
			case "PLAYSTACK_SERVER_FIND_RESPONSE":
				if availableMobile != nil && len(msg) == 3{
					mobile := Mobile{
						Ip : add.IP.String(),
						Mac : msg[1],
						Name : msg[2],
					}
					availableMobile[msg[1]] = mobile
				}
			}
		}
	}
}

func (server *UDPserver) stop()  {
	server.conn.Close()
}

func getMyIpList()  []string{
	var list []string

	addrs, err := net.InterfaceAddrs()
	if err != nil{
		return list
	}
	for _, a := range addrs {
		if ipnet, ok := a.(*net.IPNet); ok && !ipnet.IP.IsLoopback() {
			if ipnet.IP.To4() != nil {
				list = append(list, ipnet.IP.String())
			}
		}
	}

	list = append(list, "127.0.0.1")
	return list
}

func isMyIp(ip string)  bool{
	for _,myIp := range myIpList{
		if(myIp == ip){
			return true
		}
	}
	return false
}

func sendUdpPacket(msg string,ip string){
	conn, err := net.Dial("udp", ip)
	if err != nil {
		fmt.Printf("Some error %v", err)
		return
	}
	fmt.Fprintf(conn, msg)
	conn.Close()
}

func brodcastUdpPacket(port string)  {
	interfaces,interfacesErr := net.Interfaces();
	if interfacesErr != nil{
		dev("UDP_SERVER",interfacesErr)
		return
	}

	for _,i := range interfaces{
		if !strings.Contains(i.Flags.String(), "up") || strings.Contains(i.Flags.String(), "loopback"){
			continue
		}
		adds,addsErrs := i.Addrs()
		if addsErrs != nil{
			dev("UDP_SERVER",addsErrs)
		}

		for _,add := range adds{
			myIp,myInet,errCidr := net.ParseCIDR(add.String())
			if errCidr != nil{
				dev("UDP_SERVER",errCidr)
				continue
			}

			if myIp.To4() != nil{

				networkUint32 := binary.BigEndian.Uint32([]byte(myInet.IP.To4()))
				cidrUint32 := binary.BigEndian.Uint32([]byte(myInet.Mask))
				cidrUint32_invert := cidrUint32 ^ 4294967295
				brodcastUint32 := networkUint32 | cidrUint32_invert

				brodcastBytes := make([]byte, 4)
				binary.BigEndian.PutUint32(brodcastBytes, brodcastUint32)

				brodcastIp := net.IPv4(brodcastBytes[0],brodcastBytes[1],brodcastBytes[2],brodcastBytes[3])

				sendUdpPacket("PLAYSTACK_SERVER_FIND_REQUEST",brodcastIp.String() +":" + port)
			}
		}
	}
}

func findMobiles()  {
	myIpList = getMyIpList()

	//store list for checkin new event
	availableMobile = make(map[string]Mobile)
	connectedMobile = make(map[string]Mobile)
	brodcastUdpPacket("8080")
}