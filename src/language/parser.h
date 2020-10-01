
struct ASTNode
{
	ASTNode* next;
	ASTNode* child;
	ASTNode* parent;
	Token* token;
	char type = 'T';
	
	void print(bool all = false, string level = "")
	{
		if(this->token)
			printf("%s\u001b[32m%c %i \u001b[34m%i:%i \u001b[33m%s\u001b[0m\n", level.c_str(),
				this->type,
				this->token->type, this->token->col, this->token->line, this->token->text.c_str());
		else
			printf("%s%c :\n", level.c_str(), this->type);
		if(this->child) this->child->print(true, level+"  ");
		if(all && this->next) this->next->print(true, level);
	}
};

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
		result->type = 'E';
		if(delim == ';') result->type = 'S';
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
				n->token = token;
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
		result->type = 'L';
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
