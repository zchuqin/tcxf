package stoner.tcxf.service.impl;

import stoner.tcxf.bean.User;
import stoner.tcxf.service.UserService;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService(endpointInterface = "stoner.tcxf.service.UserService",serviceName = "userService")
public class UserServiceImpl implements UserService {
    public String saySomeThing() {
        return "hello world";
    }

    public String sayHello(String name) {
        return "Hello,".concat(name);
    }

    public List<User> findUsers(String name) {
        ArrayList<User> users = new ArrayList<User>();
        for (int i = 0; i < 3; i++) {
            users.add(new User(String.valueOf(users.size()), name, "PROTECT"));
        }
        return users;
    }
}
