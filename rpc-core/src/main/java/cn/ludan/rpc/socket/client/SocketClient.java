package cn.ludan.rpc.socket.client;

import cn.ludan.rpc.RpcClient;
import cn.ludan.rpc.entity.RpcRequest;
import cn.ludan.rpc.entity.RpcResponse;
import cn.ludan.rpc.enumeration.ResponseCode;
import cn.ludan.rpc.enumeration.RpcError;
import cn.ludan.rpc.exception.RpcException;
import cn.ludan.rpc.serializer.CommonSerializer;
import cn.ludan.rpc.socket.util.ObjectReader;
import cn.ludan.rpc.socket.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * Socket方式远程方法调用的消费者（客户端）
 * 服务器ip和端口已知
 * @author Ludan
 * @date 2021/8/24 11:14
*/
public class SocketClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private final String host;
    private final int port;
    private CommonSerializer serializer;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        try(Socket socket = new Socket(host,port)){
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream,rpcRequest,serializer);
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            if(rpcResponse == null){
                logger.error("服务调用失败，service：{}",rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE," service:"+rpcRequest.getInterfaceName());
            }
            if(rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()){
                logger.error("调用服务失败，service：{}，response：{}",rpcRequest.getInterfaceName(),rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE," service:"+rpcRequest.getInterfaceName());
            }
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
