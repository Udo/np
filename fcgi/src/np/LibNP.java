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
		methods.put("print", findMethod("b_print"));
		methods.put("=", findMethod("b_assign"));
		methods.put(":", findMethod("b_named"));
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
	
	public CoreObject b_print(CoreCall cc) throws InterpreterException
	{
		CoreObject ca = cc.argPop();
		for(int i = 0; i < cc.argCount; i++)
		{
			Interpreter.instance.output.append(ca.toString());
			ca = cc.argPop();
		}
		return new CoreObject();
	}

	public CoreObject b_println(CoreCall cc) throws InterpreterException
	{
		CoreObject result = b_print(cc);
		Interpreter.instance.output.append("\n");
		return result;
	}
	
	
}
