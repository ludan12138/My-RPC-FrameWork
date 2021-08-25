package cn.ludan.rpc.client;

import cn.ludan.rpc.entity.RpcRequest;
import cn.ludan.rpc.entity.RpcResponse;
import cn.ludan.rpc.enumeration.ResponseCode;
import cn.ludan.rpc.enumeration.RpcError;
import cn.ludan.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 远程方法调用的消费者（客户端）
 * @author Ludan
 * @date 2021/8/24 11:14
*/
public class RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    public Object sendRequest(RpcRequest rpcRequest,String host,int port) {
        try(Socket socket = new Socket(host,port)){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            RpcResponse rpcResponse = (RpcResponse) objectInputStream.readObject();
            if(rpcResponse == null){
                logger.error("服务调用失败，service：{}",rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE," service:"+rpcRequest.getInterfaceName());
            }
            if(rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()){
                logger.error("调用服务失败，service：{}，response：{}",rpcRequest.getInterfaceName(),rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE," service:"+rpcRequest.getInterfaceName());
            }
            return rpcResponse.getData();
        } catch (IOException | ClassNotFoundException e){
            logger.error("调用时有错误发生");
            throw new RpcException("服务调用失败: ",e);
        }
    }
}
