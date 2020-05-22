package space.code.fei.configure;


import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.code.fei.bean.OkHttp3Fast;
import space.code.fei.properties.OkHttp3FastProperties;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass(OkHttp3Fast.class)
@EnableConfigurationProperties(OkHttp3FastProperties.class)
public class OkHttp3FastAutoConfigure {

    @Autowired
    private OkHttp3FastProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "okhttp3.fast.", value = "https", havingValue = "false")
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (properties.isHttps()) {
            SSLSocketFactoryImp ssl = null;
            try {
                ssl = new SSLSocketFactoryImp(
                        KeyStore.getInstance(KeyStore.getDefaultType()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            builder.connectTimeout(properties.getConnectTimeout(), TimeUnit.SECONDS)
                    .readTimeout(properties.getReadTimeout(), TimeUnit.SECONDS)
                    .writeTimeout(properties.getWriteTimeout(),TimeUnit.SECONDS)
                    .retryOnConnectionFailure(properties.isRetryOnConnectionFailure())
                    .sslSocketFactory(ssl.getSSLContext().getSocketFactory(), ssl.getTrustManager())
                    .hostnameVerifier(DO_NOT_VERIFY);
        } else {
            builder.connectTimeout(properties.getConnectTimeout(), TimeUnit.SECONDS)
                    .readTimeout(properties.getReadTimeout(), TimeUnit.SECONDS)
                    .writeTimeout(properties.getWriteTimeout(),TimeUnit.SECONDS)
                    .retryOnConnectionFailure(properties.isRetryOnConnectionFailure());
        }
        return builder.build();
    }


    @Bean
    @ConditionalOnMissingBean
    public OkHttp3Fast okHttp3Fast() {
        return new OkHttp3Fast(okHttpClient());
    }


    private TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] x509Certificates = new X509Certificate[0];
                    return x509Certificates;
                }
                @Override
                public void checkClientTrusted(
                        X509Certificate[] certs, String authType) {
                }
                @Override
                public void checkServerTrusted(
                        X509Certificate[] certs, String authType) {
                }
            }
    };

    private HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private SSLSocketFactory getTrustedSSLSocketFactory() {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            return sc.getSocketFactory();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
