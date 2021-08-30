package cn.ludan.rpc.provider;

import cn.ludan.rpc.enumeration.RpcError;
import cn.ludan.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultServiceProvider implements ServiceProvider {
    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceProvider.class);

    /**
     * key为服务名 value为提供服务的对象
     */
    private static final Map<String,Object> serviceMap = new ConcurrentHashMap<>();
    /**
     * 记录有哪些对象已经被注册
     */
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public synchronized <T> void register(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if(registeredService.contains(serviceName)){
            return;
        }
        registeredService.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length==0){

        }
        for(Class<?> i : interfaces){
            serviceMap.put(i.getCanonicalName(),service);
        }
        logger.info("向接口：{} 注册服务： {}",interfaces,serviceName);
    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
