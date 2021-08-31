package cn.ludan.rpc.transport;

import cn.ludan.rpc.serializer.CommonSerializer;

/**
 * 服务器类通用接口
 * @author Ludan
 * @date 2021/8/25 16:26
*/

public interface RpcServer {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    /**
     * 启动服务端
     * @param
     */
    void start();

    /**
     * 向Nacos注册服务
     * @param service
     * @param serviceClass
     * @param <T>
     */
    <T> void publishService(T service,String serviceName);

}
