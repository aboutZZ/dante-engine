/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2022 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.oauth2.authorization.customizer;

import cn.herodotus.engine.assistant.core.constants.HttpHeaders;
import cn.herodotus.engine.oauth2.authorization.definition.HerodotusConfigAttribute;
import cn.herodotus.engine.oauth2.authorization.definition.HerodotusRequestMatcher;
import cn.herodotus.engine.oauth2.authorization.processor.SecurityMatcherConfigurer;
import cn.herodotus.engine.oauth2.authorization.storage.SecurityMetadataSourceStorage;
import cn.herodotus.engine.web.core.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: 自定义 SecurityMetadataSource </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/5/20 12:24
 */
public class HerodotusSecurityMetadataSource {

    private static final Logger log = LoggerFactory.getLogger(HerodotusSecurityMetadataSource.class);

    private final SecurityMetadataSourceStorage securityMetadataSourceStorage;
    private final SecurityMatcherConfigurer securityMatcherConfigurer;

    public HerodotusSecurityMetadataSource(SecurityMetadataSourceStorage securityMetadataSourceStorage, SecurityMatcherConfigurer securityMatcherConfigurer) {
        this.securityMetadataSourceStorage = securityMetadataSourceStorage;
        this.securityMatcherConfigurer = securityMatcherConfigurer;
    }

    /**
     * 判定用户请求的url是否在权限表中
     * 如果在权限表中，则返回给decide方法，用来判定用户是否有此权限
     * 如果不在权限表中则放行
     * <p>
     * 如果getAttributes(Object object)方法返回null的话，意味着当前这个请求不需要任何角色就能访问，甚至不需要登录
     * 此方法返回的是Collection<ConfigAttribute>。主要考虑是url路径的多重匹配，例如/user/**，逻辑上可以匹配多个路径。
     *
     * @param object 当前请求
     * @return 根据当前请求匹配的配置信息，此处为权限信息
     * @throws IllegalArgumentException 参数错误
     * @see <a href="https://blog.csdn.net/u012373815/article/details/54633046">参考资料</a>
     */

    public HerodotusConfigAttribute getAttributes(RequestAuthorizationContext object) throws IllegalArgumentException {

        //object 中包含用户请求的request 信息
        final HttpServletRequest request = object.getRequest();

        String url = request.getRequestURI();
        String method = request.getMethod();

        if (WebUtils.isStaticResources(url)) {
            log.trace("[Herodotus] |- Is Static Resource : [{}], Passed!", url);
            return null;
        }

        if (WebUtils.isPathMatch(securityMatcherConfigurer.getPermitAllArray(), url)) {
            log.trace("[Herodotus] |- Is White List Resource : [{}], Passed!", url);
            return null;
        }

        String feignInnerFlag = request.getHeader(HttpHeaders.X_HERODOTUS_FROM_IN);
        if (StringUtils.isNotBlank(feignInnerFlag)) {
            log.trace("[Herodotus] |- Is Feign Inner Invoke : [{}], Passed!", url);
            return null;
        }

        return findConfigAttribute(url, method, request);
    }

    private HerodotusConfigAttribute findConfigAttribute(String url, String method, HttpServletRequest request) {

        log.debug("[Herodotus] |- Current Request is : [{}] - [{}]", url, method);

        HerodotusConfigAttribute configAttribute = this.securityMetadataSourceStorage.getConfigAttribute(url, method);
        if (ObjectUtils.isNotEmpty(configAttribute)) {
            log.debug("[Herodotus] |- Get configAttributes from local storage for : [{}] - [{}]", url, method);
            return configAttribute;
        } else {
            LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> compatible = this.securityMetadataSourceStorage.getCompatible();
            if (MapUtils.isNotEmpty(compatible)) {
                // 支持含有**通配符的路径搜索
                for (Map.Entry<HerodotusRequestMatcher, HerodotusConfigAttribute> entry : compatible.entrySet()) {
                    if (entry.getKey().matches(request)) {
                        log.debug("[Herodotus] |- Request match the wildcard [{}] - [{}]", entry.getKey(), entry.getValue());
                        return entry.getValue();
                    }
                }
            }
        }

        return null;
    }
}
