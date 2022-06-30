import Pyro4
import os
import time
from Process import Process

ring = Pyro4.Proxy("PYRONAME:election.ring")
print("ANEL: ", ring)

process = Process(1)
ring.addProcess(process)

while not ring.start_permission():
  print("ola mundo")
