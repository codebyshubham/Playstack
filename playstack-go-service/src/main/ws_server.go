package main

import (
	"net/http"
	"github.com/gorilla/websocket"
	"fmt"
)



/*for main server object*/
type WSserver struct {
	address string
}

func (server *WSserver) start(){
	http.HandleFunc("/",serveHome)
	http.HandleFunc("/ws",serveWs)
	err := http.ListenAndServe(server.address, nil)
	if err != nil {
		dev("ws:",err)
	}
}

func serveHome(w http.ResponseWriter, r *http.Request)  {
	/*u,_ := user.Current()
	fmt.Println(u.Uid)
	fmt.Fprint(w,u.Username +":" +u.Uid)*/
	fmt.Println("uri:" + r.URL.String())
	findMobiles()
}





/* for hadle websocket connection*/
type WsConnection struct {
	ws *websocket.Conn
	send chan []byte
	closeWs bool
}

var upgrader = websocket.Upgrader{
	ReadBufferSize: 1024,
	WriteBufferSize: 1024,
	CheckOrigin: checkOrigin,
}

func checkOrigin(r *http.Request)  bool{
	return true
}

func (conn *WsConnection) write(mt int, payload []byte) error {
	return conn.ws.WriteMessage(mt, payload)
}
func (conn *WsConnection) close(){
	close(conn.send)
	conn.closeWs = true
	conn.ws.Close()
}

func writer(conn *WsConnection)  {
	for  {
		msg, ok := <- conn.send
		if !ok{
			conn.write(websocket.CloseMessage, []byte{})
			return
		}
		if err := conn.write(websocket.TextMessage, msg); err != nil {
			return
		}
	}

}



func reader(conn *WsConnection)  {
	defer conn.close()

	for{
		_,msg,err := conn.ws.ReadMessage()
		if err != nil{
			if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway) {
				dev("ws",err)
			}
			break
		}
		wsControle(conn,string(msg))
	}

}



func serveWs(w http.ResponseWriter, r *http.Request)  {
	fmt.Println("ws",r.URL.String())

	ws, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		dev("ws",err)
		return
	}

	currentWs = &WsConnection{
		send:make(chan []byte),
		ws : ws,
		closeWs:false,
	}

	go writer(currentWs)
	reader(currentWs)
}