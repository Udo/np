package np;

import np.Interpreter.InterpreterException;

public class ClastModule extends ClastNode
{

	public ClastModule(Token t)
    {
	    super(t);
	    // TODO Auto-generated constructor stub
    }
	
	public RTObject evaluate(Interpreter itp, RTObject objectContext, CallContext cc) throws InterpreterException
	{
		itp.debugTrace.append("module start "+this.getClass().getName()+" ("+content+") \n");
		if(child != null)
			itp.evaluateTree(child, objectContext, cc);
		return new RTObject();
	}
	

}
