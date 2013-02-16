package np;

import np.Interpreter.InterpreterException;

public class ClastFunction extends ClastNode
{

	public ClastFunction(Token t)
    {
	    super(t);
    }

	public RTObject evaluate(Interpreter itp, RTObject objectContext, CallContext cc) throws InterpreterException
	{
		//itp.debugTrace.append("function declared "+this.getClass().getName()+" (" + content + ") \n");
		
		return new RTObject(this, objectContext);
	}
		
}
