package cn.ludan.test;

import cn.ludan.rpc.api.HelloObject;
import cn.ludan.rpc.api.HelloService;
import cn.ludan.rpc.RpcClient;
import cn.ludan.rpc.RpcClientProxy;
import cn.ludan.rpc.socket.client.SocketClient;

/**
 * 测试使用消费者（客户端）
 * @author Ludan
 * @date 2021/8/24 12:59
*/
public class SocketTestClient {
    public static void main(String[] args) {
        RpcClient rpcClient = new SocketClient("127.0.0.1",9000);
        RpcClientProxy proxy = new RpcClientProxy(rpcClient);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12,"This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
