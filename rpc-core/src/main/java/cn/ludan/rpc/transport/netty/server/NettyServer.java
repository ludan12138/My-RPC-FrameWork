package cn.ludan.rpc.transport.netty.server;

import cn.ludan.rpc.hook.ShutdownHook;
import cn.ludan.rpc.provider.ServiceProvider;
import cn.ludan.rpc.provider.ServiceProviderImpl;
import cn.ludan.rpc.registry.NacosServiceRegister;
import cn.ludan.rpc.registry.ServiceRegistry;
import cn.ludan.rpc.transport.RpcServer;
import cn.ludan.rpc.codec.CommonDecoder;
import cn.ludan.rpc.codec.CommonEncoder;
import cn.ludan.rpc.enumeration.RpcError;
import cn.ludan.rpc.exception.RpcException;
import cn.ludan.rpc.serializer.CommonSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * NIO方式服务提供
 * @author Ludan
 * @date 2021/8/27 20:10
*/
public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final String host;

    private final int port;

    private final ServiceRegistry serviceRegistry;

    private final ServiceProvider serviceProvider;

    private CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegister();
        serviceProvider = new ServiceProviderImpl();
    }

    /**
     * 注册服务到本地服务表和Nacos服务注册中心
     * @param service
     * @param serviceClass
     * @param <T>
     */
    @Override
    public <T> void publishService(T service, Class<T> serviceClass) {
        if(serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.addServiceProvider(service,serviceClass);
        serviceRegistry.register(serviceClass.getCanonicalName(),new InetSocketAddress(host,port));
        start();
    }

    @Override
    public void start() {
        if(serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG,256)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new CommonEncoder(serializer));
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            ShutdownHook.getShutdownHook().addClearAllHook();
            future.channel().closeFuture().sync();
        }catch (InterruptedException e){
            logger.error("启动服务器时有错误发生 ",e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }


}
