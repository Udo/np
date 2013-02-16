package np;

import np.Interpreter.InterpreterException;

public class ClastString extends ClastNode
{

	public ClastString(Token t)
    {
	    super(t);
	    // TODO Auto-generated constructor stub
    }

	public RTObject evaluate(Interpreter itp, RTObject objectContext, CallContext cc) throws InterpreterException
	{
		//itp.debugTrace.append("string "+this.getClass().getName()+" (" + content + ") \n");
		return new RTObject(content);
	}

}
