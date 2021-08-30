package cn.ludan.rpc.registry;

import cn.ludan.rpc.enumeration.RpcError;
import cn.ludan.rpc.exception.RpcException;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

public class NacosServiceRegister implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegister.class);

    private static final String SERVER_ADDR = "127.0.0.1:8848";
    private static final NamingService nameService;

    static {
        try{
            nameService = NamingFactory.createNamingService(SERVER_ADDR);
        }catch (NacosException e){
            logger.error("连接到Nacos时有错误发生");
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try{
            nameService.registerInstance(serviceName,inetSocketAddress.getHostName(),inetSocketAddress.getPort());
        }catch (NacosException e){
            logger.error("注册服务时有错误发生:",e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try{
            List<Instance> instances = nameService.getAllInstances(serviceName);
            // 在服务提供者列表中选择第一个，后续负载均衡时可以改进
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(),instance.getPort());
        }catch (NacosException e){
            logger.error("获取服务时有错误发生:",e);
        }
        return null;
    }
}
