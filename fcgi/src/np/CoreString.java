package np;

import np.Interpreter.InterpreterException;

/*
 * CoreString represents the runtime string object
 */
public class CoreString extends CoreObject
{
	public CoreString(String s) throws InterpreterException
	{
		value = s;
		outer = getOuterCore();
	}

	public CoreString()
	{	
	}
	
	public CoreObject init()
	{
		CoreObject ir = new CoreObject();
		// todo add core class methods
		return ir;
	}
	
	public String toString()
	{
		return value.toString();
	}
	
	public String getType()
	{
		return("String");
	}
	
}
