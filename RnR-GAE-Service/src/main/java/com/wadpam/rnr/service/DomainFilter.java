/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.rnr.service;

import com.google.appengine.api.NamespaceManager;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author os
 */
public class DomainFilter implements Filter {
    public static final String PARAM_DOMAIN = "dn";
    public static final String KEY_PREFIX_NAMESPACE = "du.ns.";
    
    static final Logger LOG = LoggerFactory.getLogger(DomainFilter.class);

    private static final Pattern CONTEXT_PATTERN = Pattern.compile("\\A/([^/]+)/([^/]+)");
    private static final Pattern AUTHENTICATE_PATTERN = Pattern.compile("\\A/authenticate");
    
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<String>();
    private static final ThreadLocal<String> DOMAIN = new ThreadLocal<String>();
    
    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final String uri = request.getRequestURI();
        
        CONTEXT.remove();
        DOMAIN.remove();

        // preserve current namespace:
        final String currentNamespace = NamespaceManager.get();
        
        String context = null;
        String domain = null;
        
        // try the /context/domain pattern first:
        Matcher m = CONTEXT_PATTERN.matcher(uri);
        final boolean isDomain = m.find();
        if (isDomain) {
            context = m.group(1);
            domain = m.group(2);
        }
        else {
            // is this an /authenticate request?
            m = AUTHENTICATE_PATTERN.matcher(uri);
            if (m.find()) {
                // set the domain to the parameter value (including null)
                domain = request.getParameter(PARAM_DOMAIN);
            }
        }
        
        try {
            LOG.debug("======= Switching namespace to {}, context is /{}/ ========", domain, context);
                
            if (null != domain) {
                CONTEXT.set(context);
                DOMAIN.set(domain);
                
                NamespaceManager.set(domain);
            }

            chain.doFilter(req, res);

        }
        finally {
            // restore initial namespace:
            NamespaceManager.set(currentNamespace);
            DOMAIN.remove();
        }
    }
    
    @Override
    public void destroy() {
    }
    
    public static final String getContext() {
        return CONTEXT.get();
    }
    
    public static final String getDomain() {
        return DOMAIN.get();
    }
    
}
