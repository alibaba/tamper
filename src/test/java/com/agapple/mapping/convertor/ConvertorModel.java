package com.agapple.mapping.convertor;

import java.math.BigDecimal;

/**
 * @author jianghang 2011-6-21 下午09:49:29
 */
public class ConvertorModel implements Comparable {

    private int        i;
    private Integer    integer;
    private BigDecimal bigDecimal;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    @Override
    public String toString() {
        return "ConvertorModel [bigDecimal=" + bigDecimal + ", i=" + i + ", integer=" + integer + "]";
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof ConvertorModel) {
            return ((ConvertorModel) o).getI();
        }

        return -1;
    }

}
