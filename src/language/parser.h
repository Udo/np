struct ASTNode
{
	
	int col;
	int line;
	
	ASTNode(Token* t)
	{
		col = t->col;
		line = t->line;
	}
	
	virtual void print(string indent = "")
	{
		printf("%s NODE\n", indent.c_str());
	}
	
};

struct ASTAssignment : ASTNode
{
	
	string identifier;
	ASTNode* value;
	
	ASTAssignment(Token* t) : ASTNode(t) {};
	
	void print(string indent = "")
	{
		printf("%s Assigment\n", indent.c_str());
	}

};

struct ASTString : ASTNode
{

	string value;

	ASTString(Token* t) : ASTNode(t)
	{
		value = t->literal;
	}

	void print(string indent = "")
	{
		printf("%s String '%s'\n", indent.c_str(), value.c_str());
	}
	
};

struct ASTIdentifier : ASTNode
{

	string identifier;

	ASTIdentifier(Token* t) : ASTNode(t)
	{
		identifier = t->literal;
	}

	void print(string indent = "")
	{
		printf("%s Identifier %s\n", indent.c_str(), identifier.c_str());
	}
	
};

struct ASTOperator : ASTNode
{

	string op;

	ASTOperator(Token* t) : ASTNode(t)
	{
		op = t->literal;
	}
	
	void print(string indent = "")
	{
		printf("%s Op\n", indent.c_str());
	}

};

struct ASTType : ASTNode
{
	
	string identifier;
	string name;
	bool is_function = false;
	ASTType* subtype = 0;
	std::vector<ASTType*> params;
	
	ASTType(Token* t) : ASTNode(t) {};

	void print(string indent = "")
	{
		printf("%s Type %s '%s' \n", indent.c_str(), identifier.c_str(), name.c_str(), is_function ? "Function" : "");
		if(subtype) subtype->print(indent+"   Sub ");
		for(auto s : params)
			s->print(indent+"  ");
	}

};

struct ASTDeclaration : ASTNode
{

	string identifier;
	bool auto_type = false;
	ASTType* type = 0;
	ASTNode* value = 0;
	
	ASTDeclaration(Token* t) : ASTNode(t) {};

	void print(string indent = "")
	{
		printf("%s Declaration %s %s\n", indent.c_str(), identifier.c_str(), auto_type ? "auto" : "");
		if(type) type->print(indent + "   Typ ");
		if(value) value->print(indent + "   Val ");
	}

};

struct ASTExpression : ASTNode
{

	std::vector<ASTNode*> items;
	
	ASTExpression(Token* t) : ASTNode(t) {};

	void print(string indent = "")
	{
		printf("%s Expression\n", indent.c_str());
		for(auto s : items)
			s->print(indent+"  ");
	}
	
};

struct ASTBlock : ASTNode
{
	
	std::vector<ASTNode*> statements;
	
	ASTBlock(Token* t) : ASTNode(t) {};

	void print(string indent = "")
	{
		printf("%s Block\n", indent.c_str());
		for(auto s : statements)
			s->print(indent+"  ");
	}
	
};

struct ASTIf : ASTNode
{
	
	ASTNode* condition = 0;
	ASTNode* thenblock = 0;
	ASTNode* elseblock = 0;
	
	ASTIf(Token* t) : ASTNode(t) {};

	void print(string indent = "")
	{
		printf("%s If\n", indent.c_str());
		condition->print(indent+"   Cond ");
		thenblock->print(indent+"   Then ");
		elseblock->print(indent+"   Else ");
	}

};

struct ASTCall : ASTNode
{
	
	string identifier;
	std::vector<ASTNode*> params;
	std::map<string, ASTNode*> named_blocks;
	
	ASTCall(Token* t) : ASTNode(t) {};

	void print(string indent = "")
	{
		printf("%s Call %s\n", indent.c_str(), identifier.c_str());
		for(auto s : params)
			s->print(indent+"  ");		
	}

};

struct ASTFunction : ASTNode
{
	
	ASTFunction(Token* t) : ASTNode(t) {};
	
	void print(string indent = "")
	{
		printf("%s Function\n", indent.c_str());
	}

};

struct ASTBase : ASTNode
{
	
	ASTBlock* block = 0;
	
	ASTBase(Token* t) : ASTNode(t) {};
	
	void print(string indent = "")
	{
		printf("%s Base\n", indent.c_str());
		block->print(indent+"  ");
	}
	
};

struct Parser
{
	ASTBase* ast_root;
	Token* token_list;
	Token* token;
	Token* token_next;
	Token* token_next_next;
	Token* token_before = 0;
	Token* neutral_token;
	bool cancel = false;

	void consume()
	{
		token_next = neutral_token;
		token_next_next = neutral_token;
		if(token)
		{
			token_before = token;
			token = token->next;
			if(token && token->next)
			{
				token_next = token->next;
				if(token_next->next)
					token_next_next = token_next->next;
			}
		}
	}

	Token* expect(string token_type_text, bool do_consume = true)
	{
		if(!token || cancel) return(new Token());
		auto n = token;
		if(token_type_text != token->text)
		{
			error("expected "+token_type_text+" at", token);
			return(n);
		}
		if(do_consume) consume();
		return(n);
	}

	bool match_token(string m, Token* t)
	{
		if(!t || t == neutral_token)
			return(false);
		if(m.substr(0, 2) == "::")
		{
			return(t->type == TIDENTIFIER && t->literal == m.substr(2));
		}
		else
		{
			return(m == t->text);
		}
	}

	bool match(string m1, string m2 = "", string m3 = "")
	{
		if(!token || cancel) return(false);
		if(m3 != "")
			return(match_token(m1, token) && match_token(m2, token_next) && match_token(m3, token_next_next));
		if(m2 != "")
			return(match_token(m1, token) && match_token(m2, token_next));
		return(match_token(m1, token));
	}

	ASTType* parse_type(string delim1 = "", string delim2 = "")
	{
		auto result = new ASTType(token);
		result->identifier = expect("Identifier")->literal;
		if(match("("))
		{
			// function signature
			result->is_function = true;
			consume();
			while(token && !cancel)
			{
				if(token->text == ")")
				{
					consume();
					return(result);
				}
				auto param = new ASTType(token);
				result->params.push_back(param);
				param->name = expect("Identifier")->literal;
				expect(":");
				if(match("Identifier", "("))
					param->subtype = parse_type();
				else
					param->identifier = expect("Identifier")->literal;
				if(match(")"))
				{
					consume();
					return(result);
				}
				expect(",");
			}
		}
		return(result);
	}

	ASTIf* parse_if()
	{
		auto result = new ASTIf(token);
		expect("(");
		result->condition = parse_expression(")");
		expect(")");
		result->thenblock = parse_statement(";");
		if(match(";")) consume();
		if(match("::else"))
		{
			consume();
			result->elseblock = parse_statement(";");
		}
		return(result);
	}

	ASTCall* parse_call(Token* callee)
	{
		auto result = new ASTCall(token);
		result->identifier = callee->literal;
		expect("(");
		while(token && !cancel)
		{
			result->params.push_back(parse_expression(",", ")"));
			if(match(")"))
			{
				consume();
				break;
			}
			else
			{
				expect(",");
			}
		}
		string block_name = "block";
		while(match("{"))
		{
			consume();
			result->named_blocks[block_name] = parse_block("}");
			expect("}");
			if(match("Identifier", "{"))
			{
				block_name = token->literal;
				consume();
			}
			else
			{
				break;
			}
		}
		return(result);
	}

	ASTNode* parse_expression(string delim = "None", string delim2 = "None")
	{
		if(match("{"))
		{
			consume();
			auto block = parse_block("}");
			expect("}");
			return(block);
		}
		auto result = new ASTExpression(token);
		while(token && !cancel)
		{
			if(match("::if"))
			{
				consume();
				result->items.push_back(parse_if());
			}
			else if(match(delim) || match(delim2))
			{
				if(result->items.size() == 1)
					return(result->items[0]);
				return(result);
			}
			else if(match("("))
			{
				consume();
				result->items.push_back(parse_expression(")"));
				expect(")");
			}
			else if(match("Identifier", "("))
			{
				result->items.push_back(parse_call(expect("Identifier")));
			}
			else if(match("{"))
			{
				consume();
				result->items.push_back(parse_block("}"));
				expect("}");
			}
			else if(token->is_closing)
			{
				error("unexpected", token, "in expression");
				consume();
				return(result);
			}
			else if(match("Identifier"))
			{
				result->items.push_back(new ASTIdentifier(token));
				consume();
			}
			else if(token->type == TPUNCT)
			{
				result->items.push_back(new ASTOperator(token));
				consume();
			}
			else if(match("String"))
			{
				result->items.push_back(new ASTString(token));
				consume();
			}
			else
			{
				error("unexpected", token, "in expression");
				consume();
			}
		}
		if(result->items.size() == 1)
			return(result->items[0]);
		return(result);
	}

	ASTDeclaration* parse_declaration()
	{
		auto result = new ASTDeclaration(token);
		result->identifier = expect("Identifier")->literal;
		expect(":");
		if(match("="))
		{
			result->auto_type = true;
		}
		else
		{
			result->type = parse_type("=", ";");
		}
		if(match("="))
		{
			consume();
			result->value = parse_expression(";");
			return(result);
		}
		if(!match(";"))
		{
			error("unexpected", token, "in declaration");
		}
		return(result);
	}

	ASTAssignment* parse_assignment()
	{
		auto result = new ASTAssignment(token);
		result->identifier = expect("Identifier")->literal;
		expect("=");
		result->value = parse_expression(";");
		return(result);
	}

	ASTNode* parse_statement(string delim = ";")
	{
		if(token && !cancel)
		{
			if(token->text == delim)
			{
				return(new ASTNode(token));
			}
			else if(match("Identifier", ":"))
			{
				return(parse_declaration());
			}
			else if(match("{"))
			{
				consume();
				auto block = parse_block("}");
				expect("}");
				return(block);
			}
			else if(match("Identifier", "="))
			{
				return(parse_assignment());
			}
			else if(token->is_closing)
			{
				error("unexpected", token, "in statement");
				return(new ASTNode(token));
			}
			else
			{
				return(parse_expression(";"));
			}
		}
		return(new ASTNode(token));
	}

	ASTBlock* parse_block(string delim = "None")
	{
		auto result = new ASTBlock(token);
		while(token && !cancel)
		{
			if(token->text == delim)
			{
				return(result);
			}
			else if(match(";"))
			{
				consume();
			}
			else if(token->is_closing)
			{
				error("unexpected", token, "in block");
				return(result);
			}
			else
			{
				result->statements.push_back(parse_statement(";"));
			}
		}
		return(result);
	}

	void parse(Token* token_list)
	{
		token_next = neutral_token = new Token();
		token_list = token_list;
		token = token_list;
		if(token && token->next) token_next = token->next;
		ast_root = new ASTBase(token);
		ast_root->block = parse_block();
	}

	void error(string message, Token* token, string message2 = "")
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
