/*
** $Id: lconvlib.c,v 0.1 udo.schroeter@gmail.com
** Conversion library
*/

#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define lconvlib_c
#define LUA_LIB

#include "lua.h"

#include "lauxlib.h"
#include "lualib.h"

#define SPACECHARS	" \f\n\r\t\v"

/* macro to `unsign' a character */
#define uchar(c)	((unsigned char)(c))
#define L_ESC		'%'
#define SPECIALS	"^$*+?.([%-"

static int luaCV_tostring (lua_State *L) {
  luaL_checkany(L, 1);
  luaL_tolstring(L, 1, NULL);
  return 1;
}

static int luaCV_tonumber (lua_State *L) {
	if(luaL_callmeta(L, 1, "toNumber")) {
		return 1;
	}
  else if (lua_isnoneornil(L, 2)) {  /* standard conversion */
    int isnum;
    lua_Number n = lua_tonumberx(L, 1, &isnum);
    if (isnum) {
      lua_pushnumber(L, n);
      return 1;
    }  /* else not a number; must be something */
    luaL_checkany(L, 1);
  }
  else {
    size_t l;
    const char *s = luaL_checklstring(L, 1, &l);
    const char *e = s + l;  /* end point for 's' */
    int base = luaL_checkint(L, 2);
    int neg = 0;
    luaL_argcheck(L, 2 <= base && base <= 36, 2, "base out of range");
    s += strspn(s, SPACECHARS);  /* skip initial spaces */
    if (*s == '-') { s++; neg = 1; }  /* handle signal */
    else if (*s == '+') s++;
    if (isalnum((unsigned char)*s)) {
      lua_Number n = 0;
      do {
        int digit = (isdigit((unsigned char)*s)) ? *s - '0'
                       : toupper((unsigned char)*s) - 'A' + 10;
        if (digit >= base) break;  /* invalid numeral; force a fail */
        n = n * (lua_Number)base + (lua_Number)digit;
        s++;
      } while (isalnum((unsigned char)*s));
      s += strspn(s, SPACECHARS);  /* skip trailing spaces */
      if (s == e) {  /* no invalid trailing characters? */
        lua_pushnumber(L, (neg) ? -n : n);
        return 1;
      }  /* else not a number */
    }  /* else not a number */
  }
  lua_pushnil(L);  /* not a number */
  return 1;
}


/*
** {======================================================
** STRING FORMAT
** =======================================================
*/

/*
** LUA_INTFRMLEN is the length modifier for integer conversions in
** 'string.format'; LUA_INTFRM_T is the integer type corresponding to
** the previous length
*/
#if !defined(LUA_INTFRMLEN)	/* { */
#if defined(LUA_USE_LONGLONG)

#define LUA_INTFRMLEN		"ll"
#define LUA_INTFRM_T		long long

#else

#define LUA_INTFRMLEN		"l"
#define LUA_INTFRM_T		long

#endif
#endif				/* } */


/*
** LUA_FLTFRMLEN is the length modifier for float conversions in
** 'string.format'; LUA_FLTFRM_T is the float type corresponding to
** the previous length
*/
#if !defined(LUA_FLTFRMLEN)

#define LUA_FLTFRMLEN		""
#define LUA_FLTFRM_T		double

#endif


/* maximum size of each formatted item (> len(format('%99.99f', -1e308))) */
#define MAX_ITEM	512
/* valid flags in a format specification */
#define FLAGS	"-+ #0"
/*
** maximum size of each format specification (such as '%-099.99d')
** (+10 accounts for %99.99x plus margin of error)
*/
#define MAX_FORMAT	(sizeof(FLAGS) + sizeof(LUA_INTFRMLEN) + 10)


static void addquoted (lua_State *L, luaL_Buffer *b, int arg) {
  size_t l;
  const char *s = luaL_checklstring(L, arg, &l);
  luaL_addchar(b, '"');
  while (l--) {
		switch(*s) {
			case '\n': { luaL_addchar(b, '\\'); luaL_addchar(b, 'n'); break; }
			case '\r': { luaL_addchar(b, '\\'); luaL_addchar(b, 'r'); break; }
			case '"': { luaL_addchar(b, '\\'); luaL_addchar(b, '"'); break; }
			case '\\': { luaL_addchar(b, '\\'); luaL_addchar(b, '\\'); break; }
			case '/': { luaL_addchar(b, '\\'); luaL_addchar(b, '/'); break; }
			case '\b': { luaL_addchar(b, '\\'); luaL_addchar(b, 'b'); break; }
			case '\f': { luaL_addchar(b, '\\'); luaL_addchar(b, 'f'); break; }
			case '\t': { luaL_addchar(b, '\\'); luaL_addchar(b, 't'); break; }
			default: {
				if (*s == '\0' || iscntrl(uchar(*s))) {
		      char buff[10];
        	sprintf(buff, "\\u%04x", (int)uchar(*s));
		      luaL_addstring(b, buff);
		    }
		    else
		      luaL_addchar(b, *s);
			}
		}
    s++;
  }
  luaL_addchar(b, '"');
}

static void addunquote (lua_State *L, luaL_Buffer *b, int arg) {
  size_t l, ol;
  const char *s = luaL_checklstring(L, arg, &l);
	ol = l-1;
	char prevChar = ' ';
  while (l--) {
		if((l == ol || l == 0) && *s == '"') {
			// ignore leading and trailing quotation mark
		}
		else if(prevChar == '\\') {
			// previous character was an escape marker
			switch(*s) {
				case 'n': { luaL_addchar(b, '\n'); break; }
				case 'r': { luaL_addchar(b, '\r'); break; }
				case '"': { luaL_addchar(b, '"'); break; }
				case '\\': { luaL_addchar(b, '\\'); break; }
				case 'b': { luaL_addchar(b, '\b'); break; }
				case 'f': { luaL_addchar(b, '\f'); break; }
				case 't': { luaL_addchar(b, '\t'); break; }
				case 'a': { luaL_addchar(b, '\a'); break; }
				case 'v': { luaL_addchar(b, '\v'); break; }
				case '?': { luaL_addchar(b, '\?'); break; }
				case '\'': { luaL_addchar(b, '\''); break; }
				case 'u': {
					int ch;
					sscanf(s, "u%04x", &ch);
					luaL_addchar(b, ch);
					s += 4; l -= 4;
					break;
				}
			}
		} else {
			if(*s != '\\') luaL_addchar(b, *s);
		}
		prevChar = *s;
    s++;
  }
}


static const char *scanformat (lua_State *L, const char *strfrmt, char *form) {
  const char *p = strfrmt;
  while (*p != '\0' && strchr(FLAGS, *p) != NULL) p++;  /* skip flags */
  if ((size_t)(p - strfrmt) >= sizeof(FLAGS)/sizeof(char))
    luaL_error(L, "invalid format (repeated flags)");
  if (isdigit(uchar(*p))) p++;  /* skip width */
  if (isdigit(uchar(*p))) p++;  /* (2 digits at most) */
  if (*p == '.') {
    p++;
    if (isdigit(uchar(*p))) p++;  /* skip precision */
    if (isdigit(uchar(*p))) p++;  /* (2 digits at most) */
  }
  if (isdigit(uchar(*p)))
    luaL_error(L, "invalid format (width or precision too long)");
  *(form++) = '%';
  memcpy(form, strfrmt, (p - strfrmt + 1) * sizeof(char));
  form += p - strfrmt + 1;
  *form = '\0';
  return p;
}


/*
** add length modifier into formats
*/
static void addlenmod (char *form, const char *lenmod) {
  size_t l = strlen(form);
  size_t lm = strlen(lenmod);
  char spec = form[l - 1];
  strcpy(form + l - 1, lenmod);
  form[l + lm - 1] = spec;
  form[l + lm] = '\0';
}


static int str_format (lua_State *L) {
  int top = lua_gettop(L);
  int arg = 1;
  size_t sfl;
  const char *strfrmt = luaL_checklstring(L, arg, &sfl);
  const char *strfrmt_end = strfrmt+sfl;
  luaL_Buffer b;
  luaL_buffinit(L, &b);
  while (strfrmt < strfrmt_end) {
    if (*strfrmt != L_ESC)
      luaL_addchar(&b, *strfrmt++);
    else if (*++strfrmt == L_ESC)
      luaL_addchar(&b, *strfrmt++);  /* %% */
    else { /* format item */
      char form[MAX_FORMAT];  /* to store the format (`%...') */
      char *buff = luaL_prepbuffsize(&b, MAX_ITEM);  /* to put formatted item */
      int nb = 0;  /* number of bytes in added item */
      if (++arg > top)
        luaL_argerror(L, arg, "no value");
      strfrmt = scanformat(L, strfrmt, form);
      switch (*strfrmt++) {
        case 'c': {
          nb = sprintf(buff, form, luaL_checkint(L, arg));
          break;
        }
        case 'd': case 'i': {
          lua_Number n = luaL_checknumber(L, arg);
          LUA_INTFRM_T ni = (LUA_INTFRM_T)n;
          lua_Number diff = n - (lua_Number)ni;
          luaL_argcheck(L, -1 < diff && diff < 1, arg,
                        "not a number in proper range");
          addlenmod(form, LUA_INTFRMLEN);
          nb = sprintf(buff, form, ni);
          break;
        }
        case 'o': case 'u': case 'x': case 'X': {
          lua_Number n = luaL_checknumber(L, arg);
          unsigned LUA_INTFRM_T ni = (unsigned LUA_INTFRM_T)n;
          lua_Number diff = n - (lua_Number)ni;
          luaL_argcheck(L, -1 < diff && diff < 1, arg,
                        "not a non-negative number in proper range");
          addlenmod(form, LUA_INTFRMLEN);
          nb = sprintf(buff, form, ni);
          break;
        }
        case 'e': case 'E': case 'f':
#if defined(LUA_USE_AFORMAT)
        case 'a': case 'A':
#endif
        case 'g': case 'G': {
          addlenmod(form, LUA_FLTFRMLEN);
          nb = sprintf(buff, form, (LUA_FLTFRM_T)luaL_checknumber(L, arg));
          break;
        }
        case 'q': {
          addquoted(L, &b, arg);
          break;
        }
        case 's': {
          size_t l;
          const char *s = luaL_tolstring(L, arg, &l);
          if (!strchr(form, '.') && l >= 100) {
            /* no precision and string is too long to be formatted;
               keep original string */
            luaL_addvalue(&b);
            break;
          }
          else {
            nb = sprintf(buff, form, s);
            lua_pop(L, 1);  /* remove result from 'luaL_tolstring' */
            break;
          }
        }
        default: {  /* also treat cases `pnLlh' */
          return luaL_error(L, "invalid option " LUA_QL("%%%c") " to "
                               LUA_QL("format"), *(strfrmt - 1));
        }
      }
      luaL_addsize(&b, nb);
    }
  }
  luaL_pushresult(&b);
  return 1;
}

static int luaCV_quote (lua_State *L) {
  luaL_Buffer b;
  luaL_buffinit(L, &b);
	addquoted(L, &b, 1);
  luaL_pushresult(&b);
  return 1;
}

static int luaCV_unquote (lua_State *L) {
  luaL_Buffer b;
  luaL_buffinit(L, &b);
	addunquote(L, &b, 1);
  luaL_pushresult(&b);
  return 1;
}

static int luaCV_tokenize (lua_State *L) {
  size_t l;
	int pos = 1;
  const char *s = luaL_checklstring(L, 1, &l);
	lua_createtable(L, 0, 1);

  while (l--) {
		
		if(isalpha(*s)) {
			lua_pushlstring(L, s, 1);
			lua_rawseti(L, 2, pos++);
		}
		
		s++;
  }
	
  return 1;
}

static const luaL_Reg conv_funcs[] = {
  {"toNumber", luaCV_tonumber},
  {"toString", luaCV_tostring},
  {"format", str_format},
	{"quote", luaCV_quote},
	{"unquote", luaCV_unquote},
	{"tokenize", luaCV_tokenize},
	{NULL, NULL}
};


LUAMOD_API int luaopen_conv (lua_State *L) {
  luaL_newlib(L, conv_funcs);
  return 1;
}