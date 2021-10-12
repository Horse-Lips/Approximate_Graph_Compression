package graphComponents;


/*Graph class, maintains a list of Vertices (See Vertex class)*/
public class Graph {
	private Vertex[] vertList;
	private int      vertCount;
	
	public Graph(int vertCount) {
		this.vertCount = vertCount;				//Size of Graph as count of Vertices
		this.vertList  = new Vertex[vertCount];	//List of all Vertices in the Graph
		
		for (int i = 0; i < vertCount; i++) {
			vertList[i] = new Vertex(i);
		}
	}
	
	
	/*Returns the Vertex at the given index*/
	public Vertex getVertex(int i) {
		return vertList[i];
	}
	
	
	/*Returns the size of the Graph represented by vertCount*/
	public int size() {
		return this.vertCount;
	}
	
	
	public String toString() {
		String retString = this.vertCount + "\n";
		
		for (int vertIndex = 0; vertIndex < this.vertCount; vertIndex++) {
			String[] currentLine = {"0", "0", "0", "0", "0", "0", "0"};
			Vertex currentVertex = this.getVertex(vertIndex);
			
			for (AdjNode currentAdj: currentVertex.getAdj()) {
				currentLine[currentAdj.getIndex()] = Float.toString(currentAdj.getWeight());
			}
			
			retString += String.join(" ", currentLine) + "\n";
		}
		
		return retString;
	}
}
