//package com.xqxls.test;
//
//import com.xqxls.dao.ILogInfoDao;
//import com.xqxls.po.LogInfo;
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//
///**
// * @Description:
// * @Author: xqxls
// * @CreateTime: 2024/2/15 23:14
// */
//public class MyBatisTest {
//
//    @Test
//    public void test_mybatis() throws IOException {
//        // 1. 从SqlSessionFactory中获取SqlSession
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//
//        // 2. 获取映射器对象
//        ILogInfoDao logInfoDao = sqlSession.getMapper(ILogInfoDao.class);
//
//        // 3. 测试验证
//        LogInfo logInfo = LogInfo.builder()
//                .namespace("namespace")
//                .methodName("methodName")
//                .type("1")
//                .executeSql("sql")
//                .finalSql("finalSql")
//                .parameter(null)
//                .returnValue(null)
//                .spendTime(111L)
//                .createTime(LocalDateTime.now())
//                .updateTime(LocalDateTime.now())
//                .isDeleted(false)
//                .build();
//        logInfoDao.insert(logInfo);
//        sqlSession.commit();
//
//    }
//}
