package com.xqxls.test;

import com.alibaba.fastjson.JSON;
import com.xqxls.dao.ILogInfoDao;
import com.xqxls.po.LogInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @Description:
 * @Author: xqxls
 * @CreateTime: 2024/2/11 15:52
 * VM options：
 * -javaagent:E:\itstack\booklet\idea-plugin\guide\guide-idea-plugin-probe\probe-agent\build\libs\probe-agent-1.0-SNAPSHOT-all.jar
 */
public class ApiTest {


    public String queryUserInfo(String uid, String token) throws InterruptedException {
        Thread.sleep(new Random().nextInt(500));
        return "德莱联盟，王牌工程师。申请出栈！";
    }

    public static LogInfo testFindAll() throws IOException, ClassNotFoundException, SQLException {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ILogInfoDao logInfoDao = sqlSession.getMapper(ILogInfoDao.class);
        LogInfo logInfo = logInfoDao.queryLogInfoById(31L);
        sqlSession.commit();
        sqlSession.close();
        return logInfo;
    }

    public static int testUpdate() throws IOException, ClassNotFoundException, SQLException {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ILogInfoDao logInfoDao = sqlSession.getMapper(ILogInfoDao.class);
        LogInfo logInfo = new LogInfo();
        logInfo.setId(62L);
        int count = logInfoDao.update(logInfo);
        sqlSession.commit();
        sqlSession.close();
        return count;
    }

    public static void main(String[] args) throws InterruptedException, IOException, SQLException, ClassNotFoundException {
//        String res = new ApiTest().queryUserInfo("100001", "LikdlNL13423");
//        System.out.println(res);

//        List<LogInfo> logInfoList = Collections.singletonList(testFindAll());
//        System.out.println(JSON.toJSONString(logInfoList));

        int count = testUpdate();
        System.out.println(count);
    }

}
