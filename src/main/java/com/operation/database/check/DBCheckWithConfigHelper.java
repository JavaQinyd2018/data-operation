package com.operation.database.check;

import com.operation.database.entity.Configuration;
import com.operation.database.service.SelectHelper;
import com.operation.database.service.SelectWithConfigHelper;
import com.operation.database.utils.CSVUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Yadong Qin
 * @Date: 2019/1/27
 */
@Slf4j
public class DBCheckWithConfigHelper {

    public static void checkEquals(Configuration configuration, String envFlag, String csvPathFile, int index, String message) {
        Map<String, Object> map = CSVUtils.parseVerticalCsvFile(csvPathFile, index);
        if (MapUtils.isEmpty(map)) {
            throw new RuntimeException("csv文件解析失败结果为空");
        }
        String tableName = (String) map.get("class");
        Map<String, Object> condition = (Map<String, Object>) map.get("condition");
        Map<String, Object> expectMap = (Map<String, Object>) map.get("value");
        List<String> fieldList = expectMap.keySet().stream().collect(Collectors.toList());
        Map<String, Object> actualMap = SelectWithConfigHelper.selectByFieldList(configuration, envFlag, tableName,
                fieldList, condition, null);
        DBCheckHelper.checkEquals(actualMap, expectMap, message);
    }

    public static void checkEquals(Configuration configuration, String envFlag,String csvFilePath, String message) {
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
            Map<String, Object> actualMap = SelectWithConfigHelper
                    .selectByFieldList(configuration, envFlag, tableName, fieldList, conditionMap, null);
            if (MapUtils.isEmpty(actualMap)) {
                log.error("数据库查询的结果为空");
            }
            log.info("当前集合的位置为：第{}个位置",i);
            DBCheckHelper.checkEquals(actualMap, expectMap, message);
        }
    }
}
