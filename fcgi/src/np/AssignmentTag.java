package np;

public class AssignmentTag
{
	public CoreObject container;
	public String name;
	
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
}
