
struct ASTNode
{
	ASTNode* next;
	ASTNode* previous;
	ASTNode* child;
	ASTNode* parent;
	Token* token;
	char type;
	
	void print(bool all = false, string level = "")
	{
		if(this->token)
			printf("%s\u001b[32m%c %i \u001b[34m%i:%i \u001b[33m%s\u001b[0m\n", level.c_str(),
				this->type,
				this->token->type, this->token->col, this->token->line, this->token->text.c_str());
		else
			printf("%s %c -\n", level.c_str(), this->type);
		if(this->child) this->child->print(true, level+"  ");
		if(all && this->next) this->next->print(true, level);
	}
};

struct Parser
{
	ASTNode* ast_root;
	ASTNode* ast;
	Token* token_list;
	Token* token;
	
	ASTNode* parse_expression(char delim)
	{
		ASTNode* result = new ASTNode();
		ASTNode* current = 0;
		result->type = 'E';
		while(token)
		{
			ASTNode* n;
			if(token->type == TCOMMENT)
			{
				n = 0;
			}
			else if(token->text[0] == delim)
			{
				token = token->next;
				return(result);
			}
			else if(token->text[0] == '(')
			{
				token = token->next;
				n = parse_expression(')');
			}
			else if(token->text[0] == '[')
			{
				token = token->next;
				n = parse_expression(']');
			}
			else if(token->text[0] == '{')
			{
				token = token->next;
				n = parse_expression('}');
			}
			else
			{
				n = new ASTNode();
				n->token = token;
			}
			
			if(n)
			{
				if(!current) 
				{
					result->child = n;
					current = n;
				}
				else
				{
					current->next = n;
					current = n;
				}
			}
			if(token) token = token->next;
		}
		return(result);
	}
	
	void parse(Token* token_list)
	{
		ast_root = new ASTNode();
		ast = ast_root;
		token_list = token_list;
		token = token_list;
		while(token)
		{			
			ast->next = parse_expression(';');
			ast = ast->next;
		}
	}
};
