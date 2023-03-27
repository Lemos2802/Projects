class Node:

    def __init__(self,name,coord,vel):
        self.name = str(name)
        self.coord = coord
        self.vel = vel
        
    
    def getName(self):
        return self.name

    def setName(self, name):
        self.name = name
    
    def getCoord(self):
        return self.coord

    def setCoord(self,coord):
        self.coord = coord

    def getVel(self):
        return self.vel

    def setVel(self,vel):
        self.vel = vel
    
    def __str__(self):
        return self.name 
    
    def __eq__(self, other):
        
        return self.name == other.name 

    def __hash__(self):
        return hash(self.name)