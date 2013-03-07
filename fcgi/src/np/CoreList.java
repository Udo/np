package np;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import np.Interpreter.InterpreterException;

public class CoreList extends CoreObject
{
	
	public ArrayList<CoreObject> items = new ArrayList<CoreObject>();

	public CoreList() throws InterpreterException
	{
		value = items;
		members.put("parent", getOuterCore());
	}

	public CoreObject init() throws InterpreterException
	{
		CoreObject ir = new CoreObject();
		// todo add core class methods
		ir.putMember("item", new CoreBuiltin("xItem", this), true);
		ir.putMember("add", new CoreBuiltin("xAdd", this), true);
		ir.putMember("count", new CoreBuiltin("xCount", this), true);
		ir.putMember("popFirst", new CoreBuiltin("xPopFirst", this), true);
		ir.putMember("popLast", new CoreBuiltin("xPopLast", this), true);
		ir.putMember("remove", new CoreBuiltin("xRemove", this), true);
		ir.putMember("removeObject", new CoreBuiltin("xRemoveObject", this), true);
		ir.putMember("set", new CoreBuiltin("xSet", this), true);
		ir.putMember("insert", new CoreBuiltin("xInsert", this), true);
		ir.putMember("sort", new CoreBuiltin("xSort", this), true);
		ir.putMember("each", new CoreBuiltin("xEach", this), true);
		return ir;
	}
	
	public CoreList getCurrentObject(CoreCall cc)
	{
		return (CoreList) cc.members.get("container");
	}
	
	public CoreObject xItem(CoreCall cc) throws InterpreterException { return getCurrentObject(cc).item(cc.argPop().toDouble().intValue()); }
	
	public CoreObject xAdd(CoreCall cc) throws InterpreterException	{ return getCurrentObject(cc).add(cc.argPop()); }
	
	public CoreObject xCount(CoreCall cc) throws InterpreterException { return new CoreNumber(getCurrentObject(cc).items.size()); }
	
	public CoreObject xPopFirst(CoreCall cc) throws InterpreterException { return getCurrentObject(cc).popFirst(); }

	public CoreObject xPopLast(CoreCall cc) throws InterpreterException { return getCurrentObject(cc).popLast(); }

	public CoreObject xRemove(CoreCall cc) throws InterpreterException { return getCurrentObject(cc).removeByIdx(cc.argPop().toDouble().intValue()); }

	public CoreObject xRemoveObject(CoreCall cc) throws InterpreterException { return getCurrentObject(cc).removeByObject(cc.argPop()); }

	public CoreObject xSet(CoreCall cc) throws InterpreterException	{ return getCurrentObject(cc).set(cc.argPop().toDouble().intValue(), cc.argPop()); }
	
	public CoreObject xInsert(CoreCall cc) throws InterpreterException	{ return getCurrentObject(cc).insert(cc.argPop().toDouble().intValue(), cc.argPop()); }

	public CoreObject xSort(CoreCall cc) throws InterpreterException	{ return getCurrentObject(cc).sort(cc); }
	
	public CoreObject xEach(CoreCall cc) throws InterpreterException	
	{ 
		Iterator<CoreObject> i = getCurrentObject(cc).items.iterator();
		int idx = -1;
		CoreObject yieldFunction = cc.argPop();
		while(i.hasNext())
		{
			idx++;
			ClastCapsule args = new ClastCapsule(new Token(), i.next());
			args.next = new ClastCapsule(new Token(), new CoreNumber(idx));
			yieldFunction.execute(new CoreCall(cc.callerContext, getCurrentObject(cc), args));
		}
		return this;
	}

	private class NumComp implements Comparator<CoreObject> {
		public String dir;
		public NumComp(String direction)
		{
			super();
			dir = direction;
		}
		public int compare(CoreObject o1, CoreObject o2)
		{
			if(dir.equals("asc"))
				return o1.toDouble().intValue() - o2.toDouble().intValue();
			else
				return o2.toDouble().intValue() - o1.toDouble().intValue();
		}		
	}
	
	private class AlphaComp implements Comparator<CoreObject> {
		public String dir;
		public AlphaComp(String direction)
		{
			super();
			dir = direction;
		}
		public int compare(CoreObject o1, CoreObject o2)
		{
			if(dir.equals("asc"))
			  	return o1.toString().compareToIgnoreCase(o2.toString());
			else
				return o2.toString().compareToIgnoreCase(o1.toString());
		}		
	}
	
	public CoreObject sort(CoreCall cc)
	{
		String sortMode = cc.getMemberDefaultString("mode", "alpha");
		String sortDirection = cc.getMemberDefaultString("dir", "asc");
		if(sortMode.equals("alpha"))
			Collections.sort(items, new AlphaComp(sortDirection));
		else if(sortMode.equals("numeric"))
			Collections.sort(items, new NumComp(sortDirection));
		return this;
	}
	
	public CoreObject insert(int atIndex, CoreObject ni)
	{
		items.add(atIndex, ni);
		return ni;
	}
	
	public CoreObject add(CoreObject ni)
	{
		items.add(ni);
		return ni;
	}
	
	public CoreObject set(int atIndex, CoreObject ni)
	{
		try
		{
			items.set(atIndex, ni);
		}
		catch (Exception e)
		{
			items.add(ni);
		}
		return ni;
	}
	
	public int count()
	{
		return items.size();
	}
	
	public void clear()
	{
		items.clear();
	}
	
	private CoreObject itemAccess(int idx, boolean del)
	{
		CoreObject result;
		try
		{
			result = items.get(idx);
			if(del)
				items.remove(idx);
		}
		catch (Exception e)
		{
			result = null;
		}
		if(result == null)
			result = new CoreObject();
		return result;
	}
	
	public CoreObject item(int idx)
	{
		return itemAccess(idx, false);
	}
	
	public CoreObject popFirst()
	{
		CoreObject result = itemAccess(0, true);		
		return result;
	}
	
	public CoreObject popLast()
	{
		CoreObject result = itemAccess(items.size()-1, true);		
		return result;
	}
	
	public CoreObject removeByIdx(int idx)
	{
		return itemAccess(idx, true);
	}
	
	public CoreObject removeByObject(CoreObject o)
	{
		try
		{
			items.remove(o);
		}
		catch (Exception e) { }
		return o;
	}
	
	public String getType()
	{
		return("List");
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
        sb.append("(list ");

        for(int i = 0; i < items.size(); i++)
        {
                if(i > 0) sb.append(" ");
                CoreObject itemObject = items.get(i);
                sb.append("'"+itemObject.toString()+"'");
        }

        sb.append(")");
        return sb.toString();
	}

}
