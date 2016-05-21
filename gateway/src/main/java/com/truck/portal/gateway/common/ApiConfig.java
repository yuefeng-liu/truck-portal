package com.truck.portal.gateway.common;

import com.truck.utils.gateway.utils.entity.CompileConfig;
import com.truck.utils.gateway.utils.util.AESTokenHelper;
import com.truck.utils.gateway.utils.util.AesHelper;
import com.truck.utils.gateway.utils.util.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * api 相关的配置
 */
public class ApiConfig {
    private static final Logger logger = LoggerFactory.getLogger(ApiConfig.class);
    private static final String DEFAULT_SEVICE_VERSION = "default";
    private static ApiConfig instance;

    private ApiConfig() {
    }

    public static void init(Properties prop) {
        synchronized (ApiConfig.class) {
            if (instance == null) {
                instance = new ApiConfig();
            }
            if (prop == null) {
                throw new RuntimeException("api config init failed.");
            } else {
                instance.setApiTokenAes(prop.getProperty("com.truck.portal.gateway.tokenAes"));
                instance.setCsrfTokenSecret(prop.getProperty("com.truck.portal.gateway.csrfTokenSecret"));
                instance.setProtobufStream(Boolean.valueOf(prop.getProperty("com.truck.portal.gateway.protobufStream")));
                instance.setStaticSignPwd(prop.getProperty("com.truck.portal.gateway.staticSignPwd"));
                instance.setApiJarPath(prop.getProperty("com.truck.portal.gateway.jarPath"));
                instance.setInternalPort(prop.getProperty("com.truck.portal.gateway.internalPort"));
                instance.setSSLPort(prop.getProperty("com.truck.portal.gateway.sslPort"));
                instance.setZkAddress(prop.getProperty("dubbo.registry.url"));
                instance.setServiceVersion(prop.getProperty("dubbo.reference.version"));
                instance.setRiskServiceRedisKeySet(prop.getProperty("com.truck.portal.gateway.riskServiceRedisKeySet"));
                instance.setRiskServiceRedisConfig(prop.getProperty("com.truck.portal.gateway.riskServiceRedisConfig"));
            }
        }
    }

    public static ApiConfig getInstance() {
        if (instance == null) {
            throw new RuntimeException("apigw config not init.");
        }
        return instance;
    }

    /**
     * apigw在注册中心的名字
     */
    private String applicationName = "apigw";

    public String getApplicationName() {
        return applicationName;
    }

    /**
     * 是否对protobuf启用多接口调用时的分批结果下发
     */
    private boolean protobufStream = true;

    private void setProtobufStream(boolean protobufStream) {
        this.protobufStream = protobufStream;
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]com.truck.portal.gateway.protobufStream:{}", this.protobufStream);
    }

    public boolean isProtobufStream() {
        return protobufStream;
    }

    /**
     * api 静态签名密钥
     */
    private String staticSignPwd = null;

    private void setStaticSignPwd(String staticSignPwd) {
        this.staticSignPwd = staticSignPwd;
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]com.truck.portal.gateway.staticSignPwd:{}", this.staticSignPwd);
    }

    public String getStaticSignPwd() {
        return staticSignPwd;
    }

    /**
     * 用于为h5端生成csrf token
     */
    private String csrfTokenSecret = null;

    private void setCsrfTokenSecret(String csrfTokenSecret) {
        this.csrfTokenSecret = csrfTokenSecret;
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]com.truck.portal.gateway.csrfTokenSecret:{}", this.csrfTokenSecret);
    }

    public String getCsrfTokenSecret() {
        return csrfTokenSecret;
    }

    /**
     * 用于加密服务端token
     */
    private String apiTokenAes = null;

    private void setApiTokenAes(String apiTokenAes) {
        this.apiTokenAes = apiTokenAes;
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]com.truck.portal.gateway.tokenAes:{}", apiTokenAes);
    }

    private ThreadLocal<AESTokenHelper> apiTokenHelper = new ThreadLocal<AESTokenHelper>();

    public AESTokenHelper getApiTokenHelper() {
        AESTokenHelper helper = apiTokenHelper.get();
        if (helper == null) {
            helper = new AESTokenHelper(new AesHelper(Base64Util.decode(apiTokenAes), null));
            apiTokenHelper.set(helper);
        }
        return helper;
    }

    private String apiJarPath = null;

    private void setApiJarPath(String apiJarPath) {
        this.apiJarPath = apiJarPath;
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]com.truck.portal.gateway.jarPath:{}", this.apiJarPath);
    }

    public String getApiJarPath() {
        return apiJarPath;
    }

    private String zkAddress = null;

    private void setZkAddress(String zkAddress) {
        if (zkAddress == null || zkAddress.isEmpty()) {
            throw new RuntimeException("can not find zk address config");
        }
        this.zkAddress = zkAddress;
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]dubbo.registry.url:{}", this.zkAddress);
    }

    public String getZkAddress() {
        return zkAddress;
    }

    private String serviceVersion = null;

    private void setServiceVersion(String serviceVersion) {
        if (serviceVersion != null && !serviceVersion.isEmpty()) {
            if (!serviceVersion.trim().isEmpty() && !serviceVersion.equalsIgnoreCase(DEFAULT_SEVICE_VERSION)) {
                this.serviceVersion = serviceVersion;
            } else {
                this.serviceVersion = "";
            }
        } else {
            throw new RuntimeException("can not find service version config");
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]dubbo.reference.version:{}", this.serviceVersion);
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    private int internalPort = -1;

    private void setInternalPort(String internalPort) {
        if (internalPort != null && !internalPort.isEmpty() && !internalPort.trim().isEmpty()) {
            this.internalPort = Integer.parseInt(internalPort);
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]com.truck.portal.gateway.internalPort:{}", internalPort);
    }

    public int getInternalPort() {
        return internalPort;
    }

    private int sslPort = -1;

    private void setSSLPort(String sslPort) {
        if (sslPort != null && !sslPort.isEmpty() && !sslPort.trim().isEmpty()) {
            this.sslPort = Integer.parseInt(sslPort);
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]com.truck.portal.gateway.sslPort:{}", sslPort);
    }

    public int getSSLPort() {
        return sslPort;
    }

    private String riskServiceRedisKeySet = null;

    public String getRiskServiceRedisKeySet() {
        return riskServiceRedisKeySet;
    }

    private void setRiskServiceRedisKeySet(String riskServiceRedisKeySet) {
        if (riskServiceRedisKeySet != null && riskServiceRedisKeySet.length() > 0) {
            this.riskServiceRedisKeySet = riskServiceRedisKeySet;
        } else {
            this.riskServiceRedisKeySet = null;
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]com.truck.portal.gateway.riskServiceRedisKeySet:{}", this.riskServiceRedisKeySet);
    }

    private String riskServiceRedisConfig = null;

    public String getRiskServiceRedisConfig() {
        return riskServiceRedisConfig;
    }

    private void setRiskServiceRedisConfig(String riskServiceRedisConfig) {
        if (riskServiceRedisConfig != null && riskServiceRedisConfig.length() > 0) {
            this.riskServiceRedisConfig = riskServiceRedisConfig;
        } else {
            this.riskServiceRedisConfig = null;
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]com.truck.portal.gateway.riskServiceRedisConfig:{}", this.riskServiceRedisConfig);
    }
}
