package dataProviderExample;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payloads.SpacePayload;
import tests.BaseTest;

import static io.restassured.RestAssured.given;


public class DataProviderExample extends BaseTest {

    @Test(dataProvider = "TitleAndExternalId")
    @Step("Создание нового пространства (space)")
    @Description("data provider пример")
    @Severity(SeverityLevel.NORMAL)
    public void createSpase(String title, String external_id){

        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(SpacePayload.postBody(title, external_id))
                .when()
                .post(constants.SPACES)
                .then()
                .log()
                .all()
                .extract().response();
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(response.jsonPath().getString("title"), title);
        Assert.assertEquals(response.jsonPath().getString("external_id"), external_id);

    }

    @DataProvider(name = "TitleAndExternalId")
    public Object[][] getData(){
        return new Object[][]{
                {"Data provider title","2"}, {"Rest-assured", "1"}};
        }
    }

