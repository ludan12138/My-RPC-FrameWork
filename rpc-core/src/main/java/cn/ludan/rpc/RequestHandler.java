package cn.ludan.rpc;

import cn.ludan.rpc.entity.RpcRequest;
import cn.ludan.rpc.entity.RpcResponse;
import cn.ludan.rpc.enumeration.ResponseCode;
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


    public Object handle(RpcRequest rpcRequest,Object service){
        Object result = null;
        try {
            result = invokeMethod(rpcRequest,service);
            logger.info("服务：{}成功调用方法：{}",rpcRequest.getInterfaceName(),rpcRequest.getMethodName());
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.info("调用或发送时有错误发生：",e);
        }
        return result;
    }



    private Object invokeMethod(RpcRequest rpcRequest,Object service) throws IllegalAccessException,InvocationTargetException{
        Method method;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND);
        }
        return method.invoke(service, rpcRequest.getParameters());
    }
}
