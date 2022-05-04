import sys
import Pyro4
import os
import subprocess

os.system("pyro4-ns.bat")

@Pyro4.expose
class Quiz(object):
    def helloWorld(self):
        return "Hello World!"

daemon = Pyro4.Daemon()       # make a Pyro daemon
ns = Pyro4.locateNS()         # find the name server
uri = daemon.register(Quiz)   # register the greeting maker as a Pyro object
ns.register("quiz", uri)   # register the object with a name in the name server

print("Ready.")
daemon.requestLoop()                   # start the event loop of the server to wait for calls