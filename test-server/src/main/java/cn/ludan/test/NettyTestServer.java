package cn.ludan.test;

import cn.ludan.rpc.annotation.ServiceScan;
import cn.ludan.rpc.serializer.CommonSerializer;
import cn.ludan.rpc.transport.RpcServer;
import cn.ludan.rpc.api.HelloService;
import cn.ludan.rpc.transport.netty.server.NettyServer;
import cn.ludan.rpc.provider.ServiceProviderImpl;
import cn.ludan.rpc.provider.ServiceProvider;
import cn.ludan.rpc.serializer.KryoSerializer;

@ServiceScan
public class NettyTestServer {
    public static void main(String[] args) {
        RpcServer server = new NettyServer("127.0.0.1",9999, CommonSerializer.KRYO_SERIALIZER);
        server.start();
    }
}
