package np;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.apache.commons.lang3.StringEscapeUtils;

import np.Interpreter.InterpreterException;

/*
 * this contains all the built-in behavior, so it is responsible for a huge part of the 
 * actual language implementation (remember that most language constructs are internally
 * represented by function calls as well).
 */
public class LibNP
{
	public HashMap<String, Method> methods = new HashMap<String, Method>();
	private Random randomizr = new Random();
	
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
		// basic
		methods.put("unsafeprint", findMethod("b_unsafeprint"));
		methods.put("print", findMethod("b_print"));
		methods.put("println", findMethod("b_println"));
		methods.put("cat", findMethod("b_cat"));
		//methods.put("local", findMethod("b_local"));
		methods.put("eval", findMethod("b_eval"));
		// misc
		methods.put("random", findMethod("b_random"));
		// logic 
		methods.put("==", findMethod("b_equal"));
		methods.put("!=", findMethod("b_notequal"));
		methods.put(">", findMethod("b_greater"));
		methods.put(">=", findMethod("b_greaterorequal"));
		methods.put("<", findMethod("b_smaller"));
		methods.put("<=", findMethod("b_smallerorequal"));
		methods.put("!", findMethod("b_negate"));
		methods.put("&&", findMethod("b_and"));
		methods.put("||", findMethod("b_or"));
		// flow control
		methods.put("if", findMethod("b_if"));
		methods.put("for", findMethod("b_for"));
		methods.put("while", findMethod("b_while"));
		// assignment
		methods.put("=", findMethod("b_assign")); 
		// scope and accessors
		methods.put(":", findMethod("b_named")); 
		methods.put("::", findMethod("b_arrayscope")); 
		methods.put(".", findMethod("b_objectscope")); 
		// math
		methods.put("+", findMethod("b_plus")); 
		methods.put("-", findMethod("b_minus"));
		methods.put("/", findMethod("b_divide")); 
		methods.put("*", findMethod("b_multiply")); 
		// constructor calls
		methods.put("list", findMethod("b_list"));
		methods.put("map", findMethod("b_map"));
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
	public CoreObject execute(Object o, Method m, CoreCall cc) throws InterpreterException
	{
		try
		{
			return (CoreObject) m.invoke(o, cc);
		}
		catch (Exception e)
		{
			Throwable cause = e.getCause();
			if(cause.getClass() == InterpreterException.class)
				throw (InterpreterException) cause;
			else
				throw new InterpreterException(cause.toString()+"\n"+stackTraceToString(e.getCause()), ((ClastNode) cc.value).token);
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
	
	public CoreObject b_objectscope(CoreCall cc) throws InterpreterException
	{
		CoreObject currentObject = null;
		CoreObject previousObject = cc.argPop();
		for(int i = 1; i < cc.argCount; i++)
		{
			currentObject = cc.argPopCtx(cc.callerContext, previousObject);

			if(Interpreter.instance.assignmentMode)
			{
				AssignmentTag at = Interpreter.instance.assignmentList.get(currentObject);
				/*
				 * if the referenced object doesn't exist, create it since we're going to be assigning a value to it
				 */
				if(previousObject.members.get(at.name) == null)
				{
					previousObject.members.put(at.name, currentObject);
					at = new AssignmentTag(currentObject, previousObject, at.name);
				}
				
				at.initMembers();
				at.members.put("container", previousObject);
				
				//Interpreter.instance.debugTrace.append("dotscope parent="+previousObject.hashCode()+" current="+currentObject.hashCode()+"\n");
			}
			else
			{
				currentObject.members.put("container", previousObject);
			}
			
			previousObject = currentObject;
		}
		return currentObject;
	}

	public CoreObject b_arrayscope(CoreCall cc) throws InterpreterException
	{
		CoreObject name = cc.argPop();
		CoreObject source = cc.argPop();
		if(name.getClass() == CoreList.class)
		{
			CoreList list = (CoreList) name;
			int idx = source.toDouble().intValue();
			CoreObject result = list.item(idx);
			
			Interpreter.instance.debugTrace.append("array scope list="+list.hashCode()+" item#="+
			  idx+" content="+result.toString()+" source="+source.toString()+"\n");
			
			if(Interpreter.instance.assignmentMode)
				new AssignmentTag(result, list, idx);

			return result;
		}
		else if(name.getClass() == CoreMap.class)
		{
			CoreMap list = (CoreMap) name;
			String idx = source.toString();
			CoreObject result = list.item(idx);
			
			//Interpreter.instance.debugTrace.append("array scope map="+list.hashCode()+" item#="+
			//  idx+" content="+result.toString()+" source="+source.toString()+"\n");
			
			if(Interpreter.instance.assignmentMode)
				new AssignmentTag(result, list, idx);

			return result;
		}
		else
		{
			throw new InterpreterException("(array scope) list or map expected", cc.firstArgNode.token);
		}
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
		
		CoreObject source = cc.argPop();

		destinationLocation.doAssignment(cc, source);
		
		Interpreter.instance.debugTrace.append("assign "+destinationLocation.container+" "+destinationLocation.name+"="+source+"\n");
		
		if(source.isExecutable())
			return new CoreObject();
		
		return source;
	}
	
	private String s_cat(CoreCall cc) throws InterpreterException
	{
		CoreObject separateBy = cc.members.get("_sep");
		CoreObject first = cc.members.get("_first");
		CoreObject last = cc.members.get("_last");
		CoreObject pre = cc.members.get("_pre");
		CoreObject post = cc.members.get("_post");

		StringBuilder r = new StringBuilder();
		for(int i = 0; i < cc.argCount; i++)
		{
			CoreObject ca = cc.argPop();

			if(i > 0 && separateBy != null)
				r.append(separateBy.toString());

			if(i == cc.argCount-1 && last != null)
				r.append(last.toString());

			if(pre != null)
				r.append(pre.toString());
			
			r.append(ca.toString());
			
			if(post != null)
				r.append(post.toString());

			if(i == 0 && first != null)
				r.append(first.toString());
		}
		return r.toString();
	}

	
	public CoreObject b_cat(CoreCall cc) throws InterpreterException
	{
		return new CoreString(s_cat(cc));
	}

	public CoreObject b_unsafeprint(CoreCall cc) throws InterpreterException
	{
		String result = s_cat(cc);
		Interpreter.instance.output.append(result);
		return new CoreString(result);
	}

	public CoreObject b_print(CoreCall cc) throws InterpreterException
	{
		String result = StringEscapeUtils.escapeHtml4(s_cat(cc));
		Interpreter.instance.output.append(result);
		return new CoreString(result);
	}

	public CoreObject b_println(CoreCall cc) throws InterpreterException
	{
		String result = StringEscapeUtils.escapeHtml4(s_cat(cc));
		Interpreter.instance.output.append(result+"\n");
		return new CoreString(result);
	}
	
	public CoreObject b_equal(CoreCall cc) throws InterpreterException
	{
		CoreObject opnd1 = cc.argPop();
		CoreObject opnd2 = cc.argPop();
		
		if(opnd1 == opnd2) 
			return new CoreBoolean(true);

		if(opnd1.getClass() == CoreNumber.class)
			return new CoreBoolean(opnd1.toDouble().equals(opnd2.toDouble()));

		return new CoreBoolean(opnd1.toString().equals(opnd2.toString()));
	}
	
	public CoreObject b_notequal(CoreCall cc) throws InterpreterException
	{
		CoreBoolean result = (CoreBoolean) b_equal(cc);
		result.value = ((Boolean) result.value);
		return result;
	}

	private CoreObject b_compare(CoreCall cc, int opType) throws InterpreterException
	{
		boolean result = false;
		
		Double opnd1 = cc.argPop().toDouble();
		Double opnd2 = cc.argPop().toDouble();
		switch (opType)
		{
			case 0: // >
				result = opnd1 > opnd2;
				break;
			case 1: // >=
				result = opnd1 >= opnd2;
				break;
			case 2: // <
				result = opnd1 < opnd2;
				break;
			case 3: // <=
				result = opnd1 <= opnd2;
				break;
		}
		return new CoreBoolean(result);
	}

	public CoreObject b_greater(CoreCall cc) throws InterpreterException { return b_compare(cc, 0); }
	public CoreObject b_greaterorequal(CoreCall cc) throws InterpreterException { return b_compare(cc, 1); }
	public CoreObject b_smaller(CoreCall cc) throws InterpreterException { return b_compare(cc, 2); }
	public CoreObject b_smallerorequal(CoreCall cc) throws InterpreterException { return b_compare(cc, 3); }
	
	public CoreObject b_and(CoreCall cc) throws InterpreterException
	{
		CoreObject opnd1 = cc.argPop();
		CoreObject opnd2 = cc.argPop();
		return new CoreBoolean(opnd1.toBoolean() && opnd2.toBoolean());
	}

	public CoreObject b_or(CoreCall cc) throws InterpreterException
	{
		CoreObject opnd1 = cc.argPop();
		if(opnd1.toBoolean()) return new CoreBoolean(true);
		CoreObject opnd2 = cc.argPop();
		return new CoreBoolean(opnd2.toBoolean());
	}
	
	public CoreObject b_negate(CoreCall cc) throws InterpreterException
	{
		return new CoreBoolean(!cc.argPop().toBoolean());
	}
	
	public CoreObject b_if(CoreCall cc) throws InterpreterException
	{
		Interpreter.instance.debugTrace.append("if "+cc.argCount+" args\n");
		for(int i = 0; i < cc.argCount-1; i += 2)
		{
			Interpreter.instance.debugTrace.append("   stage "+i+"\n");
			if(cc.argPop().toBoolean() == true)
				return cc.argPop().execute(cc.flatCall());
			cc.argNOP();
		}
		if(cc.currentArgNode != null)
			return cc.argPop().execute(cc.flatCall());
		return new CoreObject();
	}

	public CoreObject b_for(CoreCall cc) throws InterpreterException
	{
		CoreObject result = null;
		int startValue = cc.argPop().toDouble().intValue();
		int endValue = cc.argPop().toDouble().intValue();
		CoreObject yieldFunction = cc.argPop();
		for(int i = startValue; i <= endValue; i++)
		{
			ClastCapsule args = new ClastCapsule(new Token(), new CoreNumber(i));
			result = yieldFunction.execute(cc.flatCall(args));
		}
		if(result == null)
			result = new CoreObject();
		return result;
	}

	public CoreObject b_while(CoreCall cc) throws InterpreterException
	{
		CoreObject result = null;
		ClastNode condition = cc.argPopExpr();
		CoreObject yieldFunction = cc.argPop();
		while( condition.run(cc.callerContext, null).toBoolean() == true )
		{
			result = yieldFunction.execute(cc.flatCall());
		}
		if(result == null)
			result = new CoreObject();
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
				CoreObject nmd = co.run(cc.callerContext, null);
				cc.callerContext.members.put(nmd.name, nmd);
			}
			co = co.next;
		}
		
		return new CoreObject();
	}
	
	public CoreObject b_eval(CoreCall cc) throws InterpreterException
	{
		Interpreter outerInstance = Interpreter.instance;
		String src = cc.argPop().toString();
		
		Interpreter i2 = new Interpreter(Interpreter.instance.req);
		Interpreter.instance = i2;
		i2.rootContext.members.put("request", outerInstance.rootContext.members.get("request"));
		if(cc.members.get("_vars") != null)
		{
			CoreObject vars = cc.members.get("_vars");
			if(vars.getClass() != CoreMap.class)
				throw new InterpreterException("map object expected for #vars", cc.firstArgNode.token);
			CoreMap vmap = (CoreMap) vars;
			Iterator<String> ite = vmap.items.keySet().iterator();
			while(ite.hasNext())
			{
				String identf = ite.next();
				i2.rootContext.members.put(identf, vmap.items.get(identf));
			}
		}
		
		CoreObject result = i2.eval(src);
		Interpreter.instance = outerInstance;
		return result;
	}

	public CoreObject b_random(CoreCall cc) throws InterpreterException
	{
		int fromValue = cc.argPop().toDouble().intValue();
		int toValue = cc.argPop().toDouble().intValue();
		int totalSpan = toValue - fromValue;
		if (totalSpan < 0) totalSpan = -totalSpan;
		int result = randomizr.nextInt(totalSpan+1) + fromValue;
		return new CoreNumber(result);
	}


	/*
	 * makes a list
	 */
	public CoreObject b_list(CoreCall cc) throws InterpreterException
	{
		CoreList result = new CoreList();
		
		if(cc.argCount > 0)
		{
			ClastNode co = cc.firstArgNode;
			
			Interpreter.instance.debugTrace.append("new list "+co.token.toString()+"\n");
			
			while (co != null)
			{
				CoreObject nmd = co.run(cc.callerContext, null);
				result.add(nmd);
				co = co.next;
			}
		}
		
		return result;
	}
	
	/*
	 * makes a list
	 */
	public CoreObject b_map(CoreCall cc) throws InterpreterException
	{
		CoreMap result = new CoreMap();
		
		ClastNode co = cc.firstArgNode;
		
		while (co != null)
		{
			CoreObject nmd = co.run(cc.callerContext, null);
			result.add(nmd);
			co = co.next;
		}
		
		return result;
	}
	
}
