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

package cn.herodotus.engine.oss.minio.definition.dto.api;

import io.minio.BucketArgs;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * <p>Description: Minio 基础 Bucket Dto </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/1 23:44
 */
public abstract class BucketArgsDto<B extends BucketArgs.Builder<B, A>, A extends BucketArgs> extends BaseArgsDto<B, A> {

    @NotNull(message = "存储桶名称不能为空")
    @Schema(name = "存储桶名称")
    private String bucketName;
    @Schema(name = "存储区域")
    private String region;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    protected void prepare(B builder) {
        builder.bucket(getBucketName());
        if (StringUtils.isNotBlank(getRegion())) {
            builder.region(getRegion());
        }
        super.prepare(builder);
    }

    @Override
    public A build() {
        B builder = getBuilder();
        prepare(builder);
        return builder.build();
    }
}
