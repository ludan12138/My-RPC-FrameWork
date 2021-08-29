package cn.ludan.test;

import cn.ludan.rpc.RpcServer;
import cn.ludan.rpc.api.HelloService;
import cn.ludan.rpc.netty.server.NettyServer;
import cn.ludan.rpc.registry.DefaultServiceRegistry;
import cn.ludan.rpc.registry.ServiceRegistry;

public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        RpcServer rpcServer = new NettyServer();
        rpcServer.start(9999);

    }
}
