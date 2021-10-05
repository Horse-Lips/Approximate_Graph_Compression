# Graph Compression for Preserving Shortest Path Distances
## Project Description
In the graph compression literature there are two main approaches for reducing the size of a graph to improve the efficiency and scalability of graph-based optimisation problems:
 - The first method reduces the number of edges in the graph while preserving properties essential to the relevant applications
 - The second approach is vertex sparsification which entails reducing the number of vertices and preserving features only for the vertices that remain in the compressed graph

Arguably, the second approach is much closer to practical applications since many real-world graphs are sparse to begin with.  

Computing the shortest path distance between nodes in a given graph is one of the key algorithmic primitives with numerous applications across different scientific disciplines.    

This project aims to understand the performance of two graph reduction rules for (approximately) preserving shortest path distances among the vertices that remain in the reduced graph.
 - The first is based on eliminating vertices via Gaussian elimination and the {min, +} operation
 - The second involves employing the minimum degree heuristic together with random edge contractions*

The goal is to empirically evaluate the performance of these algorithms, as well as understand the trade-offs between the running time and the quality of the solutions produced by these two algorithms.    


*An edge contraction is an operation that removes an edge from the graph and merges the two vertices that it previously connected