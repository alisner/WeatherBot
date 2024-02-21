package com.projects.WeatherBot.controller;

import com.projects.WeatherBot.entity.WeatherData;
import com.projects.WeatherBot.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    WeatherService weatherService;

    @Value("${bot.name}")
    private String botName;

    public TelegramBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            String memberName = update.getMessage().getFrom().getFirstName();

            if (messageText.startsWith("/")) {
                switch (messageText) {
                    case "/start":
                        startBot(chatId, memberName);
                        break;
                    default:
                        log.info("Unexpected message");
                }
            }
            else { // Поиск погоды по городу
                WeatherData weatherData = weatherService.getCurrentWeatherByName(messageText);
                if (weatherData.getMain() != null) {
                    String text = "Город: " + weatherData.getName() +
                            "\nТемпература: " + weatherData.getMain().getTemp() +
                            "\nОщущается как: " + weatherData.getMain().getFeels_like() +
                            "\nПогода: " + weatherData.getWeather().get(0).getDescription() +
                            "\nВлажность: " + weatherData.getMain().getHumidity() +
                            "\nВетер: " + weatherData.getWind().getSpeed() +
                            "\nДавление: " + weatherData.getMain().getPressure();
                    sendMessage(chatId, text);
                }
                else {
                    sendMessage(chatId, "Город с таким именем не найден!");
                }
            }
        }
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет " + userName + "! Я телеграм бот для поиска погоды.\n" +
                "Чтобы использовать меня введи название города!");
        try {
            execute(message);
            log.info("Reply sent");
        }
        catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
            log.info("Reply sent");
        }
        catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}

