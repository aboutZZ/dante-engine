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

package cn.herodotus.engine.web.rest.autoconfigure;

import cn.herodotus.engine.web.rest.annotation.ConditionalOnFeignUseOkHttp;
import cn.herodotus.engine.web.rest.enhance.OkHttpResponseInterceptor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import okhttp3.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.loadbalancer.FeignLoadBalancerAutoConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

import java.util.concurrent.TimeUnit;

/**
 * <p>Description: OkHttp 自动配置 </p>
 * <p>
 * 1. 默认让 Feign 使用 OkHttp 作为 HttpClient。所以直接使用 Feign 的配置来对 OkHttp 进行配置。
 * 2. 如果存在 `feign.okhttp.enabled` 配置， 同时其值为 `true`，就会自动配置 OkHttp。
 * 3. 在此处配置 OkHttp，也是为了共用 OkHttp 的配置，让其可以同时支持 RestTemplate
 * <p>
 * {@code org.springframework.cloud.openfeign.loadbalancer.OkHttpFeignLoadBalancerConfiguration}
 *
 * @author : gengwei.zheng
 * @date : 2022/5/29 17:54
 * @see <a href='http://leejoker.github.io/post/feign%E4%BD%BF%E7%94%A8okhttp3%E7%9A%84%E6%AD%A3%E7%A1%AE%E5%A7%BF%E5%8A%BF/'> 参考资料</a>
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnFeignUseOkHttp
@AutoConfigureBefore(FeignLoadBalancerAutoConfiguration.class)
public class OkHttpAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OkHttpAutoConfiguration.class);

    private okhttp3.OkHttpClient okHttpClient;

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Web OkHttp] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean(ConnectionPool.class)
    public ConnectionPool ConnectionPool(FeignHttpClientProperties feignHttpClientProperties, OkHttpClientConnectionPoolFactory connectionPoolFactory) {
        int maxTotalConnections = feignHttpClientProperties.getMaxConnections();
        long timeToLive = feignHttpClientProperties.getTimeToLive();
        TimeUnit ttlUnit = feignHttpClientProperties.getTimeToLiveUnit();
        return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
    }

    @Bean
    public okhttp3.OkHttpClient okHttpClient(OkHttpClientFactory okHttpClientFactory, ConnectionPool connectionPool, FeignClientProperties feignClientProperties, FeignHttpClientProperties feignHttpClientProperties) {
        FeignClientProperties.FeignClientConfiguration defaultConfig = feignClientProperties.getConfig().get("default");
        int connectTimeout = feignHttpClientProperties.getConnectionTimeout();
        int readTimeout = defaultConfig.getReadTimeout();
        boolean disableSslValidation = feignHttpClientProperties.isDisableSslValidation();
        boolean followRedirects = feignHttpClientProperties.isFollowRedirects();
        this.okHttpClient = okHttpClientFactory.createBuilder(disableSslValidation)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .followRedirects(followRedirects)
                .connectionPool(connectionPool)
                .addInterceptor(new OkHttpResponseInterceptor())
                .build();
        return this.okHttpClient;
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(okhttp3.OkHttpClient okHttpClient) {
        OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        log.trace("[Herodotus] |- Bean [Client Http Request Factory for OkHttp] Auto Configure.");
        return factory;
    }

    @PreDestroy
    public void destroy() {
        if (this.okHttpClient != null) {
            this.okHttpClient.dispatcher().executorService().shutdown();
            this.okHttpClient.connectionPool().evictAll();
        }
    }
}
