#!/bin/sh
# -U 强制刷新maven依赖
# -Dmaven.test.skip=true 跳过测试
# -Pdev 打包指定不同环境
# -f xxx/pom.xml 代表指定的pom文件

mvn clean install package -U -Dmaven.test.skip=true -Pdev -f xx/pom.xml
