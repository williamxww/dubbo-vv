package com.bow.extension;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vv
 * @since 2017/1/15.
 */
public class ZkUtil {

    public static final String ROOT = "/vapp";

    public static final String SLASH = "/";

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkUtil.class);

    private ZkClient zkClient;

    private static volatile boolean initialized = false;

    private static ZkUtil instance;

    private ZkUtil() {
        zkClient = new ZkClient("127.0.0.1", 60000, 5000);
        IZkStateListener zkStateListener = new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
                // do nothing
            }

            @Override
            public void handleNewSession() throws Exception {

            }
        };
        zkClient.subscribeStateChanges(zkStateListener);
    }

    public static ZkUtil getInstance() {
        if (!initialized) {
            synchronized (ZkUtil.class) {
                if (!initialized) {
                    instance = new ZkUtil();
                }
            }
        }
        return instance;
    }

    public void create(String path, Object data, CreateMode mode) {
        zkClient.create(path, data, mode);
    }

    public boolean exists(String path) {
        return zkClient.exists(path);
    }

    public void subscribe(String path, IZkChildListener listener) {
        zkClient.subscribeChildChanges(path, listener);
        List<String> children = zkClient.getChildren(path);
        try {
            listener.handleChildChange(path, children);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
