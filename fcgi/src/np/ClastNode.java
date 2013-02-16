package np;

import np.Interpreter.InterpreterException;

public class ClastNode
{
	public ClastNode next = null;
	public ClastNode child = null;
	public Token token;
	public String content;
	
	public RTObject evaluate(Interpreter itp, RTObject objectContext, CallContext cc) throws InterpreterException
	{
		//itp.debugTrace.append("eval "+this.getClass().getName()+" (" + cc.argCount() + ") \n");
		return new RTObject();
	}
	
	public ClastNode(Token t)
	{
		token = t;
		content = t.value;
	}
}
