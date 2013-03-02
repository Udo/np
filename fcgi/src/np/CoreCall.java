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
	public int argCount = 0;
	
	public CoreCall(CoreObject callerCtx, CoreObject functionContext, ClastNode args) throws InterpreterException
	{
		if(args == null)
			args = new ClastNode(new Token());
		value = args;
		callerContext = callerCtx;
		outer = functionContext;
		currentArgNode = args;
		firstArgNode = args;
		argCount = preParseArgs();
		skipNamedParam();
		//Interpreter.instance.debugTrace.append("new call context="+this+" outer="+outer+"\n");
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
			if(!crn.isNamed()) 
				result++;
			else
			{
				CoreObject co = crn.run(callerContext);
				members.put("_"+co.name, co);
			}
			crn = crn.next;
		}
		//Interpreter.instance.debugTrace.append("parse args "+members.toString()+"\n");
		return result;
	}
	
	public CoreObject argPopCtx(CoreObject ctx) throws InterpreterException
	{
		if(currentArgNode == null)
			return new CoreObject();
		
		CoreObject result = null;
		//Interpreter.instance.debugTrace.append("ctx pop "+currentArgNode.toString()+"\n");
		result = currentArgNode.run(ctx);

		//Interpreter.instance.debugTrace.append("pop "+currentArgNode.token.value+" arg="+result.toString()+"\n");
		
		currentArgNode = currentArgNode.next;
		skipNamedParam();
		
		return result;
	}
		
	public CoreObject argPop() throws InterpreterException
	{
		return argPopCtx(callerContext);
	}
		
	public String getType()
	{
		return("Call");
	}
	
}
