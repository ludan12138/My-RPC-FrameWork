package cn.ludan.rpc.handler;

import cn.ludan.rpc.entity.RpcRequest;
import cn.ludan.rpc.entity.RpcResponse;
import cn.ludan.rpc.enumeration.ResponseCode;
import cn.ludan.rpc.enumeration.RpcError;
import cn.ludan.rpc.exception.RpcException;
import cn.ludan.rpc.provider.ServiceProvider;
import cn.ludan.rpc.provider.ServiceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 该对象用于处理请求
 * 根据RpcRequest和反射来获取方法的结果
 * @author Ludan
 * @date 2021/8/24 12:49
*/

public class RequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RpcRequest rpcRequest){
        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest,service);
    }


    private Object invokeTargetMethod(RpcRequest rpcRequest,Object service){
        Object result = null;
        try{
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
            result = method.invoke(service,rpcRequest.getParameters());
            logger.info("服务：{}成功调用方法：{}",rpcRequest.getInterfaceName(),rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            throw new RpcException("方法调用失败",e);
            // return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND,rpcRequest.getRequestId());
        }
        return result;
    }

}
