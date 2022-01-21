package Base.Tests;

import Base.BodyForRequests.BodyForCreateUsers;
import Base.Steps.ExampleSteps.ExampleSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExampleTest extends ExampleSteps {

    @DisplayName("Проверяем код 200 на запрос списка юзеров")
    @Test
    public void getUsers(){
        getUsersList();
        responseChekSuccess();
    }

    @DisplayName("Проверяем модель по конкретному юзеру(4)")
    @Test
    public void getUser(){
        getSingleUser(4);
        responseChekSuccess();
        jsonSchemaValidation("schema/userSchema.json");
    }

    @DisplayName("Создаем юзера")
    @Test
    public void createUser(){
        createUser(new BodyForCreateUsers(
                "stiven",
                "capitan_america"));
        responseCheckSuccess201();
        jsonSchemaValidation("schema/createUsersSchema.json");
    }
}
