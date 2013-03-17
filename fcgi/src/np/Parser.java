package np;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import np.Interpreter.InterpreterException;

/*
 * The Parser takes in the flat token list spewed out by the Lexer and turns
 * that into a tree representation. The parser contains rules that tell it where
 * to descend deeper into the AST and it's also the first stage of the compilation
 * process that actually yields error messages. Almost all of the complexity of
 * the Parser comes from the convenience parsing of operators, which are internally
 * represented as functions, and which have to adhere to a specific operator 
 * precedence the user expects. 
 */
public class Parser 
{
	public TreeItem root;
	public ArrayList<Token> tokens;
	public int tokenIndex;
	public Token currentToken;
	public HashMap<String, String> tokenNameToChar = new HashMap<String, String>();
	
	public class OperatorOptions 
	{
		Boolean gobbleRightSide = false;
		Boolean uniRight = false;
		Boolean uniLeft = false;
	}
	
	/*
	 * these are the rules for operator recognition and parsing
	 */
	private final Set<String> OP_UNI0 = new HashSet<String>(Arrays.asList(
		     new String[] {"#", "@", "$"}
		));	
	private final Set<String> OP_UNI1 = new HashSet<String>(Arrays.asList(
		     new String[] {"!", "PFX+", "PFX-"}
		));	
	private final Set<String> OP_AR0 = new HashSet<String>(Arrays.asList(
		     new String[] {"*", "/"}
		));	
	private final Set<String> OP_AR1 = new HashSet<String>(Arrays.asList(
		     new String[] {"+", "-"}
		));	
	private final Set<String> OP_UNILEFT = new HashSet<String>(Arrays.asList(
		     new String[] {"++", "--"}
		));	
	private final Set<String> OP_BOOL = new HashSet<String>(Arrays.asList(
		     new String[] {"==", "!=", "<=", "<", ">", ">="}
		));	 
	private final Set<String> OP_ASSIGN = new HashSet<String>(Arrays.asList(
		     new String[] {"=", "+=", "-="}
		));	
	private final Set<String> OP_DOT = new HashSet<String>(Arrays.asList(
		     new String[] {".", ":", "::"}
		));	
	private final Set<String> UNEXPECTED_TOKENS = new HashSet<String>(Arrays.asList(
		     new String[] {"ParenStart", "ParenEnd", "FnStart", "FnEnd", "StEnd", "IdentExprStart", "IdentExprEnd"}
		));	
	
	public Parser()
	{
		/*
		 * translate some of the named punctuation tokens back into symbol ones
		 */
		tokenNameToChar.put("ParenStart", "(");
		tokenNameToChar.put("ParenEnd", ")");
		tokenNameToChar.put("FnStart", "{");
		tokenNameToChar.put("FnEnd", "}");
		tokenNameToChar.put("StEnd", ";");
		tokenNameToChar.put("IdentExprStart", "[");
		tokenNameToChar.put("IdentExprEnd", "]");
	}

	public Token next()
	{
		Token result = null;
		if(tokenIndex < tokens.size())
		{
			result = (Token) tokens.get(tokenIndex);
			tokenIndex++;
		}
		currentToken = result;
		return result;
	}
	
	public void FatalError(String msg, Token tk) throws InterpreterException
	{
		currentToken = null;
		tokenIndex = tokens.size();
		throw new Interpreter.InterpreterException(msg, tk);
	}
	
	public TreeItem parseParens() throws InterpreterException 
	{
		next(); 
		TreeItem result = parseLevel("ParenEnd");
		result.token.type = "Exp";
		return result;
	}
	
	public TreeItem parseIdentExpr() throws InterpreterException
	{
		next(); 
		TreeItem result = parseLevel("IdentExprEnd");
		result.token.type = "IdentExpr";
		return result;
	}
	
	public TreeItem parseFunction() throws InterpreterException
	{
		TreeItem result = new TreeItem(new Token("Function", ""));
		result.token.pos = currentToken.pos;
		TreeItem current = result;
		next(); // consume FnStart
		
		while (current != null && currentToken != null && !currentToken.type.equals("FnEnd"))
		{
			TreeItem newItem = parseLevel("StEnd"); 
			
			if(currentToken != null && currentToken.type.equals("StEnd") && currentToken.value.equals("|"))
			{
				newItem.token.type = "ArgDef";
			}
				
			if(current == result)
				current.child = newItem;
			else
				current.next = newItem;

			current = newItem;
			next();
		}
		
		if(currentToken == null)
			FatalError("} expected", current.token);
		
		return result;
	}
	
	public TreeItem parseLevel(String limitedByTokenType) throws InterpreterException
	{
		if(currentToken == null)
			return(null);
		
		TreeItem result = new TreeItem("Exp");
		result.token.pos = currentToken.pos;

		TreeItem current = null;
		TreeItem newItem = null;

		while (currentToken != null && currentToken != null && !currentToken.type.equals(limitedByTokenType))
		{
			if(currentToken.type.equals("ParenStart"))
				newItem = parseParens();
			else if(currentToken.type.equals("FnStart"))
				newItem = parseFunction();
			else if(currentToken.type.equals("IdentExprStart"))
				newItem = parseIdentExpr();
			else
				newItem = new TreeItem(currentToken);

			if(current == null)
				result.child = newItem;
			else
				current.next = newItem;
			
			current = newItem;

			next();
		}

		if(currentToken == null)
			FatalError(tokenNameToChar.get(limitedByTokenType) + " expected", current.token);

		return(result);
	}
	
	/*
	 * this is the place where different types of operators leed to different types of
	 * AST transformations, depending on where the operator and its operands need to be
	 * inserted. it's long, but really simple because it does nothing besides swapping
	 * pointers around.
	 */
	public void applyOpExp(TreeItem node, Set<String> opType, OperatorOptions opt) throws InterpreterException
	{
		TreeItem lastNode = null;
		while (node != null)
		{
			if(node.token.type.equals("Op") && opType.contains(node.token.value))
			{
				if(opt.uniLeft)
				{
					if(lastNode == null)
						FatalError("missing operand", node.token);

					TreeItem op1 = new TreeItem();
					op1.child = lastNode.child;
					op1.token = lastNode.token;
					
					lastNode.token = new Token("Exp", "");
					lastNode.token.pos = node.token.pos;
					lastNode.child = new TreeItem(new Token("Identifier", node.token.value));
					lastNode.child.token.pos = node.token.pos;
					lastNode.child.next = op1;
					// remove next node from chain, skip to next.next
					lastNode.next = node.next;
					node = lastNode.next;
				}
				else if(opt.uniRight)
				{
					if(node.next == null)
					{
						FatalError("missing operand", node.token);
						return;
					}
					Token opToken = node.token;
					TreeItem nextNode = node.next.next;
					if(node.token.value.equals("#"))
					{
						node.token = new Token("String", node.next.token.value);
					}
					else
					{
						node.token = new Token("Exp", "");
						node.child = new TreeItem(new Token("Identifier", opToken.value));
						node.child.next = node.next;
						node.child.next.next = null;
					}
					node.next = nextNode;
					lastNode = node;
					node = node.next;
				}
				else
				{
					if(node.next == null || lastNode == null)
					{
						FatalError("missing operand", node.token);
						return;
					}
					TreeItem op1 = new TreeItem();
					op1.child = lastNode.child;
					op1.token = lastNode.token;
					
					TreeItem op2 = new TreeItem();
					op2.child = node.next.child;
					op2.token = node.next.token;

					lastNode.token = new Token("Exp", "");
					lastNode.token.pos = node.token.pos;
					lastNode.child = new TreeItem(new Token("Identifier", node.token.value));
					lastNode.child.token.pos = node.token.pos;
					lastNode.child.next = op1;
					
					if(opt.gobbleRightSide && node.next.next != null)
					{
						TreeItem newExp = new TreeItem();
						newExp.token = new Token("Exp", "");
						newExp.child = node.next;
						lastNode.next = null;
						lastNode.child.next.next = newExp;
						applyOpExp(newExp, opType, opt);
						return;
					}
					else
					{
						lastNode.child.next.next = op2;
						// remove next node from chain, skip to next.next
						lastNode.next = node.next.next;
						node = lastNode.next;
					}
				}
			}
			else
			{
				lastNode = node;
				node = node.next;
			}
		}
	}
	
	public void applyOp(TreeItem node, Set<String> opType, OperatorOptions opt) throws InterpreterException
	{
		while (node != null)
		{
			if(node.token.type.equals("Exp") || node.token.type.equals("List"))
				applyOpExp(node.child, opType, opt);

			if(node.child != null)
				applyOp(node.child, opType, opt);
			
			node = node.next;
		}
	}
	
	/*
	 * This is how operator precedence is defined, you can guess the precedence order
	 * by looking at the sequence of applyOp() calls here. The inefficient part is of
	 * course that the AST needs to be traversed once for every category, but on the 
	 * upside this doesn't take very long and it's a paradigm that produces easy-to-
	 * understand code (at least for my taste).
	 */
	public void applyOperators() throws InterpreterException
	{
		applyOp(root, OP_UNI0, new OperatorOptions() {{ uniRight = true; }});
		applyOp(root, OP_DOT, new OperatorOptions() {{ }});
		applyOp(root, OP_UNILEFT, new OperatorOptions() {{ uniLeft = true; }});
		applyOp(root, OP_AR0, new OperatorOptions() {{ }});
		applyOp(root, OP_AR1, new OperatorOptions() {{ }});
		applyOp(root, OP_UNI1, new OperatorOptions() {{ uniRight = true; }});
		applyOp(root, OP_BOOL, new OperatorOptions() {{ }});
		applyOp(root, OP_ASSIGN, new OperatorOptions() {{ gobbleRightSide = true; }});
	}
	
	public void checkForErrors(TreeItem node) throws InterpreterException
	{
		while(node != null)
		{
			if(node.child != null)
				checkForErrors(node.child);

			if(UNEXPECTED_TOKENS.contains(node.token.type))
				FatalError("unexpected " + tokenNameToChar.get(node.token.type), node.token);
				
			node = node.next;
		}
	}
	
	public void parse(ArrayList<Token> tk, String moduleName) throws InterpreterException
	{
		tokens = tk;
		tokenIndex = 0;
		next();

		root = new TreeItem(new Token("Module", moduleName));
		TreeItem current = root;
		
		while (current != null)
		{
			TreeItem newItem = parseLevel("StEnd"); 
			if(current == root)
			{
				current.child = newItem;
				current = current.child;
			}
			else
			{
				current.next = newItem;
				current = current.next;
			}
			next();
		}
		
		applyOperators();
		checkForErrors(root);
	}
	
	public void showTreeLevel(int lvl, TreeItem lvlNode, StringBuilder sv)
	{
		TreeItem current = lvlNode;
		String indent = new String(new char[lvl*2]).replace("\0", " ");
		while (current != null)
		{
			sv.append(indent + current.token.toString() + "\n");
			if(current.child != null)
				showTreeLevel(lvl+1, current.child, sv);
			current = current.next;
		}
	}
	
	public String showTree()
	{
		StringBuilder sv = new StringBuilder();

		showTreeLevel(0, root, sv);
		
		return sv.toString();
	}
}
