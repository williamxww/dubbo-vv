package com.bow.extension;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.registry.NotifyListener;
import com.alibaba.dubbo.registry.RegistryService;

/**
 * @author vv
 * @since 2017/1/15.
 */
public class EmsState {

    @Autowired
    protected RegistryService registryService;


    public void register(){
        String host;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            host = "empty";
        }
        URL url = new URL("ems",host,0,"emsId");
        url.addParameter("emsId",10000);
        url.addParameter(Constants.DYNAMIC_KEY, true);
        registryService.register(url);

    }

    public void subscribe(){
        String host;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            host = "empty";
        }
        URL url = new URL("ems",host,0,"emsId");
        registryService.subscribe(url, new NotifyListener() {
            @Override
            public void notify(List<URL> urls) {
                System.out.println(urls);
            }
        });
    }
}
