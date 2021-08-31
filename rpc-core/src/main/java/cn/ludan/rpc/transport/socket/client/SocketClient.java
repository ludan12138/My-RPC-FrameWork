package cn.ludan.rpc.transport.socket.client;

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
import cn.ludan.rpc.transport.socket.util.ObjectReader;
import cn.ludan.rpc.transport.socket.util.ObjectWriter;
import cn.ludan.rpc.util.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Socket方式远程方法调用的消费者（客户端）
 * 服务器ip和端口已知
 * @author Ludan
 * @date 2021/8/24 11:14
*/
public class SocketClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private final ServiceDiscovery serviceDiscovery;

    private CommonSerializer serializer;

    public SocketClient() {
        this.serviceDiscovery = new NacosServiceDiscovery();
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        try(Socket socket = new Socket()){
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream,rpcRequest,serializer);
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            RpcMessageChecker.check(rpcRequest,rpcResponse);
            return rpcResponse.getData();
        } catch (IOException e){
            logger.error("调用时有错误发生");
            throw new RpcException("服务调用失败: ",e);
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
