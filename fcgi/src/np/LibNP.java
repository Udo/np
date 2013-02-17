package np;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;

import np.Interpreter.InterpreterException;

public class LibNP
{
	
	public HashMap<String, Method> methods = new HashMap<String, Method>();
	public HashMap<String, Method> pmethods = new HashMap<String, Method>();
	
	private Method getMethod(String name)
	{
		try
		{
			return this.getClass().getMethod(name, 
				Interpreter.class, RTObject.class, CallContext.class, ClastNode.class);
		}
		catch (Exception e)
		{
			System.out.println("Runtime exception: "+e.toString());
		}
		return null;
	}
	
	LibNP()
	{
		methods.put("!", getMethod("negate"));
		methods.put("@", getMethod("fnptr"));
		methods.put(".", getMethod("dotscope"));
		methods.put(":", getMethod("namedarg"));
		methods.put("::", getMethod("arrayscope"));
		methods.put("*", getMethod("multiply"));
		methods.put("/", getMethod("divide"));
		methods.put("+", getMethod("add"));
		methods.put("-", getMethod("subtract"));
		methods.put("=", getMethod("assign"));
		methods.put("+=", getMethod("aplus"));
		methods.put("-=", getMethod("aminus"));
		methods.put("==", getMethod("eq"));
		methods.put("!=", getMethod("neq"));
		methods.put("<", getMethod("lt"));
		methods.put("<=", getMethod("lteq"));
		methods.put(">", getMethod("gt"));
		methods.put(">=", getMethod("gteq"));		
		methods.put("++", getMethod("plusplus"));		
		methods.put("--", getMethod("minusminus"));		
		methods.put("true", getMethod("btrue"));		
		methods.put("false", getMethod("bfalse"));		
		methods.put("null", getMethod("bnull"));		
		methods.put("if", getMethod("_if"));
		methods.put("print", getMethod("print"));
		methods.put("println", getMethod("println"));
		methods.put("cat", getMethod("cat"));
		methods.put("fibonacci", getMethod("fibonacci"));
		
		pmethods.put("return", getMethod("_return"));		
		pmethods.put("argcount", getMethod("argcount"));
		pmethods.put("pop", getMethod("pop"));
		pmethods.put("getlocal", getMethod("getlocal"));

		pmethods.put("req_header", getMethod("req_header"));
		pmethods.put("req_config", getMethod("req_config"));
	}
	
	public Method resolveMethod(String name)
	{
		return methods.get(name);
	}


	private RTObject hashMapBridge(HashMap<String, String> hm, Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		if(cc.argCount() == 1)
		{
			RTObject headerName = cc.argPop(itp);
			String headerContentString = hm.get(headerName);
			if(headerContentString == null) 
				return new RTObject();
			else
				return new RTObject(headerContentString);
		}
		else
		{
			HashMap<String, RTObject> nparams = cc.getNamedArgs(itp);
			Iterator<String> i = nparams.keySet().iterator();
			while(i.hasNext())
			{
				String key = i.next();
				hm.put(key, nparams.get(key).value.toString());
			}
			return new RTObject();
		}
	}
	
	// -------------------------- logical operators --------------------------------------
	
	public RTObject bnull(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return new RTObject();
	}
	
	public RTObject btrue(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return new RTObject(true);
	}
	
	public RTObject bfalse(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return new RTObject(false);
	}
	
	public RTObject gteq(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return new RTObject(cc.argPop(itp).toDouble() >= cc.argPop(itp).toDouble());
	}
	
	public RTObject gt(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return new RTObject(cc.argPop(itp).toDouble() > cc.argPop(itp).toDouble());
	}

	public RTObject lteq(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return new RTObject(cc.argPop(itp).toDouble() <= cc.argPop(itp).toDouble());
	}
	
	public RTObject lt(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return new RTObject(cc.argPop(itp).toDouble() < cc.argPop(itp).toDouble());
	}

	public RTObject neq(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		RTObject result = eq(itp, objectContext, cc, node);
		result.value = !((Boolean) result.value);
		return result;
	}

	public RTObject eq(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		Boolean result = false;
		RTObject op1 = cc.argPop(itp);
		RTObject op2 = cc.argPop(itp);

		if(op1.value.getClass() == op2.value.getClass())
        	return new RTObject(op1.value.equals(op2.value));
        if(op1.value.getClass() == String.class)
        	return new RTObject(op2.value.toString().equals(op1.value));
        if(op1.value.getClass() == Double.class)
        	return new RTObject(Double.parseDouble(op2.value.toString()) == (Double) op1.value);
        if(op1.value.getClass() == Boolean.class)
        	return new RTObject(Boolean.parseBoolean(op2.value.toString()) == (Boolean) op1.value);
		
		return new RTObject(result);
	}

	// -------------------------- assignment --------------------------------------

	public RTObject aminus(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		RTObject result = new RTObject(cc.argPop(itp).toDouble() - cc.argPop(itp).toDouble());

		objectContext.add(node.next.content, result);

		return result;
	}

	public RTObject aplus(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		RTObject result = new RTObject(cc.argPop(itp).toDouble() + cc.argPop(itp).toDouble());

		objectContext.add(node.next.content, result);
		
		return result;
	}
	
	public RTObject plusplus(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		RTObject result = new RTObject(cc.argPop(itp).toDouble() + new Double(1));

		objectContext.add(node.next.content, result);
		
		return result;
	}
	
	public RTObject minusminus(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		RTObject result = new RTObject(cc.argPop(itp).toDouble() - new Double(1));

		objectContext.add(node.next.content, result);
		
		return result;
	}
	
	public RTObject assign(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		// skip the first param for now, we'll need it later
		cc.argPopNOP(itp); 
		// evaluate the "right side" first
		RTObject source = cc.argPop(itp);
		
		/*if(target.name != null && !target.name.equals("null"))
			objectContext.add(target.name, source);
		else
			objectContext.add(cc.firstArgNode.content, source);*/

		// todo change the context s
		RTObject result = cc.firstArgNode.evaluate(itp, objectContext, cc);
		
		itp.debugTrace.append("assign "+cc.firstArgNode.content+"="+source.value.getClass().getName()+"\n");
		
		if(result.isExecutable())
			return new RTObject();
		else
			return result;
	}

	// -------------------------- arithmetics --------------------------------------

	private abstract class BatchCalculateable
	{
		abstract Double withTwoOperands(Double op1, Double op2);
	}
	
	private RTObject batchArithmetics(BatchCalculateable calc, Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		Double result = null;
		RTObject item = cc.argPop(itp);
		while(!item.isNull())
		{
			Double part = new Double(0);
			if(item.value.getClass() == Double.class)
				part = (Double) item.value;
			else
				part = Double.parseDouble(item.value.toString());
			if(result == null)
				result = new Double(part);
			else
				result = calc.withTwoOperands(result, part);
			item = cc.argPop(itp);
		}
		if(result == null) result = new Double(0);
		//itp.debugTrace.append("arithmetic result=" + result.toString()+"\n");
		return new RTObject(result);
	}
	
	private class BatchMultiply extends BatchCalculateable {
		@Override
        Double withTwoOperands(Double op1, Double op2)
        {
	        return op1 * op2;
        }
	}

	public RTObject multiply(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return batchArithmetics(new BatchMultiply(), itp, objectContext, cc, node);
	}

	private class BatchSubtract extends BatchCalculateable {
		@Override
        Double withTwoOperands(Double op1, Double op2)
        {
	        return op1 - op2;
        }
	}

	public RTObject subtract(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return batchArithmetics(new BatchSubtract(), itp, objectContext, cc, node);
	}

	private class BatchAdd extends BatchCalculateable {
		@Override
        Double withTwoOperands(Double op1, Double op2)
        {
	        return op1 + op2;
        }
	}

	public RTObject add(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return batchArithmetics(new BatchAdd(), itp, objectContext, cc, node);
	}

	private class BatchDivide extends BatchCalculateable {
		@Override
        Double withTwoOperands(Double op1, Double op2)
        {
	        return op1 / op2;
        }
	}

	public RTObject divide(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return batchArithmetics(new BatchDivide(), itp, objectContext, cc, node);
	}

	// -------------------------- unitary operators --------------------------------------

	public RTObject negate(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return new RTObject(!cc.argPop(itp).toBoolean());
	}
	
	public RTObject fnptr(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return new RTObject();
	}
	
	// -------------------------- builtin basic functions --------------------------------------
	
	public RTObject _if(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		int acnt = cc.argCount();
		for(int a = 0; a < acnt; a++)
		{
			RTObject condition = cc.argPop(itp);
			if(condition.value.getClass() == ClastFunction.class)
				return condition.invoke(itp, objectContext, cc.call(itp, objectContext, null), cc.currentArgNode);
			if(condition.toBoolean() == true)
			{
				RTObject exe = cc.argPop(itp);
				if(exe.value.getClass() == ClastFunction.class)
					return exe.invoke(itp, objectContext, cc.call(itp, objectContext, null), cc.currentArgNode);
				else
					throw new InterpreterException("function expected", cc.currentArgNode.token);
			}
			cc.argPopNOP(itp);
		}		
		return new RTObject();
	}
	
	public RTObject getlocal(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		//itp.debugTrace.append("local: " + objectContext.members.toString() + "\n");
		return new RTObject(objectContext.members.toString());
	}
	
	public RTObject argcount(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		CallContext ncc = (CallContext) objectContext.internal;
		return new RTObject(new Double(ncc.argCount()));
	}	
	
	public RTObject pop(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		CallContext ncc = (CallContext) objectContext.internal;
		return ncc.argPop(itp);
	}
	
	public RTObject _return(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		RTObject result = cc.argPop(itp);
		CallContext ncc = (CallContext) objectContext.internal;
		ncc.returnValue = result;
		return new RTObject();
	}
	
	public RTObject cat(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		HashMap<String, RTObject> nparams = cc.getNamedArgs(itp);
		StringBuilder result = new StringBuilder();
		int argCount = cc.argCount();
		itp.debugTrace.append(argCount+"/"+nparams.toString()+"\n");
		for(int a = 0; a < argCount; a ++)
		{
			RTObject item = cc.argPop(itp);
			result.append(item.toString());
			if(nparams.get("sep") != null)
				if(a < argCount-1) result.append(nparams.get("sep"));
		}
		return new RTObject(result.toString());
	}
	
	public RTObject print(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		itp.output.append(cat(itp, objectContext, cc, node).value.toString());
		return new RTObject();
	}
	
	public RTObject println(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		itp.output.append(cat(itp, objectContext, cc, node).value.toString()+"\n");
		return new RTObject();
	}	

	private Double _fib(Double f)
	{
		if (f < 2)
			return new Double(1);
		else
			return _fib(f-1) + _fib(f-2);
	} 
	
	public RTObject fibonacci(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return new RTObject(_fib(cc.argPop(itp).toDouble()));
	}

	// -------------------------- scope --------------------------------------
	
	public RTObject arrayscope(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		if(cc.argCount() < 2)
			throw new InterpreterException("missing operand", node.token);
			
		RTObject parent = cc.argPop(itp);
		
		if(parent.value.getClass() == RTObjectList.class)
		{
			// list item accessor by name
			String identifier = null;
			if(cc.currentArgNode.token.type.equals("Identifier"))
				identifier = cc.currentArgNode.token.value;
			else
				identifier = cc.argPop(itp).toString();
			
			RTObject result = ((RTObjectList) parent.value).keys.get(identifier);

			if(result == null)
				return new RTObject();
			else
				return result;
		}
		else
		{
			throw new InterpreterException("list expected", cc.currentArgNode.token);
		}
	}

	public RTObject namedarg(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		if(cc.argCount() < 2)
			throw new InterpreterException("missing operand", node.token);
			
		RTObject parent = cc.argPop(itp);
		
		RTObject payload = cc.argPop(itp);
		if(payload == null)
			payload = new RTObject();
			
		payload.name = parent.value.toString();
		return payload;
	}

	public RTObject dotscope(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		if(cc.argCount() < 2)
			throw new InterpreterException("missing operand", node.token);
			
		RTObject parent = cc.argPopCtx(itp, cc.callerContext, true);
		
		String identifier = null;
		if(cc.currentArgNode.token.type.equals("Identifier"))
			identifier = cc.currentArgNode.token.value;

		RTObject result = cc.argPopCtx(itp, parent, true);
		
		// fixme: this won't work because the object is not necessarily just available in this place
		// we'll most likely need a setter function for dotscope and arrayscope which is called by assign somehow
		// or: we'll delete the name/origin hints when the call context / statement finishes?
		// or: assign could drop a hint at pop, down the eval chain <-----
		if(identifier != null)
			result.name = identifier;
		
		return result;
	}

	// -------------------------- special object functions --------------------------------------

	public RTObject req_header(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		if(cc.argCount() == 1)
		{
			RTObject headerName = cc.argPop(itp);
			String headerContentString = itp.response.headers.get(headerName);
			if(headerContentString == null) 
				return new RTObject();
			else
				return new RTObject(headerContentString);
		}
		else
		{
			HashMap<String, RTObject> nparams = cc.getNamedArgs(itp);
			Iterator<String> i = nparams.keySet().iterator();
			while(i.hasNext())
			{
				String key = i.next();
				RTObject o = nparams.get(key);
				if(o.value != null)
					itp.response.headers.put(key.toLowerCase(), key + ": " + o.value.toString());
			}
			return new RTObject();
		}
	}

	public RTObject req_config(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		return hashMapBridge(itp.response.config, itp, objectContext, cc, node);
	}

}
