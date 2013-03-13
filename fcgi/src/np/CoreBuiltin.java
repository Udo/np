package np;

import java.lang.reflect.Method;
import np.Interpreter.InterpreterException;

public class CoreBuiltin extends CoreObject
{
	Object forObject = null;
	boolean vectorizable = false;
	
	public CoreBuiltin(Method m, Object o)
	{
		value = m;
		forObject = o;
	}
	
	public void initWithMethodName(String methodName, Object o) throws InterpreterException
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
	
	public CoreBuiltin(String methodName, Object o) throws InterpreterException
	{
		initWithMethodName(methodName, o);
	}
	
	public CoreBuiltin(String methodName, Object o, String tags) throws InterpreterException
	{
		if(tags.contains("V")) vectorizable = true;
		initWithMethodName(methodName, o);
	}
	
	public static CoreObject lookUp(String identifier) throws InterpreterException
	{
		Method m = LibRuntime.instance.methods.get(identifier);
		if(m == null)
		{
			CoreObject globalObject = Interpreter.instance.rootContext.members.get(identifier);
			return globalObject;
		}
		return new CoreBuiltin(m, LibRuntime.instance);
	}
	
	public CoreList executeVectorParam(CoreList v) throws InterpreterException
	{
		CoreList result = new CoreList();
		CoreCall cfx = new CoreCall(new CoreObject(), null, null, null);
		
		for(int i = 0; i < v.count(); i++)
		{
			cfx.firstArgNode = new ClastCapsule(new Token(), v.items.get(i));
			cfx.currentArgNode = cfx.firstArgNode;
			result.items.add(execute(cfx));
		}
		
		return result;
	}
	
	public CoreObject execute(CoreCall cc) throws InterpreterException
	{
		if(vectorizable && cc.firstArgNode != null)
		{
			CoreObject firstArg = cc.argPop();
			if(firstArg.getClass() == CoreList.class)
			{
				return executeVectorParam((CoreList) firstArg);
			}
			else
			{
				// if not, restore argument structure
				ClastCapsule cap = new ClastCapsule(cc.firstArgNode.token, firstArg);
				cap.next = cc.firstArgNode.next;
				cc.firstArgNode = cap;
				cc.currentArgNode = cc.firstArgNode;
			}
		}
		return LibRuntime.instance.execute(forObject, (Method) value, cc);
	}
	
	public boolean isExecutable()
	{
		return true;
	}

	public String getType()
	{
		return("Builtin");
	}
	
	public String toString()
	{
		return("Function");
	}

}
