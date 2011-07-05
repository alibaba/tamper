package com.agapple.mapping.object.inherit;

import java.io.Serializable;

/**
 * @author jianghang 2011-6-22 下午03:40:07
 */
public class FirstObject implements Serializable {

    private static final long serialVersionUID = 2827426602844138332L;
    private String            firstName;
    private int               firstValue;

    public FirstObject(String name){
        this.firstName = name;
    }

    public String getName() {
        return firstName;
    }

    public void setName(String name) {
        this.firstName = name;
    }

    public int getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(int firstValue) {
        this.firstValue = firstValue;
    }

}
