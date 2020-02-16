package com.egovorushkin.telegrambot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Evgenii Govorushkin
 * @version 1.0
 */

public class MainClass {
	public static void main(String[] args) {
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

		try {
			telegramBotsApi.registerBot(new Bot());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

		TimerTask timerTask = new WeatherShedule();
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, 86400000);

	}
}