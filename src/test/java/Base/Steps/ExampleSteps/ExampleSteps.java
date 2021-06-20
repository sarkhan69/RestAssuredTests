package Base.Steps.ExampleSteps;

import Base.BodyForRequests.BodyForCreateUsers;
import Base.Steps.CommonSteps;
import Base.utils.Environment;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ExampleSteps extends CommonSteps{
    protected RequestSpecBuilder exampleSpec() {
        return getCommonRequestSpecBuilder()
                .setBasePath(Environment.get("base.path"));
    }

    @Step("Получаем список юзеров")
    public Response getUsersList() {
        return getResponseWithLogging(
                given()
                        .spec(getRequestSpecWithLogging(exampleSpec()))
                        .get("users"));
    }

    @Step("Получаем конкретного юзера по id")
    public Response getSingleUser(int idUsers){
        return getResponseWithLogging(
                given()
                        .spec(getRequestSpecWithLogging(exampleSpec()))
                        .get("users/" + idUsers));
    }

    @Step("Создаем нового ЮЗЕРА")
    public Response createUser(BodyForCreateUsers body){
        return getResponseWithLogging(
                given()
                        .spec(getRequestSpecWithLogging(exampleSpec()))
                        .body(body)
                        .post("users"));
    }
}
