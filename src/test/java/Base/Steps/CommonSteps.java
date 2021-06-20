package Base.Steps;

import Base.utils.Environment;
import com.google.common.base.Preconditions;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CommonSteps {

    private static boolean isRequestLoggingEnabled;

    static { isRequestLoggingEnabled = Boolean.parseBoolean(Environment.get("settings.request.logging.enabled"));
    }

    private Response lastResponse;

    protected ValidatableResponse then() {
        return lastResponse().then();
    }

    protected Response lastResponse() {
        Preconditions.checkNotNull(lastResponse, "Response should be created");
        return lastResponse;
    }

    @Step
    public void responseShouldBeSuccess() {
        then().assertThat().statusCode(200);
    }

    @Step
    public void responseShouldBeSuccess201() {
        then().assertThat().statusCode(201);
    }

    @Step
    public void responseShouldBeBadRequest() { then().assertThat().statusCode(400);}

    @Step
    public void responseShouldBeUnauthorized() {
        then().assertThat().statusCode(401);
    }

    @Step
    public void responseShouldBeForbidden() {
        then().assertThat().statusCode(403);
    }

    @Step
    public void responseShouldBeNotFound() {
        then().assertThat().statusCode(404);
    }

    @Step
    public void responseShouldBeInternalServerError() {
        then().assertThat().statusCode(500);
    }

    @Step
    public void jsonSchemaValidation(String pathToSchema){
        then().assertThat().body(matchesJsonSchemaInClasspath(pathToSchema));}

    protected RequestSpecification getRequestSpecWithLogging(RequestSpecBuilder requestSpecBuilder) {
        return isRequestLoggingEnabled
                ? requestSpecBuilder.addFilter(new AllureRestAssured())
                .build()
                .log()
                .all()
                : requestSpecBuilder.build();
    }

    protected Response getResponseWithLogging(Response response) {
        if (isRequestLoggingEnabled) {
            response.body().print();
        }

        lastResponse = response;
        return response;
    }

    protected RequestSpecBuilder getCommonRequestSpecBuilder(){
        return new RequestSpecBuilder()
                .setBaseUri(Environment.get("base.url"));
    }

}

