package stoner.tcxf.utils;

import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import stoner.tcxf.Sever;

import javax.xml.namespace.QName;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WsUtils {
    public static String getService() throws Exception {
        //第一步：创建服务地址，不是WSDL地址
        URL url = new URL("http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx");
        //第二步：打开一个通向服务地址的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //第三步：设置参数
        //3.1发送方式设置：POST必须大写
        connection.setRequestMethod("POST");
        //3.2设置数据格式：content-type
        connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
        //3.3设置输入输出，因为默认新创建的connection没有读写权限，
        connection.setDoInput(true);
        connection.setDoOutput(true);

        //第四步：组织SOAP数据，发送请求
        String soapXML = getXML("18819451225");
        OutputStream os = connection.getOutputStream();
        os.write(soapXML.getBytes());

        //第五步：接收服务端响应，打印（xml格式数据）
        StringBuilder sb = new StringBuilder();
        int responseCode = connection.getResponseCode();
        if(200 == responseCode){//表示服务端响应成功
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            try {
                is = connection.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

                String temp = null;
                while(null != (temp = br.readLine())){
                    sb.append(temp);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                br.close();
                isr.close();
                is.close();
            }
        }

        os.close();
        return sb.toString();
    }

    public static String getXML(String name){
        String soapXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                +"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                +"<soap:Body>"
                +"<getMobileCodeInfo xmlns=\"http://WebXml.com.cn/\">"
                +"<mobileCode>"+name+"</mobileCode>"
                +"<userID></userID>"
                +"</getMobileCodeInfo>"
                +"</soap:Body>"
                +"</soap:Envelope>";
        return soapXML;
    }

    public static Object[] invokeRemoteMethod(String url, String operation, Object[] parameters){
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        if (!url.endsWith("wsdl")) {
            url += "?wsdl";
        }
        org.apache.cxf.endpoint.Client client = dcf.createClient(url);
        //处理webService接口和实现类namespace不同的情况，CXF动态客户端在处理此问题时，会报No operation was found with the name的异常
        Endpoint endpoint = client.getEndpoint();
        QName opName = new QName(endpoint.getService().getName().getNamespaceURI(),operation);
        BindingInfo bindingInfo= endpoint.getEndpointInfo().getBinding();
        if(bindingInfo.getOperation(opName) == null){
            for(BindingOperationInfo operationInfo : bindingInfo.getOperations()){
                if(operation.equals(operationInfo.getName().getLocalPart())){
                    opName = operationInfo.getName();
                    break;
                }
            }
        }
        Object[] res = null;
        try {
            res = client.invoke(opName, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
