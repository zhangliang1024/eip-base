package com.eip.cloud.eip.common.sensitive.config;

import com.eip.cloud.eip.common.sensitive.annotation.EnableSensitive;
import com.eip.cloud.eip.common.sensitive.properties.SensitiveConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

@Configuration
@EnableConfigurationProperties({SensitiveConfigProperties.class})
public class SensitivePropertiesAutoConfiguration implements ImportAware {

    private SensitiveConfigProperties configProperties;

    @Nullable
    protected AnnotationAttributes enableSensitive;

    public SensitivePropertiesAutoConfiguration(SensitiveConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableSensitive = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableSensitive.class.getName(), false));
        if (this.enableSensitive == null) {
            throw new IllegalArgumentException("@EnableSensitive is not present on importing class " + importMetadata.getClassName());
        }

        String[] packages = enableSensitive.getStringArray("packages");
        if (configProperties.getClassPackage().isEmpty()) {
            if (packages == null || packages.length < 1) {
                String className = importMetadata.getClassName();
                int index = className.indexOf(".");
                String startPackage = className.substring(0, ++index);
                String child = className.substring(index, className.length());
                index = child.indexOf(".");
                String secondPrefix = child.substring(0, index);
                configProperties.getClassPackage().add(startPackage + secondPrefix);
            }
        }
    }
}