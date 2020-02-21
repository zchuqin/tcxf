package stoner.tcxf;

import stoner.tcxf.service.UserService;
import stoner.tcxf.service.impl.UserServiceImpl;

import javax.xml.ws.Endpoint;

public class Sever {
    public static final String ADDRESS = "http://localhost:9090/userService/";

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        Endpoint.publish(ADDRESS, userService);

    }
}
