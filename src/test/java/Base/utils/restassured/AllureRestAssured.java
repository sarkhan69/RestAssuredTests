package Base.utils.restassured;

import io.qameta.allure.Step;
import io.restassured.filter.FilterContext;
import io.restassured.filter.OrderedFilter;
import io.restassured.internal.NameAndValue;
import io.restassured.internal.support.Prettifier;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.qameta.allure.Allure.getLifecycle;
import static Base.utils.restassured.RequestAttachment.Builder.create;

public class AllureRestAssured implements OrderedFilter {

    private String requestAttachmentNameCurl = "request.curl";
    private String requestAttachmentNameHeaders = "request.headers";
    private String requestAttachmentNameCookies = "request.cookies";
    private String requestAttachmentNameBody = "request.body";
    private String responseAttachmentNameBody = "response.body";
    private String responseAttachmentNameHeaders = "response.headers";

    @Override
    public Response filter(final FilterableRequestSpecification requestSpec,
                           final FilterableResponseSpecification responseSpec,
                           final FilterContext filterContext) {
        final Prettifier prettifier = new Prettifier();
        final String url = requestSpec.getURI();

        // Request
        // Add request curl attachment
        final RequestAttachment.Builder requestCurlBuilder = create(requestAttachmentNameCurl, url)
                .setMethod(requestSpec.getMethod())
                .setHeaders(toMapConverter(requestSpec.getHeaders()))
                .setCookies(toMapConverter(requestSpec.getCookies()));

        if (Objects.nonNull(requestSpec.getBody())) {
            requestCurlBuilder.setBody(prettifier.getPrettifiedBodyIfPossible(requestSpec));
        }

        final RequestAttachment requestCurl = requestCurlBuilder.build();

        new RestAssuredAttachmentProcessor().addAttachment(requestCurl, new RestAssuredAttachmentRenderer());

        // Add request headers attachment
        if (!toMapConverter(requestSpec.getHeaders()).isEmpty()) {
            final RequestAttachment requestHeaders = create(requestAttachmentNameHeaders)
                    .setHeaders(toMapConverter(requestSpec.getHeaders()))
                    .build();

            new RestAssuredAttachmentProcessor().addAttachment(requestHeaders, new RestAssuredAttachmentRenderer());
        }

        // Add request cookies attachment
        if (!toMapConverter(requestSpec.getCookies()).isEmpty()) {
            final RequestAttachment requestCookies = create(requestAttachmentNameCookies)
                    .setCookies(toMapConverter(requestSpec.getCookies()))
                    .build();

            new RestAssuredAttachmentProcessor().addAttachment(requestCookies, new RestAssuredAttachmentRenderer());
        }

        // Add request body attachment
        if (Objects.nonNull(requestSpec.getBody())) {
            final RequestAttachment requestBody = create(requestAttachmentNameBody)
                    .setBody(prettifier.getPrettifiedBodyIfPossible(requestSpec))
                    .build();

            new RestAssuredAttachmentProcessor().addAttachment(requestBody, new RestAssuredAttachmentRenderer());
        }

        // Response
        final Response response = filterContext.next(requestSpec, responseSpec);
        final String requestStep = String.format("%s %s -> %s", requestSpec.getMethod(), url, response.getStatusLine());
        logStep(requestStep);

        // Add response body attachment
        if (Objects.nonNull(response.getBody())) {
            final ResponseAttachment responseBody = ResponseAttachment.Builder.create(responseAttachmentNameBody)
                    .setBody(prettifier.getPrettifiedBodyIfPossible(response, response.getBody()))
                    .build();

            new RestAssuredAttachmentProcessor().addAttachment(responseBody, new RestAssuredAttachmentRenderer());
        }

        // Add response headers attachment
        if (!toMapConverter(response.getHeaders()).isEmpty()) {
            final ResponseAttachment responseHeaders = ResponseAttachment.Builder.create(responseAttachmentNameHeaders)
                    .setHeaders(toMapConverter(response.getHeaders()))
                    .build();

            new RestAssuredAttachmentProcessor().addAttachment(responseHeaders, new RestAssuredAttachmentRenderer());
        }

        return response;
    }

    private static Map<String, String> toMapConverter(final Iterable<? extends NameAndValue> items) {
        final Map<String, String> result = new HashMap<>();
        items.forEach(h -> result.put(h.getName(), h.getValue()));
        return result;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Step("{0}")
    protected void logStep(String logStep) {
        getLifecycle().updateStep(step -> step.getParameters().remove(0));
    }
}
