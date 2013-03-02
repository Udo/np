package np;

import np.Interpreter.InterpreterException;

public class ClastIdentExpr extends ClastNode
{

	public ClastIdentExpr(Token t)
    {
	    super(t);
	    // TODO Auto-generated constructor stub
    }
	
	public CoreObject run(CoreObject objectContext) throws InterpreterException
	{
		if(child == null)
			return new CoreObject();
			
		CoreObject xres = ClastExp.invoke(objectContext, child, child.next);
		
		//Interpreter.instance.debugTrace.append("-- ident expr "+xres.toString()+"\n");
		
		//return ClastIdentifier.iEval(this, xres.toString(), objectContext);
		return xres;
	}

}
