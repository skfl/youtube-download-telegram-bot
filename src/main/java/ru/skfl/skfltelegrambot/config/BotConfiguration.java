package ru.skfl.skfltelegrambot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
@RequiredArgsConstructor
@Getter
@Setter
public class BotConfiguration {
    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;
}
