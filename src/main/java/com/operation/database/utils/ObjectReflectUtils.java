package com.operation.database.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/3 13:36
 * 对象反射获取相关数据的工具类
 */
public class ObjectReflectUtils {

    public static <T> Object invokeGetMethod(T entity, Class<T> clazz, String fieldName) {
        if (entity == null) {
            throw new IllegalArgumentException("传入的实体对象不能为空");
        }
        Method method = getGetMethod(clazz, fieldName);
        if (method != null) {
            try {
                return method.invoke(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> Method getGetMethod(Class<T> clazz, String fieldName) {
        Method methodReturn = null;
        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("字段名不能为空");
        }
        Set<Method> getSet = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withPrefix("get"), ReflectionUtils.withParametersCount(0));
        if (CollectionUtils.isEmpty(getSet)) {
            throw new RuntimeException("该类型没有get方法");
        }
        for (Method method : getSet) {
            String get = method.getName().substring(method.getName().indexOf("get") + 3);
            if (StringUtils.equalsIgnoreCase(fieldName,get)) {
                methodReturn = method;
            }
        }
        return methodReturn;
    }

    public static List<String> getFieldNameList(Class<?> clazz) {
        Set<Field> allFields = ReflectionUtils.getAllFields(clazz);
        return allFields.stream().map(Field::getName).collect(Collectors.toList());
    }

    public static Map<String, Class<?>> getFieldNameTypeMap(Class<?> clazz) {
        Map<String, Class<?>> map = Maps.newLinkedHashMap();
        Set<Field> allFields = ReflectionUtils.getAllFields(clazz);
        allFields.forEach(field -> {
            map.put(field.getName(), field.getType());
        });
        return map;
    }

    /**
     * 获取set方法
     * @param clazz
     * @param fieldName
     * @param <T>
     * @return
     */
    public static <T> Method getSetMethod(Class<T> clazz, String fieldName) {
        Method methodReturn = null;
        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("字段名不能为空");
        }
        Set<Method> methodSet = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withPrefix("set"), ReflectionUtils.withParametersCount(1));
        if (CollectionUtils.isEmpty(methodSet)) {
            throw new RuntimeException("该类型没有set方法");
        }
        for (Method method : methodSet) {
            String set = method.getName().substring(method.getName().indexOf("set") + 3);
            if (StringUtils.equalsIgnoreCase(fieldName,set)) {
                methodReturn = method;
            }
        }
        return methodReturn;
    }

    /**
     * 调用set方法
     * @param entity
     * @param clazz
     * @param fieldName
     * @param value
     * @param <T>
     */
    public static <T> void invokeSetMethod(T entity, Class<T> clazz, String fieldName, Object value) {
        Method method = getSetMethod(clazz, fieldName);
        if (method != null) {
            try {
                method.invoke(entity, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


    public static <T> T invokeSet(Class<T> clazz, Map<String, Object> mapParam) {
        T entity = null;
        try {
            entity = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        List<String> fieldNameList = getFieldNameList(clazz);
        for (String fieldName : fieldNameList) {
            Method setMethod = getSetMethod(clazz, fieldName);
            Object value = mapParam.get(fieldName);
            if (value != null && entity != null) {
                try {
                    setMethod.invoke(entity, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return entity;
    }

}
