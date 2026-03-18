package tests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

@Epic("E2E тесты для создания и удаления пространства, доски")
@Feature("Создание объектов")

public class E2ECreateDeleteBoard  extends BaseTest{

    private final HashMap<String, Object> dataMap = new HashMap<>();

    @Test(priority=1)
    @Step("Создание нового пространства (space)")
    @Description("Создание нового пространства")
    @Severity(SeverityLevel.NORMAL)
    public void createSpase() {
        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        " \"title\": \"E2EE2ECreateDeleteBoard\",\n" +
                        " \"external_id\": 1}")
                .when()
                .post(constants.SPACES)
                .then()
                .log()
                .all()
                .extract().response();
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(response.jsonPath().getString("title"), "E2EE2ECreateDeleteBoard");
        Assert.assertEquals(response.jsonPath().getString("external_id"), "1");
        Assert.assertEquals(response.jsonPath().getString("archived"), "false");

        dataMap.put("space_id", response.jsonPath().getInt("id"));
    }

    @Test(priority = 2)
    @Step("Создание доски (board) в пространстве с ID: {spaceId}")
    @Description("Создание доски с колонками To do, In progress, Done и дополнительными настройками")
    public void createBoard(){
        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"title\": \"Test Tanya\",\n" +
                        "  \"columns\": [\n" +
                        "    {\n" +
                        "      \"title\": \"To do\",\n" +
                        "      \"type\": 1\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"title\": \"In progress\",\n" +
                        "      \"type\": 2\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"title\": \"Done\",\n" +
                        "      \"type\": 3\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"lanes\": [\n" +
                        "    {\n" +
                        "      \"title\": \"Вот что такое lanes\",\n" +
                        "      \"type\": 2\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"description\": \"my description\",\n" +
                        "  \"top\": 1,\n" +
                        "  \"left\": 1,\n" +
                        "  \"default_card_type_id\": 1,\n" +
                        "  \"first_image_is_cover\": false,\n" +
                        "  \"reset_lane_spent_time\": false,\n" +
                        "  \"automove_cards\": false,\n" +
                        "  \"backward_moves_enabled\": false,\n" +
                        "  \"auto_assign_enabled\": false,\n" +
                        "  \"sort_order\": 1,\n" +
                        "  \"external_id\": 5\n" +
                        "}")
                .when()
                .post(constants.SPACES + dataMap.get("space_id") + constants.BOARDS)
                .then()
                .log()
                .all()
                .extract().response();
        dataMap.put("board_id", response.jsonPath().getInt("id"));
        Assert.assertEquals(200, response.statusCode());
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
