package cn.ludan.rpc.provider;

public interface ServiceProvider {
    /**
     * 注册服务
     * @param service
     * @param serviceName
     */
    <T> void addServiceProvider (T service, String serviceName);

    /**
     * 根据服务名获取对应的服务实现
     * @param serviceName
     * @return
     */
    Object getServiceProvider(String serviceName);
}
