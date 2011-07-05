package com.agapple.mapping.object.inherit;

import java.math.BigInteger;

/**
 * @author jianghang 2011-6-22 下午03:40:44
 */
public class TwoObject extends FirstObject {

    private static final long serialVersionUID = 5368446589651421075L;
    private String            twoName;
    private BigInteger        twoValue;

    public TwoObject(String fristName, String twoName){
        super(fristName);
        this.twoName = twoName;
    }

    public String getName() {
        return twoName;
    }

    public void setName(String name) {
        this.twoName = name;
    }

    public BigInteger getTwoValue() {
        return twoValue;
    }

    public void setTwoValue(BigInteger twoValue) {
        this.twoValue = twoValue;
    }

}
