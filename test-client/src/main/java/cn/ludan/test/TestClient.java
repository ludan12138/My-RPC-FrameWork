package cn.ludan.test;

import cn.ludan.rpc.api.HelloObject;
import cn.ludan.rpc.api.HelloService;
import cn.ludan.rpc.client.RpcClientProxy;

/**
 * 测试使用消费者（客户端）
 * @author Ludan
 * @date 2021/8/24 12:59
*/
public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1",9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12,"This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
