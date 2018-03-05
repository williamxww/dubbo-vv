package com.bow.http.demo;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * @author wwxiang
 * @since 2018/3/5.
 */
public class SimpleServletConfig implements ServletConfig {
    private final ServletContext servletContext;

    public SimpleServletConfig(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public String getServletName() {
        return "DispatcherServlet";
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public String getInitParameter(String s) {
        return null;
    }

    public Enumeration getInitParameterNames() {
        return new Enumeration() {
            public boolean hasMoreElements() {
                return false;
            }

            public Object nextElement() {
                return null;
            }
        };
    }
}
