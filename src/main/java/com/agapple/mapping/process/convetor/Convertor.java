package com.agapple.mapping.process.convetor;

/**
 * 自定义的convertor接口
 * 
 * <pre>
 * 不选择现有convertor的理由：
 * BeanUtils 不支持alias别名，必须绑定到具体的对象
 * alibaba convert 支持alias别名，但注册时只是建立class对象和alias之间有一映射关系
 *     
 * 我们的需求：
 * 定义convert，指定对应名字name, 在dsl描述时引用name进行convertor处理
 * </pre>
 * 
 * @author jianghang 2011-5-25 下午10:08:48
 */
public interface Convertor {

    public Object convert(Object src, Class destClass);

    /**
     * 支持多级collection映射，需指定多级的componentClass
     */
    public Object convertCollection(Object src, Class destClass, Class... componentClasses);
}
