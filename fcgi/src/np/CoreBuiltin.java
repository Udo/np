package np;

import java.lang.reflect.Method;

import np.Interpreter.InterpreterException;

public class CoreBuiltin extends CoreObject
{
	Object forObject = null;
	
	public CoreBuiltin(Method m, Object o)
	{
		value = m;
		forObject = o;
	}
	
	public CoreBuiltin(String methodName, Object o) throws InterpreterException
	{
		try
		{
			value = o.getClass().getMethod(methodName, CoreCall.class);
		}
		catch (Exception e)
		{
			throw new InterpreterException("error initializing builtin: "+e.toString(), new Token());
		}
		forObject = o;
	}
	
	public static CoreBuiltin lookUp(String identifier) throws InterpreterException
	{
		Method m = LibNP.instance.methods.get(identifier);
		if(m == null)
			return null;
		return new CoreBuiltin(m, LibNP.instance);
	}
	
	public CoreObject execute(CoreCall cc) throws InterpreterException
	{
		return LibNP.instance.execute(forObject, (Method) value, cc);
	}
	
	public boolean isExecutable()
	{
		return true;
	}

	public String getType()
	{
		return("Builtin");
	}
	

}
