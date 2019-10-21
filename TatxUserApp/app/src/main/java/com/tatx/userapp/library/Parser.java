package com.tatx.userapp.library;

import java.util.List;

//. by Haseem Saheed
public interface Parser {
    List<Route> parse() throws RouteException;
}