package tests;

import constants.Constants;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import static io.restassured.RestAssured.baseURI;

public class BaseTest {

    public String token = "Bearer 3acab98a-efb7-463c-99dd-599baa1f8471";
    Constants constants = new Constants();

    @BeforeTest
    public void setup() {
        baseURI = "https://vtanuxa.kaiten.ru/api/latest/";
    }

    @AfterTest
    public void tearDown() {
    }
}
