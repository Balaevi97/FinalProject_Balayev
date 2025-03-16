package Calls;

import io.restassured.response.Response;
import org.json.JSONObject;

import static Steps.FrontSteps.LogInSteps.token;
import static Utils.APIRequestSpec.getRequestSpec;
import static Utils.APIRequestSpec.getResponseSpec;
import static Utils.URL.MyCredoAccountList;
import static io.restassured.RestAssured.given;

public class MyCredoGetAccountList {


    public static Response  GetAccountListWithPersonID () {
        String URL;

        Response response = given()
                .spec(getRequestSpec(MyCredoAccountList, token))
                .when()
                .get("/graphql");

        response.then().log().all().spec(getResponseSpec());
        return response;
    }

    public static Response  PostAccountListWithPersonID (String Query) {
        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg2YTA0OGZmNWJiNTYzNmZkMGRhMWY3ODczMGRlZDZjIiwidHlwIjoiSldUIn0.eyJuYmYiOjE3NDE5Njk4ODEsImV4cCI6MTc0NDU2MTg4MSwiaXNzIjoiaHR0cDovL3Rlc3RpZGVudGl0eXNzby5teWNyZWRvLmdlOjQwNDQxIiwiYXVkIjpbImh0dHA6Ly90ZXN0aWRlbnRpdHlzc28ubXljcmVkby5nZTo0MDQ0MS9yZXNvdXJjZXMiLCJjcmVkb19hcGkiLCJrZWVwel9hcGkiLCJyZXRhaWxfbG9hbnNfYXBpIiwicmV0YWlsX29mZmVyc19hcGkiLCJyZXRhaWxfdHJhbnNmZXJzX2FwaSJdLCJjbGllbnRfaWQiOiJteWNyZWRvX3dlYl9jbGllbnQiLCJzdWIiOiI3MjQ5MiIsImF1dGhfdGltZSI6MTc0MTk2OTg4MSwiaWRwIjoibG9jYWwiLCJVc2VyTmFtZSI6ImJiYWxhZXZpOTciLCJBY2Nlc3NUeXBlIjoiZnVsbCIsIkN1c3RvbWVySWQiOiIyMTU3MTIxIiwiUGVyc29uYWxOdW1iZXIiOiIwMTg3MDAwMDIyMyIsIlBob25lIjoiNTU1MjI1NjgzIiwiSGFzVGVtcFBhc3N3b3JkIjoiRmFsc2UiLCJDaGFubmVsIjoiV0VCIiwiVHJhY2VJZCI6IjZUdWhramVYUUFCTXlDSjUxOFhLSnRiWXNmaEU2OXpSV0I2TUFRNGQ2QU09Iiwic2NvcGUiOlsiY3JlZG9fYXBpIiwia2VlcHpfYXBpIiwicmV0YWlsX2xvYW5zX2FwaSIsInJldGFpbF9vZmZlcnNfYXBpIiwicmV0YWlsX3RyYW5zZmVyc19hcGkiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsicHdkIl19.sgbrfSFVYCBVWxeyLxrTVeAI9XAigIcWdJy3RiZAz3EU01YkFkjblqEXv_lArzco_Xo2jUswJREptpgXx2H7UK5zRbl6KVF82onimmtNevEFqRdghZGqsxPozkeL16p8MYKjak7qqVQKjw7tlBk7yEQRpq_tB7ufjYYrciRnHj_s594flch3mU_m6HgoSiac6s9TrSSVVcsyxm59jj2cTa9dM6jnPhM-35OBlgi1eR1835cppp4U6FYsQdkJXTSI66NhHpmNPRYktlu5Ikw0OKYAkDU5Xi79CAgmPdcyhKJJRaYIJKKHpdk4YPXnCL6RWm9kit4cFR6I2FeW1L6_EQ";
        Response response = given()
                .spec(getRequestSpec(MyCredoAccountList, token))
                .body(Query)
                .when()
                .post("/graphql");

        response.then().spec(getResponseSpec());
        return response;
    }
}
