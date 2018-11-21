#### 一、项目介绍
基于zipkin调用链封装starter实现springmvc、dubbo、restTemplate等实现全链路跟踪 。

#### 二、软件架构
##### 1、架构图
![blockchain](https://img2018.cnblogs.com/blog/843808/201811/843808-20181111233335288-234628875.png "架构图")

#### 三、实现思路
##### 1、过滤器实现思路
所有调用链数据都通过过滤器实现埋点并收集、同一条链共享一个traceId、每个节点有唯一的spanId。
##### 2、共享传递方式
1.rpc调用：通过隐式传参、dubbo有提供spi在rpc调用之前塞到请求中。参考：dubbo系列六、SPI扩展Filter隐式传参
2.http调用：通过servlet过滤器、在请求前放入requestHead中传递、resTemplate也是如此。参考：调用链二、Zipkin 和 Brave 实现(springmvc、RestTemplate)服务调用跟踪
3.redis和DB等调用：原理相似，可以通过aop在调用前后拦截。

#### 四、安装教程
1.pom.xml添加maven包
2.application.yml添加trace配置
3.在springboot开启注解
#### 五、博客地址
[基于zipkin调用链封装starter实现springmvc、dubbo、restTemplate等实现全链路跟踪 ](https://www.cnblogs.com/wangzhuxing/p/9944135.html)
