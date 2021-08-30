package cn.ludan.rpc.socket.server;

import cn.ludan.rpc.RequestHandler;
import cn.ludan.rpc.enumeration.RpcError;
import cn.ludan.rpc.exception.RpcException;
import cn.ludan.rpc.provider.ServiceProvider;
import cn.ludan.rpc.RpcServer;
import cn.ludan.rpc.serializer.CommonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class SocketServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private final ExecutorService threadPool;
    private CommonSerializer serializer;
    private RequestHandler requestHandler = new RequestHandler();
    private final ServiceProvider serviceProvider;

    public SocketServer(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        BlockingQueue<Runnable> workingQueue= new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,workingQueue,threadFactory);
    }

    @Override
    public void start(int port){
        if(serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器正在启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                logger.info("消费者连接：{}：{}",socket.getInetAddress(),socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket,requestHandler, serviceProvider,serializer));
            }
            threadPool.shutdown();
        } catch (IOException e){
            logger.error("服务器启动时有错误发生: ",e);
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
