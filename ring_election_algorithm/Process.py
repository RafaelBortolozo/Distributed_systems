import this


class Process(object):
  def __init__(self, id, online, coordinator):
      self.id = id
      self.online = online
      self.coordinator = coordinator

  def getId(self):
    return self.id

  def setId(self, id):
    self.id = id

  def getOnline(self):
    return self.online

  def setOnline(self, online):
    self.online = online
  
  def getCoordinator(self):
    return self.coordinator

  def setCoordinator(self, coordinator):
    self.coordinator = coordinator