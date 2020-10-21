struct Resolver
{

	ASTNode* ast_root = 0;
	Scope* scope_root = 0;
	bool cancel = false;

	void resolve_declarations(ASTNode* node, Scope* scope)
	{
		Scope* current_scope = scope;
		while(node && !cancel)
		{
			if(node->is(TDECLARATION))
			{
				ASTNode* parent = node->parent;
				if(!parent->scope)
				{
					parent->scope = new Scope();
					parent->scope->parent = scope;
					current_scope = parent->scope;
				}
				ScopeEntity scope_entity;
				scope_entity.definition = node;
				if(parent->scope->contains(node->child->literal))
				{
					error("already defined", node->child);
				}
				else
				{
					parent->scope->items[node->child->literal] = scope_entity;
				}
			}
			if(node->child)
				resolve_declarations(node->child, current_scope);
			node = node->next;
		}
	}

	void resolve_all(ASTNode* start)
	{
		ast_root = start;
		init_scope();
		start->scope = scope_root;
		resolve_declarations(start, scope_root);
	}

	void init_scope()
	{
		if(!scope_root)
		{
			scope_root = new Scope();
		}
	}

	void default_scope()
	{
		scope_root->add("print");
		scope_root->add("string");
		scope_root->add("int");
		scope_root->add("float");
		scope_root->add("boolean");
		scope_root->add("map");
		scope_root->add("array");
		scope_root->add("include");
	}

	void error(string message, ASTNode* token, string message2 = "")
	{
		if(token->literal != "")
			printf("\u001b[91mERROR: %s \"%s\" at line %i col %i %s\u001b[0m\n",
				message.c_str(),
				token->literal.c_str(),
				token->line, token->col,
				message2.c_str());
		else
			printf("\u001b[91mERROR: %s %s at line %i col %i %s\u001b[0m\n",
				message.c_str(),
				token->text.c_str(),
				token->line, token->col,
				message2.c_str());
		cancel = true;
	}

};
