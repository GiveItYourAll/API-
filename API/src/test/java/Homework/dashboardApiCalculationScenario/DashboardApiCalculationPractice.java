package Homework.dashboardApiCalculationScenario;

import io.restassured.response.Response;
import net.minidev.json.JSONArray;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jayway.jsonpath.JsonPath;

import io.restassured.http.ContentType;

public class DashboardApiCalculationPractice {

	@Test
	public void dashboard() {
		String dashboardUrl = "http://dev-mb.yoll.io/api/dashboard";
		String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjExNjkwIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvbmFtZSI6IkVsc2hhblIiLCJBc3BOZXQuSWRlbnRpdHkuU2VjdXJpdHlTdGFtcCI6IjhiYzM3Y2Y2LTg4NTctNDY1OS1hMjRiLTc0MDc4ZTQxNTI4NCIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IkNvbXBhbnkiLCJodHRwOi8vd3d3LmFzcG5ldGJvaWxlcnBsYXRlLmNvbS9pZGVudGl0eS9jbGFpbXMvdGVuYW50SWQiOiIxIiwic3ViIjoiMTE2OTAiLCJqdGkiOiI3YjBlYzM1Yi0yMmU4LTQ4ZWUtODk4NS00OTQ1YjVhZjhkNjgiLCJpYXQiOjE3MTM1ODM3MzgsIm5iZiI6MTcxMzU4MzczOCwiZXhwIjoxNzQ1MTE5NzM4LCJpc3MiOiJNZWFsQiIsImF1ZCI6Ik1lYWxCIn0.nDS4fMaWBYraeDZGrUU39YBxO_yzJNFmLDBU-VRoY5c";

		Response dashboardResponse = given()

				.header("Authorization", "bearer " + token).contentType(ContentType.JSON).when().get(dashboardUrl);
		dashboardResponse.prettyPrint();

		double otherExpenses = JsonPath.read(dashboardResponse.asString(), "$.result.otherExpenses");
		System.out.println("Other expenses are " + otherExpenses);

		double travelExpenses = JsonPath.read(dashboardResponse.asString(), "$.result.travelExpenses");
		System.out.println("Travel expenses are " + travelExpenses);

		double giftExpenses = JsonPath.read(dashboardResponse.asString(), "$.result.giftExpenses");
		System.out.println("Gift expenses are " + giftExpenses);

		String getExpensesUrl = "https://dev-mb.yoll.io/api/Expenses";

		Response getExpenseResponse = given().header("Authorization", "bearer " + token).relaxedHTTPSValidation()
				.contentType(ContentType.JSON).when().get(getExpensesUrl);
		getExpenseResponse.prettyPrint();
		JSONArray expensesArray = JsonPath.read(getExpenseResponse.asString(), "$.result");
		int arrayLength = expensesArray.size();
		Double amount = null;
		double totalOtherExpenses = 0;
		double totalTravelExpenses = 0;
		double totalGiftExpenses = 0;

		for (int i = 0; i < arrayLength; i++) {
			int expenseType = JsonPath.read(getExpenseResponse.asString(), "$.result[" + i + "].expenseType");
			amount = JsonPath.read(getExpenseResponse.asString(), "$.result[" + i + "].amount");

			if (expenseType == 10) {
				totalOtherExpenses += amount;
			} else if (expenseType == 40) {
				totalTravelExpenses += amount;
			} else if (expenseType == 50) {
				totalGiftExpenses += amount;
			} else {
				System.out.println("Skipping index " + i + " due to expenseType " + expenseType);
				continue;
			}
		}

		totalOtherExpenses = Math.round(totalOtherExpenses * 100.0) / 100.0;
		System.out.println("This is the sum of other expenses " + "$" + totalOtherExpenses);
		System.out.println("This is the sum of travel expenses " + "$" + totalTravelExpenses);
		System.out.println("This is the sum of gift expenses " + "$" + totalGiftExpenses);

		assertTrue(totalOtherExpenses == otherExpenses);
		assertTrue(totalTravelExpenses == travelExpenses);
		assertTrue(totalGiftExpenses == giftExpenses);
	}
}
