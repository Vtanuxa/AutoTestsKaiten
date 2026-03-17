package regexExample;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.BaseTest;


import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class RegexExample extends BaseTest {

    String pattern = "\\d";
    String pattern1 = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    String pattern2 = "^[a-z]+(_[a-z]+)*$";
    String pattern3 = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z";
    String pattern4 = ".*space.*";
    String pattern5 = "[1-9]\\d{5}";

    private final HashMap<String, Object> dataMap = new HashMap<>();

    @Test(priority = 1)
    public void createSpase(){
        File jsonFile = new File("src/test/resources/jsonFiles/CreateSpace.json");
        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(jsonFile)
                .when()
                .post(constants.SPACES)
                .then()
                .log()
                .all()
                .extract().response();
        dataMap.put("space_id", response.jsonPath().getInt("id"));
        Assert.assertEquals(200, response.statusCode());
            Assert.assertEquals(response.jsonPath().getString("title"), "RegexExampleCreateSpase");
        Assert.assertEquals(response.jsonPath().getString("external_id"), "1");
        Assert.assertEquals(response.jsonPath().getString("archived"), "false");
    }

    @Test(priority = 2)
    public void getSpase(){
        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when()
                .get(constants.SPACES + dataMap.get("space_id"))
                .then()
                .log()
                .all()
                .extract().response();
        dataMap.put("space_id", response.jsonPath().getInt("id"));
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(response.jsonPath().getString("title"), "RegexExampleCreateSpase");
        Assert.assertTrue(response.jsonPath().getString("external_id").matches(pattern));
        Assert.assertTrue(response.jsonPath().getString("author_uid").matches(pattern1));
        Assert.assertTrue(response.jsonPath().getString("access").matches(pattern2));
        Assert.assertTrue(response.jsonPath().getString("created").matches(pattern3));
        Assert.assertTrue(response.jsonPath().getString("entity_type").matches(pattern4));
        Assert.assertTrue(response.jsonPath().getString("path").matches(pattern5));
        Assert.assertEquals(response.jsonPath().getString("archived"), "false");
    }

    @Test(priority = 3)
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
        Assert.assertTrue(response.jsonPath().getString("uid").matches(pattern1));
        Assert.assertTrue(response.jsonPath().getString("id").matches(pattern5));
        Assert.assertTrue(response.jsonPath().getString("path").matches(pattern5));
    }
}
