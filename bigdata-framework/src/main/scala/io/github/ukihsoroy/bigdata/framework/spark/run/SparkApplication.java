package io.github.ukihsoroy.bigdata.framework.spark.run;

import io.github.ukihsoroy.bigdata.framework.spark.core.TSparkApplication;

public final class SparkApplication {

    public static void run (Class<? extends TSparkApplication> clazz, String[] args) {
        TSparkApplication spark = null;
        try {
            //反射获取spark接口对象
            spark = clazz.getConstructor(String[].class).newInstance((Object) args);
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            //如果spark不为空，执行
            if (spark != null) {
                //1.前置通知
                spark.before();
                try {
                    //2.业务逻辑执行
                    spark.process();
                } catch (Throwable exp) {
                    //3.异常处理
                    spark.exception(exp);
                } finally {
                    //4.后置通知
                    spark.after();
                }
            }
        }
    }
}
