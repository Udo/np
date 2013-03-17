package np;

import java.util.Iterator;
import java.util.regex.Pattern;

import np.Interpreter.InterpreterException;

/*
 * CoreString represents the runtime string object
 */
public class CoreString extends CoreObject
{
	public CoreString(String s) throws InterpreterException
	{
		value = s;
		members.put("parent", getOuterCore());
	}

	public CoreString()
	{
	}

	public CoreObject init() throws InterpreterException
	{
		CoreObject ir = new CoreObject();
		ir.putMember("count", new CoreBuiltin("xCount", this), true);
		ir.putMember("rxEach", new CoreBuiltin("xRxEach", this), true);
		ir.putMember("each", new CoreBuiltin("xEach", this), true);
		ir.putMember("endsWith", new CoreBuiltin("xEndsWith", this), true);
		ir.putMember("item", new CoreBuiltin("xItem", this), true);
		ir.putMember("nibble", new CoreBuiltin("xNibble", this), true);
		ir.putMember("pad", new CoreBuiltin("xPad", this), true);
		ir.putMember("part", new CoreBuiltin("xPart", this), true);
		ir.putMember("pos", new CoreBuiltin("xPos", this), true);
		ir.putMember("rxReplace", new CoreBuiltin("xRxReplace", this), true);
		ir.putMember("replace", new CoreBuiltin("xReplace", this), true);
		ir.putMember("rxSplit", new CoreBuiltin("xRxSplit", this), true);
		ir.putMember("split", new CoreBuiltin("xSplit", this), true);
		ir.putMember("startsWith", new CoreBuiltin("xStartsWith", this), true);
		ir.putMember("trim", new CoreBuiltin("xTrim", this), true);
		ir.putMember("upper", new CoreBuiltin("xUpper", this), true);
		ir.putMember("lower", new CoreBuiltin("xLower", this), true);
		return ir;
	}

	public CoreString getCurrentObject(CoreCall cc)
	{
		return (CoreString) cc.members.get("container");
	}

	public CoreObject xCount(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(getCurrentObject(cc).value.toString().length());
	}

	public CoreObject xRxEach(CoreCall cc) throws InterpreterException
	{
		CoreObject yieldFunction = cc.argPop();
		String delimiter = cc.getMemberDefaultString("sep", "");
		String[] parts = getCurrentObject(cc).value.toString().split(delimiter);

		int partCount = parts.length;
		for (int i = 0; i < partCount; i++)
		{
			ClastCapsule args = new ClastCapsule(new Token(), new CoreString(parts[i]));
			args.next = new ClastCapsule(new Token(), new CoreNumber(i));
			yieldFunction.execute(cc.flatCall(args));
		}
		return getCurrentObject(cc);
	}

	public CoreObject xEach(CoreCall cc) throws InterpreterException
	{
		if(cc.members.get("_sep") != null)
			cc.members.put("_sep", new CoreString(
					Pattern.quote(cc.members.get("_sep").toString())));
		return xRxEach(cc);
	}

	public CoreObject xEndsWith(CoreCall cc) throws InterpreterException
	{
		return new CoreBoolean(getCurrentObject(cc).value.toString().endsWith(cc.argPop().toString()));
	}

	public CoreObject xItem(CoreCall cc) throws InterpreterException
	{
		return new CoreString(new Character(getCurrentObject(cc).value.toString().charAt(cc.argPop().toDouble().intValue())).toString());
	}

	public CoreObject xLower(CoreCall cc) throws InterpreterException
	{
		return new CoreString(getCurrentObject(cc).value.toString().toLowerCase());
	}

	public CoreObject xNibble(CoreCall cc) throws InterpreterException
	{
		String result = null;
		String baseString = getCurrentObject(cc).value.toString();

		String lookFor = cc.argPop().toString();
		int nibPos = baseString.indexOf(lookFor);

		if (nibPos == -1)
		{
			result = baseString;
			getCurrentObject(cc).value = "";
		}
		else
		{
			result = baseString.substring(0, nibPos - 1);
			getCurrentObject(cc).value = baseString.substring(nibPos + lookFor.length());
		}

		return new CoreString(result);
	}
	
	public CoreObject xPad(CoreCall cc) throws InterpreterException
	{
		String paddingCharacter = cc.getMemberDefaultString("with", " ");
		boolean padAtTheEnd = cc.getMemberDefaultString("at", "end").equals("end");
		StringBuilder currentString = new StringBuilder();
		currentString.append(getCurrentObject(cc).value.toString());
		int padSize = cc.argPop().toDouble().intValue();
		while(currentString.length() < padSize)
		{
			if(padAtTheEnd)
				currentString.append(paddingCharacter);
			else
				currentString.insert(0, paddingCharacter);
		}
		return new CoreString(currentString.toString());
	}
	
	public CoreObject xPart(CoreCall cc) throws InterpreterException
	{
		int from = cc.argPop().toDouble().intValue();
		int length = cc.argPop().toDouble().intValue();
		int strSize = getCurrentObject(cc).value.toString().length();
		String result = null;
		if (from < 0) // return the right half with -from characters
		{
			from = strSize + from;
			length = strSize - from;
		}
		else if (length < 0)
		{
			length = strSize + length;
		}
		if (from + length > strSize) length = strSize - from;
		result = getCurrentObject(cc).value.toString().substring(from, from + length);
		Interpreter.instance.debugTrace.append("(part) count=" + cc.argCount + "\n");
		return new CoreString(result);
	}

	public CoreObject xPos(CoreCall cc) throws InterpreterException
	{
		return new CoreNumber(getCurrentObject(cc).value.toString().indexOf(cc.argPop().toString()));
	}

	private CoreObject xGeneralReplace(CoreCall cc, boolean escapeRx) throws InterpreterException
	{
		String resultString = getCurrentObject(cc).value.toString();

		Iterator<String> i = cc.members.keySet().iterator();
		while (i.hasNext())
		{
			String replaceFind = i.next();
			String replaceWith = cc.members.get(replaceFind).toString();
			replaceFind = replaceFind.substring(1);
			if(escapeRx) replaceFind = Pattern.quote(replaceFind);
			resultString = resultString.replaceAll(replaceFind, replaceWith);
		}

		if (cc.argCount > 0)
		{
			for (int idx = 0; idx < cc.argCount; idx++)
			{
				CoreObject rObj = cc.argPop();
				if (rObj.getClass() == CoreMap.class)
				{
					CoreMap map = (CoreMap) rObj;
					Iterator<String> mi = map.items.keySet().iterator();
					while (mi.hasNext())
					{
						String replaceFind = mi.next();
						String replaceWith = map.items.get(replaceFind).toString();
						if(escapeRx) replaceFind = Pattern.quote(replaceFind);
						resultString = resultString.replaceAll(replaceFind, replaceWith);
					}
				}
				else
				{
					String replaceFind = rObj.toString();
					resultString = resultString.replaceAll(replaceFind, "");
				}
			}
		}

		return new CoreString(resultString);
	}

	public CoreObject xRxReplace(CoreCall cc) throws InterpreterException
	{
		return xGeneralReplace(cc, false);
	}

	public CoreObject xReplace(CoreCall cc) throws InterpreterException
	{
		return xGeneralReplace(cc, true);
	}

	private CoreObject xGeneralSplit(CoreCall cc, boolean escapeRx) throws InterpreterException
	{
		CoreList result = new CoreList();

		String delimiter = cc.argPop().toString();
		if(escapeRx) delimiter = Pattern.quote(delimiter);
		
		String[] parts = getCurrentObject(cc).value.toString().split(delimiter);
		int partCount = parts.length;

		for (int i = 0; i < partCount; i++)
			result.add(new CoreString(parts[i]));

		return result;
	}

	public CoreObject xRxSplit(CoreCall cc) throws InterpreterException
	{
		return xGeneralSplit(cc, false);
	}
	
	public CoreObject xSplit(CoreCall cc) throws InterpreterException
	{
		return xGeneralSplit(cc, true);
	}
	
	public CoreObject xStartsWith(CoreCall cc) throws InterpreterException
	{
		return new CoreBoolean(getCurrentObject(cc).value.toString().startsWith(cc.argPop().toString()));
	}

	public CoreObject xTrim(CoreCall cc) throws InterpreterException
	{
		return new CoreString(getCurrentObject(cc).value.toString().trim());
	}

	public CoreObject xUpper(CoreCall cc) throws InterpreterException
	{
		return new CoreString(getCurrentObject(cc).value.toString().toUpperCase());
	}

	public String toString()
	{
		return value.toString();
	}

	public boolean toBoolean()
	{
		return !((String) value).isEmpty();
	}

	public String getType()
	{
		return ("String");
	}

}
