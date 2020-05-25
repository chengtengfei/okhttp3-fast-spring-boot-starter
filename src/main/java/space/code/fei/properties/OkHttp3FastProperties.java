package space.code.fei.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("okhttp3.fast")
public class OkHttp3FastProperties {

    private int connectTimeout = 3;
    private int readTimeout = 3;
    private int writeTimeout = 3;
    private boolean retryOnConnectionFailure = false;
    private boolean https = false;


    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public void setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
    }

    public boolean isHttps() {
        return https;
    }

    public void setHttps(boolean https) {
        this.https = https;
    }

    @Override
    public String toString() {
        return "OkHttp3FastProperties{" +
                "connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", writeTimeout=" + writeTimeout +
                ", retryOnConnectionFailure=" + retryOnConnectionFailure +
                ", https=" + https +
                '}';
    }
}
