
struct ASTNode
{
	ASTNode* next;
	ASTNode* previous;
	ASTNode* child;
	ASTNode* parent;
	Token* token;
	
	void print(bool all = false, string level = "")
	{
		if(this->token)
			printf("%s\u001b[32m%i \u001b[34m%i:%i \u001b[33m%s\u001b[0m\n", level.c_str(),
				this->token->type, this->token->col, this->token->line, this->token->text.c_str());
		else
			printf("%s(empty)\n", level.c_str());
		if(this->child) this->child->print(true, level+"  ");
		if(all && this->next) this->next->print(true, level);
	}
};

ASTNode* parse(Token* token_list)
{
	ASTNode* ast_root = new ASTNode();
	ASTNode* ast_prev = ast_root;
	Token* token = token_list;
	while(token)
	{
		ASTNode* n = new ASTNode();
		n->token = token;
		
		token = token->next;
		ast_prev->next = n;
		ast_prev = n;
	}
	return(ast_root);
}
