package cn.ludan.test;

import cn.ludan.rpc.serializer.CommonSerializer;
import cn.ludan.rpc.transport.RpcServer;
import cn.ludan.rpc.api.HelloService;
import cn.ludan.rpc.transport.netty.server.NettyServer;
import cn.ludan.rpc.provider.ServiceProviderImpl;
import cn.ludan.rpc.provider.ServiceProvider;
import cn.ludan.rpc.serializer.KryoSerializer;

public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new NettyServer("127.0.0.1",9999, CommonSerializer.KRYO_SERIALIZER);
        rpcServer.publishService(helloService,HelloService.class);
    }
}
