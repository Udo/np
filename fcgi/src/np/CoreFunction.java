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
		members.put("outer", objectContext);
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
		cc.members.put("outer", members.get("outer"));
		
		ClastNode current = ((ClastNode) value).child;
		while (current != null && cc.returnValue == null)
		{
			result = current.run(cc, null);
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
