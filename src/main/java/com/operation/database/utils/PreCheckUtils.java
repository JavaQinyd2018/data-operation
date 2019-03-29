package com.operation.database.utils;

import com.operation.database.entity.Configuration;
import com.operation.database.service.basic.MetaDataService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @Author: Qinyadong
 * @Date: 2018/12/28 12:54
 * 预校验的工具类
 */
public class PreCheckUtils {

    public static <T> void checkEmpty(T reference, String message) {
        if (reference == null) {
            throw new IllegalArgumentException(message);
        }
        if (reference instanceof String) {
            if (StringUtils.isEmpty((String) reference)) {
                throw new IllegalArgumentException(message);
            }
        }

        if (reference instanceof CharSequence) {
            if (StringUtils.isEmpty((CharSequence) reference)) {
                throw new IllegalArgumentException(message);
            }
        }
        if (reference instanceof Collection) {
            if (CollectionUtils.isEmpty((Collection)reference)) {
                throw new IllegalArgumentException(message);
            }
        }
        if (reference instanceof Map) {
            if (MapUtils.isEmpty((Map<?, ?>) reference)) {
                throw new IllegalArgumentException(message);
            }
        }
    }



}
