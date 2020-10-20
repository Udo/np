
typedef Token ASTNode;

struct Parser
{
	ASTNode* ast_root;
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

	ASTNode* expect(string token_type_text, bool do_consume = true)
	{
		if(!token || cancel) return(new ASTNode());
		auto n = new ASTNode();
		n->copy_from(token);
		if(token_type_text != token->text)
		{
			error("expected "+token_type_text+" at", token);
			return(n);
		}
		if(do_consume) consume();
		return(n);
	}

	bool match(string m1, string m2 = "", string m3 = "")
	{
		if(!token || cancel) return(false);
		if(m3 != "")
			return(m1 == token->text && m2 == token_next->text && m3 == token_next_next->text);
		if(m2 != "")
			return(m1 == token->text && m2 == token_next->text);
		return(m1 == token->text);
	}

	ASTNode* parse_type(string delim1 = "", string delim2 = "")
	{
		ASTNode* result = new ASTNode();
		result->location_from(token);
		result->type = TTYPE;
		result->append_child(expect("Identifier"));
		if(match("("))
		{
			// function signature
			consume();
			while(token && !cancel)
			{
				auto param = new ASTNode();
				param->type = TPARAM;
				param->location_from(token);
				result->append_child(param);
				param->append_child(expect("Identifier"));
				expect(":");
				param->append_child(parse_type());
				if(token->text == ")") 
				{
					consume();
					return(result);
				}
				expect(",");
			}
		}
		return(result);
	}

	ASTNode* parse_call()
	{
		ASTNode* result = new ASTNode();
		result->location_from(token);
		result->type = TCALL;
		result->append_child(expect("Identifier"));
		expect("(");
		while(token && !cancel)
		{
			result->append_child(parse_expression(",", ")"));
			if(match(")"))
			{
				consume();
				return(result);
			}
			else
			{
				expect(",");
			}
		}
		return(result);
	}

	ASTNode* parse_expression(string delim = "None", string delim2 = "None")
	{
		if(token->text == "{")
		{
			consume();
			auto block = parse_block("}");
			expect("}");
			return(block);
		}		
		ASTNode* result = new ASTNode();
		result->location_from(token);
		result->type = TEXPRESSION;
		while(token && !cancel)
		{
			if(match(delim) || match(delim2))
			{
				return(result);
			}
			else if(token->text == "(")
			{
				consume();
				result->append_child(parse_expression(")"));
				expect(")");
			}
			else if(match("Identifier", "("))
			{
				result->append_child(parse_call());
			}
			else if(token->is_closing)
			{
				error("unexpected", token, "in expression");
				consume();
				return(result);
			}
			else
			{
				auto n = new ASTNode();
				n->copy_from(token);
				result->append_child(n);
				consume();
			}
		}
		return(result);
	}

	ASTNode* parse_declaration()
	{
		ASTNode* result = new ASTNode();
		result->location_from(token);
		result->type = TDECLARATION;
		result->append_child(expect("Identifier"));
		expect(":");
		result->append_child(parse_type("=", ";"));
		if(token->text == "=")
		{
			consume();
			result->append_child(parse_expression(";"));
			return(result);
		}
		if(token->text == ";")
		{
			return(result);
		}
		else
		{
			error("unexpected", token, "in declaration");
		}
		return(result);
	}

	ASTNode* parse_assignment()
	{
		ASTNode* result = new ASTNode();
		result->location_from(token);
		result->type = TASSIGNMENT;
		result->append_child(expect("Identifier"));
		expect("=");
		result->append_child(parse_expression(";"));
		return(result);
	}

	ASTNode* parse_statement(string delim = ";")
	{
		if(token && !cancel)
		{
			if(token->text == delim)
			{
				return(ASTNode::MakeEmpty(token));
			}
			else if(match("Identifier", ":"))
			{
				return(parse_declaration());
			}
			else if(match("Identifier", "="))
			{
				return(parse_assignment());
			}
			else if(token->is_closing)
			{
				error("unexpected", token, "in statement");
				return(ASTNode::MakeEmpty(token));
			}
			else
			{
				return(parse_expression(";"));
			}
		}
		return(ASTNode::MakeEmpty(token));
	}

	ASTNode* parse_block(string delim = "None")
	{
		ASTNode* result = new ASTNode();
		result->location_from(token);
		result->type = TBLOCK;
		while(token && !cancel)
		{
			if(token->text == delim)
			{
				return(result);
			}
			else if(token->is_closing)
			{
				error("unexpected", token, "in block");
				return(result);
			}
			else
			{
				result->append_child(parse_statement(";"));
				expect(";");
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
		ast_root = parse_block();
	}

	void error(string message, Token* token, string message2 = "")
	{
		if(token->literal != "")
			printf("ERROR: %s \"%s\" at line %i col %i %s\n",
				message.c_str(),
				token->literal.c_str(),
				token->line, token->col,
				message2.c_str());
		else
			printf("ERROR: %s %s at line %i col %i %s\n",
				message.c_str(),
				token->text.c_str(),
				token->line, token->col,
				message2.c_str());
		cancel = true;
	}

};
