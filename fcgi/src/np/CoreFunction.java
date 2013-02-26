package np;

import np.Interpreter.InterpreterException;

/*
 * the CoreFunction object is really simple, because it just returns a lambda
 * runtime object. invocation is handled by ClastExp
 */
public class CoreFunction extends CoreObject
{

	public CoreFunction(ClastNode fnode)
	{
		value = fnode;
	}
		
	public boolean isExecutable()
	{
		return true;
	}

	public CoreObject execute(CoreCall cc) throws InterpreterException
	{
		CoreObject result = null;
		
		ClastNode current = ((ClastNode) value).child;
		while (current != null)
		{
			result = current.run(cc);
			current = current.next;
		}
		
		return result;
	}

}
