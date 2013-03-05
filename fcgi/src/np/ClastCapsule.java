package np;

import np.Interpreter.InterpreterException;

public class ClastCapsule extends ClastNode
{
	public CoreObject payload = null;
	
	public ClastCapsule(Token t, CoreObject pl)
    {
	    super(t);
	    payload = pl;
    }
	
	public CoreObject run(CoreObject objectContext) throws InterpreterException
	{
		return payload;
	}
}
