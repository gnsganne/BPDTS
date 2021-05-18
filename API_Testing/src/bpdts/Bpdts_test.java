package bpdts;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import java.io.FileWriter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Bpdts_test
{
	String allusers,londonusers,londoninradius;
	RequestSpecification req;
	ResponseSpecification resp;
	Response response;
	
	@BeforeClass
	public void setUp()
	{
		req=new RequestSpecBuilder().setBaseUri("https://bpdts-test-app.herokuapp.com").setContentType(ContentType.JSON).build();
		resp=new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();		
	}
	@Test
	public void get_AllUsers()
	{
		response=		
			given().spec(req)						
			.when()
				.get("/users")
			.then()
				.assertThat().spec(resp).extract().response();		
		allusers=response.asString();
		writeintofile("allusers.json",allusers);
	}
	@Test
	public void get_User_City()
	{
		response=
			given().spec(req)					
			.when()
				.get("/city/London/users")
			.then()
			    .assertThat().spec(resp).extract().response();		
		londonusers=response.asString();
		writeintofile("londonusers.json",londonusers);
	}
	@Test
	public void get_usersRadius()
	{
		response=
			given().spec(req)				
			.when()
				.get("/city/London/users?lat=51.5074&long=0.1278&dist<=50")
			.then()
			     .assertThat().spec(resp).extract().response();
		
		londoninradius=response.asString();	
		writeintofile("withinradius.json",londoninradius);
	}
	public void writeintofile(String filename,String data)
	{
		try {
			 FileWriter file = new FileWriter("./"+filename);
	         file.write(data);
	         file.flush();
	         file.close();
	         }catch(Exception e) {}
	}
	@AfterClass
	public void displayData()
	{
		System.out.println("===================All users=======================");
		System.out.println(allusers);
		System.out.println("===================users in London=======================");
		System.out.println(londonusers);
		System.out.println("===================users in London and within 50 miles=======================");
		System.out.println(londoninradius);
	}
	
}
