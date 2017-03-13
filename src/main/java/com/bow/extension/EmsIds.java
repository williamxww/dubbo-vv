package com.bow.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkChildListener;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * @author vv
 * @since 2017/1/15.
 */
public class EmsIds {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmsIds.class);

    private static final String EMS_ID = "emsId";

    private static List<Integer> emsIds = new ArrayList();

    /**
     * 将emsId注册到zookeeper上
     * 
     * @param emsId
     *            emsId
     */
    public static void register(Integer emsId) {
        String prefix = ZkUtil.ROOT + ZkUtil.SLASH + EMS_ID;
        ZkUtil zkUtil = ZkUtil.getInstance();
        if (!zkUtil.exists(prefix)) {
            zkUtil.create(prefix, null, CreateMode.PERSISTENT);
        }
        String path = prefix + ZkUtil.SLASH + emsId;
        if (!zkUtil.exists(path)) {
            zkUtil.create(path, null, CreateMode.EPHEMERAL);
        }
    }

    public static void subscribe() {
        String path = ZkUtil.ROOT + ZkUtil.SLASH + EMS_ID;
        ZkUtil zkUtil = ZkUtil.getInstance();
        zkUtil.subscribe(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                List<Integer> newEmsIds = new ArrayList();
                for(String node: currentChilds){
                    try{
                        Integer emsId = Integer.parseInt(node);
                        newEmsIds.add(emsId);
                    }catch (NumberFormatException e){
                        LOGGER.error("error to parse ems node "+node, e);
                    }
                }
                emsIds = newEmsIds;
                System.out.println(JSON.toJSONString(emsIds));
            }
        });
    }

    public static List<Integer> getEmsIds(){
        return emsIds;
    }

    public static void main(String[] args) {
        EmsIds.register(20000);
        EmsIds.register(10000);

        EmsIds.subscribe();

        try {
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
