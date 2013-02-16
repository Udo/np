package np;

import np.Interpreter.InterpreterException;

public class ClastFactory
{
	private static ClastNode TI2ClastLevel(TreeItem node) throws InterpreterException
	{
		ClastNode result = null;
		ClastNode current = null;
		while (node != null)
		{
			if(current == null)
			{
				result = TI2ClastNode(node);
				current = result;
			}
			else
			{
				current.next = TI2ClastNode(node);
				current = current.next;
			}
			node = node.next;
		}
		return result;
	}
	
	private static ClastNode TI2ClastNode(TreeItem node) throws InterpreterException
	{
		ClastNode result = null;

		if(node.token.type.equals("ArgDef"))
			result = new ClastArgDef(node.token);
		else if(node.token.type.equals("Exp"))
			result = new ClastExp(node.token);
		else if(node.token.type.equals("Function"))
			result = new ClastFunction(node.token);
		else if(node.token.type.equals("Identifier"))
			result = new ClastIdentifier(node.token);
		else if(node.token.type.equals("List"))
			result = new ClastList(node.token);
		else if(node.token.type.equals("Module"))
			result = new ClastModule(node.token);
		else if(node.token.type.equals("Number"))
			result = new ClastNumber(node.token);
		else if(node.token.type.equals("String"))
			result = new ClastString(node.token);
		else
			throw new InterpreterException("unexpected token '"+node.token.value+"'", node.token);
		
		if(node.child != null)
			result.child = TI2ClastLevel(node.child);
		
		return result;
	}
	
	public static ClastNode makeClastFromTree(TreeItem fromRootItem) throws InterpreterException
	{
		return TI2ClastNode(fromRootItem);
	}
}
