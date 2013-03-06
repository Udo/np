package np;

import np.Interpreter.InterpreterException;

/*
 * the CoreFunction object is really simple, because it just returns a lambda
 * runtime object. invocation is handled by ClastExp
 */
public class CoreFunction extends CoreObject
{

	public CoreFunction(ClastNode fnode, CoreObject objectContext)
	{
		value = fnode;
		outer = null;//objectContext;
		putMember("this", this, true);
	}
		
	public boolean isExecutable()
	{
		return true;
	}

	public CoreObject init() throws InterpreterException
	{
		CoreObject ir = new CoreObject();
		// todo add core class methods
		return ir;
	}
	
	public CoreObject execute(CoreCall cc) throws InterpreterException
	{
		CoreObject result = null;
		
		ClastNode current = ((ClastNode) value).child;
		while (current != null && cc.returnValue == null)
		{
			result = current.run(cc);
			current = current.next;
		}
		
		if(cc.returnValue != null)
			result = cc.returnValue;
		
		return result;
	}
	
	public String getType()
	{
		return("Function");
	}
	
	public String toString()
	{
		return "Function";
	}

}
