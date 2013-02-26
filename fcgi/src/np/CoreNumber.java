package np;

/*
 * CoreNumber implements the runtime number data type
 */
public class CoreNumber extends CoreObject
{
	
	public CoreNumber(Double n)
	{
		value = n;
	}

	public CoreNumber(int i)
	{
		value = new Double(i);
	}
	
	public CoreNumber(String s)
	{
		value = Double.parseDouble(s);
	}
	
	public String toString()
	{
		if(value.getClass() != Double.class)
			return value.toString();
		Double v = (Double) value;
		if(v.intValue() == v.doubleValue())
			return new Integer(v.intValue()).toString();
		else
			return v.toString();
	}
	
	public Double toDouble()
	{
		return (Double) value;
	}
	
}
