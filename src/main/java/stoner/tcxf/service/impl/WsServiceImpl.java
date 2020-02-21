package stoner.tcxf.service.impl;

import com.alibaba.fastjson.JSON;
import stoner.tcxf.Sever;
import stoner.tcxf.service.WsService;
import stoner.tcxf.utils.WsUtils;

import java.util.List;

public class WsServiceImpl implements WsService {

    public static void main(String[] args) throws Exception {
        System.out.println(WsUtils.getService());
        Object[] sayHellos = WsUtils.invokeRemoteMethod(Sever.ADDRESS, "findUsers", new Object[]{"peter"});
        for (Object sayHello : sayHellos) {
            if (sayHello instanceof List) {
                for (Object o : ((List) sayHello)) {
                    System.out.println(JSON.toJSONString(o));
                }
            }
            System.out.println(sayHello);
        }

    }
}
