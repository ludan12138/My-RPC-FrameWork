package cn.ludan.rpc.transport.socket.server;

import cn.ludan.rpc.exception.RpcException;
import cn.ludan.rpc.handler.RequestHandler;
import cn.ludan.rpc.entity.RpcRequest;
import cn.ludan.rpc.entity.RpcResponse;
import cn.ludan.rpc.provider.ServiceProvider;
import cn.ludan.rpc.serializer.CommonSerializer;
import cn.ludan.rpc.transport.socket.util.ObjectReader;
import cn.ludan.rpc.transport.socket.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class SocketRequestHandlerThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(SocketRequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private CommonSerializer serializer;

    public SocketRequestHandlerThread(Socket socket, RequestHandler requestHandler, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try(InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream()){
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            Object result = requestHandler.handle(rpcRequest);
            RpcResponse<Object> response = RpcResponse.success(result,rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream,response,serializer);
            outputStream.flush();
        }catch (IOException | RpcException e){
            logger.error("调用或发送时有错误发送：",e);
        }
    }
}
