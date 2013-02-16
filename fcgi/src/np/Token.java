package np;

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
