package stoner.tcxf.service;

import stoner.tcxf.bean.User;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface UserService {

    @WebMethod
    String saySomeThing();

    @WebMethod
    String sayHello(String name);

    @WebMethod
    List<User> findUsers(String name);
}
