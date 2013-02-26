package np;

/*
 * CoreString represents the runtime string object
 */
public class CoreString extends CoreObject
{
	public CoreString(String s)
	{
		value = s;
	}

	public String toString()
	{
		return value.toString();
	}
}
