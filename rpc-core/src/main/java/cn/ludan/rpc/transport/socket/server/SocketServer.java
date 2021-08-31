package cn.ludan.rpc.transport.socket.server;

import cn.ludan.rpc.handler.RequestHandler;
import cn.ludan.rpc.enumeration.RpcError;
import cn.ludan.rpc.exception.RpcException;
import cn.ludan.rpc.hook.ShutdownHook;
import cn.ludan.rpc.provider.ServiceProvider;
import cn.ludan.rpc.provider.ServiceProviderImpl;
import cn.ludan.rpc.registry.NacosServiceRegister;
import cn.ludan.rpc.registry.ServiceRegistry;
import cn.ludan.rpc.transport.AbstractRpcServer;
import cn.ludan.rpc.transport.RpcServer;
import cn.ludan.rpc.serializer.CommonSerializer;
import cn.ludan.rpc.factory.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class SocketServer extends AbstractRpcServer {

    private final ExecutorService threadPool;

    private final CommonSerializer serializer;

    private final RequestHandler requestHandler = new RequestHandler();

    public SocketServer(String host, int port) {
        this(host,port,DEFAULT_SERIALIZER);
    }

    public SocketServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        this.serviceProvider = new ServiceProviderImpl();
        this.serviceRegistry = new NacosServiceRegister();
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serializer = CommonSerializer.getByCode(serializer);
        scanServices();
    }

    @Override
    public void start(){
        if(serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        try(ServerSocket serverSocket = new ServerSocket()){
            serverSocket.bind(new InetSocketAddress(host,port));
            logger.info("服务器正在启动...");
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                logger.info("消费者连接：{}：{}",socket.getInetAddress(),socket.getPort());
                threadPool.execute(new SocketRequestHandlerThread(socket,requestHandler, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e){
            logger.error("服务器启动时有错误发生: ",e);
        }
    }
}
