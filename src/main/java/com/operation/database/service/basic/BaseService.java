package com.operation.database.service.basic;

import java.util.List;
import java.util.Map;

/**
 * @Author: Qinyadong
 * @Date: 2019/1/8 13:20
 * 接口
 */
public interface BaseService {

    /**
     * 插入数据库
     * @param sql
     * @return
     */
    int insert(String sql);

    /**
     * 键值对的方式预编译插入
     * @param paramMap
     * @return
     */
    int insert(String tableName,  Map<String, Object> paramMap);

    /**
     * 批量插入
     * @param paramMapList
     * @return
     */
    int[] batchInsert(String tableName, List<Map<String, Object>> paramMapList);

    /**
     * 更新数据库
     * @param sql
     * @return
     */
    int update(String sql);

    /**
     * 更新
     * @param paramMap
     * @return
     */
    int update(String tableName, Map<String, Object> paramMap,Map<String, Object> whereConditionMap);

    /**
     * 删除
     * @param sql
     * @return
     */
    int delete(String sql);

    /**
     * 查询一个
     * @param sql
     * @return
     */
    Map<String, Object> selectOne(String sql);

    /**
     * 查询一个
     * @param whereConditionMap
     * @return
     */
    Map<String, Object> selectOne(String tableName,Map<String, Object> whereConditionMap);

    /**
     * 查询多个
     * @param sql
     * @return
     */
    List<Map<String, Object>> selectList(String sql);


    /**
     * 查询多个
     * @param whereConditionMap
     * @return
     */
    List<Map<String, Object>> selectList(String tableName, Map<String, Object> whereConditionMap, Map<String, String> orderByCondition);


    /**
     * 查询总数
     * @param sql
     * @return
     */
    Long selectCount(String sql);
}
