package com.xqxls;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.xqxls.dao.ILogInfoDao;
import com.xqxls.enums.Operation;
import com.xqxls.po.LogInfo;
import net.bytebuddy.implementation.bind.annotation.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @Description:
 * @Author: xqxls
 * @CreateTime: 2024/2/11 15:46
 */

public class MonitorMethod {

    @RuntimeType
    public static Object intercept(@This Object obj, @Origin Method method, @SuperCall Callable<?> callable, @AllArguments Object... args) throws Exception {
        long start = System.currentTimeMillis();
        Object resObj = null;
        try {
            resObj = callable.call();
            return resObj;
        } finally {
            if(!method.getName().equals("main")){
                String originalSql = (String) BeanUtil.getFieldValue(obj, "originalSql");
                String replaceSql = ReflectUtil.invoke(obj, "asSql");

                if(!originalSql.equals("insert into log_info(namespace, method_name, type, execute_sql, final_sql , parameter, return_value, spend_time, create_time ,update_time , is_deleted)\n" +
                        "        values(?, ?, ?, ?, ?, ?, ?, ?, now(), now(), 0)")){
                        // 类路径
                        String namespace = method.getDeclaringClass().getName();
                        System.out.println("namespace：" + namespace);
                        // 方法名
                        String methodName = method.getName();
                        System.out.println("methodName：" + methodName);
                        // 方法类型
                        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
                        //                    Configuration configuration = sqlSessionFactory.getConfiguration();
                        //                    MappedStatement mappedStatement = configuration.getMappedStatement(
                        //                            namespace + "." + methodName);
                        //                    String type = buildMethodType(mappedStatement);
                        // 入参
                        Map<String, Object> parameterMap = new HashMap<>();
                        for (int i = 0; i < method.getParameterCount(); i++) {
                            parameterMap.put(method.getParameterTypes()[i].getTypeName(),args[i]);
                        }
    //                String parameters = !parameterMap.isEmpty()? JSON.toJSONString(parameterMap):null;
                        // 结果
    //                String result = resObj==null?null:JSON.toJSONString(resObj);
                        // 耗时
                        long spendTime = System.currentTimeMillis() - start;
                        // sql
                        //                Map<String, Object> map = buildParameterObject(method, args);
                        //                BoundSql boundSql = mappedStatement.getBoundSql(map);
                        //                String sql = SqlUtil.beautifySql(boundSql.getSql());
                        //                String finalSql = SqlUtil.formatSql(boundSql,configuration);

                        // 构建对象
                        LogInfo logInfo = LogInfo.builder()
                                .namespace(namespace)
                                .methodName(methodName)
                                .type("type")
                                .executeSql(originalSql)
                                .finalSql(replaceSql)
                                .parameter(null)
                                .returnValue(null)
                                .spendTime(spendTime)
                                .createTime(LocalDateTime.now())
                                .updateTime(LocalDateTime.now())
                                .isDeleted(false)
                                .build();


                        // 1. 从SqlSessionFactory中获取SqlSession
                        SqlSession sqlSession = sqlSessionFactory.openSession();
                        // 2. 获取映射器对象
                        ILogInfoDao logInfoDao = sqlSession.getMapper(ILogInfoDao.class);
                        int count = logInfoDao.insert(logInfo);
                        System.out.println("成功插入：" + count + "条记录");
                        sqlSession.commit();
                    }
                }
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> buildParameterObject(Method method, Object[] args) throws IllegalAccessException {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Map<String,Object> map = new HashMap<>();
        for (int i = 0;i<parameterAnnotations.length;i++){
            Object object = args[i];
            if (parameterAnnotations[i].length == 0){
                //说明该参数没有注解，此时该参数可能是实体类，也可能是Map，也可能只是单参数
                if (object.getClass().getClassLoader() == null && object instanceof Map){
                    map.putAll((Map<? extends String, ?>) object);
                    System.out.println("该对象为Map");
                }
                else{
                    //形参为自定义实体类
                    map.putAll(objectToMap(object));
                    System.out.println("该对象为用户自定义的对象");
                }
            }else{
                //说明该参数有注解，且必须为@Param
                for (Annotation annotation : parameterAnnotations[i]){
                    if (annotation instanceof Param){
                        map.put(((Param) annotation).value(),object);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 获取利用反射获取类里面的值和名称
     * @param obj 待处理对象
     * @return 对象转换之后的集合
     */
    private static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println("clazz is " + clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    private static String buildMethodType(MappedStatement mappedStatement) {
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        String type;
        switch(sqlCommandType){
            case INSERT :
                type = Operation.INSERT.getDesc();
                break;
            case UPDATE:
                type = Operation.UPDATE.getDesc();
                break;
            case DELETE:
                type = Operation.DELETE.getDesc();
                break;
            case SELECT:
                type = Operation.SELECT.getDesc();
                break;
            default :
                type = Operation.UNKNOWN.getDesc();
        }
        return type;
    }


}
