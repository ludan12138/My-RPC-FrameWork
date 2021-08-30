package cn.ludan.rpc.registry;

import java.net.InetSocketAddress;


/**
 * 服务注册接口
 * @author Ludan
 * @date 2021/8/29 20:56
*/public interface ServiceRegistry {

    /**
     * 根据服务的名称和地址注册进服务注册中心
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * 根据服务名称从注册中心获取到一个服务提供者的地址
     * @param serviceName
     * @return
     */
    InetSocketAddress lookupService(String serviceName);
}
