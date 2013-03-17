package np;

public class ClastFunction extends ClastNode
{

	public ClastFunction(Token t)
    {
	    super(t);
    }
	
	public CoreObject run(CoreObject objectContext, CoreObject lookupContainer)
	{
		return new CoreFunction(this, objectContext);
	}
	
}
