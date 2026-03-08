package tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class E2ECreateDeleteSpace extends BaseTest {

    private final HashMap<String, Object> dataMap = new HashMap<>();

    @Test(priority = 1)
    public void createNewSpase() {
        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        " \"title\": \"Myspace!!!\",\n" +
                        " \"external_id\": 1}")
                .when()
                .post(constants.SPACES)
                .then()
                .log()
                .all()
                .extract().response();

        dataMap.put("space_id", response.jsonPath().getInt("id"));
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(response.jsonPath().getString("title"), "Myspace!!!");
        Assert.assertEquals(response.jsonPath().getString("external_id"), "1");
        Assert.assertEquals(response.jsonPath().getString("archived"), "false");
    }

    @Test(priority = 2)
    public void deleteSpase() {
        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when()
                .delete(constants.SPACES + dataMap.get("space_id"))
                .then()
                .log()
                .all()
                .extract().response();

        dataMap.put("space_id", response.jsonPath().getString("id"));
        Assert.assertEquals(200, response.statusCode());

    }
}