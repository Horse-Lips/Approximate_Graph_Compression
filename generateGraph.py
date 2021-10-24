import sys
import numpy as np

"""
Creates a graph as an adjacency matrix of given size

Usage:
 - python generateGraph.py outFile graphSize
"""
size = int(sys.argv[2])
A = np.zeros((size, size))

for i in range(0, size):
    randRow = np.random.randint(0, 50, (size, ))
    
    A[i, :] = randRow
    A[:, i] = randRow.T

np.fill_diagonal(A, 0)


outFile = open(sys.argv[1], 'w')
outFile.write(str(size))
outFile.write("\n")

for i in range(int(size)):
    for j in range(int(size)):
        outFile.write(str(A[i, j]))
        outFile.write(" ")
        
    outFile.write("\n")

outFile.close()