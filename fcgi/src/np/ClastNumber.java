package np;

import np.Interpreter.InterpreterException;

public class ClastNumber extends ClastNode
{

	public ClastNumber(Token t)
    {
	    super(t);
	    // TODO Auto-generated constructor stub
    }
	
	public RTObject evaluate(Interpreter itp, RTObject objectContext, CallContext cc) throws InterpreterException
	{
		//itp.debugTrace.append("number "+this.getClass().getName()+" (" + content + ") \n");
		return new RTObject(Double.parseDouble(content));
	}

}
