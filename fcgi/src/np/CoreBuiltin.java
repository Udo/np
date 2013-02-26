package np;

import java.lang.reflect.Method;

import np.Interpreter.InterpreterException;

public class CoreBuiltin extends CoreObject
{

	public CoreBuiltin(Method m)
	{
		value = m;
	}
	
	public static CoreBuiltin lookUp(String identifier) throws InterpreterException
	{
		Method m = LibNP.instance.methods.get(identifier);
		if(m == null)
			return null;
		return new CoreBuiltin(m);
	}
	
	public CoreObject execute(CoreCall cc) throws InterpreterException
	{
		return LibNP.instance.execute((Method) value, cc);
	}
	
	public boolean isExecutable()
	{
		return true;
	}

}
