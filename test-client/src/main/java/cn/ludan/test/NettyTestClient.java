package cn.ludan.test;

import cn.ludan.rpc.RpcClient;
import cn.ludan.rpc.RpcClientProxy;
import cn.ludan.rpc.api.HelloObject;
import cn.ludan.rpc.api.HelloService;
import cn.ludan.rpc.netty.client.NettyClient;

public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1",9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12,"This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
