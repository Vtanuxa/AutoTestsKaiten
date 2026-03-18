package tests;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;

import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

@Epic("E2E тесты для создания пространства, доски, карточки")
@Feature("Создание объектов")

public class CreateSpaceBoardCard extends BaseTest {

    private final HashMap<String, Object> dataMap = new HashMap<>();

    @Test(priority = 1)
    @Step("Создание нового пространства (space)")
    @Description("Создание нового пространства")
    @Severity(SeverityLevel.NORMAL)
    public void createNewSpase(){
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

    @Test(priority = 2)
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
//        dataMap.put("column_id", response.jsonPath().getInt("column_id"));
        Assert.assertEquals(200, response.statusCode());
    }

    @Test(priority = 3)
    @Step("Создание новой карточки (card)")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Создание карточки на доске")
    public void createNewCard(){
        Response response = given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        " \"title\": 1,\n" +
                        " \"board_id\": " + dataMap.get("board_id") + "\n" +
                        "}")

                .when()
                .post(constants.CARDS)
                .then()
                .log()
                .all()
                .extract().response();
        Assert.assertEquals(200, response.statusCode());
    }
}
