package np;

import np.Interpreter.InterpreterException;

/*
 * the standard per-call environment that gets passed into function calls.
 * CoreCall objects inherit from the outer context which is the function
 * object itself, as well as a second context that represents the environment
 * of the caller. as such, CoreCall is a bridge between two environments.
 */
public class CoreCall extends CoreObject
{
	public ClastNode currentArgNode = null;
	public ClastNode firstArgNode = null;
	public CoreObject callerContext = null;
	public CoreObject returnValue = null;
	public int argCount = 0;
	
	public CoreCall(CoreObject callerCtx, CoreObject functionObject, CoreObject containerObject, ClastNode args) throws InterpreterException
	{
		Interpreter.instance.checkForTimeout();
		if(args == null)
			args = new ClastNode(new Token());

		value = args;
		firstArgNode = args;
		argCount = preParseArgs();
		currentArgNode = firstArgNode;
		skipNamedParam();
		
		callerContext = callerCtx;
		if(callerCtx != functionObject && functionObject != null)
		{
			putMember("return", new CoreBuiltin("xReturn", this), true);
			putMember("pop", new CoreBuiltin("xPop", this), true);
			putMember("argcount", new CoreNumber(argCount), true);
		}
		if(functionObject != null)
			putMember("this", functionObject, true);
		if(containerObject != null)
			putMember("container", containerObject, true);
	}
	
	/*
	 * flat calls use the caller context for everything except parameters
	 */
	public CoreCall flatCall() throws InterpreterException
	{
		return flatCall(null);
	}
	
	public CoreCall flatCall(ClastNode args) throws InterpreterException
	{
		if(args == null)
		{
			Interpreter.instance.checkForTimeout();
			return (CoreCall) callerContext;
		}
		CoreCall newCall = new CoreCall(this, null, members.get("container"), args);
		newCall.members = callerContext.members;
		return newCall;
	}
	
	public CoreObject xReturn(CoreCall cc) throws InterpreterException 
	{ 
		returnValue = cc.argPop();
		return returnValue;
	}
	
	public CoreObject xPop(CoreCall cc) throws InterpreterException 
	{ 
		return argPop();
	}
	
	public void skipNamedParam()
	{
		while(currentArgNode != null && currentArgNode.isNamed())
			currentArgNode = currentArgNode.next;
	}

	public String getMemberDefaultString(String memberName, String defaultValue)
	{
		CoreObject m = members.get("_"+memberName);
		//Interpreter.instance.debugTrace.append("get default for "+memberName+" "+m+"\n");
		if(m == null)
			return defaultValue;
		return m.toString();
	}
	
	/*
	 * parse function arguments that have just been passed. here, two things are being done:
	 * first, counting the number of unnamed arguments. second, named arguments are evaluated
	 * and filled into the current object context
	 */
	public int preParseArgs() throws InterpreterException
	{
		int result = 0;
		ClastNode crn = firstArgNode;
		while(crn != null)
		{
			if(crn.token.type.equals("N"))
			{
				// ^ this is a bizarre bug that I need to track down some day
			}
			else if(!crn.isNamed()) 
				result++;
			else
			{
				CoreObject co = crn.run(callerContext, null);
				members.put("_"+co.name, co);
			}
			crn = crn.next;
		}
		if(result == 0 && firstArgNode != null && firstArgNode.token.type.equals("N"))
			firstArgNode = null;
		//Interpreter.instance.debugTrace.append("parse args "+members.toString()+"\n");
		return result;
	}
	
	public ClastNode argPopExpr()
	{
		ClastNode result = currentArgNode;
		
		currentArgNode = currentArgNode.next;
		skipNamedParam();
		
		return result;
	}
	
	public CoreObject argPopCtx(CoreObject ctx, CoreObject lookupCtx) throws InterpreterException
	{
		if(currentArgNode == null)
			return new CoreObject();
		
		CoreObject result = null;
		//Interpreter.instance.debugTrace.append("ctx pop "+currentArgNode.toString()+"\n");
		result = currentArgNode.run(ctx, lookupCtx);

		//Interpreter.instance.debugTrace.append("pop "+currentArgNode.token.value+" arg="+result.toString()+"\n");
		
		currentArgNode = currentArgNode.next;
		skipNamedParam();
		
		return result;
	}
		
	public void argNOP() throws InterpreterException
	{
		if(currentArgNode == null) return;
		
		currentArgNode = currentArgNode.next;
		skipNamedParam();
		
		return;
	}
		
	public CoreObject argPop() throws InterpreterException
	{
		return argPopCtx(callerContext, null);
	}
		
	public String getType()
	{
		return("Call");
	}
	
}
