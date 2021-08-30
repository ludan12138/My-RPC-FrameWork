package cn.ludan.test;

import cn.ludan.rpc.RpcServer;
import cn.ludan.rpc.api.HelloService;
import cn.ludan.rpc.netty.server.NettyServer;
import cn.ludan.rpc.provider.DefaultServiceProvider;
import cn.ludan.rpc.provider.ServiceProvider;
import cn.ludan.rpc.serializer.KryoSerializer;

public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceProvider registry = new DefaultServiceProvider();
        registry.register(helloService);
        RpcServer rpcServer = new NettyServer();
        rpcServer.setSerializer(new KryoSerializer());
        rpcServer.start(9999);
    }
}
