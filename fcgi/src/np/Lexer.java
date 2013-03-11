package np;

import java.util.ArrayList;

/*
 * the lexer is long, but very straight-forward. all it does is hack a string of
 * source code into a flat token list.
 */
public class Lexer
{
	public static final int PARSERMODE_CODE = 0;
	public static final int PARSERMODE_STRING_LITERAL = 1;
	public static final int PARSERMODE_LINECOMMENT = 2;
	public static final int PARSERMODE_PRINTSTRING = 3;

	int idx, codeLength, currentMode;
	public ArrayList<Integer> lineBreakDictionary = new ArrayList<Integer>();
	String sourceText, stringLiteralDelimiter, currentTokenType, lookAhead, previousChar, currentChar;
	StringBuilder currentTokenString = new StringBuilder();
	public ArrayList<Token> tokens;

	public void init(String src)
	{
		idx = 0;
		currentMode = PARSERMODE_CODE;
		stringLiteralDelimiter = "";
		currentTokenString.setLength(0);
		currentTokenType = "N";
		codeLength = src.length();
		sourceText = src;
		tokens = new ArrayList<Token>();
		lookAhead = "";
		previousChar = "";
		updateLineCounter();
	}

	public String next()
	{
		previousChar = currentChar;
		try
		{
			idx++;
			if(idx < sourceText.length())
				lookAhead = sourceText.substring(idx, idx + 1);
			else
				lookAhead = "";
			currentChar = (sourceText.substring(idx - 1, idx));
			return currentChar;
		}
		catch(Exception e)
		{
			return "";
		}
	}

	public void finishStringLiteral()
	{
		currentMode = PARSERMODE_CODE;
		commitToken();
	}

	public void beginStringLiteral(String currentItem)
	{
		currentTokenType = "String";
		stringLiteralDelimiter = currentItem;
		currentMode = PARSERMODE_STRING_LITERAL;
	}

	public void addToCurrentValue(String fragment)
	{
		currentTokenString.append(fragment);
	}

	public void commitToken()
	{
		if (currentTokenString.length() != 0 && !currentTokenType.equals('N'))
		{
			commitToken(currentTokenType, currentTokenString);
		}
	}

	public void commitToken(String tType, StringBuilder tValue)
	{
		commitToken(tType, tValue.toString());
	}

	public void commitToken(String tType, String tValue)
	{
		Token tk = new Token(tType, tValue);
		translateLocationInText(idx - tValue.length(), tk);
		tokens.add(tk);
		currentTokenString.setLength(0);
		currentTokenType = "N";
	}

	public void buildIdentifier(String currentItem)
	{
		addToCurrentValue(currentItem);
		currentTokenType = "Identifier";
	}

	public void consumeOperator(String currentItem, String[] possibleFollowUps, String soloType)
	{
		currentTokenString.append(currentItem);
		for (int i = 0; i < possibleFollowUps.length; i++)
			if (lookAhead.equals(possibleFollowUps[i]))
			{
				currentTokenString.append(next());
				commitToken("Op", currentTokenString);
				return;
			}
		commitToken(soloType, currentTokenString);
	}

	public void consumeOperatorPfx(String currentItem, String[] possibleFollowUps, String soloType)
	{
		/*
		 * this is a special case of operator consumption. if the last item was
		 * a whitespace, and the following one isn't, this is not an operator but a 
		 * negation/positive prefix sign. it's also the only place in the entire
		 * lexer where whitespace is actually significant.
		 */
		if(previousChar.matches("\\s") && !lookAhead.matches("\\s"))
			commitToken("Op", "PFX"+currentItem);
		else
			consumeOperator(currentItem, possibleFollowUps, soloType);
	}

	public void beginLineComment(int advanceIndex)
	{
		idx = idx + advanceIndex;
		currentMode = PARSERMODE_LINECOMMENT;
		previousChar = " ";
	}

	public void finishLineComment()
	{
		currentTokenString.setLength(0);
		currentTokenType = "N";
		currentMode = PARSERMODE_CODE;
		previousChar = " ";
	}
	
	public void consumeNumber(String currentItem)
	{
		commitToken();
		idx = idx - 1;
		Character currentDigit = sourceText.charAt(idx); 
		Boolean dotProcessed = false;
		while(idx < codeLength && (Character.isDigit(currentDigit) || (currentDigit.equals('.') && !dotProcessed)))
		{
			if(currentDigit.equals('.')) dotProcessed = true;
			addToCurrentValue(currentDigit.toString());
			idx++;
			if(idx < codeLength)
				currentDigit = sourceText.charAt(idx);
		}
		commitToken("Number", currentTokenString);
	}
	
	public void consumePrintString(int advanceIndex)
	{
		idx += advanceIndex;
		currentMode = PARSERMODE_CODE;
		int endStrPos = sourceText.indexOf("<?np", idx);
		String printString;
		if(endStrPos == -1)
		{
			printString = sourceText.substring(idx);
			idx = codeLength;
		}
		else
		{
			printString = sourceText.substring(idx, endStrPos);
			idx = endStrPos+4;
		}
		commitToken("ParenStart", "");
		commitToken("Identifier", "unsafeprint");
		commitToken("String", printString);
		commitToken("ParenEnd", "");
		commitToken("StEnd", "");
		previousChar = " ";
	}
	
	public void updateLineCounter()
	{
		lineBreakDictionary.clear();
		lineBreakDictionary.add(new Integer(0));
		for(int i = 0; i < codeLength; i++)
			if(sourceText.charAt(i) == '\n')
				lineBreakDictionary.add(new Integer(i+1));
	}
	
	public void translateLocationInText(int charPos, Token tok)
	{
		int rcp = -1;
		int rcpi = -1;
		int breakAt = -1;
		int lbSize = lineBreakDictionary.size();
		for(int ic = 0; ic < lbSize; ic++)
		{
			breakAt = (Integer) lineBreakDictionary.get(ic);
			if(charPos > breakAt)
			{
				rcp = charPos - breakAt;
				rcpi = ic;
			}
		}
		tok.pos.col = rcp-1;
		tok.pos.line = rcpi+1;
		if(tok.pos.col == 0)
			tok.pos.col = 1;
	}
	
	public ArrayList<Token> readText(String src)
	{
		init(src);
		
		if(sourceText.charAt(0) == '<')
			consumePrintString(0);
		
		while (idx < codeLength)
		{
			String currentItem = next();

			if (currentMode == PARSERMODE_LINECOMMENT)
			{

				if (currentItem.matches("\\r?\\n|\\r\\n")) finishLineComment();

			}
			else if (currentMode == PARSERMODE_STRING_LITERAL)
			{

				if (currentItem.equals(stringLiteralDelimiter)) finishStringLiteral();
				else
					addToCurrentValue(currentItem);

			}
			else if (currentMode == PARSERMODE_CODE)
			{

				if (currentItem.matches("\\s")) // whitespace (always closes the
				                                // current token)
				commitToken();

				else if (currentItem.matches("\\p{Punct}") && !currentItem.equals("_")) // punctuation
				{
					commitToken();

					if (currentItem.equals("\"") || currentItem.equals("'")) beginStringLiteral(currentItem);
					else if (currentItem.equals(";")) commitToken("StEnd", "");
					else if (currentItem.equals("{")) commitToken("FnStart", "");
					else if (currentItem.equals("}")) 
					{
						commitToken("StEnd", "");
						commitToken("FnEnd", "");
					}
					else if (currentItem.equals("(")) commitToken("ParenStart", "");
					else if (currentItem.equals(")")) commitToken("ParenEnd", "");
					else if (currentItem.equals("[")) commitToken("IdentExprStart", "");
					else if (currentItem.equals("]")) commitToken("IdentExprEnd", "");
					else if (currentItem.equals("$")) buildIdentifier(currentItem);
					else if (currentItem.equals("@")) buildIdentifier(currentItem);
					else if (currentItem.equals(".")) commitToken("Op", ".");
					else if (currentItem.equals("|")) 
					{
						//commitToken("Op", "|");
						commitToken("StEnd", "|");
					}
					else if (currentItem.equals(":")) consumeOperator(currentItem, ":".split(","), "Op");
					else if (currentItem.equals("#")) commitToken("Op", "#");
					else if (currentItem.equals("!")) consumeOperator(currentItem, "=".split(","), "Op");
					else if (currentItem.equals("<")) consumeOperator(currentItem, "=".split(","), "Op");
					else if (currentItem.equals(">")) consumeOperator(currentItem, "=".split(","), "Op");
					else if (currentItem.equals("=")) consumeOperator(currentItem, "=".split(","), "Op");
					else if (currentItem.equals("+")) consumeOperatorPfx(currentItem, "=,+".split(","), "Op");
					else if (currentItem.equals("-")) consumeOperatorPfx(currentItem, "=,-".split(","), "Op");
					else if (currentItem.equals("*")) consumeOperator(currentItem, "=".split(","), "Op");
					else if (currentItem.equals("/"))
					{
						if (lookAhead.equals("/")) beginLineComment(1);
						else
							commitToken("Op", currentItem);
					}
					else if (currentItem.equals("?"))
					{
						if (lookAhead.equals(">")) consumePrintString(1);
						else
							commitToken("Op", currentItem);
					}
				}
				else if (!currentItem.isEmpty() && Character.isDigit( currentItem.charAt( 0 ) ) && !currentTokenType.equals("Identifier"))
					consumeNumber(currentItem);
				else
					buildIdentifier(currentItem);
			}
		}

		commitToken();

		return tokens;
	}

}
