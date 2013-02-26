package np;

import java.lang.reflect.Method;
import java.util.HashMap;
import np.Interpreter.InterpreterException;

/*
 * this contains all the built-in behavior, so it is responsible for a huge part of the 
 * actual language implementation (remember that most language constructs are internally
 * represented by function calls as well).
 */
public class LibNP
{
	public HashMap<String, Method> methods = new HashMap<String, Method>();
	
	public static LibNP instance;
	
	/*
	 * looks up a built-in method by name and returns null if none was found.
	 */
	public Method findMethod(String name) throws InterpreterException
	{
		try
		{
			return this.getClass().getMethod(name, CoreCall.class);
		}
		catch (Exception e)
		{
			throw new InterpreterException("error initializing libNP: "+e.toString(), new Token());
		}
	}
	
	/*
	 * in this constructor we'll declare all the built-in functions. they have to be aliased through
	 * the methods HashMap because a lot of times, we're using function names that would be illegal
	 * in Java
	 */
	public LibNP() throws InterpreterException
	{
		methods.put("println", findMethod("b_println"));
		methods.put("println", findMethod("b_println"));
		methods.put("cat", findMethod("b_cat"));
		methods.put("local", findMethod("b_local"));
		methods.put("=", findMethod("b_assign"));
		methods.put(":", findMethod("b_named"));
		methods.put("+", findMethod("b_plus"));
		methods.put("-", findMethod("b_minus"));
		methods.put("/", findMethod("b_divide"));
		methods.put("*", findMethod("b_multiply"));
		//methods.put("list", findMethod("b_list"));
		instance = this;
	}
	
	public String stackTraceToString(Throwable e) {
	    StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    return sb.toString();
	}

	/*
	 * general execution wrapper for all built-in functions
	 */
	public CoreObject execute(Method m, CoreCall cc) throws InterpreterException
	{
		try
		{
			return (CoreObject) m.invoke(this, cc);
		}
		catch (Exception e)
		{
			throw new InterpreterException(e.getCause().toString()+"\n"+stackTraceToString(e.getCause()), ((ClastNode) cc.value).token);
		}
	}

	/*
	 * built-in operation to give objects a name, takes two runtime arguments: name and source
	 */
	public CoreObject b_named(CoreCall cc) throws InterpreterException
	{
		CoreObject name = cc.argPop();
		CoreObject source = cc.argPop();
		source.name = name.toString();
		return source;
	}

	/*
	 * built-in assignment operation, takes two runtime arguments: destination and source
	 */
	public CoreObject b_assign(CoreCall cc) throws InterpreterException
	{
		/*
		 * first, switch on assignmentMode to track where this assignment will be going
		 */
		Interpreter.instance.beginAssignmentMode();
		CoreObject destination = cc.argPop();
		AssignmentTag destinationLocation = Interpreter.instance.endAssignmentMode(destination);
		/*
		 * this is important: if the object doesn't exist down the context chain, AssignmentTag
		 * will return our current call context. which we don't want (we want our outer context
		 * in that case)
		 */
		if(destinationLocation.container.equals(cc))
			destinationLocation.container = cc.outer;
		
		/*
		 * now, execute the second argument in order to get the source for the assignment
		 */
		CoreObject source = cc.argPop();
		destinationLocation.container.members.put(destinationLocation.name, source);
		
		Interpreter.instance.debugTrace.append("assign "+destinationLocation.container+" "+destinationLocation.name+"="+source+"\n");
		
		if(source.isExecutable())
			return new CoreObject();
		
		return source;
	}
	
	public CoreObject b_cat(CoreCall cc) throws InterpreterException
	{
		CoreObject separateBy = cc.members.get("_sep");
		StringBuilder r = new StringBuilder();
		for(int i = 0; i < cc.argCount; i++)
		{
			CoreObject ca = cc.argPop();
			if(i > 0 && separateBy != null)
				r.append(separateBy.toString());
			r.append(ca.toString());
		}
		return new CoreString(r.toString());
	}

	public CoreObject b_print(CoreCall cc) throws InterpreterException
	{
		Interpreter.instance.output.append(b_cat(cc).toString());
		return new CoreObject();
	}

	public CoreObject b_println(CoreCall cc) throws InterpreterException
	{
		CoreObject result = b_print(cc);
		Interpreter.instance.output.append("\n");
		return result;
	}
	
	/*
	 * adds all of its operands and returns the result
	 */
	public CoreObject b_plus(CoreCall cc) throws InterpreterException
	{
		Double result = new Double(0);
		
		for(int i = 0; i < cc.argCount; i++)
		{
			Double opnd = cc.argPop().toDouble();
			result += opnd;
		}
		
		return new CoreNumber(result);
	}

	/*
	 * subtracts all of its operands and returns the result
	 */
	public CoreObject b_minus(CoreCall cc) throws InterpreterException
	{
		Double result = cc.argPop().toDouble();
		
		for(int i = 1; i < cc.argCount; i++)
		{
			Double opnd = cc.argPop().toDouble();
			result -= opnd;
		}
		
		return new CoreNumber(result);
	}
	
	/*
	 * divides all of its operands and returns the result
	 */
	public CoreObject b_divide(CoreCall cc) throws InterpreterException
	{
		Double result = cc.argPop().toDouble();
		
		for(int i = 1; i < cc.argCount; i++)
		{
			Double opnd = cc.argPop().toDouble();
			result /= opnd;
		}
		
		return new CoreNumber(result);
	}

	/*
	 * multiplies all of its operands and returns the result
	 */
	public CoreObject b_multiply(CoreCall cc) throws InterpreterException
	{
		Double result = cc.argPop().toDouble();
		
		for(int i = 1; i < cc.argCount; i++)
		{
			Double opnd = cc.argPop().toDouble();
			result *= opnd;
		}
		
		return new CoreNumber(result);
	}

	/*
	 * creates variables within the local context, overshadowing potentially identical
	 * variable names in the outer context for the duration of the call
	 */
	public CoreObject b_local(CoreCall cc) throws InterpreterException
	{
		ClastNode co = cc.firstArgNode;
		
		while (co != null)
		{
			if(co.token.type.equals("Identifier") || co.token.type.equals("String"))
				cc.callerContext.members.put(co.token.value, new CoreObject());
			else
			{
				CoreObject nmd = co.run(cc.callerContext);
				cc.callerContext.members.put(nmd.name, nmd);
			}
			co = co.next;
		}
		
		return new CoreObject();
	}

}
