package np;

import java.util.HashMap;

/*
 * CoreObject is the basic atomic runtime unit of np. Everything
 * evaluates to some subclass of CoreObject. Its main purpose is
 * to provide an untyped box for variables as well as a container
 * for object properties.
 */
public class CoreObject
{
	/*
	 * members can by any kind of field (both variable or executable)
	 * as long as they are CoreObjects as well
	 */
	public HashMap<String, CoreObject> members = new HashMap<String, CoreObject>();
	/*
	 * the actual storage unit for primitive types is here inside the
	 * value field. in value, Java native objects are stored, such
	 * as strings, doubles, method pointers and so on. when an object
	 * is called, this is the value that gets returned or executed.
	 */
	public Object value = null;
	public String name = null;

	public CoreObject outer = null;
	
	public boolean isExecutable()
	{
		return false;
	}

	public CoreObject getMember(String identifier)
	{
		CoreObject result = members.get(identifier);

		if(Interpreter.instance.assignmentMode && result != null)
			new AssignmentTag(result, this, identifier);

		//if(result != null)
		//	Interpreter.instance.debugTrace.append("object "+this+".getMember("+identifier+") result="+result+"\n");
		
		if(result == null && outer != null)
			return outer.getMember(identifier);
		
		return result;
	}
	
}
