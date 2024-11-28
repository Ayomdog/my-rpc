package com.ayom.myrpc.loadbalancer;

import com.ayom.myrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希负载均衡器
 */
public class ConsisitentHashLoadBalancer implements LoadBalancer{
    /**
     * 一致性Hash环,存放虚拟节点
     */
    private final TreeMap<Integer,ServiceMetaInfo> vituralNodes = new TreeMap<>();

    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }

        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for(int i = 0; i < VIRTUAL_NODE_NUM; i++){
                int hash = getHash(serviceMetaInfo.getServiceName() + "#" + i);
                vituralNodes.put(hash,serviceMetaInfo);
            }
        }

        int hash = getHash(requestParams);

        Map.Entry<Integer, ServiceMetaInfo> entry = vituralNodes.ceilingEntry(hash);
        if(entry == null){
            entry = vituralNodes.firstEntry();
        }

        return entry.getValue();
    }

    private int getHash(Object key) {
        return key.hashCode();
    }
}
