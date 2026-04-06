package pojoTestExample;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.Space;
import tests.BaseTest;

import java.io.File;

import static io.restassured.RestAssured.given;

public class CreateSpace extends BaseTest {

    Space space = new Space();

    @Test
    @Step("Создание нового пространства (space)")
    @Description("Создание нового пространства используя pojo")
    @Severity(SeverityLevel.NORMAL)
    public void createSpase(){
        File jsonFile = new File("src/test/resources/jsonFiles/CreateSpace.json");
        space.setTitle(JsonPath.from(jsonFile).getString("title"));
        space.setExternal_id(JsonPath.from(jsonFile).getString("external_id"));
        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(space)
                .when()
                .post(constants.SPACES)
                .then()
                .log()
                .all()
                .extract().response();
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(response.jsonPath().getString("title"), "PojoExampleCreateSpace");
        Assert.assertEquals(response.jsonPath().getString("external_id"), space.getExternal_id());
    }

}
