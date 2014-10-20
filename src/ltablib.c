/*
** $Id: ltablib.c,v 1.65.1.1 2013/04/12 18:48:47 roberto Exp $
** Library for Table Manipulation
** See Copyright Notice in lua.h
*/


#include <stddef.h>
#include <string.h>

#define ltablib_c
#define LUA_LIB

#include "lua.h"

#include "lauxlib.h"
#include "lualib.h"
#include "ltm.h"
#include "lvm.h"
#include "lapi.h"
#include "luaconf.h"
#include "lobject.h"
#include "ltable.h"
#include "lgc.h"

#define aux_getn(L,n)	(luaL_checktype(L, n, LUA_TTABLE), luaL_len(L, n))

static int tbl_max (lua_State *L) {
  lua_Number max = 0;
	int i;
	int isFirst = 1;
  luaL_checktype(L, 1, LUA_TTABLE);

  int n = aux_getn(L, 1);  /* get size of table */
  for (i=1; i <= n; i++) {
		lua_rawgeti(L, 1, i);
    lua_Number v = lua_tonumber(L, -1);
		if (isFirst) {
			max = v;
			isFirst = 0;
		}
    if (v > max) max = v;
		lua_pop(L, 1);
  }

	lua_pushnumber(L, max);
  return 1;
}

static int tbl_min (lua_State *L) {
  lua_Number max = 0;
	int i;
	int isFirst = 1;
  luaL_checktype(L, 1, LUA_TTABLE);

  int n = aux_getn(L, 1);  /* get size of table */
  for (i=1; i <= n; i++) {
		lua_rawgeti(L, 1, i);
    lua_Number v = lua_tonumber(L, -1);
		if (isFirst) {
			max = v;
			isFirst = 0;
		}
    if (v < max) max = v;
		lua_pop(L, 1);
  }

	lua_pushnumber(L, max);
  return 1;
}


static int get_maxn (lua_State *L, int stackIdx) {
  lua_Number max = 0;
  luaL_checktype(L, stackIdx, LUA_TTABLE);
  lua_pushnil(L);  /* first key */
  while (lua_next(L, stackIdx)) {
    lua_pop(L, stackIdx);  /* remove value */
    if (lua_type(L, -1) == LUA_TNUMBER) {
      lua_Number v = lua_tonumber(L, -1);
      if (v > max) max = v;
    }
  }
  return max;
}

static int maxn (lua_State *L) {
	lua_pushnumber(L, get_maxn(L, 1));
	return 1;
}

static int tbl_insert (lua_State *L) {
  int e = aux_getn(L, 1) + 1;  /* first empty element */
  int pos;  /* where to insert new element */
  switch (lua_gettop(L)) {
    case 2: {  /* called with only 2 arguments */
      pos = e;  /* insert new element at the end */
      break;
    }
    case 3: {
      int i;
      pos = luaL_checkint(L, 2);  /* 2nd argument is the position */
			if(pos <= 0) pos = e + pos;
      luaL_argcheck(L, 1 <= pos && pos <= e, 2, "position out of bounds");
      for (i = e; i > pos; i--) {  /* move up elements */
        lua_rawgeti(L, 1, i-1);
        lua_rawseti(L, 1, i);  /* t[i] = t[i-1] */
      }
      break;
    }
    default: {
      return luaL_error(L, "wrong number of arguments to " LUA_QL("insert"));
    }
  }
  lua_rawseti(L, 1, pos);  /* t[pos] = v */
  return 0;
}


static int tbl_pop (lua_State *L) {
  int size = aux_getn(L, 1);
  int pos = luaL_optint(L, 2, size);
	if (pos < 0)
		pos = size + pos +1;
  if (pos != size)  /* validate 'pos' if given */
    luaL_argcheck(L, 1 <= pos && pos <= size + 1, 1, "position out of bounds");
  lua_rawgeti(L, 1, pos);  /* result = t[pos] */
  for ( ; pos < size; pos++) {
    lua_rawgeti(L, 1, pos+1);
    lua_rawseti(L, 1, pos);  /* t[pos] = t[pos+1] */
  }
  lua_pushnil(L);
  lua_rawseti(L, 1, pos);  /* t[pos] = nil */
  return 1;
}

static int tbl_clear (lua_State *L) {
  int size = aux_getn(L, 1);
	int i;
	for(i = 1; i <= size; i++) {
	  lua_pushnil(L);
	  lua_rawseti(L, 1, i);  /* t[pos] = nil */
	}
  return 1;
}

static int tbl_join (lua_State *L) {
  luaL_Buffer b;
  size_t lsep;
  int i, last, actualCount;
	actualCount = -1;
  const char *sep = luaL_optlstring(L, 2, "", &lsep);
  luaL_checktype(L, 1, LUA_TTABLE);
  i = luaL_optint(L, 3, 1);
  last = luaL_opt(L, luaL_checkint, 4, luaL_len(L, 1));
  luaL_buffinit(L, &b);

  for (; i <= last; i++) {
		lua_rawgeti(L, 1, i);
		if (lua_isstring(L, -1)) {
			if(actualCount != -1) luaL_addlstring(&b, sep, lsep);
			actualCount++;
			luaL_addvalue(&b);
		}
  }

  luaL_pushresult(&b);
  return 1;
}


/*
** {======================================================
** Pack/unpack
** =======================================================
*/

static int pack (lua_State *L) {
  int n = lua_gettop(L);  /* number of elements to pack */
  lua_createtable(L, n, 1);  /* create result table */
  lua_pushinteger(L, n);
  lua_setfield(L, -2, "n");  /* t.n = number of elements */
  if (n > 0) {  /* at least one element? */
    int i;
    lua_pushvalue(L, 1);
    lua_rawseti(L, -2, 1);  /* insert first element */
    lua_replace(L, 1);  /* move table into index 1 */
    for (i = n; i >= 2; i--)  /* assign other elements */
      lua_rawseti(L, 1, i);
  }
  return 1;  /* return table */
}


static int unpack (lua_State *L) {
  int i, e, n;
  luaL_checktype(L, 1, LUA_TTABLE);
  i = luaL_optint(L, 2, 1);
  e = luaL_opt(L, luaL_checkint, 3, luaL_len(L, 1));
  if (i > e) return 0;  /* empty range */
  n = e - i + 1;  /* number of elements */
  if (n <= 0 || !lua_checkstack(L, n))  /* n <= 0 means arith. overflow */
    return luaL_error(L, "too many results to unpack");
  lua_rawgeti(L, 1, i);  /* push arg[i] (avoiding overflow problems) */
  while (i++ < e)  /* push arg[i + 1...e] */
    lua_rawgeti(L, 1, i);
  return n;
}

/* }====================================================== */



/*
** {======================================================
** Quicksort
** (based on `Algorithms in MODULA-3', Robert Sedgewick;
**  Addison-Wesley, 1993.)
** =======================================================
*/

static int l_strcmp_coercion (lua_State *L, int o1, int o2) {
  size_t ll, lr;
	const char * l = lua_tolstring(L, o1, &ll);
	const char * r = lua_tolstring(L, o2, &lr);
	//printf("coerc %s,%i %s,%i\n", l, ll, r, lr);
	if(ll == 0) return 1;
	if(lr == 0) return 0; 
  for (;;) {
    int temp = strcoll(l, r);
    if (temp != 0) return temp;
    else {  /* strings are equal up to a `\0' */
      size_t len = strlen(l);  /* index of first `\0' in both strings */
      if (len == lr)  /* r is finished? */
        return (len == ll) ? 0 : 1;
      else if (len == ll)  /* l is finished? */
        return -1;  /* l is smaller than r (because r is not finished) */
      /* both strings longer than `len'; go on comparing (after the `\0') */
      len++;
      l += len; ll -= len; r += len; lr -= len;
    }
  }
}

static int l_numcmp_coercion (lua_State *L, int o1, int o2) {
  lua_Number l = lua_tonumber(L, o1);
	lua_Number r = lua_tonumber(L, o2);
	return(l < r);
}
static void set2 (lua_State *L, int i, int j) {
  lua_rawseti(L, 1, i);
  lua_rawseti(L, 1, j);
}

static int sort_comp (lua_State *L, int a, int b, int sortMode) {
	switch(sortMode)
	{
		case(-1): { // function mode
	    int res;
	    lua_pushvalue(L, 2);
	    lua_pushvalue(L, a-1);  /* -1 to compensate function */
	    lua_pushvalue(L, b-2);  /* -2 to compensate function and `a' */
	    lua_call(L, 2, 1);
	    res = lua_toboolean(L, -1);
	    lua_pop(L, 1);
	    return res;
			break;
		}
		case(1): { // string coercion mode
			StkId o1 = index2addr(L, a);
			StkId o2 = index2addr(L, b);
			if (ttisstring(o1) && ttisstring(o2)) {
  	    return l_strcmp(rawtsvalue(o1), rawtsvalue(o2)) < 0;
			}
			else {
				return l_strcmp_coercion(L, a, b) < 0;
			}
			break;
		}
		case(2): { // number coercion mode
			StkId o1 = index2addr(L, a);
			StkId o2 = index2addr(L, b);
		  if (ttisnumber(o1) && ttisnumber(o2))
		    return luai_numlt(L, nvalue(o1), nvalue(o2));
			else {
				return l_numcmp_coercion(L, a, b);
			}
			break;
		}
		default: { // lua mode
	  	if (lua_isnil(L, a)) return(1);
	  	if (lua_isnil(L, b)) return(0);
			return lua_compare(L, a, b, LUA_OPLT);
			break;
		}
	}
}

static void auxsort (lua_State *L, int l, int u, int sortMode) {
  while (l < u) {  /* for tail recursion */
    int i, j;
    /* sort elements a[l], a[(l+u)/2] and a[u] */
    lua_rawgeti(L, 1, l);
    lua_rawgeti(L, 1, u);
    if (sort_comp(L, -1, -2, sortMode))  /* a[u] < a[l]? */
      set2(L, l, u);  /* swap a[l] - a[u] */
    else
      lua_pop(L, 2);
    if (u-l == 1) break;  /* only 2 elements */
    i = (l+u)/2;
    lua_rawgeti(L, 1, i);
    lua_rawgeti(L, 1, l);
    if (sort_comp(L, -2, -1, sortMode))  /* a[i]<a[l]? */
      set2(L, i, l);
    else {
      lua_pop(L, 1);  /* remove a[l] */
      lua_rawgeti(L, 1, u);
      if (sort_comp(L, -1, -2, sortMode))  /* a[u]<a[i]? */
        set2(L, i, u);
      else
        lua_pop(L, 2);
    }
    if (u-l == 2) break;  /* only 3 elements */
    lua_rawgeti(L, 1, i);  /* Pivot */
    lua_pushvalue(L, -1);
    lua_rawgeti(L, 1, u-1);
    set2(L, i, u-1);
    /* a[l] <= P == a[u-1] <= a[u], only need to sort from l+1 to u-2 */
    i = l; j = u-1;
    for (;;) {  /* invariant: a[l..i] <= P <= a[j..u] */
      /* repeat ++i until a[i] >= P */
      while (lua_rawgeti(L, 1, ++i), sort_comp(L, -1, -2, sortMode)) {
        if (i>=u) luaL_error(L, "invalid order function for sorting");
        lua_pop(L, 1);  /* remove a[i] */
      }
      /* repeat --j until a[j] <= P */
      while (lua_rawgeti(L, 1, --j), sort_comp(L, -3, -1, sortMode)) {
        if (j<=l) luaL_error(L, "invalid order function for sorting");
        lua_pop(L, 1);  /* remove a[j] */
      }
      if (j<i) {
        lua_pop(L, 3);  /* pop pivot, a[i], a[j] */
        break;
      }
      set2(L, i, j);
    }
    lua_rawgeti(L, 1, u-1);
    lua_rawgeti(L, 1, i);
    set2(L, u-1, i);  /* swap pivot (a[u-1]) with a[i] */
    /* a[l..i-1] <= a[i] == P <= a[i+1..u] */
    /* adjust so that smaller half is in [j..i] and larger one in [l..u] */
    if (i-l < u-i) {
      j=l; i=i-1; l=i+2;
    }
    else {
      j=i+1; i=u; u=j-2;
    }
    auxsort(L, j, i, sortMode);  /* call recursively the smaller one */
  }  /* repeat the routine for the larger one */
}

static int tbl_sort (lua_State *L) {
  int n = aux_getn(L, 1);
	int sortMode = 1; // default sort mode is: string
  luaL_checkstack(L, 40, "");  /* assume array is smaller than 2^40 */
  if (!lua_isnoneornil(L, 2)) {
  	if (lua_isfunction(L, 2)) 
			sortMode = -1; // function sort mode
		else {
			const char * sortModeArg = lua_tostring(L, 2);
			if (strcmp(sortModeArg, "string") == 0) sortMode = 1; // string sort mode
			else if (strcmp(sortModeArg, "number") == 0) sortMode = 2; // number sort mode
			else if (strcmp(sortModeArg, "strict") == 0) sortMode = 0; // number sort mode
			else sortMode = 1; // default to lua sort mode
		}
  }
  //  luaL_checktype(L, 2, LUA_TFUNCTION);
  lua_settop(L, 2);  /* make sure there is two arguments */
  auxsort(L, 1, n, sortMode);
	lua_pushvalue(L, 1);
  return 1;
}

static int tbl_copy_helper  (lua_State *L, int srcTable, int destTable, int countIdx) {
  int vTop = lua_gettop(L);
	lua_pushnil(L);  /* first key */
  while (lua_next(L, srcTable)) {
		if (lua_type(L, vTop+1) == LUA_TNUMBER) {
			// if it's a number index, add to the new array
			countIdx++;
			lua_pushnumber(L, countIdx);
		} else {
			// otherwise treat as key index
			lua_pushvalue(L, vTop+1);
		}
		lua_pushvalue(L, vTop+2);
		lua_settable(L, destTable);
		lua_pop(L, 1);
  }
	return countIdx;
}

static int tbl_reverse (lua_State *L) {
	int i = 0;
	luaL_checktype(L, 1, LUA_TTABLE);
	lua_settop(L, 1);

	int n = aux_getn(L, 1);  /* get size of table */
	for (i=1; i <= n; i++) {
		lua_pushnumber(L, 1 + (n*2) - i); // index
		lua_rawgeti(L, 1, i); // value
		lua_settable(L, 1); // insert new entry
	}
	for (i=1; i <= n; i++) {
  	lua_pushnil(L); 
  	lua_rawseti(L, 1, i);  /* t[pos] = nil */
	}
	lua_pushvalue(L, 1);
	return 1;
}

static int tbl_ireverse (lua_State *L) {
	int i = 0;
	luaL_checktype(L, 1, LUA_TTABLE);
	lua_settop(L, 1);

	lua_createtable(L, 0, 0);
	if (lua_getmetatable(L, 1)) {
		lua_setmetatable(L, 2);
	}
	
	int n = aux_getn(L, 1);  /* get size of table */
	for (i=1; i <= n; i++) {
		lua_pushnumber(L, 1+n-i); // index
		lua_rawgeti(L, 1, i); // value
		lua_settable(L, 2);
	}
	lua_pushvalue(L, 2);
	return 1;
}

static int tbl_isort (lua_State *L) {
	luaL_checktype(L, 1, LUA_TTABLE);
  lua_settop(L, 2);  /* make sure there is two arguments */
	lua_createtable(L, 0, 0); // this will be on position 3
	if (lua_getmetatable(L, 1)) {
		lua_setmetatable(L, 3);
	}
	tbl_copy_helper(L, 1, 3, 0); // copy table 1 to 3
	lua_replace(L, 1);
	return(tbl_sort(L));
}

static int tbl_add_helper (lua_State *L, int isImmutable) {
  luaL_checktype(L, 1, LUA_TTABLE);
	lua_settop(L, 2);
	int countIdx = 0;
	int dstTable = 0;
	
	if(isImmutable) {
		// create result table
		lua_createtable(L, 0, 0);
		if (lua_getmetatable(L, 1)) {
			lua_setmetatable(L, 3);
		}
		dstTable = 3;
		countIdx = tbl_copy_helper(L, 1, 3, 0);
	} else {
		dstTable = 1;
	  countIdx = aux_getn(L, 1);
	}	
	
	if (!lua_isnil(L, 2)) {
		lua_pushvalue(L, 2);
		lua_rawseti(L, dstTable, countIdx+1);
	}

  lua_pushvalue(L, dstTable);
  return 1;
}

static int tbl_iadd (lua_State *L) {
	return tbl_add_helper(L, 1);
}

static int tbl_madd (lua_State *L) {
	return tbl_add_helper(L, 0);
}

static int tbl_concat_helper (lua_State *L, int isImmutable) {
  luaL_checktype(L, 1, LUA_TTABLE);
	if(!lua_istable(L, 2))
		return(tbl_add_helper(L, isImmutable));
	lua_settop(L, 2);
	int dstTable;
	int countIdx = 0;
	
	if(isImmutable) {
		// create result table
		lua_createtable(L, 0, 0);
		if (lua_getmetatable(L, 1)) {
			lua_setmetatable(L, 3);
		}
		dstTable = 3;
		countIdx = tbl_copy_helper(L, 1, 3, 0);
	} else {
		dstTable = 1;
	  countIdx = get_maxn(L, 1);
	}
	
	tbl_copy_helper(L, 2, dstTable, countIdx);

  lua_pushvalue(L, dstTable);
  return 1;
}

static int tbl_iconcat (lua_State *L) {
	return tbl_concat_helper(L, 1);
}

static int tbl_mconcat (lua_State *L) {
	return tbl_concat_helper(L, 0);
}

static int tbl_copy (lua_State *L) {
  luaL_checktype(L, 1, LUA_TTABLE);
	lua_settop(L, 1);
	
  // create result table
	lua_createtable(L, 0, 0);
	if (lua_getmetatable(L, 1)) {
		lua_setmetatable(L, 2);
	}
	tbl_copy_helper(L, 1, 2, 0);
	
  lua_pushvalue(L, 2);
  return 1;
}

// todo: this is horrible, see luaH_next for ideas on how to do it with less overhead
static int tbl_each (lua_State *L) {
  int i = 0;
  luaL_checktype(L, 1, LUA_TTABLE);
  luaL_checktype(L, 2, LUA_TFUNCTION);
	lua_settop(L, 2);

  lua_pushnil(L);  /* first key */
  while (lua_next(L, 1)) {
		i++;
		lua_pushvalue(L, 2); // function
		lua_pushvalue(L, 4); // value
		lua_pushvalue(L, 3); // key
		lua_pushnumber(L, i); // numindex
		lua_call(L, 3, 1);
		lua_pop(L, 1); // pop the function result
		lua_pop(L, 1); // pop the hash value
  }
  lua_pushvalue(L, 1);
  return 1;
}

// todo: this is horrible, see luaH_next for ideas on how to do it with less overhead
static int tbl_keys (lua_State *L) {
  luaL_checktype(L, 1, LUA_TTABLE);
	lua_settop(L, 1); // source table at #1
	lua_createtable(L, 0, 0); // new table at #2
	int vTop = lua_gettop(L); // basis for reasoning about loop vars
	int countIdx = 0;
  
	lua_pushnil(L);  /* first key */
  while (lua_next(L, 1)) {
		if (lua_type(L, vTop+1) != LUA_TNUMBER) {
			countIdx += 1;
			lua_pushnumber(L, countIdx);
			lua_pushvalue(L, vTop+1);
			lua_settable(L, 2);
		}
		lua_pop(L, 1);
  }
	
  lua_pushvalue(L, 2); // push result
  return 1;
}

static int tbl_find_constant (lua_State *L) {
  int i = 0;
  lua_pushnil(L);  /* first key */
  while (lua_next(L, 1)) {
		i++;
		if(lua_compare(L, 2, 4, LUA_OPEQ)) {
			lua_pushvalue(L, 3); // return the key
			return 1;
		}
		lua_pop(L, 1); // pop the hash value
  }
  lua_pushnil(L);
	return 1;
}

static int tbl_find_function (lua_State *L) {
  int i = 0;
  lua_pushnil(L);  /* first key */
  while (lua_next(L, 1)) {
		i++;
		lua_pushvalue(L, 2); // function
		lua_pushvalue(L, 4); // value
		lua_pushvalue(L, 3); // key
		lua_pushnumber(L, i); // numindex
		lua_call(L, 3, 1);
		if(!lua_isnil(L, -1)) {
			return 1;
		}
		lua_pop(L, 1); // pop the function result
		lua_pop(L, 1); // pop the hash value
  }
  lua_pushnil(L);
	return 1;
}

// todo: this is horrible, see luaH_next for ideas on how to do it with less overhead
static int tbl_find (lua_State *L) {
  luaL_checktype(L, 1, LUA_TTABLE);
  luaL_checkany(L, 2);
	lua_settop(L, 2);

	if(lua_isfunction(L, 2))
		tbl_find_function(L);
	else
		tbl_find_constant(L);

  return 1;
}

// todo: this is horrible, see luaH_next for ideas on how to do it with less overhead
static int tbl_map (lua_State *L) {
  int i = 0, numIndex = 0;
  luaL_checktype(L, 1, LUA_TTABLE);
  luaL_checktype(L, 2, LUA_TFUNCTION);
	lua_settop(L, 2);
	// create result table
	lua_createtable(L, 0, 0);
	
  lua_pushnil(L);  /* first key */
  while (lua_next(L, 1)) {
		i++;
		lua_pushvalue(L, 2); // function
		lua_pushvalue(L, 5); // value
		lua_pushvalue(L, 4); // key
		lua_pushnumber(L, i); // numindex
		lua_call(L, 3, 2);
		if(!lua_isnil(L, -2)) {
			if(lua_isnil(L, -1) || lua_isnumber(L, -1)) { // if only one value was returned, treat it as a value
				numIndex++;
				lua_pop(L, 1);
				lua_pushnumber(L, numIndex); // and take the original element key as the new key
			}
			lua_insert(L, -2); // switch stack order from value,key to key,value
			lua_settable(L, 3);
		} else {
			lua_pop(L, 2);
		}
		lua_pop(L, 1); // pop the hash value
  }
  lua_pushvalue(L, 3);
	if (lua_getmetatable(L, 1)) {
		lua_setmetatable(L, 3);
	}
  return 1;
}

static int tbl_items (lua_State *L) {
  int i = 0;
  luaL_checktype(L, 1, LUA_TTABLE);
  luaL_checktype(L, 2, LUA_TFUNCTION);
	lua_settop(L, 2);
	
  int n = aux_getn(L, 1);  /* get size of table */
  for (i=1; i <= n; i++) {
		lua_pushvalue(L, 2); // function
		lua_rawgeti(L, 1, i); // value
		lua_pushnumber(L, i); // index
		if (!lua_isnil(L, 4)) {
			lua_call(L, 2, 1);			
			lua_pop(L, 1); // pop the return value
		} else {
			// clean up the stack
			lua_pop(L, 3);
		}
  }
  lua_pushvalue(L, 1);
  return 1;
}

/*static int tbl_toString (lua_State *L) {
	luaL_checktype(L, 1, LUA_TTABLE);
	//int lua_pcall (lua_State *L, int nargs, int nresults, int errfunc);
	if(!luaL_dostring(L, "=> { l | \
  new o = '' \
  l:items{ v | \
    if(type(v) == 'string') { v = convert.format('\%q' v) } else { v = convert.toString(v) } \
	  o <<= v << ' '; \
  } \
  l:each{ v k | \
		if(type(k) != 'number') { \
	    if(type(v) == 'string') { v = convert.format('\%q' v) } else { v = convert.toString(v) } \
			o <<= k << '=' << v << ' ' } \
  } \
  => '(: ' << o << ')' \
}")) {
	  lua_pushvalue(L, 1);
		lua_call(L, 1, 1);
  }
	else {
		lua_pushliteral(L, "[list]");
	}
	return 1;
}*/

static int tbl_size (lua_State *L) {
  int t = lua_type(L, 1);
  luaL_argcheck(L, t == LUA_TTABLE || t == LUA_TSTRING, 1,
                 "list or string expected");
	lua_pushinteger(L, lua_rawlen(L, 1, 0));
	//lua_pushinteger(L, aux_getn(L, 1));
	return 1;
}

static int tbl_keyCount (lua_State *L) {
  int t = lua_type(L, 1);
  luaL_argcheck(L, t == LUA_TTABLE, 1,
                 "list expected");
	lua_pushinteger(L, lua_keycount(L, 1));
	//lua_pushinteger(L, aux_getn(L, 1));
	return 1;
}

static int tbl_reduce (lua_State *L) {
  int i = 0;
  luaL_checktype(L, 1, LUA_TTABLE);
  luaL_checktype(L, 2, LUA_TFUNCTION);
	lua_settop(L, 3);
	if(lua_isnil(L, 3)) { // if no start value is defined
		lua_pushnumber(L, 0); // use 0
		lua_replace(L, 3); 
	}
	
  int n = aux_getn(L, 1);  /* get size of table */
  for (i=1; i <= n; i++) {
		lua_pushvalue(L, 2); // function
		lua_rawgeti(L, 1, i); // value
		if (!lua_isnil(L, -1)) {
			lua_pushvalue(L, 3); // start number
			lua_call(L, 2, 1);			
			lua_replace(L, 3);
		} else {
			lua_pop(L, 2);
		}
  }
  lua_pushvalue(L, 3);
  return 1;
}

static int tbl_create (lua_State *L) {
  // takes a table as an argument
	lua_settop(L, 3);
	if(lua_type(L, 1) == LUA_TTABLE) {
		
  	lua_createtable(L, 0, 0);	
		lua_replace(L, 3);
		
		if(lua_type(L, 2) == LUA_TTABLE) {
	  	// if a data table was given, copy it
			tbl_copy_helper(L, 2, 3, 0); 
		}
		
		lua_pushvalue(L, 1);
		lua_setmetatable(L, 3);
		
	  if (luaL_getmetafield(L, -1, "init")) {  
	    lua_pushvalue(L, -2);  
	    lua_call(L, 1, 1);  
			lua_pop(L, 1);
	  }

		lua_pushvalue(L, 3);
	} 
	else {
		lua_createtable(L, 0, 0);
		lua_replace(L, 2);
		lua_pushvalue(L, 2);
	}
	
  return 1;
}

static int tbl_getevents (lua_State *L) {
  luaL_checkany(L, 1);
  if (!lua_getmetatable(L, 1)) {
    lua_pushnil(L);
    return 1;  /* no metatable */
  }
  luaL_getmetafield(L, 1, "events");
  return 1;  /* returns either __metatable field (if present) or metatable */
}


static int tbl_setevents (lua_State *L) {
  int n = lua_gettop(L);
	if(n == 1)
		return tbl_getevents(L);
  luaL_checktype(L, 1, LUA_TTABLE);
  if (luaL_getmetafield(L, 1, "events"))
    return luaL_error(L, "cannot change a protected events list");
  lua_settop(L, 2);
  lua_setmetatable(L, 1);
  return 1;
}

static int tbl_containsKeys (lua_State *L) {
  int n = lua_gettop(L);
	if(n == 1) {
		lua_pushboolean(L, 0);
		return 1;
	}
  luaL_checktype(L, 1, LUA_TTABLE);
  luaL_checktype(L, 2, LUA_TTABLE);
	Table *h2 = hvalue(L->top - 2);
  lua_settop(L, 2);

	if(equalobj(L, L->top-1, L->top-2)) {
		lua_pushboolean(L, 1);
		return 1;
	}
	
  lua_pushnil(L);  /* first key */
  while (lua_next(L, 2)) {
		const TValue *res = luaH_get(h2, L->top - 2); 
		if(ttisnil(res)) {
			lua_pushboolean(L, 0);
			return 1;
		}
		lua_pop(L, 1); // pop the hash value
  }
	
	lua_pushboolean(L, 1);
  return 1;
}

static int tbl_implements (lua_State *L) {
	lua_settop(L, 2);
	lua_getmetatable(L, 1);
	if(ttisnil(L->top - 1)) {
		lua_pushboolean(L, 0);
		return 1;
	}
	lua_replace(L, 1);
	return tbl_containsKeys(L);
}

static int tbl_can (lua_State *L) {
  luaL_checktype(L, 1, LUA_TTABLE);
	lua_settop(L, 2);
	lua_getmetatable(L, 1);
	if(ttisnil(L->top - 1)) {
		lua_pushboolean(L, 0);
		return 1;
	}
	
	Table *h = hvalue(L->top - 1);
	const TValue *res = luaH_get(h, L->top - 2); 
	if(!ttisfunction(res)) {
		lua_pushboolean(L, 0);
	} else {
		lua_pushboolean(L, 1);
	}
	
	return 1;
}

static int tbl_rawget (lua_State *L) {
  luaL_checktype(L, 1, LUA_TTABLE);
  luaL_checkany(L, 2);
  lua_settop(L, 2);
  lua_rawget(L, 1);
  return 1;
}

static int tbl_rawset (lua_State *L) {
  luaL_checktype(L, 1, LUA_TTABLE);
  luaL_checkany(L, 2);
  luaL_checkany(L, 3);
  lua_settop(L, 3);
  lua_rawset(L, 1);
  return 1;
}



/* }====================================================== */


static const luaL_Reg tab_funcs[] = {
  {"bind", tbl_setevents},
  {"iAdd", tbl_iadd}, // todo: change these to iAdd instead
  {"iConcat", tbl_iconcat},
  {"clear", tbl_clear},
  {"condense", pack},
  {"copy", tbl_copy},
  {"create", tbl_create},
  {"each", tbl_each},
  {"keys", tbl_keys},
  {"expand", unpack},
  {"find", tbl_find},
  {"insert", tbl_insert},  // insert(list, item) or insert(list, pos, item)
  {"items", tbl_items},
  {"join", tbl_join}, // join(seperator) puts all elements together in a string seperated by the seperator
  {"maxIndex", maxn}, // sort-of counts the items by returning the highest numerical index used
  {"map", tbl_map},
  {"max", tbl_max}, // todo: move to math
  {"min", tbl_min}, // todo: move to math
  {"add", tbl_madd}, 
  {"concat", tbl_mconcat},
  {"reduce", tbl_reduce},
  {"pop", tbl_pop},
  {"reverse", tbl_reverse},
  {"containsKeys", tbl_containsKeys},
  {"implements", tbl_implements},
  {"can", tbl_can},
  {"iReverse", tbl_ireverse},
  {"size", tbl_size},
  {"keyCount", tbl_keyCount},
  {"sort", tbl_sort},
  {"iSort", tbl_isort},
  {"get", tbl_rawget},
  {"set", tbl_rawset},
  //{"toString", tbl_toString},
  {NULL, NULL}
};

#ifndef JH_LUA_TYPEMETA
#undef JH_LUA_TABLECLASS
#endif
#if defined(JH_LUA_TABLECLASS)
// it's a shame that we can't use the library as a metatable directly
static void createmetatable (lua_State *L) {
	int vTop = lua_gettop(L);

	lua_createtable(L, 0, 1);  /* table to be type metatable for tables */
  lua_pushvalue(L, -1);      /* copy table */
  lua_settypemt(L, LUA_TTABLE);   /* set table as type metatable for tables */

	// instead of just making an event method (which doesn't work as a hook for VM events)
	tbl_copy_helper(L, vTop, vTop+1, 0);
	
  lua_pop(L, 1);			 /* pop metatable */
}
#endif

#include "inline/list_tostring.h"

LUAMOD_API int luaopen_table (lua_State *L) {
  luaL_newlib(L, tab_funcs);
	luaL_addinlinefunction(L, src_inline_list_tostring_np, "toString", -2);
#if defined(LUA_COMPAT_UNPACK)
  /* _G.unpack = table.unpack */
  lua_getfield(L, -1, "expand");
  lua_setglobal(L, "expand");
#endif
#if defined(JH_LUA_TABLECLASS)
	createmetatable(L);
#endif
	// todo: add "index" pointer to this table
  return 1;
}

