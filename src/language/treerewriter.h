

struct TreeRewriter
{
	bool modified = false;
	s32	iteration_count = 0;
	
	bool match_part(ASTNode* n, TokenType t, string s)
	{
		if(!n) return(false);
		return(n->type == t && (n->type == TIDENTIFIER || n->text == s));
	}
	
	bool match(ASTNode* n, 
		TokenType t1, string s1, 
		TokenType t2 = TNONE, string s2 = "", 
		TokenType t3 = TNONE, string s3 = "")
	{
		if(!n) return(false);
		if(t3)
			return(match_part(n, t1, s1) && match_part(n->next, t2, s2) && match_part(n->next->next, t3, s3));
		else if(t2)
			return(match_part(n, t1, s1) && match_part(n->next, t2, s2));
		else
			return(match_part(n, t1, s1));
	}
	
	void traverse(ASTNode* root)
	{
		ASTNode* current = root;
		while(current)
		{
			if(match(current, TIDENTIFIER, "", TPUNCT, ":", TIDENTIFIER, "")) // typed declaration
			{
				auto identifier = new ASTNode();
				identifier->copy_from(current);
				current->type = TDECLARATION;
				current->child = identifier;
				auto typedecl = new ASTNode();
				typedecl->copy_from(current->next->next);
				identifier->next = typedecl;
				current->next = current->next->next->next;
				modified = true;
			}
			else if(match(current, TIDENTIFIER, "", TPUNCT, ":")) // auto-typed declaration
			{
				auto identifier = new ASTNode();
				identifier->copy_from(current);
				current->type = TDECLARATION;
				current->child = identifier;
				current->next = current->next->next;
				modified = true;
			}
			else if(match(current, TIDENTIFIER, "", TPUNCT, "=")) // assignment
			{
				auto identifier = new ASTNode();
				identifier->copy_from(current);
				current->type = TASSIGNMENT;
				current->child = identifier;
				auto rval = current->next->next;
				identifier->next = rval;
				current->next = current->next->next->next;
				rval->next = 0;
				modified = true;
			}
			else if(match(current, TDECLARATION, "", TPUNCT, "=")) // declaration and assignment
			{
				printf("!!!"); // FIXME
				auto rval = current->next->next;
				current->append_child(rval);
				current->next = current->next->next->next;
				rval->next = 0;
				modified = true;
			}
			if(current->child)
			{
				traverse(current->child);
			}
			current = current->next;
		}
	}
	
	void process(ASTNode* root)
	{
		modified = true;
		while(modified)
		{
			iteration_count++;
			modified = false;
			traverse(root);
		}
		printf("Iterations: %i\n", iteration_count);
	}
	
};