package com.ayom.myrpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.ayom.myrpc.config.RegistryConfig;
import com.ayom.myrpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EtcdRegistry implements Registry{
    private Client client;
    private KV kvClient;
    /**
     * 本机注册的节点key集合(用于维护续期)
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();
    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";
    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //创建Lease和KV客户端
        Lease leaseClient = client.getLeaseClient();
        //创建一个30秒的租约
        long leaseId = leaseClient.grant(30).get().getID();
        //设置要存储的键值对
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);
        //将键值对与租约关联起来,并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key,value,putOption);

        //添加节点信息到本地缓存
        localRegisterNodeKeySet.add(registryKey);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        kvClient.delete(key).get();

        //从本地缓存中删除节点信息
        localRegisterNodeKeySet.remove(registryKey);
    }

    /**
     * 服务发现
     * @param serviceKey
     * @return
     */
    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        //前缀搜索
        String searchPrefix = ETCD_ROOT_PATH + serviceKey;
        try{
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            ByteSequence key = ByteSequence.from(searchPrefix, StandardCharsets.UTF_8);
            List<KeyValue> keyValues = kvClient.get(key, getOption).get().getKvs();

            return keyValues.stream().map(
                    keyValue -> {
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value,ServiceMetaInfo.class);
                    }
            ).collect(Collectors.toList());

        }catch (Exception e){
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    @Override
    public void destory() {
        System.out.println("当前节点下线");
        for (String key : localRegisterNodeKeySet) {
            ByteSequence from = ByteSequence.from(key, StandardCharsets.UTF_8);
            try {
                kvClient.delete(from).get();
            } catch (Exception e) {
                throw new RuntimeException(key + "节点下线失败");
            }
        }
        if(kvClient != null){
            kvClient.close();
        }
        if(client != null){
            client.close();
        }
    }

    //@Override
    //public void heartBeat() {
    //    //10秒续签一次
    //    CronUtil.schedule("*/10 * * * * *",new Task(){
    //        @Override
    //        public void execute() {
    //            for (String key : localRegistryNodeKeySet) {
    //                try {
    //                    List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8)).get().getKvs();
    //                    //节点已过期
    //                    if(CollUtil.isEmpty(keyValues)){
    //                        continue;
    //                    }
    //                    //节点未过期,重新注册
    //                    KeyValue keyValue = keyValues.get(0);
    //                    String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
    //                    ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
    //                    register(serviceMetaInfo);
    //                } catch (Exception e) {
    //                    throw new RuntimeException(key + "续签失败",e);
    //                }
    //            }
    //        }
    //    });
    //    //支持秒级别定时任务
    //    CronUtil.setMatchSecond(true);
    //    CronUtil.start();
    //}
    @Override
    public void heartBeat() {
        // 10 秒续签一次
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                // 遍历本节点所有的 key
                for (String key : localRegisterNodeKeySet) {
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        // 该节点已过期（需要重启节点才能重新注册）
                        if (CollUtil.isEmpty(keyValues)) {
                            continue;
                        }
                        // 节点未过期，重新注册（相当于续签）
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(key + "续签失败", e);
                    }
                }
            }
        });

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

}
