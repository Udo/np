package np;

import np.Interpreter.InterpreterException;

public class ClastExp extends ClastNode
{

	public ClastExp(Token t)
    {
	    super(t);
    }
	
	public boolean isNamedVariable()
	{
		return child != null && child.getClass() == ClastIdentifier.class && child.content.equals(":");
	}

	public RTObject evaluate(Interpreter itp, RTObject objectContext, CallContext cc) throws InterpreterException
	{
		if(child == null) return new RTObject();

		RTObject callable = child.evaluate(itp, objectContext, cc);

		//itp.debugTrace.append("exp "+this.getClass().getName()+" "+callable.value.getClass().getName()+"\n");

		if(callable.isExecutable() && child.getClass() != ClastFunction.class)
		{
			return callable.invoke(itp, objectContext, cc.call(itp, objectContext, child.next), this.child);
		}
		if(child.next != null)
		{
			throw new InterpreterException("function identifier expected", child.token);
		}
		return callable;
	}
	

}
