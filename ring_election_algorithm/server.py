import Pyro4
import io
import random
from time import sleep
from Process import *
from itertools import cycle



@Pyro4.expose
class Election_ring(object):
  def __init__(self):
    self.ring = []

  def addProcess(self, process):
    self.ring.append(process)

  def listToCycle(self, list):
    return cycle(list)

  def getAllProccesses(self):
    self.ring = []

daemon = Pyro4.Daemon()    # make a Pyro daemon
ns = Pyro4.locateNS()      # find the name server
uri = daemon.register(Election_ring)# register the greeting maker as a Pyro object
ns.register("election.ring", uri)   # register the object with a name in the name server

print("client conected.")
daemon.requestLoop()       # start the event loop of the server to wait for calls