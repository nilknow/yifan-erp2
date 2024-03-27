package com.nilknow.yifanerp2.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.*;

/**
 * We need this to make HttpServletRequest mutable to add new header
 */
final class MutableHttpServletRequest extends HttpServletRequestWrapper {
    private final Map<String, String> headers;

    public MutableHttpServletRequest(HttpServletRequest request){
        super(request);
        this.headers = new HashMap<>();
    }

    public void putHeader(String name, String value){
        this.headers.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        // check the custom headers first
        String headerValue = headers.get(name);

        if (headerValue != null){
            return headerValue;
        }
        // else return from into the original wrapped object
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        // create a set of the custom header names
        Set<String> set = new HashSet<>(headers.keySet());

        // now add the headers from the wrapped request object
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            // add the names of the request headers into the list
            String n = e.nextElement();
            set.add(n);
        }

        // create an enumeration from the set and return
        return Collections.enumeration(set);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        Set<String> set = new HashSet<>();
        Optional.ofNullable(headers.get(name)).ifPresent(set::add);
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaders(name);
        while (e.hasMoreElements()) {
            // add the names of the request headers into the list
            String n = e.nextElement();
            set.add(n);
        }
        Optional.ofNullable(headers.get(name)).ifPresent(set::add);
        return Collections.enumeration(set);
    }
}