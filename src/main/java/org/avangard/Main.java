package org.avangard;

import org.avangard.codes.CodesDataBase;
import org.avangard.profile.ProfileDatabase;
import org.avangard.telegram.TelegramBot;
import org.avangard.util.Config;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class Main {
    public static Path path;

    public static CodesDataBase codesDataBase = new CodesDataBase(Config.getString("Url"), Config.getString("UserName"), Config.getString("Password"));
    public static ProfileDatabase profileDatabase = new ProfileDatabase(Config.getString("Url"), Config.getString("UserName"), Config.getString("Password"));

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Specify path to config in arguments (java -jar ___.jar C:\\...)");
            return;
        }
        try {
            path = Path.of(args[0]);
        } catch (InvalidPathException e) {
            System.out.println("Invalid path! Specify path to config in arguments (java -jar ___.jar C:\\... (for windows. For linux or mac else) )");
            return;
        }
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
}