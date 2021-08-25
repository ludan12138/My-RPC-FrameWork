package cn.ludan.test;

import cn.ludan.rpc.api.HelloService;
import cn.ludan.rpc.registry.DefaultServiceRegistry;
import cn.ludan.rpc.registry.ServiceRegistry;
import cn.ludan.rpc.server.RpcServer;

/**
 * 测试使用服务提供方（服务端）
 * @author Ludan
 * @date 2021/8/24 13:03
*/
public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
