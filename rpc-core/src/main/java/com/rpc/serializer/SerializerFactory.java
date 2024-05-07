package com.rpc.serializer;
import com.rpc.spi.SpiLoader;

/**
 * 序列化器工厂
 */
public class SerializerFactory {


    /**
     * 默认序列化器
     */
    private static volatile boolean isLoaded = false;

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        if(!isLoaded){
            synchronized (SerializerFactory.class){
                if(!isLoaded){
                    SpiLoader.load(Serializer.class);
                    isLoaded = true;
                }
            }
        }
        return SpiLoader.getInstance(Serializer.class, key);
    }

}