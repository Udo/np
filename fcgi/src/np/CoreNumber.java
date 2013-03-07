package np;

import np.Interpreter.InterpreterException;

/*
 * CoreNumber implements the runtime number data type
 */
public class CoreNumber extends CoreObject
{
	
	public CoreNumber(Double n) throws InterpreterException
	{
		value = n;
		members.put("parent", getOuterCore());
	}

	public CoreNumber(int i) throws InterpreterException
	{
		value = new Double(i);
		members.put("parent", getOuterCore());
	}
	
	public CoreNumber(String s) throws InterpreterException
	{
		value = Double.parseDouble(s);
		members.put("parent", getOuterCore());
	}

	public CoreObject init()
	{
		CoreObject ir = new CoreObject();
		// todo add core class methods
		return ir;
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
	
	public String getType()
	{
		return("Number");
	}
		
}
