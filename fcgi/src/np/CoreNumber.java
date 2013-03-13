package np;

import java.util.Iterator;

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

	public CoreObject init() throws InterpreterException
	{
		CoreObject ir = new CoreObject();
		ir.putMember("ceil", new CoreBuiltin("xCeil", this), true);
		ir.putMember("downTo", new CoreBuiltin("xDownTo", this), true);
		ir.putMember("even", new CoreBuiltin("xEven", this), true);
		ir.putMember("floor", new CoreBuiltin("xFloor", this), true);
		ir.putMember("mod", new CoreBuiltin("xMod", this), true);
		ir.putMember("round", new CoreBuiltin("xRound", this), true);
		ir.putMember("signum", new CoreBuiltin("xSignum", this), true);
		ir.putMember("times", new CoreBuiltin("xTimes", this), true);
		ir.putMember("upTo", new CoreBuiltin("xUpTo", this), true);
		return ir;
	}

	public CoreObject xDownTo(CoreCall cc) throws InterpreterException	
	{ 
		int thisVal = getCurrentObject(cc).toDouble().intValue();
		int endVal = cc.argPop().toDouble().intValue();
		CoreObject yieldFunction = cc.argPop();
		for(int idx = thisVal; idx >= endVal; idx--)
		{
			ClastCapsule args = new ClastCapsule(new Token(), new CoreNumber(idx));
			yieldFunction.execute(new CoreCall(cc.callerContext, yieldFunction, getCurrentObject(cc), args));
		}
		return getCurrentObject(cc);
	}

	public CoreObject xUpTo(CoreCall cc) throws InterpreterException	
	{ 
		int thisVal = getCurrentObject(cc).toDouble().intValue();
		int endVal = cc.argPop().toDouble().intValue();
		CoreObject yieldFunction = cc.argPop();
		for(int idx = thisVal; idx <= endVal; idx++)
		{
			ClastCapsule args = new ClastCapsule(new Token(), new CoreNumber(idx));
			yieldFunction.execute(new CoreCall(cc.callerContext, yieldFunction, getCurrentObject(cc), args));
		}
		return getCurrentObject(cc);
	}

	public CoreObject xTimes(CoreCall cc) throws InterpreterException	
	{ 
		CoreObject yieldFunction = cc.argPop();
		int max = getCurrentObject(cc).toDouble().intValue();
		for(int idx = 1; idx <= max; idx++)
		{
			ClastCapsule args = new ClastCapsule(new Token(), new CoreNumber(idx));
			yieldFunction.execute(new CoreCall(cc.callerContext, yieldFunction, getCurrentObject(cc), args));
		}
		return getCurrentObject(cc);
	}

	public CoreObject xEven(CoreCall cc) throws InterpreterException
	{
		return new CoreBoolean(getCurrentObject(cc).toDouble() % 2 == 0);
	}

	public CoreObject xMod(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(getCurrentObject(cc).toDouble() % cc.argPop().toDouble());
	}

	public CoreObject xCeil(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.ceil(getCurrentObject(cc).toDouble()));
	}

	public CoreObject xSignum(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.signum(getCurrentObject(cc).toDouble()));
	}

	public CoreObject xFloor(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.floor(getCurrentObject(cc).toDouble()));
	}

	public CoreObject xRound(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.round(getCurrentObject(cc).toDouble().floatValue()));
	}

	public CoreNumber getCurrentObject(CoreCall cc)
	{
		return (CoreNumber) cc.members.get("container");
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
	
	public boolean toBoolean()
	{
		return (Double) value != 0;
	}
	
	public String getType()
	{
		return("Number");
	}
		
}
