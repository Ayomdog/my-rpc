package com.ayom.myrpc.loadbalancer;

import com.ayom.myrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 随机负载均衡器
 */
public class RandomLoadBalancer implements LoadBalancer{

    private final Random random = new Random();
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        int size = serviceMetaInfoList.size();
        if(size == 1){
            return serviceMetaInfoList.get(0);
        }
        int index = random.nextInt();
        return serviceMetaInfoList.get(index);
    }
}
