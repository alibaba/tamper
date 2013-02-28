##Introduction

homepage:  https://github.com/alibaba/mapping/

wiki :     https://github.com/alibaba/mapping/wiki

author : agapple(jianghang115@gmail.com)

##Why need mapping

这里列觉了几种需要使用mapping的场景：

* model 和 DO的转化 (DO = Data Object , 数据库对象的设计是一种大宽表的设计，domain/model的设计，会有层次结构&具体)。 比如表设计存储会采用json存储动态数据，而在model中会是具体的属性
* model 和 VO的转化 (VO = View Object , 公司的产品detail页面，涉及了后端n多个domain/model的组合展示，这时候会进行包装成VO，包装一些页面组装逻辑)
* model 和 DTO的转化 (DTO = Data Transfer Object ，公司子系统比较多，系统之间会有比较多的rpc等remote调用)
* form -> bean的转化 (现在流行的几个MVC框架，都已经开始支持view层的参数注入，比如@Paramter(name="field")String , @Form("name=xx")Bean)。 提交的form表单数据，基本都是以map+list为主，就会涉及一个mapping

##Why BeanMapping
* 解决BeanUtils, BeanCopier?使用上的局限，只能针对同名属性的拷贝
* 相比于BeanUtils，性能提升是它的优势
* 相比于BeanCopier，类型之间的convertor是它的优势
* 支持插件方式的扩展，自身框架的设计也是基于插件扩展。

###目前的插件支持：
* default value支持
* convetor转换
* script脚本支持 (EL表达式处理)
* bean creator(嵌套对象自动创建)

##Maven repository

TODO: 

##FAQ

1.How to import to eclipse by maven?

      mvn eclipse:eclipse
      
2.How to build project by maven?

      mvn clean package
      
3.How to run testcase by maven?

      mvn test

<h2>Example1：</h2>
<h3>Step 1 (define mapping config)</h3>
<pre name="code" class="java">&lt;bean-mappings xmlns="http://mapping4java.googlecode.com/schema/mapping" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
        xsi:schemaLocation="http://mapping4java.googlecode.com/schema/mapping http://mapping4java.googlecode.com/svn/trunk/src/main/resources/META-INF/mapping.xsd"&gt;  
        &lt;!--  (bean-bean) mapping 测试 --&gt;  
        &lt;bean-mapping batch="true" srcClass="com.agapple.mapping.object.SrcMappingObject" targetClass="com.agapple.mapping.object.TargetMappingObject" reversable="true"&gt;  
            &lt;field-mapping srcName="intValue" targetName="intValue" /&gt;  
            &lt;field-mapping targetName="integerValue" script="src.intValue + src.integerValue" /&gt; &lt;!-- 测试script --&gt;  
            &lt;field-mapping srcName="start" targetName="start" /&gt;  
            &lt;field-mapping srcName="name" targetName="targetName" /&gt; &lt;!--  注意不同名 --&gt;  
            &lt;field-mapping srcName="mapping" targetName="mapping" mapping="true" /&gt;  
        &lt;/bean-mapping&gt;  

        &lt;bean-mapping batch="true" srcClass="com.agapple.mapping.object.NestedSrcMappingObject" targetClass="com.agapple.mapping.object.NestedTargetMappingObject" reversable="true"&gt;  
            &lt;field-mapping srcName="name" targetName="name" defaultValue="ljh" /&gt; &lt;!-- 测试default value --&gt;  
            &lt;field-mapping srcName="bigDecimalValue" targetName="value" targetClass="string" defaultValue="10" /&gt; &lt;!-- 测试不同名+不同类型+default value  --&gt;  
        &lt;/bean-mapping&gt;  

    &lt;/bean-mappings&gt;</pre>
<h3>Step 2 (do mapping) </h3>
<pre name="code" class="java">public BeanMapping srcMapping    = BeanMapping.create(SrcMappingObject.class, TargetMappingObject.class);  
public BeanMapping targetMapping = BeanMapping.create(TargetMappingObject.class , SrcMappingObject.class);  

    @Test  
    public void testBeanToBean_ok() {  
        SrcMappingObject srcRef = new SrcMappingObject();  
        srcRef.setIntegerValue(1);  
        srcRef.setIntValue(1);  
        srcRef.setName("ljh");  
        srcRef.setStart(true);  

        TargetMappingObject targetRef = new TargetMappingObject();// 测试一下mapping到一个Object对象  
        srcMapping.mapping(srcRef, targetRef);  

        SrcMappingObject newSrcRef = new SrcMappingObject();// 反过来再mapping一次  
        targetMapping.mapping(targetRef, newSrcRef);  
    }</pre>
<h2 style="font-size: 1.5em;">Example2： </h2>
<h3>类似于BeanUtils/BeanCopier，根据同名属性进行自动映射，不需要定义任何的mapping.xml</h3>
<pre name="code" class="java">public BeanCopy srcCopy    = BeanCopy.create(SrcMappingObject.class, TargetMappingObject.class);  
    public BeanCopy targetCopy = BeanCopy.create(TargetMappingObject.class , SrcMappingObject.class);  

    @Test  
    public void testBeanToBean_ok() {  
        SrcMappingObject srcRef = new SrcMappingObject();  
        srcRef.setIntegerValue(1);  
        srcRef.setIntValue(1);  
        srcRef.setName("ljh");  
        srcRef.setStart(true);  

        TargetMappingObject targetRef = new TargetMappingObject();// 测试一下mapping到一个Object对象  
        srcCopy.copy(srcRef, targetRef);  

        SrcMappingObject newSrcRef = new SrcMappingObject();// 反过来再mapping一次  
        targetCopy.copy(targetRef, newSrcRef);  
    }</pre>
<h2 style="font-size: 1.5em;">Example3： </h2>
<h3>类似于BeanUtils，处理map&lt;-&gt;bean</h3>
<pre name="code" class="java"><span style="white-space: normal;"> <span style="white-space: pre;">public BeanMap beanMap = BeanMap.create(SrcMappingObject.class);
</span></span>
    @Test
    public void testDescribe_Populate_ok() {
        SrcMappingObject srcRef = new SrcMappingObject();
        srcRef.setIntegerValue(1);
        srcRef.setIntValue(1);
        srcRef.setName("ljh");
        srcRef.setStart(true);

        Map map = beanMap.describe(srcRef);
        
        SrcMappingObject newSrcRef = new SrcMappingObject();// 反过来再mapping一次
        beanMap.populate(newSrcRef, map);
    }</pre>


More information see wiki pages please.
