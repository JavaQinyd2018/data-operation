package com.operation.database.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opencsv.CSVReader;
import com.operation.database.exception.CSVParseException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Qinyadong
 * @date 2019/6/30 9:08
 * @desciption
 * @since
 */
public final class CSVUtils {

    private CSVUtils() {}
    //csv路径为相对路径
    public static List<Map<String,Object>> parseCSVFile(String csvFilePath) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        if (StringUtils.isBlank(csvFilePath)) {
            return mapList;
        }
        csvFilePath = String.format("%s\\%s%s",System.getProperty("user.dir"),"src\\test\\resources\\",csvFilePath.replace("/","\\"));
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(csvFilePath));
            String[] headers = csvReader.readNext();
            List<String[]> list = csvReader.readAll();
            if (ArrayUtils.isEmpty(headers) || CollectionUtils.isEmpty(list)) {
                throw new CSVParseException("csv内容不能为空");
            }

            list.forEach(strings -> {
                if (headers.length != strings.length) {
                    throw new CSVParseException("csv文件格式不正确");
                }
                Map<String, Object> map = Maps.newLinkedHashMap();
                for (int i = 0; i < headers.length; i++) {
                    if (StringUtils.isBlank(strings[i])) {
                        continue;
                    }
                    map.put(headers[i], strings[i]);
                }
                mapList.add(map);
            });
        }catch (Exception e) {
            throw new CSVParseException("csv文件解析错误",e);
        }finally {
            closeReader(csvReader);
        }
        return mapList;
    }

    public static List<Map<String,Object>> parseCSVFileToUnderline(String csvFilePath) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        if (StringUtils.isBlank(csvFilePath)) {
            return mapList;
        }
        csvFilePath = String.format("%s\\%s%s",System.getProperty("user.dir"),"src\\test\\resources\\",csvFilePath.replace("/","\\"));
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(csvFilePath));
            String[] headers = csvReader.readNext();
            List<String[]> list = csvReader.readAll();
            if (ArrayUtils.isEmpty(headers) || CollectionUtils.isEmpty(list)) {
                throw new CSVParseException("csv内容不能为空");
            }

            list.forEach(strings -> {
                if (headers.length != strings.length) {
                    throw new CSVParseException("csv文件格式不正确");
                }
                Map<String, Object> map = Maps.newLinkedHashMap();
                for (int i = 0; i < headers.length; i++) {
                    if (StringUtils.isBlank(strings[i])) {
                        continue;
                    }
                    map.put(CamelToUnderlineUtils.camelToUnderline(headers[i]), strings[i]);
                }
                mapList.add(map);
            });
        }catch (Exception e) {
            throw new CSVParseException("csv文件解析错误",e);
        }finally {
            closeReader(csvReader);
        }
        return mapList;
    }

    public static Map<String, Object> parseVerticalCsvFile(String csvFilePath, int index) {
        Map<String, Object> map = Maps.newLinkedHashMap();
        if (StringUtils.isBlank(csvFilePath)) {
            return map;
        }
        csvFilePath = String.format("%s\\%s%s",System.getProperty("user.dir"),"src\\test\\resources\\",csvFilePath.replace("/","\\"));
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(csvFilePath));
            String[] headers = csvReader.readNext();
            List<String[]> list = csvReader.readAll();

            if (ArrayUtils.isEmpty(headers) || CollectionUtils.isEmpty(list)) {
                throw new CSVParseException("csv内容不能为空");
            }

            Map<String,String> conditionMap = Maps.newLinkedHashMap();
            Map<String, String> stringMap = Maps.newLinkedHashMap();
            String className = list.get(0)[0];
            list.forEach(strings -> {
                if (headers.length != strings.length || headers.length < 4) {
                    throw new CSVParseException("csv文件格式不正确或者数据为空");
                }
                String property = strings[1];
                String flag = strings[2];

                if (StringUtils.equalsIgnoreCase(flag, "N")) {
                    return;
                }
                if (StringUtils.equalsIgnoreCase(flag, "C")) {
                    conditionMap.put(property, strings[3+index]);
                    return;
                }
                stringMap.put(property, strings[3+index]);
            });

            map.put("value", stringMap);
            map.put("condition", conditionMap);
            map.put("class", className);
        } catch (IOException e) {
            throw new CSVParseException("csv文件解析错误",e);
        }finally {
            closeReader(csvReader);
        }
        return map;
    }

    public static List<Map<String, Object>> parseVerticalCsvFile(String csvFilePath) {
        int index = 0;
        List<Map<String, Object>> mapList = Lists.newArrayList();
        if (StringUtils.isBlank(csvFilePath)) {
            return mapList;
        }
        csvFilePath = String.format("%s\\%s%s",System.getProperty("user.dir"),"src\\test\\resources\\",csvFilePath.replace("/","\\"));
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(csvFilePath));
            String[] headers = csvReader.readNext();
            List<String[]> list = csvReader.readAll();

            if (ArrayUtils.isEmpty(headers) || CollectionUtils.isEmpty(list)) {
                throw new CSVParseException("csv内容不能为空");
            }
            while (index < headers.length - 3) {
                Map<String, Object> map = Maps.newLinkedHashMap();
                Map<String,String> conditionMap = Maps.newLinkedHashMap();
                Map<String, String> stringMap = Maps.newLinkedHashMap();
                String className = list.get(0)[0];
                for (String[] strings : list) {
                    if (headers.length != strings.length || headers.length < 4) {
                        throw new CSVParseException("csv文件格式不正确或者数据为空");
                    }
                    String property = strings[1];
                    String flag = strings[2];

                    if (StringUtils.equalsIgnoreCase(flag, "N")) {
                        continue;
                    }
                    if (StringUtils.equalsIgnoreCase(flag, "C")) {
                        conditionMap.put(property, strings[3+index]);
                        continue;
                    }
                    stringMap.put(property, strings[3+index]);
                    map.put("value", stringMap);
                    map.put("condition", conditionMap);
                    map.put("class", className);
                }
                mapList.add(map);
                index ++;
            }

        } catch (IOException e) {
            throw new CSVParseException("csv文件解析错误",e);
        }finally {
            closeReader(csvReader);
        }
        return mapList;
    }


    private static void closeReader(CSVReader csvReader) {
        if (csvReader != null) {
            try {
                csvReader.close();
            } catch (IOException e) {
                throw new CSVParseException("csvReader释放失败");
            }
        }
    }
}
