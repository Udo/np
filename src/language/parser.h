
typedef Token ASTNode;

struct Parser
{
	ASTNode* ast_root;
	Token* token_list;
	Token* token;
	Token* token_next;
	Token* neutral_token;
	bool cancel = false;

	void consume()
	{
		token_next = neutral_token;
		if(token)
		{
			token = token->next;
			if(token && token->next) token_next = token->next;
		}
	}

	ASTNode* expect(TokenType t, string text = "", bool do_consume = true)
	{
		if(cancel) return(0);
		auto n = new ASTNode();
		n->copy_from(token);
		if(token->type != t || (text != "" && text != token->text))
		{
			error("expected", token, TokenTypeNames[t]);
			return(n);
		}
		if(do_consume) consume();
		return(n);
	}

	ASTNode* parse_declaration()
	{
		ASTNode* result = new ASTNode();
		ASTNode* current = 0;
		result->copy_from(token);
		result->text = "";
		result->type = TDECLARATION;
		auto identifier = expect(TIDENTIFIER);
		auto p = expect(TPUNCT, ":");
		auto te = parse_type_expression();
		result->append_child(identifier);
		result->append_child(te);
		if(token && token->type == TPUNCT && token->text == "=")
		{
			auto assign = new ASTNode();
			assign->copy_from(token);
			assign->type = TASSIGNMENT;
			consume();
			auto identifier_copy = new ASTNode();
			identifier_copy->copy_from(identifier);
			assign->append_child(identifier_copy);
			assign->append_child(parse_expression(';'));
			result->next = assign;
		}
		return(result);
	}

	ASTNode* parse_type_expression()
	{
		ASTNode* result = new ASTNode();
		ASTNode* current = 0;
		result->copy_from(token);
		result->text = "";
		result->type = TTYPE;
		auto type_name = expect(TIDENTIFIER);
		result->append_child(type_name);
		if(token && !token->is_closing && !(token->type == TPUNCT && token->text == "="))
		{
			error("unexpected", token, TokenTypeNames[token->type]);
		}
		return(result);
	}

	ASTNode* parse_expression(char delim = ';')
	{
		ASTNode* result = new ASTNode();
		ASTNode* current = 0;
		result->copy_from(token);
		result->text = "";
		result->type = TEXPRESSION;
		while(token && !cancel)
		{
			bool advance = true;
			if(token->type == TCOMMENT)
			{
				consume();
			}
			else if(token->text[0] == delim)
			{
				consume();
				return(result);
			}
			else if(token->is_closing)
			{
				error("unexpected "+token->text, token);
				return(result);
			}
			else if(token->type == TPUNCT && token->text[0] == '(')
			{
				consume();
				result->append_child(parse_expression(')'));
			}
			else if(token->type == TPUNCT && token->text[0] == '[')
			{
				consume();
				result->append_child(parse_expression(']'));
			}
			else if(token->type == TPUNCT && token->text[0] == '{')
			{
				consume();
				result->append_child(parse_statements());
			}
			else if(token->type == TIDENTIFIER && token_next->type == TPUNCT && token_next->text == ":")
			{
				result->append_child(parse_declaration());
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

	ASTNode* parse_statements()
	{
		ASTNode* result = new ASTNode();
		result->type = TBLOCK;
		ASTNode* current = 0;
		while(token && !cancel)
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
		token_next = neutral_token = new Token();
		token_list = token_list;
		token = token_list;
		if(token && token->next) token_next = token->next;
		ast_root = parse_statements();
	}

	void error(string message, Token* token, string message2 = "")
	{
		printf("ERROR: %s %s at line %i col %i\n", message.c_str(), message2.c_str(), token->line, token->col);
		this->cancel = true;
	}

};
