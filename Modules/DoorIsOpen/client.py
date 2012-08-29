#!/usr/bin/env python

import os
import sys
import time
import socket
import random
import urllib
import httplib
import platform
import RPi.GPIO as GPIO
from hashlib import sha256

SERVER = 'localhost'
SERVER_PORT = 5300
LINE_LENGTH = 65
BUFFER_SIZE = 34

# Enum for packets
INITIALIZATION = "0001"
HEARTBEAT = "00FF"
EMPTY_DATA = "00000000000000000000"
SHUTDOWN = "DEAD"
DOOR_OPEN = "0301"
DOOR_CLOSED = "0302"

class ModuleClient():
    ''' This is the Base Module Template '''

    def __init__(self):
        self.helios_ip = SERVER
        self.helios_port = SERVER_PORT
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.packet = None
        self.initialized = False
        # Always starts at 0000
        self.device_id = "0000"
        # Debug device type
        self.device_type = "000A"
        self.__setup_GPIO__()

    def __setup_GPIO__(self):
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(9, GPIO.IN)
        GPIO.setup(11, GPIO.OUT)
        GPIO.output(11, GPIO.HIGH)


    def start(self):
        ''' Main entry point '''
        self.__connect_to_helios__()
        self.__start__()

    def __connect_to_helios__(self):
        try:
            self.sock.connect((self.helios_ip, self.helios_port))
        except Exception as e:
            sys.stdout.write("Unable to connect to helios at '%s' : '%d'", self.helios_ip, self.helios_port)

    
    def __start__(self):
        ''' Connect to helios and probe for packets '''
        #Wait for helios to register us
        while(self.initialized == False):
            self.__send_init__()
            self.__check_for_response__()
        while(True):
            self.__send_heartbeat__()
            #Read the door's sensor 
            inputValue = GPIO.input(9)
            if inputValue:
                self.__send_door__(DOOR_OPEN)
            else:
                self.__send_door__(DOOR_CLOSED)

        #Code to do the module specific stuff here!
        pass

    def __send_door__(self, status = DOOR_OPEN):
        ''' This will send the door is open command to helios '''
        try:
            self.sock.send(self.device_type + self.device_id + "1" + status + EMPTY_DATA + "\n")
            sys.stdout.write("[*] Sent Heartbeat\n")
            sys.stdout.flush()
            time.sleep(1)
        except KeyboardInterrupt:
            self.send_shutdown()
            sys.stdout.write("\r[!] User exit "+str(LINE_LENGTH * ' ')+'\n')
            sys.stdout.flush()
            os._exit(0)
        except Exception as e:
            print e

    def __send_heartbeat__(self):
        try:
            self.sock.send(self.device_type + self.device_id + "1" + HEARTBEAT + EMPTY_DATA + "\n")
            sys.stdout.write("[*] Sent Heartbeat\n")
            sys.stdout.flush()
            time.sleep(1)
        except KeyboardInterrupt:
            self.send_shutdown()
            sys.stdout.write("\r[!] User exit "+str(LINE_LENGTH * ' ')+'\n')
            sys.stdout.flush()
            os._exit(0)
        except Exception as e:
            print e

    def __check_for_response__(self):
        packet = self.sock.recv(BUFFER_SIZE)
        if (packet[9:13] == "1000"):
            self.device_id = packet[4:8]
            self.initialized = True
            sys.stdout.write("[*] Successfully Initialized Module!\n")

    def __send_init__(self):
        try:
            self.sock.send(self.device_type + self.device_id + "1" + INITIALIZATION + EMPTY_DATA + "\n")
            sys.stdout.write("[*] Sent Initialization packet\n")
            sys.stdout.flush()
            time.sleep(1)
        except Exception as e:
            print e
            sys.stdout.flush()

    def send_shutdown(self, reason = EMPTY_DATA):
        ''' Sends Helios a shutdown packet in hopes to unregister the module '''
        self.sock.send(self.device_type+self.device_id+"9"+SHUTDOWN+reason+"\n")
        time.sleep(1)
        

def help():
    ''' Displays a helpful message '''
    sys.stdout.write("Help comming soon\n")
    sys.stdout.flush()
  
if __name__ == '__main__':
    ''' float main() '''
    client = ModuleClient()
    try:
        if "--help" in sys.argv or "-h" in sys.argv or "/?" in sys.argv:
            help()
        else:
            sys.stdout.write("[*] Starting up Module!\n")
            client.start()
    except KeyboardInterrupt:
        client.send_shutdown()
        sys.stdout.write("\r[!] User exit "+str(LINE_LENGTH * ' ')+'\n')
        sys.stdout.flush()
    os._exit(0)
