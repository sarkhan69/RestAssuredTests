package Base.Steps;

import Base.utils.Environment;
import Base.utils.restassured.AllureRestAssured;
import com.google.common.base.Preconditions;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class MainSteps {

    private static boolean LogON;

    static { LogON = Boolean.parseBoolean(Environment.get("settings.request.logging.enabled"));
    }

    private Response actualResponse;

    protected ValidatableResponse then() {
        return lastResponse().then();
    }

    protected Response lastResponse() {
        Preconditions.checkNotNull(actualResponse, "Response should be created");
        return actualResponse;
    }

    @Step
    public void responseChekSuccess() {
        then().assertThat().statusCode(200);
    }

    @Step
    public void responseCheckSuccess201() {
        then().assertThat().statusCode(201);
    }

    @Step
    public void responseChekBadRequest() { then().assertThat().statusCode(400);}

    @Step
    public void responseCheckNotFound() {
        then().assertThat().statusCode(404);
    }

    @Step
    public void responseCheckServerError() {
        then().assertThat().statusCode(500);
    }

    @Step
    public void jsonSchemaValidation(String pathToSchema){
        then().assertThat().body(matchesJsonSchemaInClasspath(pathToSchema));}

    @Step
    public String getValue(String path){
        JsonPath jsonPath = new JsonPath(actualResponse.asString());

        return jsonPath.get(path) != null? jsonPath.get(path).toString(): "нет поля";
    }

    protected RequestSpecification getRequestSpecWithLogging(RequestSpecBuilder requestSpecBuilder) {
        return LogON
                ? requestSpecBuilder.addFilter(new AllureRestAssured())
                .build()
                .log()
                .all()
                : requestSpecBuilder.build();
    }

    protected Response getResponseWithLogging(Response response) {
        if (LogON) {
            response.body().print();
        }

        actualResponse = response;
        return response;
    }

    protected RequestSpecBuilder getCommonRequestSpecBuilder(){
        return new RequestSpecBuilder()
                .setBaseUri(Environment.get("base.url"));
    }

}

