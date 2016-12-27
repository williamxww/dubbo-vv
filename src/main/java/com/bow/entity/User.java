package com.bow.entity;

import java.io.Serializable;

/**
 * @author vv
 * @since 2016/12/27.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 5553592015705872037L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
