package com.operation.database.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.operation.database.exception.ReflectProcessException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/3 13:36
 * 对象反射获取相关数据的工具类
 */
public class ReflectUtils {

    private ReflectUtils() {}

    /**
     * 递归获取某个类型的全部字段（包括父类的。。。）
     * @param clazz 类型
     * @return 字段List
     */
    public static List<Field> getAllFieldName(Class<?> clazz) {
        List<Field> fieldList = Lists.newArrayList();
        fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            fieldList.addAll(getAllFieldName(clazz.getSuperclass()));
        }
        return fieldList.stream()
                .filter(field -> !Modifier.isStatic(field.getModifiers()) && Modifier.isPrivate(field.getModifiers()))
                .collect(Collectors.toList());
    }

    public static Map<String, Class<?>> getFiledNameAndType(Class<?> clazz) {
        Map<String, Class<?>> map = Maps.newLinkedHashMap();
        List<Field> fieldList = getAllFieldName(clazz);
        fieldList.forEach(field -> map.put(field.getName(), field.getType()));
        return map;
    }

    public static Method getGetOrIsMethod(Class<?> clazz, String fieldName) {
        Method[] methods = clazz.getMethods();
        if (ArrayUtils.isNotEmpty(methods)) {
            for (Method method : methods) {
                String methodName = method.getName();
                if (StringUtils.startsWithIgnoreCase(methodName, "get")
                        && method.getParameters().length == 0
                        && StringUtils.substringAfter(methodName, "get").equalsIgnoreCase(fieldName)) {
                    return method;
                }else if (StringUtils.startsWithIgnoreCase(methodName, "is")
                        && method.getParameters().length == 0
                        && StringUtils.substringAfter(methodName, "is").equalsIgnoreCase(fieldName)) {
                    return method;
                }
            }
        }
        throw new ReflectProcessException("字段"+ fieldName+"不存在对应的get方法或者is方法");
    }

    public static <T> Object invokeGetOrIsMethod(T entity, Class<?> clazz, String fieldName) {
        Method getOrIsMethod = getGetOrIsMethod(clazz, fieldName);
        if (getOrIsMethod != null) {
            try {
                return getOrIsMethod.invoke(entity);
            } catch (Exception e) {
                throw new ReflectProcessException("字段"+fieldName+"调用get或者is方法失败",e);
            }
        }
        return null;
    }

    /**
     * 非基础数据类型，非对象类型集合
     */
    private static final List<Class<?>> COMMON_TYPE = Arrays.asList(Date.class, BigDecimal.class, Map.class, Collection.class, java.sql.Date.class, String.class);

    /**
     * 递归获取实体类中所有的字段以及对应的属性值
     * @param clazz 实体类类型
     * @param bean 实体类实例
     * @return 字段和值的map
     */
    public static Map<String, Object> getFileNameAndValueMap(Class<?> clazz, Object bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (clazz == null) {
            return map;
        }

        final Field[] fields = FieldUtils.getAllFields(clazz);
        if (ArrayUtils.isEmpty(fields)) {
            return map;
        }

        final Method[] methods = clazz.getMethods();

        for (Field field : fields) {
            Class<?> type = field.getType();
            try {
                if (!ClassUtils.isPrimitiveOrWrapper(type) && !COMMON_TYPE.contains(type) && !type.isArray() && !type.isEnum()) {
                    Method method = getGetOrIsMethod(methods, field.getName());
                    if (method == null) {
                        continue;
                    }
                    Object invoke = method.invoke(bean);
                    map.putAll(getFileNameAndValueMap(type, invoke));

                }else {
                    Method method = getGetOrIsMethod(methods, field.getName());
                    if (method == null) {
                        continue;
                    }
                    Object invoke = method.invoke(bean);
                    map.put(field.getName(), invoke);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ReflectProcessException("字段"+field.getName()+"获取值失败",e);
            }

        }
        return map;
    }

    private static Method getGetOrIsMethod(Method[] methods, String fileName) {
        if (ArrayUtils.isNotEmpty(methods)) {
            for (Method method : methods) {
                String methodName = method.getName();
                if (method.getParameters().length == 0
                        && StringUtils.substringAfter(methodName, "get").equalsIgnoreCase(fileName)
                        || StringUtils.substringAfter(methodName, "is").equalsIgnoreCase(fileName)) {
                    return method;
                }
            }
        }
        return null;
    }

}
