package cn.ludan.rpc.transport.netty.client;

import cn.ludan.rpc.loadbalancer.LoadBalancer;
import cn.ludan.rpc.loadbalancer.RandomLoadBalancer;
import cn.ludan.rpc.registry.NacosServiceDiscovery;
import cn.ludan.rpc.registry.NacosServiceRegister;
import cn.ludan.rpc.registry.ServiceDiscovery;
import cn.ludan.rpc.registry.ServiceRegistry;
import cn.ludan.rpc.transport.RpcClient;
import cn.ludan.rpc.entity.RpcRequest;
import cn.ludan.rpc.entity.RpcResponse;
import cn.ludan.rpc.enumeration.RpcError;
import cn.ludan.rpc.exception.RpcException;
import cn.ludan.rpc.serializer.CommonSerializer;
import cn.ludan.rpc.util.RpcMessageChecker;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

public class NettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private static final EventLoopGroup group;

    private static final Bootstrap bootstrap;

    private final CommonSerializer serializer;

    private final ServiceDiscovery serviceDiscovery;

    static{
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true);
    }

    public NettyClient() {
        this(DEFAULT_SERIALIZER,new RandomLoadBalancer());
    }

    public NettyClient(LoadBalancer loadBalancer){
        this(DEFAULT_SERIALIZER,loadBalancer);
    }

    public NettyClient(Integer serializer) {
        this(serializer,new RandomLoadBalancer());
    }

    public NettyClient(Integer serializer, LoadBalancer loadBalancer){
        this.serializer = CommonSerializer.getByCode(serializer);
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        // 将响应中方法返回结果放入引用类型原子类
        AtomicReference<Object> result = new AtomicReference<>(null);
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress,serializer);
            if(!channel.isActive()){
                group.shutdownGracefully();
                return null;
            }
            channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                if (future1.isSuccess()) {
                    logger.info(String.format("客户端发送消息：%s", rpcRequest.toString()));
                } else {
                    logger.error("发送消息时有错误发生：", future1.cause());
                }
            });
            channel.closeFuture().sync();
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse"+rpcRequest.getRequestId());
            RpcResponse rpcResponse = channel.attr(key).get();
            RpcMessageChecker.check(rpcRequest,rpcResponse);
            result.set(rpcResponse.getData());

        }catch (InterruptedException e){
            logger.error("发送消息时有错误发生：",e);
            Thread.currentThread().interrupt();
        }
        return result.get();
    }
}
