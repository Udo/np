package np;

import np.Interpreter.InterpreterException;

public class ClastString extends ClastNode
{

	public ClastString(Token t)
    {
	    super(t);
	    // TODO Auto-generated constructor stub
    }

	/*
	 * returns a CoreString from a ClastString
	 * @see np.ClastNode#run()
	 */
	public CoreObject run(CoreObject objectContext)
	{
		return new CoreString(token.value);
	}
		
}
