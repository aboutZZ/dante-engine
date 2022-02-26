/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.assistant.core.enums;

import cn.herodotus.engine.assistant.core.exception.HerodotusExceptionHandler;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 统一异常处理器
 * 1**	信息，服务器收到请求，需要请求者继续执行操作
 * 2**	成功，操作被成功接收并处理
 * 3**	重定向，需要进一步的操作以完成请求
 * 4**	客户端错误，请求包含语法错误或无法完成请求
 * 5**	服务器错误，服务器在处理请求的过程中发生了错误
 * <p>
 * 1开头的状态码
 * 100	Continue	继续。客户端应继续其请求
 * 101	Switching Protocols	切换协议。服务器根据客户端的请求切换协议。只能切换到更高级的协议，例如，切换到HTTP的新版本协议
 * <p>
 * 2开头的状态码
 * 200	OK	请求成功。一般用于GET与POST请求
 * 201	Created	已创建。成功请求并创建了新的资源
 * 202	Accepted	已接受。已经接受请求，但未处理完成
 * 203	Non-Authoritative Information	非授权信息。请求成功。但返回的meta信息不在原始的服务器，而是一个副本
 * 204	No Content	无内容。服务器成功处理，但未返回内容。在未更新网页的情况下，可确保浏览器继续显示当前文档
 * 205	Reset Content	重置内容。服务器处理成功，用户终端（例如：浏览器）应重置文档视图。可通过此返回码清除浏览器的表单域
 * 206	Partial Content	部分内容。服务器成功处理了部分GET请求
 * <p>
 * 3开头的状态码
 * 300	Multiple Choices	多种选择。请求的资源可包括多个位置，相应可返回一个资源特征与地址的列表用于用户终端（例如：浏览器）选择
 * 301	Moved Permanently	永久移动。请求的资源已被永久的移动到新URI，返回信息会包括新的URI，浏览器会自动定向到新URI。今后任何新的请求都应使用新的URI代替
 * 302	Found	临时移动。与301类似。但资源只是临时被移动。客户端应继续使用原有URI
 * 303	See Other	查看其它地址。与301类似。使用GET和POST请求查看
 * 304	Not Modified	未修改。所请求的资源未修改，服务器返回此状态码时，不会返回任何资源。客户端通常会缓存访问过的资源，通过提供一个头信息指出客户端希望只返回在指定日期之后修改的资源
 * 305	Use Proxy	使用代理。所请求的资源必须通过代理访问
 * 306	Unused	已经被废弃的HTTP状态码
 * 307	Temporary Redirect	临时重定向。与302类似。使用GET请求重定向
 * <p>
 * 4开头的状态码
 * 400	Bad Request	客户端请求的语法错误，服务器无法理解
 * 401	Unauthorized	请求要求用户的身份认证
 * 402	Payment Required	保留，将来使用
 * 403	Forbidden	服务器理解请求客户端的请求，但是拒绝执行此请求
 * 404	Not Found	服务器无法根据客户端的请求找到资源（网页）。通过此代码，网站设计人员可设置"您所请求的资源无法找到"的个性页面
 * 405	Method Not Allowed	客户端请求中的方法被禁止
 * 406	Not Acceptable	服务器无法根据客户端请求的内容特性完成请求
 * 407	Proxy Authentication Required	请求要求代理的身份认证，与401类似，但请求者应当使用代理进行授权
 * 408	Request Time-out	服务器等待客户端发送的请求时间过长，超时
 * 409	Conflict	服务器完成客户端的PUT请求是可能返回此代码，服务器处理请求时发生了冲突
 * 410	Gone	客户端请求的资源已经不存在。410不同于404，如果资源以前有现在被永久删除了可使用410代码，网站设计人员可通过301代码指定资源的新位置
 * 411	Length Required	服务器无法处理客户端发送的不带Content-Length的请求信息
 * 412	Precondition Failed	客户端请求信息的先决条件错误
 * 413	Request Entity Too Large	由于请求的实体过大，服务器无法处理，因此拒绝请求。为防止客户端的连续请求，服务器可能会关闭连接。如果只是服务器暂时无法处理，则会包含一个Retry-After的响应信息
 * 414	Request-URI Too Large	请求的URI过长（URI通常为网址），服务器无法处理
 * 415	Unsupported Media Type	服务器无法处理请求附带的媒体格式
 * 416	Requested range not satisfiable	客户端请求的范围无效
 * 417	Expectation Failed	服务器无法满足Expect的请求头信息
 * <p>
 * 5开头的状态码
 * 500	Internal Server Error	服务器内部错误，无法完成请求
 * 501	Not Implemented	服务器不支持请求的功能，无法完成请求
 * 502	Bad Gateway	充当网关或代理的服务器，从远端服务器接收到了一个无效的请求
 * 503	Service Unavailable	由于超载或系统维护，服务器暂时的无法处理客户端的请求。延时的长度可包含在服务器的Retry-After头信息中
 * 504	Gateway Time-out	充当网关或代理的服务器，未及时从远端服务器获取请求
 * 505	HTTP Version not supported	服务器不支持请求的HTTP协议的版本，无法完成处理
 * <p>
 * --- 自定义返回码 ---
 * <p>
 * 主要分类说明：
 * 2**.**   成功，操作被成功接收并处理
 * 3**.**	需要后续操作，需要进一步的操作以完成请求
 * 4**.**	HTTP请求错误，请求包含语法错误或无法完成请求，
 * 5**.**   平台错误，平台相关组件运行及操作错误。
 * 6**.**	关系数据库错误，服务器在处理请求的过程中发生了数据SQL操作等底层错误
 * 600.**	JDBC错误，服务器在处理请求的过程中发生了JDBC底层错误。
 * 601.**	JPA错误，服务器在处理请求的过程中发生了JPA错误。
 * 602.**	Hibernate错误，服务器在处理请求的过程中发生了Hibernate操作错误。
 * 603.**   接口参数Validation错误
 * <p>
 * 其它内容逐步补充
 *
 * @author gengwei.zheng
 */
@Schema(title = "响应结果状态", description = "自定义错误码以及对应的、友好的错误信息")
public enum ResultStatus {

    /**
     * 2*.** 成功
     */
    OK(20000, "成功"),
    NO_CONTENT(20400, "无内容"),

    /**
     * 4*.** Java常规错误
     */
    FAIL(40000, "失败"),
    WARNING(40001, "警告"),

    /**
     * 401.** 未经授权 Unauthorized	请求要求用户的身份认证
     */
    ACCESS_DENIED(40101, "您没有权限，拒绝访问"),
    INVALID_CLIENT(40102, "客户端身份验证失败"),
    INVALID_TOKEN(40103, "提供的访问令牌已过期、吊销、格式错误或无效"),
    UNAUTHORIZED_CLIENT(40104, "客户端无权使用此方法请求授权码或访问令牌"),

    /**
     * 403.** 禁止的请求，与403对应
     */
    INSUFFICIENT_SCOPE(40301, "TOKEN权限不足，您需要更高级别的权限"),
    REPEAT_SUBMISSION(40302, "请不要重复提交"),
    FREQUENT_REQUESTS(40303, "请求过于频繁请稍后再试"),
    SQL_INJECTION_REQUEST(40304, "疑似SQL注入请求"),

    /**
     * 406.** 不接受的请求，与406对应
     */
    UNSUPPORTED_GRANT_TYPE(40601, "授权服务器不支持授权授予类型"),
    UNSUPPORTED_RESPONSE_TYPE(40602, "授权服务器不支持使用此方法获取授权代码或访问令牌"),
    UNSUPPORTED_TOKEN_TYPE(40603, "授权服务器不支持撤销提供的令牌类型"),

    /**
     * 412.* 未经授权 Precondition Failed 客户端请求信息的先决条件错误
     */
    INVALID_GRANT(41201, "提供的授权授予信息无效"),
    INVALID_REDIRECT_URI(41202, "URI重定向的值无效"),
    INVALID_REQUEST(41203, "无效的请求，参数使用错误或无效."),
    INVALID_SCOPE(41204, "授权范围错误"),

    /**
     * 500.* Internal Server Error	服务器内部错误，无法完成请求
     */
    ERROR(50000, "服务器内部错误，无法完成请求"),
    SERVER_ERROR(50001, "授权服务器遇到意外情况，无法满足请求"),

    /**
     * 503.* Service Unavailable	由于超载或系统维护，服务器暂时的无法处理客户端的请求。延时的长度可包含在服务器的Retry-After头信息中
     */
    TEMPORARILY_UNAVAILABLE(50301, "由于服务器临时超载或维护，授权服务器当前无法处理该请求"),







    /* ------ 以下内容需要根据最新代码逻辑和结构进行重新梳理 ------*/

    UNAUTHORIZED(40101, "未经授权，无法访问"),

    ACCESS_DENIED_AUTHORITY_LIMITED(40104, "权限不足，拒绝访问"),
    BAD_CREDENTIALS(40105, "无效的凭证"),
    ACCOUNT_DISABLED(40106, "账户禁用"),
    ACCOUNT_EXPIRED(40107, "账户过期"),
    CREDENTIALS_EXPIRED(40108, "凭证过期"),
    ACCOUNT_LOCKED(40109, "账户被锁定"),
    INTERNAL_AUTHENTICATION(40110, "内部调用未授权接口，请检查接口权限"),
    /**
     * 本质是用户不存在，但是返回信息不应该太清晰，以防被攻击
     */
    USERNAME_NOT_FOUND(40110, "用户名或密码错误！"),
    USER_IS_DISABLED(40111, "用户被禁用"),


    /**
     *
     */
    NOT_FOUND(40400, "资源未找到"),
    HANDLER_NOT_FOUND(40401, "处理器未找到"),

    /**
     * 405.** 方法不允许 与405对应
     */
    METHOD_NOT_ALLOWED(40501, "请求方法不支持"),
    /**
     * 406.** 不接受的请求，与406对应
     */
    ILLEGAL_STAMP_PARAMETER(40603, "缺少签章身份标记参数"),
    STAMP_DELETE_FAILED(40604, "从缓存中删除签章失败"),
    STAMP_HAS_EXPIRED(40605, "签章已过期"),
    STAMP_MISSMATCH(40606, "签章信息无法匹配"),
    ILLEGAL_CAPTCHA_PARAMETER(40607, "验证码参数格式错误"),
    CAPTCHA_CATEGORY_INCORRECT(40608, "验证码分类错误"),
    CAPTCHA_HANDLER_NOT_EXIST(40609, "验证码处理器不存在"),
    CAPTCHA_MISSMATCH(40610, "验证码不匹配"),
    CAPTCHA_HAS_EXPIRED(40611, "验证码已过期"),
    CAPTCHA_IS_EMPTY(40612, "验证码不能为空"),


    INVALID_ARGUMENT(41206, "认证请求参数值错误或者参数缺失."),

    /**
     * 415.*	Unsupported Media Type	服务器无法处理请求附带的媒体格式
     */
    UNSUPPORTED_MEDIA_TYPE(41501, "不支持的 Media Type"),


    NULL_POINTER_EXCEPTION(50001, "后台代码出现了空值"),
    IO_EXCEPTION(50002, "IO异常"),
    HTTP_MESSAGE_NOT_READABLE_EXCEPTION(50003, "JSON转换为实体出错！"),
    TYPE_MISMATCH_EXCEPTION(50004, "类型不匹配"),
    MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION(50005, "接口参数使用错误或必要参数缺失，请查阅接口文档！"),
    ILLEGAL_ARGUMENT(50006, "非法参数错误"),

    SERVICE_UNAVAILABLE(50301, "Service Unavailable"),

    CLIENT_ABORT(50400, "与后端服务建立的连接已挂起"),
    /**
     * 6*.* 为数据操作相关错误
     */
    BAD_SQL_GRAMMAR(60000, "低级SQL语法错误，检查SQL能否正常运行或者字段名称是否正确"),
    /**
     * 62.* 数据库操作相关错误
     */
    DATA_INTEGRITY_VIOLATION(62000, "该数据正在被其它数据引用，请先删除引用关系，再进行数据删除操作"),
    TRANSACTION_ROLLBACK(62001, "数据事务处理失败，数据回滚"),
    /**
     * 63.* Spring Boot Validation校验相关操作
     */
    METHOD_ARGUMENT_NOT_VALID(63000, "接口参数校验错误"),
    /**
     * 64.* 临时数据操作相关错误
     */
    TOKEN_DELETE_FAILED(64000, "Token 删除失败"),

    /**
     * 7*.* 基础设施交互错误
     * 71.* Redis 操作出现错误
     * 72.* Cache 操作出现错误
     */
    PIPELINE_INVALID_COMMANDS(71000, "Redis管道包含一个或多个无效命令"),
    CACHE_CONFIG_NOT_FOUND(72000, "服务需要使用缓存，但是未找到*-cache.yaml配置");

    @Schema(title = "结果代码")
    private final int code;
    @Schema(title = "结果信息")
    private final String message;


    ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultStatus getResultEnum(int code) {
        for (ResultStatus type : ResultStatus.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return ERROR;
    }

    public static ResultStatus getResultEnum(String message) {
        for (ResultStatus type : ResultStatus.values()) {
            if (type.getMessage().equals(message)) {
                return type;
            }
        }
        return ERROR;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
