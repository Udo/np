package np;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import np.Interpreter.InterpreterException;

public class CoreMap extends CoreObject {

	public HashMap<String, CoreObject> items = new HashMap<String, CoreObject>();

	public CoreMap() throws InterpreterException
	{
		value = items;
		members.put("parent", getOuterCore());
	}
	
	public CoreMap(Properties p) throws InterpreterException
	{
		value = items;
		members.put("parent", getOuterCore());
		Iterator<Object> i = p.keySet().iterator();
	    while(i.hasNext())
	    {
	      Object ko = i.next();
	      if(ko.getClass() == String.class)
	      {
	        Object so = p.getProperty((String) ko);
	        if(so.getClass() == String.class)
	          items.put((String) ko, new CoreString((String) so));
	      }
	    }
	}

	public CoreObject init() throws InterpreterException
	{
		CoreObject ir = new CoreObject();
		// todo add core class methods
		ir.putMember("item", new CoreBuiltin("xItem", this), true);
		ir.putMember("count", new CoreBuiltin("xCount", this), true);
		ir.putMember("remove", new CoreBuiltin("xRemove", this), true);
		ir.putMember("set", new CoreBuiltin("xSet", this), true);
		ir.putMember("each", new CoreBuiltin("xEach", this), true);
		return ir;
	}
	
	public CoreMap getCurrentObject(CoreCall cc)
	{
		return (CoreMap) cc.members.get("container");
	}
	
	public CoreObject xItem(CoreCall cc) throws InterpreterException { return getCurrentObject(cc).item(cc.argPop().toString()); }	
	public CoreObject xSet(CoreCall cc) throws InterpreterException	{ getCurrentObject(cc).addWithKey(cc.argPop().toString(), cc.argPop()); return getCurrentObject(cc); }
	
	public CoreObject xCount(CoreCall cc) throws InterpreterException	
	{ 
		return new CoreNumber(getCurrentObject(cc).items.size()); 
	}
	
	public CoreObject xRemove(CoreCall cc) throws InterpreterException	
	{ 
		String identifier = cc.argPop().toString();
		CoreObject result = getCurrentObject(cc).items.remove(identifier);
		if(result == null)
			result = new CoreObject();
		return result; 
	}

	public CoreObject xEach(CoreCall cc) throws InterpreterException	
	{ 
		Iterator<String> i = getCurrentObject(cc).items.keySet().iterator();
		int idx = -1;
		CoreObject yieldFunction = cc.argPop();
		while(i.hasNext())
		{
			idx++;
			String identifier = i.next();
			ClastCapsule args = new ClastCapsule(new Token(), new CoreString(identifier));
			args.next = new ClastCapsule(new Token(), getCurrentObject(cc).items.get(identifier));
			args.next.next = new ClastCapsule(new Token(), new CoreNumber(idx));
			yieldFunction.execute(new CoreCall(cc.callerContext, getCurrentObject(cc), args));
		}
		return this;
	}

	public CoreObject item(String key)
	{
		CoreObject result = items.get(key);
		if(result == null)
			result = new CoreObject();
		return result;
	}
	
	public CoreObject add(CoreObject o)
	{
		items.put(o.name, o);
		return o;
	}
	
	public CoreObject addWithKey(String s, CoreObject o)
	{
		items.put(s, o);
		return o;
	}
	
	public String toString()
	{
		return items.toString();
	}
	
}
