package com.eip.common.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 云存储配置信息
 */
@ConfigurationProperties(prefix = "spring.cloud.store")
public class CloudStorageProperties {

    /**
     * 支持类型：ALIYUN QINIU CLOUD LOCAL
     */
    private String type;

    /*******************************************七牛云***********************************************/
    /**
     * 域名
     */
    private String qiniuDomain;
    /**
     * 前缀
     */
    private String qiniuPrefix;
    /**
     * AccessKey
     */
    private String qiniuAccessKey;
    /**
     * SecretKey
     */
    private String qiniuSecretKey;
    /**
     * BucketName
     */
    private String qiniuBucketName;

    /*******************************************阿里云***********************************************/
    /**
     * 域名
     */
    private String aliyunDomain;
    /**
     * 前缀
     */
    private String aliyunPrefix;
    /**
     * EndPoint
     */
    private String aliyunEndPoint;
    /**
     * AccessKeyId
     */
    private String aliyunAccessKeyId;
    /**
     * AccessKeySecret
     */
    private String aliyunAccessKeySecret;
    /**
     * BucketName
     */
    private String aliyunBucketName;


    /*******************************************腾讯云***********************************************/
    /**
     * 域名
     */
    private String qcloudDomain;
    /**
     * 前缀
     */
    private String qcloudPrefix;
    /**
     * AppId
     */
    private Integer qcloudAppId;
    /**
     * SecretId
     */
    private String qcloudSecretId;
    /**
     * SecretKey
     */
    private String qcloudSecretKey;
    /**
     * BucketName
     */
    private String qcloudBucketName;
    /**
     * 所属地区
     */
    private String qcloudRegion;

    /*******************************************本地***********************************************/
    /**
     * 本地路径
     */
    private String localPrefix;

    public String getQiniuDomain() {
        return qiniuDomain;
    }

    public void setQiniuDomain(String qiniuDomain) {
        this.qiniuDomain = qiniuDomain;
    }

    public String getQiniuAccessKey() {
        return qiniuAccessKey;
    }

    public void setQiniuAccessKey(String qiniuAccessKey) {
        this.qiniuAccessKey = qiniuAccessKey;
    }

    public String getQiniuSecretKey() {
        return qiniuSecretKey;
    }

    public void setQiniuSecretKey(String qiniuSecretKey) {
        this.qiniuSecretKey = qiniuSecretKey;
    }

    public String getQiniuBucketName() {
        return qiniuBucketName;
    }

    public void setQiniuBucketName(String qiniuBucketName) {
        this.qiniuBucketName = qiniuBucketName;
    }

    public String getQiniuPrefix() {
        return qiniuPrefix;
    }

    public void setQiniuPrefix(String qiniuPrefix) {
        this.qiniuPrefix = qiniuPrefix;
    }

    public String getAliyunDomain() {
        return aliyunDomain;
    }

    public void setAliyunDomain(String aliyunDomain) {
        this.aliyunDomain = aliyunDomain;
    }

    public String getAliyunPrefix() {
        return aliyunPrefix;
    }

    public void setAliyunPrefix(String aliyunPrefix) {
        this.aliyunPrefix = aliyunPrefix;
    }

    public String getAliyunEndPoint() {
        return aliyunEndPoint;
    }

    public void setAliyunEndPoint(String aliyunEndPoint) {
        this.aliyunEndPoint = aliyunEndPoint;
    }

    public String getAliyunAccessKeyId() {
        return aliyunAccessKeyId;
    }

    public void setAliyunAccessKeyId(String aliyunAccessKeyId) {
        this.aliyunAccessKeyId = aliyunAccessKeyId;
    }

    public String getAliyunAccessKeySecret() {
        return aliyunAccessKeySecret;
    }

    public void setAliyunAccessKeySecret(String aliyunAccessKeySecret) {
        this.aliyunAccessKeySecret = aliyunAccessKeySecret;
    }

    public String getAliyunBucketName() {
        return aliyunBucketName;
    }

    public void setAliyunBucketName(String aliyunBucketName) {
        this.aliyunBucketName = aliyunBucketName;
    }

    public String getQcloudDomain() {
        return qcloudDomain;
    }

    public void setQcloudDomain(String qcloudDomain) {
        this.qcloudDomain = qcloudDomain;
    }

    public String getQcloudPrefix() {
        return qcloudPrefix;
    }

    public void setQcloudPrefix(String qcloudPrefix) {
        this.qcloudPrefix = qcloudPrefix;
    }

    public Integer getQcloudAppId() {
        return qcloudAppId;
    }

    public void setQcloudAppId(Integer qcloudAppId) {
        this.qcloudAppId = qcloudAppId;
    }

    public String getQcloudSecretId() {
        return qcloudSecretId;
    }

    public void setQcloudSecretId(String qcloudSecretId) {
        this.qcloudSecretId = qcloudSecretId;
    }

    public String getQcloudSecretKey() {
        return qcloudSecretKey;
    }

    public void setQcloudSecretKey(String qcloudSecretKey) {
        this.qcloudSecretKey = qcloudSecretKey;
    }

    public String getQcloudBucketName() {
        return qcloudBucketName;
    }

    public void setQcloudBucketName(String qcloudBucketName) {
        this.qcloudBucketName = qcloudBucketName;
    }

	public String getQcloudRegion() {
		return qcloudRegion;
	}

	public void setQcloudRegion(String qcloudRegion) {
		this.qcloudRegion = qcloudRegion;
	}

    public String getLocalPrefix() {
        return localPrefix;
    }

    public void setLocalPrefix(String localPrefix) {
        this.localPrefix = localPrefix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
