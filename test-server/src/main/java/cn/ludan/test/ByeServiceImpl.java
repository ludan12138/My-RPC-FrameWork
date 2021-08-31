package cn.ludan.test;

import cn.ludan.rpc.annotation.Service;
import cn.ludan.rpc.api.ByeService;

@Service
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        return "bye, "+name;
    }
}
