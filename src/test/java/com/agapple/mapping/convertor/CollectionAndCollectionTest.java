package com.agapple.mapping.convertor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import junit.framework.TestCase;

import org.junit.Test;

import com.agapple.mapping.process.convetor.Convertor;
import com.agapple.mapping.process.convetor.ConvertorHelper;

/**
 * @author jianghang 2011-6-21 下午09:43:46
 */
public class CollectionAndCollectionTest extends TestCase {

    private ConvertorHelper helper = new ConvertorHelper();

    @Test
    public void testArrayToList() {
        Convertor intList = helper.getConvertor(int[].class, List.class);
        Convertor integerList = helper.getConvertor(Integer[].class, List.class);

        int[] intArray = new int[] { 1, 2 };
        Integer[] integerArray = new Integer[] { 1, 2 };
        List intListValue = (List) intList.convert(intArray, List.class);
        List integerListValue = (List) integerList.convert(integerArray, List.class);
        assertEquals(intListValue.size(), intArray.length);
        assertEquals(intListValue.get(0), intArray[0]);
        assertEquals(integerListValue.size(), integerArray.length);
        assertEquals(integerListValue.get(0), integerArray[0]);
        // 测试不同类型转化, common对象
        List<BigInteger> intListValueOther = (List) intList.convertCollection(intArray, List.class, BigInteger.class); // int强制转为BigInteger
        List<BigDecimal> integerListValueOther = (List) intList.convertCollection(intArray, List.class,
                                                                                  BigDecimal.class); // int强制转为BigDecimal
        assertEquals(intListValueOther.size(), intArray.length);
        assertEquals(intListValueOther.get(0).intValue(), intArray[0]);
        assertEquals(integerListValueOther.size(), integerArray.length);
        assertEquals(integerListValueOther.get(0).intValue(), integerArray[0].intValue());

        // BigDecimal & BigInteger
        Convertor bigDecimalList = helper.getConvertor(BigDecimal[].class, ArrayList.class);
        Convertor bigIntegerList = helper.getConvertor(BigInteger[].class, Vector.class);

        BigDecimal[] bigDecimalArray = new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ONE };
        BigInteger[] bigIntegerArray = new BigInteger[] { BigInteger.ZERO, BigInteger.ONE };
        List bigDecimalListValue = (List) bigDecimalList.convert(bigDecimalArray, ArrayList.class);
        List bigIntegerListValue = (List) bigIntegerList.convert(bigIntegerArray, Vector.class);
        assertEquals(bigDecimalListValue.size(), bigDecimalArray.length);
        assertEquals(bigDecimalListValue.get(0), bigDecimalArray[0]);
        assertEquals(bigIntegerListValue.size(), bigIntegerArray.length);
        assertEquals(bigIntegerListValue.get(0), bigIntegerArray[0]);

        // Object Array
        Convertor modelList = helper.getConvertor(ConvertorModel[].class, LinkedList.class);
        ConvertorModel[] modelArray = new ConvertorModel[] { new ConvertorModel(), new ConvertorModel() };
        List modelListValue = (List) modelList.convert(modelArray, LinkedList.class);
        assertEquals(modelListValue.size(), modelArray.length);
        assertEquals(modelListValue.get(0), modelArray[0]);
        // 测试不同类型, 自定义对象
        List<ConvertorOtherModel> modelListValueOther = (List) modelList.convertCollection(modelArray,
                                                                                           LinkedList.class,
                                                                                           ConvertorOtherModel.class); // int强制转为ConvertorOtherModel
        assertEquals(modelListValueOther.size(), modelArray.length);
        assertEquals(modelListValueOther.get(0).getI(), modelArray[0].getI());
    }

    @Test
    public void testArrayAndSet() {
        Convertor intSet = helper.getConvertor(int[].class, Set.class);
        Convertor integerSet = helper.getConvertor(Integer[].class, Set.class);

        int[] intArray = new int[] { 1, 2 };
        Integer[] integerArray = new Integer[] { 1, 2 };
        Set intSetValue = (Set) intSet.convert(intArray, Set.class);
        Set integerSetValue = (Set) integerSet.convert(integerArray, Set.class);
        assertEquals(intSetValue.size(), intArray.length);
        assertEquals(intSetValue.iterator().next(), intArray[0]);
        assertEquals(integerSetValue.size(), integerArray.length);
        assertEquals(integerSetValue.iterator().next(), integerArray[0]);
        // 测试不同类型转化, common对象
        Set<BigInteger> intSetValueOther = (Set) intSet.convertCollection(intArray, Set.class, BigInteger.class); // int强制转为BigInteger
        Set<BigDecimal> integerSetValueOther = (Set) integerSet.convertCollection(intArray, Set.class, BigDecimal.class); // int强制转为BigDecimal
        assertEquals(intSetValueOther.size(), intArray.length);
        assertEquals(intSetValueOther.iterator().next().intValue(), intArray[0]);
        assertEquals(integerSetValueOther.size(), integerArray.length);

        // BigDecimal & BigInteger
        Convertor bigDecimalSet = helper.getConvertor(BigDecimal[].class, HashSet.class);
        Convertor bigIntegerSet = helper.getConvertor(BigInteger[].class, LinkedHashSet.class);

        BigDecimal[] bigDecimalArray = new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ONE };
        BigInteger[] bigIntegerArray = new BigInteger[] { BigInteger.ZERO, BigInteger.ONE };
        Set bigDecimalSetValue = (Set) bigDecimalSet.convert(bigDecimalArray, HashSet.class);
        Set bigIntegerSetValue = (Set) bigIntegerSet.convert(bigIntegerArray, LinkedHashSet.class);
        assertEquals(bigDecimalSetValue.size(), bigDecimalArray.length);
        assertEquals(bigDecimalSetValue.iterator().next(), bigDecimalArray[0]);
        assertEquals(bigIntegerSetValue.size(), bigIntegerArray.length);
        assertEquals(bigIntegerSetValue.iterator().next(), bigIntegerArray[0]);

        // Object Array
        Convertor modelSet = helper.getConvertor(ConvertorModel[].class, TreeSet.class);
        ConvertorModel[] modelArray = new ConvertorModel[] { new ConvertorModel() };
        Set modelSetValue = (Set) modelSet.convert(modelArray, TreeSet.class);
        assertEquals(modelSetValue.size(), modelArray.length);
        assertEquals(modelSetValue.iterator().next(), modelArray[0]);
        // 测试不同类型, 自定义对象
        Set<ConvertorOtherModel> modelSetValueOther = (Set) modelSet.convertCollection(modelArray, TreeSet.class,
                                                                                       ConvertorOtherModel.class); // int强制转为ConvertorOtherModel
        assertEquals(modelSetValueOther.size(), modelArray.length);
        assertEquals(modelSetValueOther.iterator().next().getI(), modelArray[0].getI());
    }

    @Test
    public void testCollectionToArray() {
        // 进行List -> Array处理
        List<Integer> intListValue = Arrays.asList(1);
        // 测试不同类型转化, common对象
        Convertor intList = helper.getConvertor(List.class, int[].class);
        Convertor integerList = helper.getConvertor(List.class, Integer[].class);
        int[] intArray = (int[]) intList.convert(intListValue, int[].class);
        Integer[] integerArray = (Integer[]) integerList.convert(intListValue, Integer[].class);
        assertEquals(intListValue.size(), intArray.length);
        assertEquals(intListValue.get(0).intValue(), intArray[0]);
        assertEquals(intListValue.size(), integerArray.length);
        assertEquals(intListValue.get(0), integerArray[0]);
        // 测试不同类型转化, common对象
        BigInteger[] bigIntegerValueOther = (BigInteger[]) intList.convertCollection(intListValue, BigInteger[].class,
                                                                                     BigInteger.class); // int强制转为BigInteger
        BigDecimal[] bigDecimalValueOther = (BigDecimal[]) intList.convertCollection(intListValue, BigDecimal[].class,
                                                                                     BigDecimal.class); // int强制转为BigDecimal
        assertEquals(bigIntegerValueOther.length, intListValue.size());
        assertEquals(bigIntegerValueOther[0].intValue(), intListValue.get(0).intValue());
        assertEquals(bigDecimalValueOther.length, intListValue.size());
        assertEquals(bigDecimalValueOther[0].intValue(), intListValue.get(0).intValue());

        // BigDecimal & BigInteger
        Convertor bigDecimalSet = helper.getConvertor(List.class, BigDecimal[].class);
        Convertor bigIntegerSet = helper.getConvertor(List.class, BigInteger[].class);

        List<BigDecimal> bigDecimalList = Arrays.asList(BigDecimal.ONE);
        List<BigInteger> bigIntegerList = Arrays.asList(BigInteger.ONE);
        BigDecimal[] bigDecimalArrayValue = (BigDecimal[]) bigDecimalSet.convert(bigDecimalList, BigDecimal[].class);
        BigInteger[] bigIntegerArrayValue = (BigInteger[]) bigIntegerSet.convert(bigIntegerList, BigInteger[].class);
        assertEquals(bigDecimalArrayValue.length, bigDecimalList.size());
        assertEquals(bigDecimalArrayValue[0].intValue(), bigDecimalList.get(0).intValue());
        assertEquals(bigIntegerArrayValue.length, bigIntegerList.size());
        assertEquals(bigIntegerArrayValue[0].intValue(), bigIntegerList.get(0).intValue());

        // Object Array
        Convertor modelSet = helper.getConvertor(List.class, ConvertorModel[].class);
        List<ConvertorModel> modelList = Arrays.asList(new ConvertorModel());
        ConvertorModel[] modelArrayValue = (ConvertorModel[]) modelSet.convert(modelList, ConvertorModel[].class);
        assertEquals(modelArrayValue.length, modelList.size());
        assertEquals(modelArrayValue[0], modelList.get(0));
        // 测试不同类型
        ConvertorOtherModel[] modelArrayValueOther = (ConvertorOtherModel[]) modelSet.convertCollection(
                                                                                                        modelList,
                                                                                                        ConvertorOtherModel[].class,
                                                                                                        ConvertorOtherModel.class); // int强制转为ConvertorOtherModel
        assertEquals(modelArrayValueOther.length, modelList.size());
        assertEquals(modelArrayValueOther[0].getI(), modelList.get(0).getI());
    }

    @Test
    public void testCollectionAndCollection() {
        Convertor intSet = helper.getConvertor(List.class, Set.class);

        List intList = Arrays.asList(1);
        Set intSetValue = (Set) intSet.convert(intList, Set.class);
        assertEquals(intSetValue.size(), intList.size());
        assertEquals(intSetValue.iterator().next(), intList.get(0));
        // 测试不同类型转化, common对象
        Set<BigInteger> intSetValueOther = (Set) intSet.convertCollection(intList, Set.class, BigInteger.class); // int强制转为BigInteger
        Set<BigDecimal> decimalSetValueOther = (Set) intSet.convertCollection(intList, Set.class, BigDecimal.class); // int强制转为BigDecimal
        assertEquals(intSetValueOther.size(), intList.size());
        assertEquals(intSetValueOther.iterator().next().intValue(), intList.get(0));
        assertEquals(decimalSetValueOther.size(), intList.size());
        assertEquals(decimalSetValueOther.iterator().next().intValue(), intList.size());

        // BigDecimal & BigInteger
        Convertor bigDecimalSet = helper.getConvertor(List.class, HashSet.class);
        Convertor bigIntegerSet = helper.getConvertor(List.class, LinkedHashSet.class);

        List bigDecimalList = Arrays.asList(BigDecimal.ONE);
        List bigIntegerList = Arrays.asList(BigInteger.ONE);
        Set bigDecimalSetValue = (Set) bigDecimalSet.convert(bigDecimalList, HashSet.class);
        Set bigIntegerSetValue = (Set) bigIntegerSet.convert(bigIntegerList, LinkedHashSet.class);
        assertEquals(bigDecimalSetValue.size(), bigDecimalList.size());
        assertEquals(bigDecimalSetValue.iterator().next(), bigDecimalList.get(0));
        assertEquals(bigIntegerSetValue.size(), bigIntegerList.size());
        assertEquals(bigIntegerSetValue.iterator().next(), bigIntegerList.get(0));

        // Object Array
        Convertor modelSet = helper.getConvertor(List.class, TreeSet.class);
        List<ConvertorModel> modelList = Arrays.asList(new ConvertorModel());
        Set modelSetValue = (Set) modelSet.convert(modelList, TreeSet.class);
        assertEquals(modelSetValue.size(), modelList.size());
        assertEquals(modelSetValue.iterator().next(), modelList.get(0));
        // 测试不同类型
        Set<ConvertorOtherModel> modelSetValueOther = (Set) modelSet.convertCollection(modelList, TreeSet.class,
                                                                                       ConvertorOtherModel.class); // int强制转为ConvertorOtherModel
        assertEquals(modelSetValueOther.size(), modelList.size());
        assertEquals(modelSetValueOther.iterator().next().getI(), modelList.get(0).getI());
    }

    @Test
    public void testNestedMapping() {
        Map map = new HashMap();
        map.put("i", 1);
        map.put("integer", Integer.valueOf(1));
        map.put("bigDecimal", BigDecimal.ONE);

        List<Map> mapList = Arrays.asList(map);
        Convertor mapSet = helper.getConvertor(List.class, Set.class);
        Convertor mapArray = helper.getConvertor(List.class, ConvertorModel[].class);

        Set<Map> setMap = (Set) mapSet.convert(mapList, Set.class);
        Set<ConvertorModel> setModel = (Set) mapSet.convertCollection(mapList, Set.class, ConvertorModel.class);

        Map[] arrayMap = (Map[]) mapArray.convert(mapList, Map[].class);
        ConvertorModel[] arrayModel = (ConvertorModel[]) mapArray.convertCollection(mapList, ConvertorModel[].class,
                                                                                    ConvertorModel.class);

        assertEquals(setMap.size(), mapList.size());
        assertEquals(setMap.iterator().next(), mapList.get(0));

        assertEquals(setModel.size(), mapList.size());
        assertEquals(setModel.iterator().next().getI(), mapList.get(0).get("i"));
        assertEquals(setModel.iterator().next().getInteger(), mapList.get(0).get("integer"));
        assertEquals(setModel.iterator().next().getBigDecimal(), mapList.get(0).get("bigDecimal"));

        assertEquals(arrayMap.length, mapList.size());
        assertEquals(arrayMap[0], mapList.get(0));

        assertEquals(arrayModel.length, mapList.size());
        assertEquals(arrayModel[0].getI(), mapList.get(0).get("i"));
        assertEquals(arrayModel[0].getInteger(), mapList.get(0).get("integer"));
        assertEquals(arrayModel[0].getBigDecimal(), mapList.get(0).get("bigDecimal"));
    }

    @Test
    public void testMultiNestedMapping() {
        Map first = new HashMap();
        first.put("i", 1);
        first.put("integer", Integer.valueOf(1));
        first.put("bigDecimal", BigDecimal.ONE);

        Map two = new HashMap();
        two.putAll(first);

        // 构建Set<List<Map>>的两层结构对象
        List<Map> mapListValue = Arrays.asList(first);
        Set<List<Map>> nestedMapSetValue = new HashSet<List<Map>>();
        nestedMapSetValue.add(mapListValue);

        Convertor mapList = helper.getConvertor(Set.class, List.class);
        Convertor mapSet = helper.getConvertor(Set.class, HashSet.class);
        Convertor mapArray = helper.getConvertor(Set.class, List[].class);

        List<List<Map>> listMap = (List) mapList.convert(nestedMapSetValue, List.class);
        Set<Set<ConvertorModel>> setMap = (Set) mapSet.convertCollection(nestedMapSetValue, Set.class, new Class[] {
                Set.class, ConvertorModel.class });
        List<Map>[] listArray = (List<Map>[]) mapArray.convertCollection(nestedMapSetValue, List[].class, new Class[] {
                List.class, Map.class });
        Map[][] arrayArray = (Map[][]) mapArray.convertCollection(nestedMapSetValue, Map[][].class, new Class[] {
                Map[].class, Map.class });

        ConvertorModel[][] oldModelArray = (ConvertorModel[][]) mapArray.convert(nestedMapSetValue,
                                                                                 ConvertorModel[][].class);

        ConvertorModel[][] modelArray = (ConvertorModel[][]) mapArray.convertCollection(nestedMapSetValue,
                                                                                        ConvertorModel[][].class,
                                                                                        new Class[] {
                                                                                                ConvertorModel[].class,
                                                                                                ConvertorModel.class });

        assertEquals(listMap.size(), nestedMapSetValue.size());
        assertEquals(setMap.size(), nestedMapSetValue.size());
        assertEquals(listArray.length, nestedMapSetValue.size());
        assertEquals(arrayArray.length, nestedMapSetValue.size());
        assertEquals(oldModelArray.length, nestedMapSetValue.size());
        assertEquals(modelArray.length, nestedMapSetValue.size());

        // 来一个变态的，嵌套多维数组
        List<Map> firstList = Arrays.asList(first);
        List<List<Map>> twoList = Arrays.asList(firstList);
        List<List<List<Map>>> threeList = Arrays.asList(twoList);
        List<List<List<List<Map>>>> fourList = Arrays.asList(threeList);
        Convertor arrayConvertor = helper.getConvertor(List.class, ConvertorModel[][][][].class);
        Convertor setConvertor = helper.getConvertor(List.class, Set.class);
        ConvertorModel[][][][] oldValue = (ConvertorModel[][][][]) arrayConvertor.convert(fourList,
                                                                                          ConvertorModel[][][][].class);

        ConvertorModel[][][][] newValue = (ConvertorModel[][][][]) arrayConvertor.convertCollection(
                                                                                                    fourList,
                                                                                                    ConvertorModel[][][][].class,
                                                                                                    new Class[] {
                                                                                                            ConvertorModel[][][].class,
                                                                                                            ConvertorModel[][].class,
                                                                                                            ConvertorModel[].class,
                                                                                                            ConvertorModel.class });
        Set<Set<Set<Set<ConvertorOtherModel>>>> newSetValue = (Set) setConvertor.convertCollection(
                                                                                                   fourList,
                                                                                                   Set.class,
                                                                                                   new Class[] {
                                                                                                           Set.class,
                                                                                                           Set.class,
                                                                                                           Set.class,
                                                                                                           ConvertorOtherModel.class });
        assertEquals(oldValue.length, 1);
        assertEquals(oldValue[0][0][0][0].getI(), first.get("i"));
        assertEquals(oldValue[0][0][0][0].getInteger(), first.get("integer"));
        assertEquals(oldValue[0][0][0][0].getBigDecimal(), first.get("bigDecimal"));
        assertEquals(newValue.length, 1);
        assertEquals(newValue[0][0][0][0].getI(), first.get("i"));
        assertEquals(newValue[0][0][0][0].getInteger(), first.get("integer"));
        assertEquals(newValue[0][0][0][0].getBigDecimal(), first.get("bigDecimal"));
        assertEquals(newSetValue.size(), 1);
        ConvertorOtherModel model = newSetValue.iterator().next().iterator().next().iterator().next().iterator().next();
        assertEquals(model.getI(), first.get("i"));
        assertEquals(model.getInteger(), first.get("integer"));
        assertEquals(model.getBigDecimal(), first.get("bigDecimal"));
    }
}
