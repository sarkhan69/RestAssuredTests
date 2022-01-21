package Base.utils.restassured;

import io.qameta.allure.attachment.AttachmentData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestAttachment implements AttachmentData {

    private final String name;

    private String url;

    private final String method;

    private final String body;

    private final String curl;

    private final Map<String, String> headers;

    private final Map<String, String> cookies;

    public RequestAttachment(final String name, final String url, final String method,
                             final String body, final String curl, final Map<String, String> headers,
                             final Map<String, String> cookies) {
        this.name = name;
        this.url = url;
        this.method = method;
        this.body = body;
        this.curl = curl;
        this.headers = headers;
        this.cookies = cookies;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public String getCurl() {
        return curl;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Builder for HttpRequestAttachment.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    public static final class Builder {

        private final String name;

        private String url;

        private String method;

        private String body;

        private final Map<String, String> headers = new HashMap<>();

        private final Map<String, String> cookies = new HashMap<>();

        private Builder(final String name) {
            Objects.requireNonNull(name, "Name must not be null value");
            this.name = name;
        }

        private Builder(final String name, final String url) {
            Objects.requireNonNull(name, "Name must not be null value");
            this.name = name;
            this.url = url;
        }

        public static Builder create(final String attachmentName) {
            return new Builder(attachmentName);
        }

        public static Builder create(final String attachmentName, final String url) {
            return new Builder(attachmentName, url);
        }

        public Builder setMethod(final String method) {
            Objects.requireNonNull(method, "Method must not be null value");
            this.method = method;
            return this;
        }

        public Builder setHeader(final String name, final String value) {
            Objects.requireNonNull(name, "Header name must not be null value");
            Objects.requireNonNull(value, "Header value must not be null value");
            this.headers.put(name, value);
            return this;
        }

        public Builder setHeaders(final Map<String, String> headers) {
            Objects.requireNonNull(headers, "Headers must not be null value");
            this.headers.putAll(headers);
            return this;
        }

        public Builder setCookie(final String name, final String value) {
            Objects.requireNonNull(name, "Cookie name must not be null value");
            Objects.requireNonNull(value, "Cookie value must not be null value");
            this.cookies.put(name, value);
            return this;
        }

        public Builder setCookies(final Map<String, String> cookies) {
            Objects.requireNonNull(cookies, "Cookies must not be null value");
            this.cookies.putAll(cookies);
            return this;
        }

        public Builder setBody(final String body) {
            Objects.requireNonNull(body, "Body should not be null value");
            this.body = body;
            return this;
        }

        public RequestAttachment build() {
            return new RequestAttachment(name, url, method, body, getCurl(), headers, cookies);
        }

        private String getCurl() {
            if (url != null) {
                final StringBuilder builder = new StringBuilder("curl -v");
                if (Objects.nonNull(method)) {
                    builder.append(" -X ").append(method);
                }
                builder.append(" '").append(url).append('\'');
                headers.forEach((key, value) -> appendHeader(builder, key, value));
                cookies.forEach((key, value) -> appendCookie(builder, key, value));

                if (Objects.nonNull(body)) {
                    builder.append(" -d '").append(body).append('\'');
                }

                return builder.toString();
            }

            return null;
        }

        private static void appendHeader(final StringBuilder builder, final String key, final String value) {
            builder.append(" -H '")
                    .append(key)
                    .append(": ")
                    .append(value)
                    .append('\'');
        }

        private static void appendCookie(final StringBuilder builder, final String key, final String value) {
            builder.append(" -b '")
                    .append(key)
                    .append('=')
                    .append(value)
                    .append('\'');
        }
    }
}
