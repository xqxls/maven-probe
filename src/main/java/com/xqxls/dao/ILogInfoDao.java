package com.xqxls.dao;

import com.xqxls.annotation.Exclude;
import com.xqxls.po.LogInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author: xqxls
 * @CreateTime: 2024/2/6 21:49
 */
public interface ILogInfoDao {

    @Exclude
    int insert(LogInfo logInfo);

    List<LogInfo> findAll();

    LogInfo queryLogInfoById(@Param("id") Long id);

    int update(LogInfo logInfo);

}
