package cn.ludan.test;

import cn.ludan.rpc.api.HelloService;
import cn.ludan.rpc.serializer.KryoSerializer;
import cn.ludan.rpc.transport.socket.server.SocketServer;

/**
 * 测试使用服务提供方（服务端）
 * @author Ludan
 * @date 2021/8/24 13:03
*/
public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl2();
        SocketServer rpcServer = new SocketServer("127.0.0.1",9999);
        rpcServer.setSerializer(new KryoSerializer());
        rpcServer.publishService(helloService,HelloService.class);
    }
}
