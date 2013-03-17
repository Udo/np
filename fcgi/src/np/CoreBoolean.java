package np;

import np.Interpreter.InterpreterException;

public class CoreBoolean extends CoreObject
{
	public CoreBoolean(Boolean s) throws InterpreterException
	{
		value = s;
		members.put("parent", getOuterCore());
	}

	public CoreBoolean()
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
		return("Boolean");
	}
	
	public boolean toBoolean()
	{
		if(value == null) return false;
		return (Boolean) value;
	}
}
