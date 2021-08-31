package cn.ludan.test;

import cn.ludan.rpc.serializer.CommonSerializer;
import cn.ludan.rpc.transport.RpcClient;
import cn.ludan.rpc.transport.RpcClientProxy;
import cn.ludan.rpc.api.HelloObject;
import cn.ludan.rpc.api.HelloService;
import cn.ludan.rpc.transport.netty.client.NettyClient;
import cn.ludan.rpc.serializer.KryoSerializer;

public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12,"This is a message");
        for(int i = 0; i < 20; i ++) {
            String res = helloService.hello(object);
            System.out.println(res);
        }
    }
}
