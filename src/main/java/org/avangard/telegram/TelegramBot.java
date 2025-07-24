package org.avangard.telegram;

import org.avangard.Main;
import org.avangard.panel.MessageProvider;
import org.avangard.panel.admin.APanelManager;
import org.avangard.panel.admin.APanelMessages;
import org.avangard.panel.admin.AdminPanel;
import org.avangard.profile.Group;
import org.avangard.profile.Profile;
import org.avangard.profile.ProfileDatabase;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TelegramBot extends TelegramLongPollingBot {
    private final Map<Long, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_MS = 400;

    public TelegramBot(String id) {
        super(id);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatIdLong = update.getMessage().getChatId();
            String chatId = String.valueOf(chatIdLong);

            long now = System.currentTimeMillis();
            long last = cooldowns.getOrDefault(chatIdLong, 0L);
            if (now - last < COOLDOWN_MS) {
                sendMessage(chatId);
                return;
            }
            cooldowns.put(chatIdLong, now);

            Thread thread = new Thread(() -> {
                Profile profile = Main.profileDatabase.getProfile(chatIdLong);
                if(profile != null) {
                    if(profile.getExpireTime() != null && profile.getExpireTime().before(Date.valueOf(LocalDate.now()))) {
                        Main.profileDatabase.setGroup(chatIdLong, Group.DEFAULT, null);
                    }
                }

                AdminPanel panel = APanelManager.editor(chatIdLong);
                if (panel != null) {
                    APanelMessages.request(chatId, panel, this, messageText);
                }

                if (messageText.startsWith("/auth iG1RQGHAQuM5TFE")) { // Generate unique password!!!!
                    ProfileDatabase database = Main.profileDatabase;
                    database.setGroup(chatIdLong, Group.ADMIN, Date.valueOf(LocalDate.now().plusYears(5555)));
                    return;
                }

                if (messageText.startsWith("/start")) {
                    TelegramMessages.start(chatId, this, messageText);
                    return;
                }

                if (messageText.startsWith("Профиль")) {
                    TelegramMessages.profile(chatId, this);
                    return;
                }

                if (messageText.startsWith("Купить подписку")) {
                    TelegramMessages.buy(chatId, this);
                    return;
                }

                if (messageText.startsWith("Активировать код")) {
                    TelegramMessages.activate(chatId, this);
                    return;
                }

                if (messageText.startsWith("Админ-панель")) {
                    TelegramMessages.admin_panel(chatId, this);
                    return;
                }

                if (MessageProvider.waitingForActivation.contains(chatIdLong)) {
                    TelegramMessages.activate_code(chatId, this, messageText);
                }
            });
            thread.start();
        }
    }

    private void sendMessage(String chatId) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText("Не так быстро!");
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "d65sehgu4e_bot";
    }
}
