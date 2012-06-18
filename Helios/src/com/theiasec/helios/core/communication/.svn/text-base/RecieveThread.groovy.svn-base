package com.theiasec.helios.core.communication


abstract class RecieveThread {
	//Queue premade implementation so we can overload with various custom Objects
	Thread thread = new Thread()
	def queue = []
	
	//Pulls the packet data directly from the stream and converts it into a packet object
	abstract Packet readPacket() 
	
	//This is a more immediate way of grabbing a packet from the queue
	//Overload this for any criteria we might need
	abstract Packet pullPacket() 
}
