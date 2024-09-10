package com.webapp;

import java.io.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class WebServlet extends HttpServlet {



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String apiKey="5556addae4d6fd02f64c93e97d710b1e";
			String city=request.getParameter("loc");
			if (city == null || city.isEmpty()) {
			    city = "London"; 
			}
			String apiUrl="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey;
			URL url=new URL(apiUrl);
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			InputStream ips = con.getInputStream();
			InputStreamReader reader = new InputStreamReader(ips);
			Scanner scanner = new Scanner(reader);

			StringBuilder respo = new StringBuilder();

			while (scanner.hasNextLine()) {
			    respo.append(scanner.nextLine()).append("\n");
			}

			scanner.close();
			Gson gson =new Gson();
			JsonObject jso=gson.fromJson(respo.toString(), JsonObject.class);
	
			long dateTimestamp =jso.get("dt").getAsLong()*1000;
			String date= new Date(dateTimestamp).toString();
			
			double tempKel=jso.getAsJsonObject("main").get("temp").getAsDouble();
			int tempCel=(int) (tempKel-273.15);
			
			int humidity=jso.getAsJsonObject("main").get("humidity").getAsInt();
			
			double windSpeed=jso.getAsJsonObject("wind").get("speed").getAsDouble();
			
			String weatherCondition = jso.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();


		
			request.setAttribute("date", date);
			request.setAttribute("city", city);
			request.setAttribute("temp", tempCel);
			request.setAttribute("weathercondition", weatherCondition);
			request.setAttribute("humidity", humidity);
			request.setAttribute("windspeed", windSpeed);
			request.setAttribute("weatherdata", respo);
			con.disconnect();
			RequestDispatcher rd= request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);
	}
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
