package np;

import java.math.BigInteger;
import java.util.Random;

import np.Interpreter.InterpreterException;

public class LibMath extends CoreObject
{
	private Random randomizr = new Random();

	public LibMath() throws InterpreterException
	{
		putMember("abs", new CoreBuiltin("xAbs", this, "V"), true);
		putMember("acos", new CoreBuiltin("xAcos", this, "V"), true);
		putMember("asin", new CoreBuiltin("xAsin", this, "V"), true);
		putMember("atan", new CoreBuiltin("xAtan", this, "V"), true);
		putMember("atan2", new CoreBuiltin("xAtan2", this), true);
		putMember("cbrt", new CoreBuiltin("xCbrt", this, "V"), true);
		putMember("ceil", new CoreBuiltin("xCeil", this, "V"), true);
		putMember("cos", new CoreBuiltin("xCos", this, "V"), true);
		putMember("cosh", new CoreBuiltin("xCosh", this, "V"), true);
		putMember("deg", new CoreBuiltin("xToDeg", this, "V"), true);
		putMember("exp", new CoreBuiltin("xExp", this, "V"), true);
		putMember("floor", new CoreBuiltin("xFloor", this, "V"), true);
		putMember("exponent", new CoreBuiltin("xExponent", this, "V"), true);
		putMember("hypotenuse", new CoreBuiltin("xHypotenuse", this), true);
		putMember("logn", new CoreBuiltin("xLogn", this, "V"), true);
		putMember("log10", new CoreBuiltin("xLog10", this, "V"), true);
		putMember("max", new CoreBuiltin("xMax", this), true);
		putMember("min", new CoreBuiltin("xMin", this), true);
		putMember("pi", new CoreNumber(Math.PI), true);
		putMember("e", new CoreNumber(Math.E), true);
		putMember("power", new CoreBuiltin("xPower", this), true);
		putMember("round", new CoreBuiltin("xRound", this, "V"), true);
		putMember("signum", new CoreBuiltin("xSignum", this, "V"), true);
		putMember("sin", new CoreBuiltin("xSin", this, "V"), true);
		putMember("sinh", new CoreBuiltin("xSinh", this, "V"), true);
		putMember("sqrt", new CoreBuiltin("xSqrt", this, "V"), true);
		putMember("tan", new CoreBuiltin("xTan", this, "V"), true);
		putMember("tanh", new CoreBuiltin("xTanh", this, "V"), true);
		putMember("rad", new CoreBuiltin("xToRad", this, "V"), true);
		putMember("random", new CoreBuiltin("xRandom", this), true);
		putMember("fib", new CoreBuiltin("xFib", this), true);
		// to do
		//putMember("gcd", new CoreBuiltin("xGcd", this), true);
		//putMember("lcm", new CoreBuiltin("xLcm", this), true);
		
	}
	
	public CoreObject xMin(CoreCall cc) throws InterpreterException
	{
		Double result = cc.argPop().toDouble();
		
		for(int i = 1; i < cc.argCount; i++)
		{
			Double n = cc.argPop().toDouble();
			if(n < result) result = n;
		}
		
		return new CoreNumber(result);
	}

	public CoreObject xMax(CoreCall cc) throws InterpreterException
	{
		Double result = cc.argPop().toDouble();
		
		for(int i = 1; i < cc.argCount; i++)
		{
			Double n = cc.argPop().toDouble();
			if(n > result) result = n;
		}
		
		return new CoreNumber(result);
	}

	public CoreObject xRandom(CoreCall cc) throws InterpreterException
	{
		int fromValue = cc.argPop().toDouble().intValue();
		int toValue = cc.argPop().toDouble().intValue();
		int totalSpan = toValue - fromValue;
		if (totalSpan < 0) totalSpan = -totalSpan;
		int result = randomizr.nextInt(totalSpan+1) + fromValue;
		return new CoreNumber(result);
	}

	/*
	 * fibonacci, for fun
	 * see: http://nayuki.eigenstate.org/page/fast-fibonacci-algorithms
	 */
	
	private static BigInteger fibMultiply(BigInteger x, BigInteger y) {
		return x.multiply(y);  // Replace this line with Karatsuba multiplication, etc. if available
	}
	
	private static BigInteger fibSlow(int n) {
		BigInteger a = BigInteger.ZERO;
		BigInteger b = BigInteger.ONE;
		for (int i = 0; i < n; i++) {
			BigInteger c = a.add(b);
			a = b;
			b = c;
		}
		return a;
	}
	
	private static BigInteger fibFast(int n) {
		BigInteger a = BigInteger.ZERO;
		BigInteger b = BigInteger.ONE;
		int m = 0;
		for (int i = 31; i >= 0; i--) {
			// Loop invariant: a = F(m), b = F(m+1)
			assert a.equals(fibSlow(m));
			assert b.equals(fibSlow(m+1));
			
			// Double it
			BigInteger d = fibMultiply(a, b.shiftLeft(1).subtract(a));
			BigInteger e = fibMultiply(a, a).add(fibMultiply(b, b));
			a = d;
			b = e;
			m *= 2;
			assert a.equals(fibSlow(m));
			assert b.equals(fibSlow(m+1));
			
			// Advance by one conditionally
			if (((1 << i) & n) != 0) {
				BigInteger c = a.add(b);
				a = b;
				b = c;
				m++;
				assert a.equals(fibSlow(m));
				assert b.equals(fibSlow(m+1));
			}
		}
		return a;
	}
	
	public CoreObject xFib(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(fibFast(cc.argPop().toDouble().intValue()).intValue());
	}

	public CoreObject xHypotenuse(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.hypot(cc.argPop().toDouble(), cc.argPop().toDouble()));
	}

	public CoreObject xPower(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.pow(cc.argPop().toDouble(), cc.argPop().toDouble()));
	}

	public CoreObject xAbs(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.abs(cc.argPop().toDouble()));
	}

	public CoreObject xAcos(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.acos(cc.argPop().toDouble()));
	}

	public CoreObject xAsin(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.asin(cc.argPop().toDouble()));
	}

	public CoreObject xAtan(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.atan(cc.argPop().toDouble()));
	}

	public CoreObject xAtan2(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.atan2(cc.argPop().toDouble(), cc.argPop().toDouble()));
	}

	public CoreObject xCbrt(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.cbrt(cc.argPop().toDouble()));
	}

	public CoreObject xCeil(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.ceil(cc.argPop().toDouble()));
	}

	public CoreObject xCos(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.cos(cc.argPop().toDouble()));
	}

	public CoreObject xCosh(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.cosh(cc.argPop().toDouble()));
	}

	public CoreObject xExp(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.exp(cc.argPop().toDouble()));
	}

	public CoreObject xFloor(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.floor(cc.argPop().toDouble()));
	}

	public CoreObject xExponent(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.getExponent(cc.argPop().toDouble()));
	}

	public CoreObject xLogn(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.log(cc.argPop().toDouble()));
	}

	public CoreObject xLog10(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.log10(cc.argPop().toDouble()));
	}

	public CoreObject xToThe(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.pow(cc.argPop().toDouble(), cc.argPop().toDouble()));
	}

	public CoreObject xRound(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(new Double(Math.round(cc.argPop().toDouble())));
	}

	public CoreObject xSignum(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.signum(cc.argPop().toDouble()));
	}

	public CoreObject xSin(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.sin(cc.argPop().toDouble()));
	}

	public CoreObject xSinh(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.sinh(cc.argPop().toDouble()));
	}

	public CoreObject xSqrt(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.sqrt(cc.argPop().toDouble()));
	}

	public CoreObject xTan(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.tan(cc.argPop().toDouble()));
	}

	public CoreObject xTanh(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.tanh(cc.argPop().toDouble()));
	}

	public CoreObject xToDeg(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.toDegrees(cc.argPop().toDouble()));
	}

	public CoreObject xToRad(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(Math.toRadians(cc.argPop().toDouble()));
	}

}
