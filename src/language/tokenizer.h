#include <cctype>
#include <map>

enum TokenType {
	TNONE, TIDENTIFIER, TPUNCT, TCOMMENT,
	TSTRINGLITERAL, TEXPRESSION, TBLOCK, TPARAM,
	TDECLARATION, TASSIGNMENT, TTYPE, TCALL,
	TCALLEE, TVALUE, TSIGNATURE, TUNIT,
	TIF, TELSE, TTHEN,
};

char* TokenTypeNames[] = {
	"None", "Ident", "Punct", "Comment",
	"String", "Expr", "Block", "Param",
	"Decl", "Assign", "Type", "Call",
	"Callee", "Value", "Signature", "Unit",
	"If", "Else", "Then",
};

struct Token;

struct ScopeEntity
{
	Token* definition = 0;
};

struct Scope
{
	Scope* parent = 0;
	std::map<string, ScopeEntity> items;

	ScopeEntity* add(string label)
	{
		ScopeEntity se;
		items[label] = se;
		return(&items[label]);
	}

	bool contains(string label)
	{
		return(items.count(label) != 0);
	}
};

struct Token
{
	int type;
	int start;
	int col;
	int line;
	string text = "";
	string literal = "";
	std::vector<int> tags;
	Token* prev = 0;
	Token* next = 0;
	Token* child = 0;
	Token* parent = 0;
	
	Scope* scope = 0;
	int child_count = 0;
	char delim = 0;
	bool is_closing = false;

	static Token* MakeEmpty(Token* token)
	{
		auto result = new Token();
		result->location_from(token);
		return(result);
	}

	Token* apply_tag(int tag_id)
	{
		if(!is(tag_id))
			tags.push_back(tag_id);
		return(this);
	}

	bool is(int tag_query)
	{
		for(auto titem : tags)
			if(tag_query == titem) return(true);
		return(false);
	}

	void print(bool all = false, string level = "")
	{
		string tagn = "";
		for(auto titem : tags)
			tagn += string(TokenTypeNames[titem])+" ";
		if(tags.size() == 0)
			tagn += string("\u001b[35m")+TokenTypeNames[type];
		string ds = text;
		if(literal != "")
			ds = "\""+literal+"\"";
		if(this)
		{
			printf("%s\u001b[32m%s \u001b[34m%i:%i \u001b[33m%s\u001b[0m\n", level.c_str(),
				tagn.c_str(),
				this->col,
				this->line,
				ds.c_str());
			if(scope)
			{
				string scope_labels = "";
				for(auto se : scope->items)
					scope_labels += se.first+" ";
				printf("%s  \u001b[36m[ %s]\u001b[0m\n", level.c_str(),
					scope_labels.c_str()
					);
			}
		}
		else
			return;
		if(this->child) this->child->print(true, level+"  ");
		if(all && this->next) this->next->print(true, level);
	}

	void copy_from(Token* from)
	{
		this->start = from->start;
		this->col = from->col;
		this->line = from->line;
		this->text = from->text;
		this->literal = from->literal;
		this->delim = from->delim;
		this->apply_tag(from->type);
	}

	void location_from(Token* from)
	{
		this->start = from->start;
		this->col = from->col;
		this->line = from->line;
	}

	void append_child(Token* c)
	{
		child_count++;
		c->parent = this;
		if(!this->child)
		{
			this->child = c;
		}
		else
		{
			auto tn = this->child;
			while(tn->next) tn = tn->next;
			tn->next = c;
		}
	}

};

Token* tokenize(string src)
{
	Token* token_list = new Token();
	Token* current_token = token_list;
	int len = src.length();
	int col_ctr = 0;
	int line_ctr = 1;
	for(int i = 0; i < len; i++)
	{
		char c = src[i];
		char nc = src[i+1];
		int cmode = TNONE;
		char delim = 0;
		if(isalnum(c)) cmode = TIDENTIFIER;
		if(ispunct(c)) cmode = TPUNCT;
		if(c == '_') cmode = TIDENTIFIER;
		if(c == '/' && nc == '/') cmode = TCOMMENT;
		if(current_token->type == TCOMMENT)
		{
			if(c != '\n') cmode = TCOMMENT;
		}
		else if(current_token->type == TSTRINGLITERAL)
		{
			if(c != current_token->delim) cmode = TSTRINGLITERAL;
			else cmode = TNONE;
		}
		else
		{
			if(c == '"' || c == '\'')
			{
				delim = c;
				c = 0;
				cmode = TSTRINGLITERAL;
			}
		}

		col_ctr++;
		if(c == '\n')
		{
			col_ctr = 0;
			line_ctr += 1;
		}

		if((current_token->type != TNONE) && (cmode != current_token->type || cmode == TPUNCT))
		{
			if(current_token->type == TSTRINGLITERAL)
			{
				current_token->literal = current_token->text;
				current_token->text = "String";
			}
			else if(current_token->type == TIDENTIFIER)
			{
				current_token->literal = current_token->text;
				current_token->text = "Identifier";
			}
			if(current_token->type == TCOMMENT)
			{
				current_token->start = i;
				current_token->type = TNONE;
				current_token->text = "";
				current_token->literal = "";
			}
			else
			{
				Token* next_token = new Token();
				next_token->prev = current_token;
				current_token->next = next_token;
				current_token = next_token;
				current_token->start = i;
			}
		}

		if(cmode != TNONE)
		{
			current_token->type = cmode;
			if(c > 0) current_token->text += c;
			if(delim > 0) current_token->delim = delim;
			if(!current_token->line)
			{
				current_token->col = col_ctr;
				current_token->line = line_ctr;
			}
		}

		if(current_token->type == TPUNCT)
		{
			current_token->is_closing = (current_token->text == ")" ||
				current_token->text == "}" ||
				current_token->text == "]" ||
				current_token->text == ";");
		}

	}
	return(token_list);
}

// typedef Token ASTNode;
