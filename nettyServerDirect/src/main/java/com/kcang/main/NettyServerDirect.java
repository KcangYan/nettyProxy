package com.kcang.main;

import com.kcang.annotation.BeanConfiguration;
import com.kcang.annotation.InjectBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class NettyServerDirect {
    private NettyServerDirect(){}
    private static Logger myLogger = LoggerFactory.getLogger(NettyServerDirect.class);
    private static List<String> classPaths = new ArrayList<String>();
    /**
     * 初始化全局配置
     * @param Main 主入口类
     */
    public static void run(Class Main){
        String basePack = "com.kcang";
        String classpath = Main.getResource("/").getPath();
        basePack = basePack.replace(".", File.separator);
        String searchPath = classpath + basePack;
        doPath(new File(searchPath));
        for (String s : classPaths) {
            s = s.replace(classpath.replace("/","\\").replaceFirst("\\\\",""),"").replace("\\",".").replace(".class","");
            try {
                Class cls = Class.forName(s);
                beanConfiguration(cls);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        for(Object key : beanMap.keySet()){
            try {
                injectBean(beanMap.get(key));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    /**
     * 该方法会得到所有的类，将类的绝对路径写入到classPaths中
     * @param file file object
     */
    private static void doPath(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            assert files != null : "files path null!";
            for (File f1 : files) {
                doPath(f1);
            }
        } else {
            if (file.getName().endsWith(".class")) {
                classPaths.add(file.getPath());
            }
        }
    }

    //bean管理map
    private static final ConcurrentHashMap beanMap = new ConcurrentHashMap();
    /**
     * 根据beanName获取相应的实例
     * @param beanName 注解的bean名字
     * @return 返回对应实例
     */
    public static Object getBean(String beanName){
        if(beanMap.containsKey(beanName)){
            return beanMap.get(beanName);
        }else {
            throw new RuntimeException("类未注册进bean实例列表: "+beanName);
        }
    }
    private static void setBeanName(String beanName, Object obj){
        beanMap.put(beanName,obj);
    }

    /**
     * 实现自定义bean托管注解
     * @param cls 反射类
     * @throws IllegalAccessException 1
     * @throws InstantiationException 1
     */
    private static void beanConfiguration(Class cls) throws IllegalAccessException, InstantiationException{
        BeanConfiguration beanConfiguration = (BeanConfiguration) cls.getDeclaredAnnotation(BeanConfiguration.class);
        if(beanConfiguration != null){
            Object obj = cls.newInstance();
            if(beanConfiguration.name().equals("")){
                setBeanName(cls.getName(),obj);
            }else {
                if(beanMap.containsKey(beanConfiguration.name())){
                    throw new RuntimeException("bean名称重复："+cls.getName()+" 与 "+getBean(beanConfiguration.name()).getClass().getName());
                }
                setBeanName(beanConfiguration.name(),obj);
            }
        }
    }

    /**
     *
     * @param obj 传入实例
     * @throws NoSuchFieldException 1
     * @throws IllegalAccessException 1
     */
    private static void injectBean(Object obj) throws NoSuchFieldException, IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field : fields){
            InjectBean injectBean = field.getDeclaredAnnotation(InjectBean.class);
            if(injectBean != null){
                String beanName = injectBean.name();
                Field field1 = obj.getClass().getDeclaredField(field.getName());
                field1.setAccessible(true);
                if(beanName.equals("")){
                    field1.set(obj, getBean(field1.getType().getName()));
                }else {
                    field1.set(obj, getBean(beanName));
                }
            }
        }
    }

}
