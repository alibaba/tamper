package com.agapple.mapping.process.convetor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

/**
 * convert转化helper类，注册一些默认的convertor
 * 
 * @author jianghang 2011-5-20 下午04:44:38
 */
public class ConvertorHelper {

    public static final String              ALIAS_DATE_TIME_TO_STRING     = StringAndDateConvertor.DateTimeToString.class.getSimpleName();
    public static final String              ALIAS_DATE_DAY_TO_STRING      = StringAndDateConvertor.DateDayToString.class.getSimpleName();
    public static final String              ALIAS_STRING_TO_DATE_TIME     = StringAndDateConvertor.StringToDateTime.class.getSimpleName();
    public static final String              ALIAS_STRING_TO_DATE_DAY      = StringAndDateConvertor.StringToDateDay.class.getSimpleName();
    public static final String              ALIAS_CALENDAR_TIME_TO_STRING = StringAndDateConvertor.CalendarTimeToString.class.getSimpleName();
    public static final String              ALIAS_CALENDAR_DAY_TO_STRING  = StringAndDateConvertor.CalendarDayToString.class.getSimpleName();
    public static final String              ALIAS_STRING_TO_CALENDAR_TIME = StringAndDateConvertor.StringToCalendarTime.class.getSimpleName();
    public static final String              ALIAS_STRING_TO_CALENDAR_DAY  = StringAndDateConvertor.StringToCalendarDay.class.getSimpleName();

    // ommon对象范围：8种Primitive和对应的Java类型，BigDecimal, BigInteger
    private static Map<Class, Object>       commonTypes                   = new HashMap<Class, Object>();
    private static final Convertor          stringToCommon                = new StringAndCommonConvertor.StringToCommon();
    private static final Convertor          commonToCommon                = new CommonAndCommonConvertor.CommonToCommon();
    private static final Convertor          arrayToArray                  = new CollectionAndCollectionConvertor.ArrayToArray();
    private static final Convertor          arrayToCollection             = new CollectionAndCollectionConvertor.ArrayToCollection();
    private static final Convertor          collectionToArray             = new CollectionAndCollectionConvertor.CollectionToArray();
    private static final Convertor          collectionToCollection        = new CollectionAndCollectionConvertor.CollectionToCollection();
    private static final Convertor          objectToString                = new StringAndObjectConvetor.ObjectToString();

    private static volatile ConvertorHelper singleton                     = null;

    private ConvertorRepository             repository                    = null;

    public ConvertorHelper(){
        repository = new ConvertorRepository();
        initDefaultRegister();
    }

    public ConvertorHelper(ConvertorRepository repository){
        // 允许传入自定义仓库
        this.repository = repository;
        initDefaultRegister();
    }

    /**
     * 单例方法
     */
    public static ConvertorHelper getInstance() {
        if (singleton == null) {
            synchronized (ConvertorHelper.class) {
                if (singleton == null) { // double check
                    singleton = new ConvertorHelper();
                }
            }
        }
        return singleton;
    }

    /**
     * 根据class获取对应的convertor
     * 
     * @return
     */
    public Convertor getConvertor(Class src, Class dest) {
        if (src == dest) {
            // 对相同类型的直接忽略，不做转换
            return null;
        }

        // 按照src->dest来取映射
        Convertor convertor = repository.getConvertor(src, dest);

        // 处理下Array|Collection的映射
        // 如果src|dest是array类型，取一下Array.class的映射，因为默认数组处理的注册直接注册了Array.class
        boolean isSrcArray = src.isArray();
        boolean isDestArray = dest.isArray();
        if (convertor == null && src.isArray() && dest.isArray()) {
            convertor = arrayToArray;
        } else {
            boolean isSrcCollection = Collection.class.isAssignableFrom(src);
            boolean isDestCollection = Collection.class.isAssignableFrom(dest);
            if (convertor == null && isSrcArray && isDestCollection) {
                convertor = arrayToCollection;
            }
            if (convertor == null && isDestArray && isSrcCollection) {
                convertor = collectionToArray;
            }
            if (convertor == null && isSrcCollection && isDestCollection) {
                convertor = collectionToCollection;
            }
        }

        // 如果dest是string，获取一下object->string. (系统默认注册了一个Object.class -> String.class的转化)
        if (convertor == null && dest == String.class) {
            convertor = objectToString;
        }

        // 如果是其中一个是String类型，另一个是Common类型，进行特殊处理
        if (convertor == null && src == String.class && commonTypes.containsKey(dest)) {
            convertor = stringToCommon;
        }

        // 如果src/dest都是Common类型，进行特殊处理
        if (convertor == null && commonTypes.containsKey(src) && commonTypes.containsKey(dest)) {
            convertor = commonToCommon;
        }

        return convertor;
    }

    /**
     * 根据alias获取对应的convertor
     * 
     * @return
     */
    public Convertor getConvertor(String alias) {
        return repository.getConvertor(alias);
    }

    /**
     * 注册class对应的convertor
     */
    public void registerConvertor(Class src, Class dest, Convertor convertor) {
        repository.registerConvertor(src, dest, convertor);
    }

    /**
     * 注册alias对应的convertor
     */
    public void registerConvertor(String alias, Convertor convertor) {
        repository.registerConvertor(alias, convertor);
    }

    // ======================= register处理 ======================

    public void initDefaultRegister() {
        initCommonTypes();
        StringDateRegister();
    }

    private void StringDateRegister() {
        // 注册string<->date对象处理
        Convertor stringToDateDay = new StringAndDateConvertor.StringToDateDay();
        Convertor stringToDateTime = new StringAndDateConvertor.StringToDateTime();
        Convertor stringToCalendarDay = new StringAndDateConvertor.StringToCalendarDay();
        Convertor stringToCalendarTime = new StringAndDateConvertor.StringToCalendarTime();
        Convertor dateDayToString = new StringAndDateConvertor.DateDayToString();
        Convertor dateTimeToString = new StringAndDateConvertor.DateTimeToString();
        Convertor calendarDayToString = new StringAndDateConvertor.CalendarDayToString();
        Convertor calendarTimeToString = new StringAndDateConvertor.CalendarTimeToString();
        // 注册默认的String <-> Date的处理
        repository.registerConvertor(String.class, Date.class, stringToDateTime);
        repository.registerConvertor(Date.class, String.class, dateTimeToString);
        repository.registerConvertor(String.class, Calendar.class, stringToCalendarTime);
        repository.registerConvertor(Calendar.class, String.class, calendarTimeToString);
        // 注册为别名
        repository.registerConvertor(ALIAS_STRING_TO_DATE_DAY, stringToDateDay);
        repository.registerConvertor(ALIAS_STRING_TO_DATE_TIME, stringToDateTime);
        repository.registerConvertor(ALIAS_STRING_TO_CALENDAR_DAY, stringToCalendarDay);
        repository.registerConvertor(ALIAS_STRING_TO_CALENDAR_TIME, stringToCalendarTime);
        repository.registerConvertor(ALIAS_DATE_DAY_TO_STRING, dateDayToString);
        repository.registerConvertor(ALIAS_DATE_TIME_TO_STRING, dateTimeToString);
        repository.registerConvertor(ALIAS_CALENDAR_DAY_TO_STRING, calendarDayToString);
        repository.registerConvertor(ALIAS_CALENDAR_TIME_TO_STRING, calendarTimeToString);
    }

    private void initCommonTypes() {
        commonTypes.put(int.class, ObjectUtils.NULL);
        commonTypes.put(Integer.class, ObjectUtils.NULL);
        commonTypes.put(short.class, ObjectUtils.NULL);
        commonTypes.put(Short.class, ObjectUtils.NULL);
        commonTypes.put(long.class, ObjectUtils.NULL);
        commonTypes.put(Long.class, ObjectUtils.NULL);
        commonTypes.put(boolean.class, ObjectUtils.NULL);
        commonTypes.put(Boolean.class, ObjectUtils.NULL);
        commonTypes.put(byte.class, ObjectUtils.NULL);
        commonTypes.put(Byte.class, ObjectUtils.NULL);
        commonTypes.put(char.class, ObjectUtils.NULL);
        commonTypes.put(Character.class, ObjectUtils.NULL);
        commonTypes.put(float.class, ObjectUtils.NULL);
        commonTypes.put(Float.class, ObjectUtils.NULL);
        commonTypes.put(double.class, ObjectUtils.NULL);
        commonTypes.put(Double.class, ObjectUtils.NULL);
        commonTypes.put(BigDecimal.class, ObjectUtils.NULL);
        commonTypes.put(BigInteger.class, ObjectUtils.NULL);
    }

    // ========================= setter / getter ===================

    public void setRepository(ConvertorRepository repository) {
        this.repository = repository;
    }
}
