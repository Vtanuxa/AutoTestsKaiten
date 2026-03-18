package tests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

@Epic("E2E тесты для создания пространства, доски, колонки")
@Feature("Создание объектов")

public class E2ECreateDeleteColumn extends BaseTest{

    private final HashMap<String, Object> dataMap = new HashMap<>();

    @Test(priority=1)
    @Step("Создание нового пространства (space)")
    @Description("Создание нового пространства")
    @Severity(SeverityLevel.BLOCKER)
    @Flaky
    public void createSpace(){
        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        " \"title\": \"Myspace\",\n" +
                        " \"external_id\": 1}")
                .when()
                .post(constants.SPACES)
                .then()
                .log()
                .all()
                .extract().response();

        dataMap.put("space_id", response.jsonPath().getInt("id"));
        Assert.assertEquals(200, response.statusCode());
        Assert.assertEquals(response.jsonPath().getString("title"), "Myspace");
        Assert.assertEquals(response.jsonPath().getString("external_id"), "1");
        Assert.assertEquals(response.jsonPath().getString("archived"), "false");
    }

    @Test(priority=2)
    @Step("Создание доски (board) в пространстве с ID: {spaceId}")
    @Description("Создание доски с колонками To do, In progress, Done и дополнительными настройками")
    @Severity(SeverityLevel.NORMAL)
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

    @Test(priority=3)
    @Step("Создание новой колонки (column)")
    @Flaky
    @Description("Создание колонки")
    @Severity(SeverityLevel.NORMAL)
    public void createColumn(){
        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        " \"title\": \"TanyaOfColumn\"\n" +
                        "}")
                .when()
                .post(constants.BOARDS + "/" + dataMap.get("board_id") + constants.COLUMNS)
                .then()
                .log()
                .all()
                .extract().response();
        dataMap.put("column_id", response.jsonPath().getInt("id"));
        Assert.assertEquals(200, response.statusCode());
    }

    @Test(priority=4)
    @Step("Удаление колонки (column)")
    @Flaky
    @Description("Удаление колонки")
    @Severity(SeverityLevel.NORMAL)
    public void deleteColumn(){
        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when()
                .delete(constants.BOARDS + "/" + dataMap.get("board_id") + constants.COLUMNS + "/" +  dataMap.get("column_id"))
                .then()
                .log()
                .all()
                .extract().response();
        Assert.assertEquals(200, response.statusCode());
    }

    @Test(priority=5)
    @Step("Удаление доски (board)")
    @Flaky
    @Description("Удаление доски")
    @Severity(SeverityLevel.NORMAL)
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

    @Test(priority = 6)
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
