
[English](https://github.com/chengtengfei/okhttp3-fast-spring-boot-starter/blob/master/README_EN.md)

# okhttp3-fast-spring-boot-starter
在`Spring Boot`项目中方便的使用`okhttp3`，并提供了简单易用的`POST`，`GET`，`PUT`，`DELETE`方法帮助你连接`HTTP`请求，本项目依赖`OkHttp`的版本为`3.14.9`，适用于`Spring Boot 2.X`版本。
本项目不仅可助力快速使用`okhttp3`，还可以作为学习`okhttp3`使用方式的入门示例。

### Getting Started
目前项目处于测试中，若是需要使用则可以添加`snapshot`版本的`jar`包进行试用，发现问题或者对本项目有更多想法的话，请通过`1062011419@qq.com`联系我。

```
<dependency>
    <groupId>com.github.chengtengfei</groupId>
    <artifactId>okhttp3-fast-spring-boot-starter</artifactId>
    <version>0.0.2-SNAPSHOT</version>
</dependency>
```

### How to use it

`Spring Boot`项目中可以很方便的使用，只需要简单的注入即可。

```
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.chengtengfei.bean.OkHttp3Fast;
import com.github.chengtengfei.constant.RawMediaType;
import okhttp3.*;

@Component
public class Okhttp3FastTest {

    @Autowired
    private OkHttp3Fast okHttp3Fast;

    public void run() throws Exception {
        
        // get method
        Response response = okHttp3Fast.get("http://www.baidu.com/");
        if (response.isSuccessful()) {
            System.out.println(response.body() == null ? "" : response.body().string());
        }
        
        // post method
        String body = "{\"name\":\"Alice\", \"age\":18}";
        Response postResponse = okHttp3Fast.putRaw("http://www.space.test.com/search", body, RawMediaType.APPLICATION_JSON);
        if (postResponse.isSuccessful()) {
            System.out.println(postResponse.body() == null ? "" : postResponse.body().string());
        }
    }
}
```

同时`okhttp3-fast-spring-boot-starter`提供了几个用于配置请求的参数，可以在`application.properties`文件中配置以修改请求的方式。

```
# 用来控制OkHttp3Fast是否支持对HTTPS接口的请求，默认是true
okhttp3.fast.https
# 用来控制OkHttp3Fast请求接口时的连接超时时间，默认3秒
okhttp3.fast.connectTimeout
# 用来控制OkHttp3Fast请求接口时的读取超时时间，默认3秒
okhttp3.fast.readTimeout
# 用来控制OkHttp3Fast请求接口时的写入超时时间，默认3秒
okhttp3.fast.writeTimeout
# 用来控制OkHttp3Fast请求失败的时候是否执行重连，默认false
okhttp3.fast.retryOnConnectionFailure
```

上述参数中`okhttp3.fast.retryOnConnectionFailure`对请求失败执行重连的时候，要保证该请求失败的情况是是以下情况之一。
- 无法访问的IP地址。如果网址的主机有多个IP地址，则无法访问任何单个IP地址不会使整个请求失败。这可以增加多宿主服务的可用性。
- 连接池中失效的连接。 `ConnectionPool`重用连接以减少请求延迟，但是这些连接有时会超时。
- 无法访问的代理服务器。

关于`OkHttp3Fast`发送请求时连接、读取、写入超时时间的说明，可参考[TBD](http://www.baidu.com/)。

### Features

#### `OkHttpClient`复用

`okhttp3-fast-spring-boot-starter`不仅暴露出名为`okHttp3Fast`的`Spring Bean`，还暴露出`okHttpClient`的`Spring Bean`，而`okHttp3Fast`又是依赖于`okHttpClient`。若是项目中已存在名为`okHttpClient`的`Spring Bean`，则
`okHttp3Fast`则直接依赖于已经存在的`okHttpClient`而不会使用`okhttp3-fast-spring-boot-starter`提供的`okHttpClient`。

所以如果想改写`okHttp3Fast`的一些功能，则可以直接提供一个自己喜欢的`okHttpClient`供`okHttp3Fast`使用。

若是想增强`okHttp3Fast`的功能，也可以通过对`okhttp3-fast-spring-boot-starter`提供的`okHttpClient`添加更多的功能来完成。

### 易用的认证方式

`okhttp3.fast.retryOnConnectionFailure`基于`OkHttp3`的`Authenticator`机制提供了更方便易用的认证使用方式。若是请求的服务开启了`Baisc Auth`或者
`Digest Auth`认证方式，在使用`OkHttp3Fast`访问接口时，只需要在访问前设置如下代码。
```
// Baisc Auth认证
AuthConfig.basicAuth("admin", "123456");
AuthConfig.on();
// okHttp3Fast method sent
AuthConfig.off();

// Digest Auth认证
AuthConfig.digestAuth("admin", "123456");
AuthConfig.on();
// okHttp3Fast method sent
AuthConfig.off();

```

#### 监控功能
基于`Spring Boot Actuator`的监控功能提供`metrics`统计，监控暴露端点为`okhttp3fast`，在`Spring Boot 2.X`项目开启`Actuator`并使用默认配置时，通过访问
`http://localhost:8080/actuator/okhttp3fast`即可获取如下的统计信息。
```
{
    "okhttp3-fast": "https://github.com/chengtengfei/okhttp3-fast-spring-boot-starter",
    "author": "tengfei.cheng@foxmail.com",
    "metrics": {
        "okhttp3.fast.client.www.baidu.com.GET.meanRate": "1.88",
        "okhttp3.fast.client.www.baidu.com.GET.fifteenMinuteRate": "18.93",
        "okhttp3.fast.client.www.baidu.com.GET.count": 107,
        "okhttp3.fast.client.www.baidu.com.GET.oneMinuteRate": "8.76",
        "okhttp3.fast.client.www.baidu.com.GET.fiveMinuteRate": "16.95"
    }
}
```
其中`okhttp3.fast.client`标识请求客户端，`www.baidu.com`标识请求的域名，`GET`标识请求使用的`HTTP Method`。
从五个指标统计对某个地址的请求。
 - `count`表示应用启动后对总的请求次数
 - `oneMinuteRate`表示最近1分钟平均请求数
 - `fiveMinuteRate`表示最近5分钟平均请求数
 - `fifteenMinuteRate`表示最近15分钟平均请求数
 - `meanRate`表示应用启动后总的平均请求数（单位为秒）
 
#### 详细的调用日志

使用`okhttp3-fast-spring-boot-starter`时把日志级别调到`DEBUG`模式，`okhttp3-fast-spring-boot-starter`会把所有`HTTP`请求的的详细信息打印出来，方便追踪和查找问题。

### License

This project is licensed under the Apache 2.0 License.

### Acknowledgments
- [OkHttp](https://square.github.io/okhttp/) 
- [spring-boot-starter-okhttp3](spring-boot-starter-okhttp3)