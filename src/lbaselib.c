/*
** $Id: lbaselib.c,v 1.276.1.1 2013/04/12 18:48:47 roberto Exp $
** Basic library
** See Copyright Notice in lua.h
*/



#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define lbaselib_c
#define LUA_LIB

#include "lua.h"

#include "lauxlib.h"
#include "lualib.h"
#include "lstring.h"

static int luaB_print (lua_State *L) {
  int n = lua_gettop(L);  /* number of arguments */
  int i;
  lua_getglobal(L, LUA_CONVLIBNAME);
	lua_getfield(L, -1, "toString");
	for (i=1; i<=n; i++) {
    const char *s;
    size_t l;
    lua_pushvalue(L, -1);  /* function to be called */
    lua_pushvalue(L, i);   /* value to print */
    lua_call(L, 1, 1);
    s = lua_tolstring(L, -1, &l);  /* get result */
    if (s == NULL)
      return luaL_error(L,
         LUA_QL("toString") " must return a string to " LUA_QL("print"));
    if (i>1) luai_writestring(" ", 1);
    luai_writestring(s, l);
    lua_pop(L, 1);  /* pop result */
  }
  luai_writeline();
  return 0;
}


#define SPACECHARS	" \f\n\r\t\v"


static int luaB_error (lua_State *L) {
  int level = luaL_optint(L, 2, 1);
  lua_settop(L, 1);
  if (lua_isstring(L, 1) && level > 0) {  /* add extra information? */
    luaL_where(L, level);
    lua_pushvalue(L, 1);
    lua_concat(L, 2);
  }
  return lua_error(L);
}


static int luaB_rawlen (lua_State *L) {
  int t = lua_type(L, 1);
  luaL_argcheck(L, t == LUA_TTABLE || t == LUA_TSTRING, 1,
                   "list or string expected");
  lua_pushinteger(L, lua_rawlen(L, 1, 0));
  return 1;
}


static int luaB_rawalen (lua_State *L) {
  int t = lua_type(L, 1);
  luaL_argcheck(L, t == LUA_TTABLE || t == LUA_TSTRING, 1,
                   "list or string expected");
  lua_pushinteger(L, lua_rawlen(L, 1, 1));
  return 1;
}


static int pairsmeta (lua_State *L, const char *method, int iszero,
                      lua_CFunction iter) {
  //if (!luaL_getmetafield(L, 1, method)) {  /* no metamethod? */
		if(lua_type(L, 1) == LUA_TTABLE) {
	    lua_pushcfunction(L, iter);  /* will return generator, */
	    lua_pushvalue(L, 1);  /* state, */
	    if (iszero) lua_pushinteger(L, 0);  /* and initial value */
	    else lua_pushnil(L);
		}
		else {
			lua_pushnil(L);
		}
 // }
 // else {
 //   lua_pushvalue(L, 1);  /* argument 'self' to metamethod */
 //   lua_call(L, 1, 3);  /* get 3 values from metamethod */
 // }
  return 3;
}


static int luaB_next (lua_State *L) {
  luaL_checktype(L, 1, LUA_TTABLE);
  lua_settop(L, 2);  /* create a 2nd argument if there isn't one */
  if (lua_next(L, 1))
    return 2;
  else {
    lua_pushnil(L);
    return 1;
  }
}


static int luaB_pairs (lua_State *L) {
  return pairsmeta(L, "keys", 0, luaB_next);
}


static int ipairsaux (lua_State *L) {
  int i = luaL_checkint(L, 2);
  luaL_checktype(L, 1, LUA_TTABLE);
  i++;  /* next value */
  lua_pushinteger(L, i);
  lua_rawgeti(L, 1, i);
  return (lua_isnil(L, -1)) ? 1 : 2;
}


static int luaB_ipairs (lua_State *L) {
  return pairsmeta(L, "values", 1, ipairsaux);
}



static int luaB_assert (lua_State *L) {
  if (!lua_toboolean(L, 1))
    return luaL_error(L, "%s", luaL_optstring(L, 2, "assertion failed!"));
  return lua_gettop(L);
}


static int luaB_select (lua_State *L) {
  int n = lua_gettop(L);
  if (lua_type(L, 1) == LUA_TSTRING && *lua_tostring(L, 1) == '#') {
    lua_pushinteger(L, n-1);
    return 1;
  }
  else {
    int i = luaL_checkint(L, 1);
    if (i < 0) i = n + i;
    else if (i > n) i = n;
    luaL_argcheck(L, 1 <= i, 1, "index out of range");
    return n - i;
  }
}


static int finishpcall (lua_State *L, int status) {
  if (!lua_checkstack(L, 1)) {  /* no space for extra boolean? */
    lua_settop(L, 0);  /* create space for return values */
    lua_pushboolean(L, 0);
    lua_pushstring(L, "stack overflow");
    return 2;  /* return false, msg */
  }
  lua_pushboolean(L, status);  /* first result (status) */
  lua_replace(L, 1);  /* put first result in first slot */
  return lua_gettop(L);
}


static int pcallcont (lua_State *L) {
  int status = lua_getctx(L, NULL);
  return finishpcall(L, (status == LUA_YIELD));
}

static int luaB_xpcall (lua_State *L) {
  int status;
  int n = lua_gettop(L);
  luaL_argcheck(L, n >= 2, 2, "value expected");
  lua_pushvalue(L, 1);  /* exchange function... */
  lua_copy(L, 2, 1);  /* ...and error handler */
  lua_replace(L, 2);
  status = lua_pcallk(L, n - 2, LUA_MULTRET, 1, 0, pcallcont);
  return finishpcall(L, (status == LUA_OK));
}

static int luaB_pcall (lua_State *L) {
  int n = lua_gettop(L);
	if(n > 1)
		return luaB_xpcall(L);
  int status;
  luaL_checkany(L, 1);
  lua_pushnil(L);
  lua_insert(L, 1);  /* create space for status result */
  status = lua_pcallk(L, lua_gettop(L) - 2, LUA_MULTRET, 0, 0, pcallcont);
  return finishpcall(L, (status == LUA_OK));
}

static int luaB_type (lua_State *L) {
  luaL_checkany(L, 1);
  lua_pushstring(L, luaL_typename(L, 1));
  return 1;
}

static const luaL_Reg base_funcs[] = {
 // {"toString", luaB_tostring},
  {"assert", luaB_assert},
  //{"dofile", luaB_dofile},
  {"each", luaB_pairs},
  {"raise", luaB_error},
  //{"getmetatable", luaB_getmetatable},
  {"items", luaB_ipairs},
  //{"loadfile", luaB_loadfile},
  {"size", luaB_rawlen},
  {"length", luaB_rawalen},
  {"next", luaB_next},
  {"try", luaB_pcall},
  {"print", luaB_print},
  {"select", luaB_select},
  {"type", luaB_type},
  /*{"xpcall", luaB_xpcall},*/
  {NULL, NULL}
};

static int trap_returnNil (lua_State *L) {
	lua_pushnil(L);
	return(1);
}

static int trap_returnEmpty (lua_State *L) {
	lua_pushstring(L, "");
	return(1);
}

static const luaL_Reg trap_lib[] = {
  {"nilEvent", trap_returnNil},
  {"nilConcat", trap_returnNil},
  {"emptyString", trap_returnEmpty},
  {NULL, NULL}
};


LUAMOD_API int luaopen_base (lua_State *L) {
  /* set global _G */
  lua_pushglobaltable(L);
  lua_pushglobaltable(L);
  lua_setfield(L, -2, "_G");
  /* open lib into global table */
  luaL_setfuncs(L, base_funcs, 0);
	
	lua_createtable(L, 0, 1);
	luaL_setfuncs(L, trap_lib, 0);
	lua_setglobal(L, LUA_POLICYLIBNAME);
	
  return 1;
}


