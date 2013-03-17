package np;

/*
 * The Token object first enters into life inside the Lexer, then gets ordered
 * into a tree structure by the Parser, then it's still passed around for tracing
 * and debugging purposes inside the Interpreter itself. Besides its own category
 * and pay load, a Token also keeps track of where exactly in the source code it
 * originated, allowing for more informative error messages.
 */
public class Token {
	public String value = "";
	public String type = "N";
	public SrcFilePosition pos = new SrcFilePosition();

	public class SrcFilePosition {
		public int line = -1;
		public int col = -1;
	}

	Token() {
		
	}
	
	Token(String tp, String vl) {
		type = tp;
		value = vl;
	}
	
	public Boolean isEndToken()
	{
		return type.equals("StEnd") || type.equals("ParenEnd") || type.equals("FnEnd") || type.equals("ListEnd");
	}
	
	public String toString() {
		return type+"("+value+") :"+pos.line+":"+pos.col;
	}
	
}
