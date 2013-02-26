package np;

/*
 * A TreeItem object is nothing else but an encapsulation of a Token object
 * to allow for a tree-like structure.
 */
public class TreeItem {
	Token token;
	TreeItem next;
	TreeItem child;
	
	TreeItem() {
		
	}
	TreeItem(String tType) {
		token = new Token(tType, "");
	}
	TreeItem(Token tk) {
		token = tk;
	}
	
	public String toString()
	{
		return "Function#"+new Integer(hashCode()).toString()+"";
	}
}
