package tests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Epic("Тест для создания пространства")
@Feature("Создание объектов")

public class CreateSpase extends BaseTest{

    @Test
    @Step("Создание нового пространства (space)")
    @Description("Создание нового пространства")
    @Severity(SeverityLevel.NORMAL)
    public void createSpase(){
        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        " \"title\": \"test\",\n" +
                        " \"external_id\": 1}")
                .when()
                .post(constants.SPACES)
                .then()
                .log()
                .all()
                .extract().response();
            Assert.assertEquals(200, response.statusCode());
            Assert.assertEquals(response.jsonPath().getString("title"), "test");
            Assert.assertEquals(response.jsonPath().getString("external_id"), "1");
        Assert.assertEquals(response.jsonPath().getString("archived"), "false");
    }
}
