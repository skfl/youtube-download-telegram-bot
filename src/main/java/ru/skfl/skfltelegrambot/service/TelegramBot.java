package ru.skfl.skfltelegrambot.service;

import com.sapher.youtubedl.YoutubeDLException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.skfl.skfltelegrambot.config.BotConfiguration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfiguration botConfiguration;
    @Autowired
    private YouTubeDownloadService downloadService;

    @Override
    public String getBotUsername() {
        return botConfiguration.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfiguration.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                URL url = new URL(update.getMessage().getText());
                sendVideoMessage(update.getMessage().getChatId(), downloadService.download(url.toString()));
            } catch (MalformedURLException e) {
                sendTextMessage(update.getMessage().getChatId(), "You should provide valid url");
                throw new RuntimeException(e);
            } catch (YoutubeDLException e) {
                sendTextMessage(update.getMessage().getChatId(), "An error occurred while downloading");
                throw new RuntimeException(e);
            }
        }
    }

    private void sendTextMessage(long chatId, String messageText) {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(chatId);
        messageToSend.setText(messageText);
        try {
            execute(messageToSend);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendVideoMessage(long chatId, File file) {
        SendVideo videoToSend = new SendVideo();
        videoToSend.setChatId(chatId);
        videoToSend.setVideo(new InputFile(file));
        try {
            execute(videoToSend);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
