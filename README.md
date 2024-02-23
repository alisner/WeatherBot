# WeatherBot

## Описание сервиса

Язык программирования - `Java`, Фреймворк - `Spring`, Библиотеки: [TelegramBots](https://github.com/rubenlagus/TelegramBots) для создания бота, [Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html) для запросов к *OpenWeather API*.

Реализована модель long polling бот - боту не требуется внешний IP и какой-либо деплой - можно запускать на любой машине, имеющей доступ к [TelegramBots](https://github.com/rubenlagus/TelegramBots)

Этот Telegram-бот принимает местоположение пользователя в виде текста, а затем сообщает о текущих прогнозах погоды.

Информация о погоде предоставляется *OpenWeatherMap*.

Бот отправляет информацию о текущей температуре, ощущаемой температуре, облачности, влажности, ветре и давлении.

## Процесс работы

- Пользователь отправляет боту название города.
- Из этих данных формируется запрос к [openweathermap](https://openweathermap.org/) и из полученного ответа получаются данные о температуре и погодном статусе.
- Результат отправляется пользователю текстовым сообщением.

## Как запустить

Добавить в application.properties название бота и токен (получается у [@BotFather](https://telegram.me/botfather) при создании нового бота командой /newbot) и ключ [OpenWeather API](https://openweathermap.org/).

```
server.port=8084

bot.name=...
bot.token=...

weather.id=...
```

Для запуска необходима java версии 17 и maven.
```
mvn package
java -jar WeatherBot-0.0.1-SNAPSHOT.jar
```
