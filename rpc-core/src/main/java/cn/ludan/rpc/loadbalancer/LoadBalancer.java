package cn.ludan.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 从Nacos获取服务提供者时的负载均衡策略
 * @author Ludan
 * @date 2021/8/31 15:30
*/
public interface LoadBalancer {

    Instance select(List<Instance> instances);

}
