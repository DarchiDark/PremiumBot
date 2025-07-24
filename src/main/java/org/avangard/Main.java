package org.avangard;

import lombok.SneakyThrows;
import org.avangard.codes.CodesDataBase;
import org.avangard.profile.ProfileDatabase;
import org.avangard.telegram.TelegramBot;
import org.avangard.util.Config;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static CodesDataBase codesDataBase;
    public static ProfileDatabase profileDatabase;

    public static void main(String[] args) {
        generateFiles();
        codesDataBase = new CodesDataBase(Config.getString("Url"), Config.getString("UserName"), Config.getString("Password"));
        profileDatabase = new ProfileDatabase(Config.getString("Url"), Config.getString("UserName"), Config.getString("Password"));
        TelegramBotsApi botsApi;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            System.out.println("Bot started!");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        try {
            botsApi.registerBot(new TelegramBot(Config.getString("Bot_token")));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static void generateFiles() {
        File config_file = new File("config.yml");
        if(!config_file.exists()) {
            boolean isCreated = config_file.createNewFile();
            if(isCreated) {
                String defaultConfig = """
                        Url: jdbc:mysql://localhost:3306/premiumbot
                        UserName: root
                        Password: admin
                        Bot_token: default_token
                        """;

                Files.write(Paths.get(config_file.getPath()), defaultConfig.getBytes());
            }
        }

        File content_folder = new File("content");
        if(!content_folder.exists()) {
            content_folder.mkdir();
        }

    }
}