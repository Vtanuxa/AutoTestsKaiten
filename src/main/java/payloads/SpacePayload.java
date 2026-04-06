package payloads;

public class SpacePayload {

    public static String postBody(String title, String external_id) {
        String body = "{" +
                "\"title\": \"" + title + "\"," + "\" \"external_id\": \"" + external_id + "\"}";
        return body;
    }
}
