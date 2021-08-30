package cn.ludan.test;

import cn.ludan.rpc.api.HelloService;
import cn.ludan.rpc.provider.DefaultServiceProvider;
import cn.ludan.rpc.provider.ServiceProvider;
import cn.ludan.rpc.serializer.KryoSerializer;
import cn.ludan.rpc.socket.server.SocketServer;

/**
 * 测试使用服务提供方（服务端）
 * @author Ludan
 * @date 2021/8/24 13:03
*/
public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceProvider serviceProvider = new DefaultServiceProvider();
        serviceProvider.register(helloService);
        SocketServer rpcServer = new SocketServer(serviceProvider);
        rpcServer.setSerializer(new KryoSerializer());
        rpcServer.start(9999);
    }
}
