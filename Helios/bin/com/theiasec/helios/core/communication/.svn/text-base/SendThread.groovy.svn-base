package com.theiasec.helios.core.communication


abstract class SendThread {
	//Queue premade implementation so we can overload with various custom Objects
	Thread thread
	
	//Adds this to the queue
	abstract void pushPacket(Packet packet) 
	
	//Writes the packet data directly to the outbound stream
	abstract boolean writePacket(Packet packet)
	
	//This should be overloaded with anything we want to pop off the stream
	abstract void popPacket()
	
	
}
