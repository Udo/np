package np;

import np.Interpreter.InterpreterException;

/*
 * the expression node invokes functions, both built-in and in native code,
 * here the first parameter is always the function name and the following ones
 * (optional) represent the arguments to that function call. as always, function
 * and variable names are resolved from inner to outer environments.
 */
public class ClastExp extends ClastNode
{

	public ClastExp(Token t)
    {
	    super(t);
    }
	
	public static CoreObject invoke(CoreObject objectContext, ClastNode method, ClastNode args) throws InterpreterException
	{
		CoreObject methodObject = method.run(objectContext);

		if(methodObject == null)
			return new CoreObject();
		
		if(methodObject.getClass() == CoreFunction.class)
			return ((CoreFunction) methodObject).execute(new CoreCall(objectContext, methodObject, args));
		
		if(methodObject.getClass() == CoreBuiltin.class)
			return ((CoreBuiltin) methodObject).execute(new CoreCall(objectContext, methodObject, args));

		return methodObject;
		
		//throw new InterpreterException("function identifier expected ('"+method.token.toString()+"' found)", token);
	}
	
	public CoreObject run(CoreObject objectContext) throws InterpreterException
	{
		if(child == null)
			return new CoreObject();
			
		return invoke(objectContext, child, child.next);
	}
	
}
