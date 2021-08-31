package cn.ludan.rpc.registry;

import cn.ludan.rpc.enumeration.RpcError;
import cn.ludan.rpc.exception.RpcException;
import cn.ludan.rpc.util.NacosUtil;
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

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try{
            NacosUtil.registerService(serviceName,inetSocketAddress);
        }catch (NacosException e){
            logger.error("注册服务时有错误发生:",e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

}
