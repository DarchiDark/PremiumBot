package org.avangard.telegram;

import org.avangard.Main;
import org.avangard.panel.MessageProvider;
import org.avangard.panel.admin.APanelManager;
import org.avangard.panel.admin.AdminPanel;
import org.avangard.profile.Group;
import org.avangard.profile.ProfileDatabase;
import org.avangard.profile.ReferralDataBase;
import org.avangard.util.Config;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramMessages {
    public static void profile(String chatId, TelegramBot telegramBot) {
        String text = "*Загружаю данные\\.\\.\\.*";
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode("MarkdownV2");
        try {
            Message sent = telegramBot.execute(message);
            text = MessageProvider.onProfile(chatId);
            EditMessageText edit = new EditMessageText();
            edit.setChatId(chatId);
            edit.setMessageId(sent.getMessageId());
            edit.setParseMode("MarkdownV2");
            edit.setText(text);
            ReplyKeyboardMarkup keyboardMarkup = KeyBoardManager.getUserPanel(chatId);
            message.setReplyMarkup(keyboardMarkup);
            telegramBot.execute(edit);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    // ToDo
    public static void availableContent(String chatId, TelegramBot telegramBot) {
        String text = "*Загружаю данные\\.\\.\\.*";
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode("MarkdownV2");
        try {
            Message sent = telegramBot.execute(message);
            text = MessageProvider.onProfile(chatId);
            EditMessageText edit = new EditMessageText();
            edit.setChatId(chatId);
            edit.setMessageId(sent.getMessageId());
            edit.setParseMode("MarkdownV2");
            edit.setText(text);
            ReplyKeyboardMarkup keyboardMarkup = KeyBoardManager.getUserPanel(chatId);
            message.setReplyMarkup(keyboardMarkup);
            telegramBot.execute(edit);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public static void buy(String chatId, TelegramBot telegramBot) {
        String text = MessageProvider.onBuy(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        ReplyKeyboardMarkup keyboardMarkup = KeyBoardManager.getUserPanel(chatId);
        message.setReplyMarkup(keyboardMarkup);
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public static void activate(String chatId, TelegramBot telegramBot) {
        String text = MessageProvider.activate(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        ReplyKeyboardMarkup keyboardMarkup = KeyBoardManager.getUserPanel(chatId);
        message.setReplyMarkup(keyboardMarkup);
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public static void admin_panel(String chatId, TelegramBot telegramBot) {
        ProfileDatabase database = Main.profileDatabase;
        if (database.getProfile(Long.parseLong(chatId)).getGroup() == Group.ADMIN) {
            String text = MessageProvider.admin_panel();
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(text);
            ReplyKeyboardMarkup keyboardMarkup = KeyBoardManager.keyBoardBuilder("Рефералы \uD83C\uDF1F%nb%Коды \uD83D\uDC8E%nl%Выйти из админки \uD83D\uDEAA");
            message.setReplyMarkup(keyboardMarkup);
            try {
                telegramBot.execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            APanelManager.getEditSessions().add(new AdminPanel(Long.parseLong(chatId)));
        }
    }

    public static void activate_code(String chatId, TelegramBot telegramBot, String messageText) {
        String text = MessageProvider.executeCode(chatId, messageText);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode("MarkdownV2");
        ReplyKeyboardMarkup keyboardMarkup = KeyBoardManager.getUserPanel(chatId);
        message.setReplyMarkup(keyboardMarkup);
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public static void start(String chatId, TelegramBot telegramBot, String messageText) {
        String[] parts = messageText.split(" ", 2);
        if (parts.length > 1) {
            String startParam = parts[1];
            ReferralDataBase referralDataBase = new ReferralDataBase(Config.getString("Url"), Config.getString("UserName"), Config.getString("Password"));
            referralDataBase.incrementActivations(chatId, startParam);
        }
        String text = MessageProvider.onStart();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = KeyBoardManager.getUserPanel(chatId);
        message.setReplyMarkup(keyboardMarkup);

        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
