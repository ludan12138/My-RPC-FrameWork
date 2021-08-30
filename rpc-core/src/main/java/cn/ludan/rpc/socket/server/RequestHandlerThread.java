package cn.ludan.rpc.socket.server;

import cn.ludan.rpc.RequestHandler;
import cn.ludan.rpc.entity.RpcRequest;
import cn.ludan.rpc.entity.RpcResponse;
import cn.ludan.rpc.provider.ServiceProvider;
import cn.ludan.rpc.serializer.CommonSerializer;
import cn.ludan.rpc.socket.util.ObjectReader;
import cn.ludan.rpc.socket.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class RequestHandlerThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceProvider serviceProvider;
    private CommonSerializer serializer;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceProvider serviceProvider, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceProvider = serviceProvider;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try(InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream()){
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceProvider.getService(interfaceName);
            Object result = requestHandler.handle(rpcRequest,service);
            RpcResponse<Object> response = RpcResponse.success(result);
            ObjectWriter.writeObject(outputStream,response,serializer);
            outputStream.flush();
        }catch (IOException e){
            logger.error("调用或发送时有错误发送：",e);
        }
    }
}
