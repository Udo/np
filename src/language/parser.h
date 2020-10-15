
typedef Token ASTNode;

struct Parser
{
	ASTNode* ast_root;
	Token* token_list;
	Token* token;
	
	void consume()
	{
		if(token) token = token->next;
	}
	
	ASTNode* parse_expression(char delim)
	{
		ASTNode* result = new ASTNode();
		ASTNode* current = 0;
		result->type = TEXPRESSION;
		if(delim == ';') result->type = TSTATEMENT;
		while(token)
		{
			bool advance = true;
			ASTNode* n;
			if(token->type == TCOMMENT)
			{
				consume();
				n = 0;
			}
			else if(token->text[0] == delim)
			{
				consume();
				return(result);
			}
			else if(token->text[0] == '(')
			{
				consume();
				n = parse_expression(')');
			}
			else if(token->text[0] == '[')
			{
				consume();
				n = parse_expression(']');
			}
			else if(token->text[0] == '{')
			{
				consume();
				n = parse_statements();
			}
			else
			{
				n = new ASTNode();
				n->copy_from(token);
				consume();
			}
			
			if(n)
			{
				if(!current) 
					result->child = n;
				else
					current->next = n;
				current = n;
				current->parent = result;
			}
		}
		return(result);
	}
	
	ASTNode* parse_statements()
	{
		ASTNode* result = new ASTNode();
		result->type = TBLOCK;
		ASTNode* current = 0;
		while(token)
		{			
			if(token->text[0] == '}') 
			{
				consume();
				return(result);
			}
			else
			{
				auto n = parse_expression(';');
				if(!current)
					result->child = n;
				else
					current->next = n;
				current = n;
				current->parent = result;
			}
		}
		return(result);
	}
	
	void parse(Token* token_list)
	{
		token_list = token_list;
		token = token_list;
		ast_root = parse_statements();
	}
};
