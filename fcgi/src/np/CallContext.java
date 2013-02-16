package np;

import java.util.HashMap;

import np.Interpreter.InterpreterException;

public class CallContext
{
	CallContext parent = null;
	ClastNode firstArgNode = null;
	ClastNode currentArgNode = null;
	//RTObject calleeContext;
	RTObject callerContext;
	RTObject returnValue = null;
	
	public CallContext call(Interpreter itp, RTObject caller, ClastNode firstArg) throws InterpreterException
	{
		CallContext cc = new CallContext();
		cc.parent = this;
		cc.firstArgNode = firstArg;
		cc.currentArgNode = firstArg;
		cc.callerContext = caller;
		return cc;
	}
	
	public CallContext()
	{

	}
	
	public CallContext(ClastNode firstArg)
	{
		firstArgNode = firstArg;
	}
	
	public void processNamedArgs(Interpreter itp, RTObject callee) throws InterpreterException
	{
		ClastNode current = firstArgNode;
		while(current != null)
		{
			if(current.getClass() == ClastExp.class && ((ClastExp) current).isNamedVariable()) 
			{
				RTObject namedArg = current.evaluate(itp, callerContext, this);
				String name = namedArg.name;
				if(namedArg.isExecutable() && current.getClass() != ClastFunction.class)
					namedArg = namedArg.invoke(itp, callerContext, this.call(itp, callerContext, null), current);
				callee.addLocal("_"+name, namedArg);
			}
			current = current.next;
		}
	}

	public HashMap<String, RTObject> getNamedArgs(Interpreter itp) throws InterpreterException
	{
		HashMap<String, RTObject> argList = new HashMap<String, RTObject>();
		ClastNode current = firstArgNode;
		while(current != null)
		{
			if(current.getClass() == ClastExp.class && ((ClastExp) current).isNamedVariable()) 
			{
				RTObject namedArg = current.evaluate(itp, callerContext, this);
				String name = namedArg.name;
				if(namedArg.isExecutable() && current.getClass() != ClastFunction.class)
					namedArg = namedArg.invoke(itp, callerContext, this.call(itp, callerContext, null), current);
				argList.put(name, namedArg);
			}
			current = current.next;
		}
		return argList;
	}

	public void toNextUnnamedArgNode()
	{
		while(currentArgNode != null)
		{
			if(currentArgNode.getClass() == ClastExp.class && ((ClastExp) currentArgNode).isNamedVariable())
				currentArgNode = currentArgNode.next;
			else
				return;
		}
	}
	
	public RTObject argPop(Interpreter itp) throws InterpreterException
	{
		return argPopCtx(itp, callerContext, false);
	}
	
	/* The general form of argPop with options.
	 * Normally, you want args evaluated at pop, and the context for their evaluation is the caller of the function.
	 */
	public RTObject argPopCtx(Interpreter itp, RTObject ctx, boolean noExec) throws InterpreterException
	{
		toNextUnnamedArgNode();
		if(currentArgNode != null)
		{
			RTObject result = currentArgNode.evaluate(itp, ctx, this);
			if(result.isExecutable() && currentArgNode.getClass() != ClastFunction.class && !noExec)
				result = result.invoke(itp, ctx, this.call(itp, ctx, null), currentArgNode);
			//itp.debugTrace.append("arg pop: " + currentArgNode.getClass().getName() + " = " + result.value.toString() + "\n");
			currentArgNode = currentArgNode.next;
			return result;
		}
		else
		{
			//itp.debugTrace.append("arg pop: null\n");
			return new RTObject();
		}
	}

	public RTObject argPopNOP(Interpreter itp, RTObject objectContext) throws InterpreterException
	{
		if(currentArgNode != null)
			currentArgNode = currentArgNode.next;
		return new RTObject();
	}
	
	public int argCount()
	{
		ClastNode ac = firstArgNode;
		int result = 0;
		while(ac != null)
		{
			if(ac.getClass() != ClastExp.class || !((ClastExp) ac).isNamedVariable())
				result++;
			ac = ac.next;
		}
		return result;
	}
}
