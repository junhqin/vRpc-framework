package com.rpc.utils;


import
        cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import cn.hutool.setting.yaml.YamlUtil;

import java.util.Map;

/**
 * 配置工具类
 */
public class ConfigUtils {

    /**
     * 加载配置对象
     *
     * @param tClass
     * @param prefix
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    public static <T> T loadConfig(Class<T> tClass,String prefix, String environment){
        StringBuilder sb = new StringBuilder("application");
        if(StrUtil.isNotBlank(environment)){
            sb.append("-").append(environment);
        }
        sb.append(".yaml");
        if(FileUtil.exist(sb.toString())){
            return loadFromYaml(tClass, prefix, environment);
        }else
            return loadFromPros(tClass, prefix, environment);
    }

    private static <T> T loadFromYaml(Class<T> tClass, String prefix, String environment) {
        StringBuilder sb = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            sb.append("-").append(environment);
        }
        sb.append(".yaml");
        T config = null;
        Map<String, Object> map = YamlUtil.loadByPath(sb.toString());
        if (map.get(prefix) != null) {
            config = (T) BeanUtil.toBean(map.get(prefix), tClass);
        }
        return config;
    }

    /**
     * 加载配置对象，支持区分环境
     *
     * @param tClass
     * @param prefix
     * @param environment
     * @param <T>
     * @return
     */
    //TODO:提供以下扩展思路，可自行实现:
    //1)支持读取 application.yml、application.yaml 等不同格式的配置文件。
    //2)支持监听配置文件的变更，并自动更新配置对象。参考思路:使用 Hutool 工具类的 props.autoLoad() 可以实现配置文件变更的监听和自动加载
    //3)配置文件支持中文
    //参考思路:需要注意编码问题
    //4)配置分组。后续随着配置项的增多，可以考虑对配置项进行分组参考思路:可以通过嵌套配置类实现。
    public static <T> T loadFromPros(Class<T> tClass, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());
        return props.toBean(tClass, prefix);
    }


}
