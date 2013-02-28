package np;

import java.util.HashMap;
import java.util.Iterator;

public class AssignmentTag
{
	public CoreObject container;
	public String name;
	public int listIndex = -1;
	public HashMap<String, CoreObject> members = null;
	
	/*
	 * The AssignmentTag is a trick I use to avoid keeping track of an object's 
	 * container all the time. When it's time for an assignment operation, the
	 * Interpreter is put into assignmentMode, which causes member lookup operations
	 * to put container info into a HashMap. When it's time to deliver the source
	 * object into the destination, we simply need to do a lookup for the destination
	 * (see LibNP.b_assign() for more details).
	 */
	public AssignmentTag(CoreObject variable, CoreObject cntnr, String variableName)
	{
		container = cntnr;
		name = variableName;
		Interpreter.instance.assignmentList.put(variable, this);
	}
	
	public AssignmentTag(CoreObject variable, CoreList cntnr, int idx)
	{
		container = cntnr;
		listIndex = idx;
		Interpreter.instance.assignmentList.put(variable, this);
	}
	
	public void initMembers()
	{
		members = new HashMap<String, CoreObject>();
	}
	
	public void doAssignment(CoreCall cc, CoreObject source)
	{
		/*
		 * this is important: if the object doesn't exist down the context chain, AssignmentTag
		 * will return our current call context. which we don't want (we want our outer context
		 * in that case)
		 */
		if(container.equals(cc))
			container = cc.outer;
		
		if(listIndex > 0)
		{
			((CoreList) container).set(listIndex, source);
		}
		else
		{
			container.members.put(name, source);
		}
		
		if(members != null)
		{
			Iterator<String> i = members.keySet().iterator();
			while (i.hasNext())
			{
				String key = i.next();
				source.members.put(key, members.get(key));
				Interpreter.instance.debugTrace.append("at put member "+key+"="+members.get(key)+" to="+source.hashCode()+" "+source.getClass().getName()+" \n");
			}
		}
	}
}
