

struct TreeRewriter
{
	bool match_part(ASTNode* n, TokenType t, string s)
	{
		if(!n || !n->token) return(false);
		return(n->token->type == t && (n->token->type == TIDENTIFIER || n->token->text == s));
	}
	
	bool match(ASTNode* n, 
		TokenType t1, string s1, 
		TokenType t2 = TNONE, string s2 = "", 
		TokenType t3 = TNONE, string s3 = "")
	{
		if(!n || !n->token) return(false);
		if(t3)
			return(match_part(n, t1, s1) && match_part(n->next, t2, s2) && match_part(n->next->next, t3, s3));
		else if(t2)
			return(match_part(n, t1, s1) && match_part(n->next, t2, s2));
		else
			return(match_part(n, t1, s1));
	}
	
	void process(ASTNode* root)
	{
		ASTNode* current = root;
		while(current)
		{
			if(match(current, TIDENTIFIER, "", TPUNCT, ":", TIDENTIFIER, "")) // typed declaration
			{
				current->type = 'D';
				auto identifier = new ASTNode();
				identifier->token = current->token;
				current->token = 0;
				current->child = identifier;
				auto typedecl = new ASTNode();
				typedecl->token = current->next->next->token;
				identifier->next = typedecl;
				current->next = current->next->next->next;
				//delete current->next;
			}
			else if(match(current, TIDENTIFIER, "", TPUNCT, ":")) // auto-typed declaration
			{
				current->type = 'D';
				auto identifier = new ASTNode();
				identifier->token = current->token;
				current->token = 0;
				current->child = identifier;
				current->next = current->next->next;
				//delete current->next;
			}
			if(current->child)
			{
				process(current->child);
			}
			current = current->next;
		}
	}
	
};