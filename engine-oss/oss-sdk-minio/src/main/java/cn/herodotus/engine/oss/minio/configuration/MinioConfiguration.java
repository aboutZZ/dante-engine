/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.oss.minio.configuration;

import cn.herodotus.engine.oss.minio.annotation.ConditionalOnMinioEnabled;
import cn.herodotus.engine.oss.minio.core.MinioAsyncClientObjectPool;
import cn.herodotus.engine.oss.minio.core.MinioClientObjectPool;
import cn.herodotus.engine.oss.minio.properties.MinioProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>Description: Minio配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/11/8 11:30
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMinioEnabled
@EnableConfigurationProperties({MinioProperties.class})
public class MinioConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MinioConfiguration.class);

    @PostConstruct
    public void init() {
        log.debug("[Herodotus] |- SDK [Oss Minio] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public MinioClientObjectPool minioClientPool(MinioProperties minioProperties) {
        MinioClientObjectPool minioClientObjectPool = new MinioClientObjectPool(minioProperties);
        log.trace("[Herodotus] |- Bean [Minio Client Pool] Auto Configure.");
        return minioClientObjectPool;
    }

    @Bean
    @ConditionalOnMissingBean
    public MinioAsyncClientObjectPool minioAsyncClientPool(MinioProperties minioProperties) {
        MinioAsyncClientObjectPool minioAsyncClientObjectPool = new MinioAsyncClientObjectPool(minioProperties);
        log.trace("[Herodotus] |- Bean [Minio Async Client Pool] Auto Configure.");
        return minioAsyncClientObjectPool;
    }

    @Configuration(proxyBeanMethods = false)
    @ComponentScan(basePackages = {
            "cn.herodotus.engine.oss.minio.service",
            "cn.herodotus.engine.oss.minio.processor",
            "cn.herodotus.engine.oss.minio.controller",
    })
    static class MinioLogicConfiguration {
        @PostConstruct
        public void init() {
            log.debug("[Herodotus] |- SDK [Oss Minio Logic] Auto Configure.");
        }
    }
}
