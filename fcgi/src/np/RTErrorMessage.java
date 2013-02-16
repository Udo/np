package np;

public class RTErrorMessage
{
	public Token token;
	public String message = "";
	public String type = "";
	
	RTErrorMessage(String msg, Token tk)
	{
		token = tk;
		message = msg;			
	}
	
	public String toString()
	{
		String posInfo = "";
		if(token != null && token.pos != null)
			posInfo = " at line " + token.pos.line + " char " + token.pos.col;
		return message + posInfo;
	}
}
