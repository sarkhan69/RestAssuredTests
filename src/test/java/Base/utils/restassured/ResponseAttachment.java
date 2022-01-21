package Base.utils.restassured;

import io.qameta.allure.attachment.AttachmentData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResponseAttachment implements AttachmentData {

    private final String name;

    private final String url;

    private final String body;

    private final int responseCode;

    private final Map<String, String> headers;

    private final Map<String, String> cookies;

    public ResponseAttachment(final String name, final String url,
                              final String body, final int responseCode,
                              final Map<String, String> headers, final Map<String, String> cookies) {
        this.name = name;
        this.url = url;
        this.body = body;
        this.responseCode = responseCode;
        this.headers = headers;
        this.cookies = cookies;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    /**
     * Builder for HttpRequestAttachment.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    public static final class Builder {

        private final String name;

        private String url;

        private int responseCode;

        private String body;

        private final Map<String, String> headers = new HashMap<>();

        private final Map<String, String> cookies = new HashMap<>();

        private Builder(final String name) {
            Objects.requireNonNull(name, "Name must not be null value");
            this.name = name;
        }

        public static Builder create(final String attachmentName) {
            return new Builder(attachmentName);
        }

        public Builder setUrl(final String url) {
            Objects.requireNonNull(url, "Url must not be null value");
            this.url = url;
            return this;
        }

        public Builder setResponseCode(final int responseCode) {
            this.responseCode = responseCode;
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

        public ResponseAttachment build() {
            return new ResponseAttachment(name, url, body, responseCode, headers, cookies);
        }
    }
}
