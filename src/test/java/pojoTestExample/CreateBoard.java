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
import pojo.Board;
import pojo.Space;
import tests.BaseTest;

import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class CreateBoard extends BaseTest {

    Space space = new Space();
    Board board = new Board();

    private final HashMap<String, Object> dataMap = new HashMap<>();

    @Test(priority=1)
    @Step("Создание нового пространства pojo (space)")
    @Description("Создание нового пространства используя pojo")
    @Severity(SeverityLevel.NORMAL)
    public void createSpase() {
        File jsonFile = new File("src/test/resources/jsonFiles/CreateSpace.json");
        space.setTitle(JsonPath.from(jsonFile).getString("title"));
        space.setDescription(JsonPath.from(jsonFile).getString("description"));
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
        Assert.assertEquals(response.jsonPath().getString("external_id"), "1");

        dataMap.put("space_id", response.jsonPath().getInt("id"));
    }

    @Test(priority = 2)
    @Step("Создание доски (board) в пространстве с ID: {spaceId}")
    @Description("Создание доски с колонками To do, In progress, Done и дополнительными настройками. (Pojo)")
    public void createBoard(){
        File jsonFile = new File("src/test/resources/jsonFiles/CreateBoard.json");
        board.setTitle(JsonPath.from(jsonFile).getString("title"));
        board.setDescription(JsonPath.from(jsonFile).getString("description"));

        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(board)
                .when()
                .post(constants.SPACES + dataMap.get("space_id") + constants.BOARDS)
                .then()
                .log()
                .all()
                .extract().response();
        dataMap.put("board_id", response.jsonPath().getInt("id"));
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(response.jsonPath().getString("title"), "PojoExampleCreateBoard");
    }

    @Test(priority = 3)
    @Step("Удаление доски (board)")
    public void deleteBoard(){

        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        " \"force\": true\n" +
                        "}")
                .when()
                .delete(constants.SPACES + dataMap.get("space_id") + constants.BOARDS +"/" + dataMap.get("board_id"))
                .then()
                .log()
                .all()
                .extract().response();
        Assert.assertEquals(200, response.statusCode());
    }

    @Test(priority = 4)
    @Step("Удаление пространства (space)")
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
