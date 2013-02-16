package np;

import java.lang.reflect.Method;
import java.util.HashMap;

import np.Interpreter.InterpreterException;

public class RTObject
{
	public String type = "Object";
	public String name = null;
	public java.lang.Object value = null;
	public java.lang.Object internal = null;
	public HashMap<String, RTObject> members = new HashMap<String, RTObject>();
	public RTObject parent = null;
	public int depth = 0;
	
	public RTObject (String s) {
		type = "String";
		value = s;
	}
	public RTObject (Double s) {
		type = "Number";
		value = s;
	}
	public RTObject (ClastFunction s, RTObject prnt) {
		type = "Function";
		parent = prnt;
		depth = prnt.depth+1;
		value = s;
	}
	public RTObject (Boolean s) {
		type = "Boolean";
		value = s;
	}
	public RTObject (Method s, Object o) throws InterpreterException {
		if(s == null) throw new InterpreterException("builtin method assignment is invalid", new Token());
		type = "Builtin";
		value = s;
		internal = o;
	}
	public RTObject (RTObjectList s) {
		type = "List";
		value = s;
	}
	public RTObject () {
		value = new RTNull();
	}
	
	public RTObject resolve(String identifierString)
	{
		RTObject result = members.get(identifierString);
		
		if(result == null && parent != null)
			return parent.resolve(identifierString);
		
		return result;
	}
	
	public RTObject spawn(Interpreter itp, CallContext cc) throws InterpreterException
	{
		RTObject newObject = new RTObject();
		newObject.parent = this;
		newObject.internal = cc;
		newObject.type = "FunctionCall";
		newObject.members.put("pop", new RTObject(itp.builtin.pmethods.get("pop"), itp.builtin));
		newObject.members.put("argcount", new RTObject(itp.builtin.pmethods.get("argcount"), itp.builtin));
		newObject.members.put("return", new RTObject(itp.builtin.pmethods.get("return"), itp.builtin));
		newObject.members.put("getlocal", new RTObject(itp.builtin.pmethods.get("getlocal"), itp.builtin));
		cc.processNamedArgs(itp, newObject);
		newObject.depth = depth+1;
		//itp.debugTrace.append("spawn depth="+newObject.depth+" parent="+this.hashCode()+" new="+newObject.hashCode()+"\n");
		return newObject;
	}
	
	public RTObject invoke(Interpreter itp, RTObject objectContext, CallContext cc, ClastNode node) throws InterpreterException
	{
		if(value.getClass() == Method.class)
		{
			try 
			{ 
				//itp.debugTrace.append("invoke internal "+((Method) value).getName()+" () \n");
				return (RTObject) ((Method) value).invoke(internal, itp, objectContext, cc, node); 
			}
			catch (Exception e) 
			{ 
				Throwable cause = e.getCause();
				if(cause.getClass() == InterpreterException.class)
					throw (InterpreterException) cause;
				throw new InterpreterException("error '"+cause.toString() +
						"' invoking '"+((Method) value).getName()+"'\n" +
						itp.getInternalStackTrace(e), node.token); 
			}
		}
		else if(value.getClass() == ClastFunction.class)
		{
			ClastFunction fn = (ClastFunction) value;
			//itp.debugTrace.append("invoke native "+fn.toString()+"\n");
			return itp.evaluateTree(fn.child, this.spawn(itp, cc), cc);
		}
		else
		{
			throw new InterpreterException("function expected", node.token);
		}
	}

	public Boolean rAdd(String identifierString, RTObject obj)
	{
		RTObject found = members.get(identifierString);

		if(found != null)
		{
			members.put(identifierString, obj);
			return true;
		}
		else if(parent != null)
		{
			return parent.rAdd(identifierString, obj);
		}
		
		return false;
	}
	
	public void add(String identifierString, RTObject obj)
	{
		if(!rAdd(identifierString, obj))
			members.put(identifierString, obj);
	}
	
	public void addLocal(String identifierString, RTObject obj)
	{
		members.put(identifierString, obj);
	}
	
	public Double toDouble()
	{
		if(value.getClass() == Double.class)
			return (Double) value;
		if(value.getClass() == RTNull.class)
			return new Double(0);
		else
			return Double.parseDouble(value.toString());
	}
	
	public Boolean toBoolean()
	{
		if(value.getClass() == Boolean.class)
			return (Boolean) value;
		if(value.getClass() == RTNull.class)
			return false;
		return true;
	}
	
	public Boolean isExecutable()
	{
		return value.getClass() == ClastFunction.class || value.getClass() == Method.class;
	}
	
	public Boolean isNull()
	{
		return value.getClass() == RTNull.class;
	}
	
	public String toString()
	{
		return(value.toString());
	}
}
