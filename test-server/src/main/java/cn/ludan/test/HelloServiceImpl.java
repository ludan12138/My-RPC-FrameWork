package cn.ludan.test;

import cn.ludan.rpc.annotation.Service;
import cn.ludan.rpc.api.HelloObject;
import cn.ludan.rpc.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author Ludan
* @date 2021/8/24 10:08
*/
@Service
public class HelloServiceImpl implements HelloService{

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到：{}",object.getMessage());
        return "本次处理来自Netty服务";
    }
}
