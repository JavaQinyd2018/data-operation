package com.operation.database.check;

import com.operation.database.service.SelectHelper;
import com.operation.database.utils.CSVUtils;
import com.operation.database.utils.ObjectReflectUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/11 9:26
 * 数据库校验工具
 */
@SuppressWarnings("all")
@Slf4j
public abstract class DBCheckHelper {

    /**
     * 校验某个类型是否一致
     * @param clazz
     * @param actual
     * @param expect
     * @param message
     * @param <T>
     */
    public static <T> void checkEquals(Class<T> clazz, T actual, T expect, String message) {
        if (actual != null && expect == null || actual == null && expect != null) {
            throw new RuntimeException(message + "\n" + String.format("当前实际值为：【%s】，期望值为：【%s】",actual, expect));
        }

        if (actual != null) {
            List<String> fieldNameList = ObjectReflectUtils.getFieldNameList(clazz);
            List<String> list = Lists.newArrayList();
            fieldNameList.forEach(field -> {
                Object o = ObjectReflectUtils.invokeGetMethod(actual, clazz, field);
                Object o1 = ObjectReflectUtils.invokeGetMethod(expect, clazz, field);
                if (o != null && o1 != null && o.equals(o1)) {
                    return;
                }
                if (o == null && o1 == null) {
                    return;
                }
                list.add(String.format("当前校验的字段为：【%s】，实际值为：【%s】，期望值为：【%s】", field, o, o1));
            });
            if (CollectionUtils.isNotEmpty(list)) {
                throw new RuntimeException(message + "\n" + StringUtils.join(list, "\n"));
            }
        }
    }

    /**
     * 校验集合
     * @param clazz
     * @param actualCollection
     * @param exceptCollection
     * @param message
     * @param <T>
     */
    public static <T> void  checkEquals(Class<T> clazz, Collection<T> actualCollection, Collection<T> exceptCollection, String message) {
        if (CollectionUtils.isEmpty(actualCollection) && CollectionUtils.isNotEmpty(exceptCollection)
                || CollectionUtils.isNotEmpty(actualCollection) && CollectionUtils.isEmpty(exceptCollection)) {
            throw new RuntimeException(message + "\n" + String.format("当前实际值为：【%s】，期望值为：【%s】",actualCollection, exceptCollection));
        }

        if (actualCollection.size() != exceptCollection.size()) {
            throw new RuntimeException(message + "\n" + String.format("当前集合大小实际为：【%s】，期望值为：【%s】，二者大小不一样",actualCollection.size(), exceptCollection.size()));
        }

        if (actualCollection instanceof List && exceptCollection instanceof List) {
            for (int i = 0; i < ((List) actualCollection).size(); i++) {
                T actual = (T)(((List) actualCollection).get(i));
                T expect = (T)(((List) exceptCollection).get(i));
                checkEquals(clazz, actual, expect, "集合中第"+(i+1)+"对象==>"+message +"<==集合中第"+(i+1)+"对象");
            }
        }

        if (actualCollection instanceof Set && exceptCollection instanceof Set) {
            for (int i = 0; i < ((Set) actualCollection).toArray().length; i++) {
                T actual = (T)(((Set) actualCollection).toArray()[i]);
                T expect = (T)(((Set) exceptCollection).toArray()[i]);
                checkEquals(clazz, actual, expect, "集合中第"+(i+1)+"对象==>"+message +"<==集合中第"+(i+1)+"对象");
            }
        }

        if (actualCollection instanceof List && exceptCollection instanceof Set ||
                actualCollection instanceof Set && exceptCollection instanceof List) {
            throw new RuntimeException(message + "\n" + String.format("当前实际值类型为：【%s】，期望值类型为：【%s】，二者不匹配",actualCollection.getClass(), exceptCollection.getClass()));
        }
    }


    public static void checkEquals(String actual, String expect, String message) {
        if (StringUtils.isBlank(actual) && StringUtils.isNotBlank(expect)
        || StringUtils.isNotBlank(actual) && StringUtils.isBlank(expect)) {
            throw new RuntimeException(message + "\n" + String.format("当前实际值为：【%s】，期望值为：【%s】二者不匹配",actual, expect));
        }
        if (StringUtils.isNotBlank(actual) && StringUtils.isNotBlank(expect) && !StringUtils.equals(actual, expect)) {
            throw new RuntimeException(message + "\n" + String.format("当前实际值为：【%s】，期望值为：【%s】二者不匹配",actual, expect));
        }
    }


    public static void checkEquals(Object actual, Object expect, String message) {
        if (actual != null && expect == null || actual == null && expect != null) {
            throw new RuntimeException(message + "\n" + String.format("当前实际值为：【%s】，期望值为：【%s】",actual, expect));
        }

        if (actual != null) {
            if (!actual.equals(expect)) {
                throw new RuntimeException(message + "\n" + String.format("当前实际值为：【%s】，期望值为：【%s】",actual, expect));
            }
        }

        if (expect != null) {
            if (!expect.equals(actual)) {
                throw new RuntimeException(message + "\n" + String.format("当前实际值为：【%s】，期望值为：【%s】",actual, expect));
            }
        }
    }

    public static void checkEquals(Object[] actual, Object[] expect, String message) {
        if (ArrayUtils.isNotEmpty(actual) && ArrayUtils.isEmpty(expect) || ArrayUtils.isEmpty(actual) && ArrayUtils.isNotEmpty(expect)) {
            throw new RuntimeException(message + "\n" + String.format("当前实际值为：【%s】，期望值为：【%s】", Arrays.asList(actual), Arrays.asList(expect)));
        }
        if (actual.length != expect.length) {
            throw new RuntimeException(message + "\n" + String.format("当前实际值数组长度为：【%s】，期望值数组长度为：【%s】", actual.length, expect.length));
        }
        List<String> list = Lists.newArrayList();
        if (ArrayUtils.isNotEmpty(actual) && ArrayUtils.isNotEmpty(expect)) {
            for (int i = 0; i < actual.length; i++) {
                String errorMessage2 = "";
                if (actual[i] != null && expect[i] == null || actual[i] == null && expect[i] != null) {
                    if (actual[i] != null && ! actual[i].equals(expect[i])) {
                        errorMessage2 = String.format("当前实际值为：【%s】，期望值为：【%s】", actual[i] , expect[i]);
                    }
                }
                if (actual[i] != null && expect[i] == null || actual[i] == null && expect[i] != null) {
                    if (expect[i] != null && ! expect[i].equals(actual[i])) {
                        errorMessage2 = String.format("当前实际值为：【%s】，期望值为：【%s】", actual[i] , expect[i]);
                    }
                }
                if (StringUtils.isNotBlank(errorMessage2)) {
                    list.add(errorMessage2);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(list)) {
            throw new RuntimeException(message + "\n" + StringUtils.join(list, "\n"));
        }
    }

    public static void checkEquals(Map<String, Object> actualMap, Map<String, Object> expectMap, String message) {
        if (MapUtils.isEmpty(actualMap) && MapUtils.isNotEmpty(expectMap) || MapUtils.isNotEmpty(actualMap) && MapUtils.isEmpty(expectMap)) {
            throw new RuntimeException(message + "\n" + String.format("当前实际值为：【%s】，期望值为：【%s】", actualMap, expectMap));
        }

        if (actualMap.size() != expectMap.size()) {
            throw new RuntimeException(message + "\n" + String.format("当前实际值map长度为：【%s】，期望值map长度为：【%s】", actualMap.size(), expectMap.size()));
        }

        List<String> list = Lists.newArrayList();
        List<String> expectList = Lists.newArrayList();
        if (MapUtils.isNotEmpty(actualMap) && MapUtils.isNotEmpty(expectMap)) {

            for (Map.Entry<String, Object> entry : actualMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (StringUtils.isNotBlank(key) && value != null && value.equals(expectMap.get(key))) {
                    expectMap.remove(key);
                    continue;
                }

                if (StringUtils.isNotBlank(key) && expectMap.get(key) != null && expectMap.get(key).equals(value)) {
                    expectMap.remove(key);
                    continue;
                }

                list.add(String.format("当前Map的对应的实际值为：【%s <==> %s】，", key, value));

            }

            expectMap.forEach((s, o) -> {
                expectList.add(String.format("期望值为：【%s <==> %s】", s, o));
            });
        }
        String[] strings = null;
        if (CollectionUtils.isNotEmpty(list) && CollectionUtils.isNotEmpty(expectList) && list.size() == expectList.size()) {
            strings = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                String errorMessage = list.get(i) + expectList.get(i);
                strings[i] = errorMessage;
            }
        }

        if (ArrayUtils.isNotEmpty(strings)) {
            throw new RuntimeException(message + "\n" + StringUtils.join(strings, "\n"));
        }
    }

    public static void checkEquals(String csvPathFile, int index, String message) {
        Map<String, Object> map = CSVUtils.parseVerticalCsvFile(csvPathFile, index);
        if (MapUtils.isEmpty(map)) {
            throw new RuntimeException("csv文件解析失败结果为空");
        }
        String tableName = (String) map.get("class");
        Map<String, Object> condition = (Map<String, Object>) map.get("condition");
        Map<String, Object> expectMap = (Map<String, Object>) map.get("value");
        List<String> fieldList = expectMap.keySet().stream().collect(Collectors.toList());
        Map<String, Object> actualMap = SelectHelper.selectByFieldList(tableName, fieldList, condition, null);
        checkEquals(actualMap, expectMap, message);
    }

    public static void checkEquals(String csvFilePath, String message) {
        List<Map<String, Object>> list = CSVUtils.parseVerticalCsvFile(csvFilePath);
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("解析csv文件结果为空");
        }

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            String tableName = (String) map.get("class");
            Map<String, Object> conditionMap = (Map<String, Object>) map.get("condition");
            Map<String, Object> expectMap = (Map<String, Object>) map.get("value");
            List<String> fieldList = expectMap.keySet().stream().collect(Collectors.toList());
            Map<String, Object> actualMap = SelectHelper.selectByFieldList(tableName, fieldList, conditionMap, null);
            if (MapUtils.isEmpty(actualMap)) {
                log.error("数据库查询的结果为空");
            }
            log.info("当前集合的位置为：第{}个位置",i);
            checkEquals(actualMap, expectMap, message);
        }
    }
}
