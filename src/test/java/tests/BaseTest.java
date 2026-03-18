package tests;

import constants.Constants;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import static io.restassured.RestAssured.baseURI;

public class BaseTest {

    public String token = "Bearer 3acab98a-efb7-463c-99dd-599baa1f8471";
    public Constants constants = new Constants();

    @BeforeTest
    public void setup() {
        baseURI = "https://vtanuxa.kaiten.ru/api/latest/";
        RestAssured.filters(new AllureRestAssured());
    }

    @AfterTest
    public void tearDown() {
    }
}
