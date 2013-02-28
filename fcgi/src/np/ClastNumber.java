package np;

import np.Interpreter.InterpreterException;

public class ClastNumber extends ClastNode
{

	public ClastNumber(Token t)
    {
	    super(t);
	    // TODO Auto-generated constructor stub
    }
	
	/*
	 * returns a CoreNumber from a ClastNumber
	 * @see np.ClastNode#run()
	 */
	public CoreObject run(CoreObject objectContext) throws InterpreterException
	{
		return new CoreNumber(token.value);
	}
	
}
