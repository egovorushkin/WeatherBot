package com.egovorushkin.telegrambot;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Evgeniш Govorushkin
 * @version 1.0
 */

public class Weather {
	private static volatile Weather instance;

	private Weather() {
	}

	public static Weather getInstance() {
		Weather currentInstance;
		if (instance == null) {
			synchronized (Weather.class) {
				if (instance == null) {
					instance = new Weather();
				}
				currentInstance = instance;
			}
		} else {
			currentInstance = instance;
		}
		return currentInstance;
	}

	// current weather
	public String currentWeather(Float lat, Float lon) throws IOException {
		String cityFound;
		String responseToUser = "";
		String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon
				+ "&units=metric&appid=49b6f70f5c9a9ca6d2bd3f22dd0fa8d9\n";
		URL obj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
		connection.setRequestMethod("GET");
		connection.setUseCaches(false);

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		JSONObject myResponse = new JSONObject(response.toString());

		if (myResponse.getInt("cod") == 200) {
			cityFound = myResponse.getString("name") + " (" + myResponse.getJSONObject("sys").getString("country")
					+ ")";
			String temp = (int) (myResponse.getJSONObject("main").getDouble("temp")) + " °C";
			String cloudiness = myResponse.getJSONObject("clouds").getInt("all") + " %";
			String weatherDesc = myResponse.getJSONArray("weather").getJSONObject(0).getString("description");
			responseToUser = String.format("Current weather in %s: %n Temperature: %s %n Overcast: %s %n Weather: %s",
					cityFound, temp, cloudiness, weatherDesc);
		}
		return responseToUser;
	}

	// weather forecast for the next day
	public String forecastWeather(Float lat, Float lon) throws IOException {
		String cityFound;
		String responseToUser = "";
		String url = "http://api.openweathermap.org/data/2.5/forecast/?lat=" + lat + "&lon=" + lon
				+ "&cnt=8&units=metric&appid=49b6f70f5c9a9ca6d2bd3f22dd0fa8d9\n";
		URL obj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
		connection.setRequestMethod("GET");
		connection.setUseCaches(false);

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		JSONObject myResponse = new JSONObject(response.toString());

		if (myResponse.getInt("cod") == 200) {
			cityFound = myResponse.getJSONObject("city").getString("name") + " ("
					+ myResponse.getJSONObject("city").getString("country") + ")";
			responseToUser = String.format("Weather forecast for the next day %s:%n %s%n", cityFound,
					convertWeatherToString(myResponse));
		}

		return responseToUser;
	}

	private String convertWeatherToString(JSONObject jsonObject) {
		String responseToUser = "";
		for (int i = 0; i < jsonObject.getJSONArray("list").length(); i++) {
			JSONObject internalJSON = jsonObject.getJSONArray("list").getJSONObject(i);
			responseToUser += convertToString(internalJSON);
		}
		return responseToUser;
	}

	private String convertToString(JSONObject internalJSON) {
		String responseToUser = "";
		String tempMax;
		String tempMin;
		String weather;
		tempMax = ((int) internalJSON.getJSONObject("main").getDouble("temp_max")) + " °C";
		tempMin = ((int) internalJSON.getJSONObject("main").getDouble("temp_min")) + " °C";
		String date2 = internalJSON.getString("dt_txt");
		JSONObject weatherObject = internalJSON.getJSONArray("weather").getJSONObject(0);
		weather = weatherObject.getString("description");
		responseToUser = String.format("Дата: %s%nПогода: %s%nМакс. темп.: %s%nМин. тем.: %s%n", date2, weather,
				tempMax, tempMin);
		return responseToUser;
	}

}
