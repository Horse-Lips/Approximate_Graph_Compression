package graphUtils;

/*Simple 3-tuple class for use with alias method*/
public class SimpleTuple<X, Y, Z> {
	private X x;
	private Z z;
	private Y y;
	
	public SimpleTuple(X x, Y y, Z z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	/*Return the first item or element 0 of the tuple*/
	public X getFirst() {
		return this.x;
	}
	
	
	/*Return the second item or element 1 of the tuple*/
	public Y getSecond() {
		return this.y;
	}
	
	/*Return the third item or element 2 of the tuple*/
	public Z getThird() {
		return this.z;
	}
}
