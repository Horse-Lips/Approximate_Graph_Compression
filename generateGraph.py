import sys
import numpy as np

"""
Creates a graph as an adjacency matrix of given size

Usage:
 - python generateGraph.py outFile graphSize
"""
size = int(sys.argv[2])
randomArray = np.random.randint(0, 50, (size, size))

np.fill_diagonal(randomArray, 0)

outFile = open(sys.argv[1], 'w')
outFile.write(str(size))
outFile.write("\n")

for i in range(int(size)):
    for j in range(int(size)):
        outFile.write(str(randomArray[i, j]))
        outFile.write(" ")
        
    outFile.write("\n")

outFile.close()