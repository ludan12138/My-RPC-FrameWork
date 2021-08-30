package cn.ludan.rpc;

import cn.ludan.rpc.serializer.CommonSerializer;

/**
 * 服务器类通用接口
 * @author Ludan
 * @date 2021/8/25 16:26
*/

public interface RpcServer {

    /**
     * 启动服务端
     * @param port
     */
    void start(int port);

    /**
     * 设置序列化器
     * @param serializer
     */
    void setSerializer(CommonSerializer serializer);


    /**
     * 向Nacos注册服务
     * @param service
     * @param serviceClass
     * @param <T>

    <T> void publishService(Object service,Class<T> serviceClass);
    */
}
