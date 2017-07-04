package main

import (
	"fmt"
)
// This receives an int from a channel. The channel is receive-only
func consumer(ch <-chan int) int {
	return <-ch
}

// This sends an int over a channel. The channel is send-only
func producer(i int, ch chan<- int) {
	ch <- i
}

func temp(ch chan int) {
	close(ch)
	ch <- 1
}

func main() {
	ch := make(chan int)
	go temp(ch)


	fmt.Println(<-ch)
	close(ch)

	//go producer(42, ch)
	/*result := consumer(ch)
	fmt.Println("received", result)*/
}