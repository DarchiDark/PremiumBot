package org.avangard.panel.admin;

import javafx.util.Pair;
import lombok.SneakyThrows;
import org.avangard.codes.Code;
import org.avangard.codes.CodesDataBase;
import org.avangard.panel.MessageProvider;
import org.avangard.profile.ReferralDataBase;
import org.avangard.telegram.KeyBoardManager;
import org.avangard.telegram.TelegramBot;
import org.avangard.util.Config;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

public class APanelMessages {
    @SneakyThrows
    public static void request(String chatId, AdminPanel adminPanel, TelegramBot telegramBot, String messageText) {
        if(messageText.endsWith("Обратно")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Возвращаюсь обратно..");
            sendMessage.setReplyMarkup(KeyBoardManager.keyBoardBuilder("Рефералы \uD83C\uDF1F%nb%Коды \uD83D\uDC8E%nl%Выйти из админки \uD83D\uDEAA"));
            telegramBot.execute(sendMessage);
            adminPanel.setEditDirection(EditDirection.NOTHING);
            return;
        }

        if(messageText.startsWith("Выйти из админки")) {
            APanelManager.quit(Long.parseLong(chatId));
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            ReplyKeyboardMarkup keyboardMarkup = KeyBoardManager.getUserPanel(chatId);
            sendMessage.setReplyMarkup(keyboardMarkup);
            sendMessage.setText(MessageProvider.admin_panel_quit());
            telegramBot.execute(sendMessage);
            return;
        }

        if (adminPanel.getEditDirection() == EditDirection.NOTHING) {
            String answer = adminPanel(messageText, chatId).getKey();
            ReplyKeyboardMarkup markup = adminPanel(messageText, chatId).getValue();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(answer);
            if(markup != null) {
                sendMessage.setReplyMarkup(markup);
            }
            telegramBot.execute(sendMessage);
            return;
        }

        if (adminPanel.getEditDirection() == EditDirection.REFERRAL_MAIN) {
            if (messageText.endsWith("Добавить")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Напишите имя цели");
                try {
                    telegramBot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                adminPanel.setEditDirection(EditDirection.REFERRAL_ADD);
            }
            if (messageText.endsWith("Удалить")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Напишите имя цели");
                telegramBot.execute(sendMessage);
                adminPanel.setEditDirection(EditDirection.REFERRAL_REMOVE);
            }
            if (messageText.endsWith("Статистика")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Напишите имя цели. Для полной статистики напишите \"all\"");
                telegramBot.execute(sendMessage);
                adminPanel.setEditDirection(EditDirection.REFERRAL_STATISTICS);
            }
            return;
        }

        if (adminPanel.getEditDirection() == EditDirection.REFERRAL_STATISTICS) {
            ReferralDataBase referralDataBase = new ReferralDataBase(Config.getString("Url"), Config.getString("UserName"), Config.getString("Password"));
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            if(messageText.toLowerCase().startsWith("all")) {
                StringBuilder builder = new StringBuilder();
                builder.append("Топ по активациям \uD83D\uDCCA (10 мест): \n");
                int count = 1;
                for(Map.Entry<String, Integer> pos : referralDataBase.topReferrals(10).entrySet()) {
                    builder.append(count).append(". ").append(pos.getKey()).append(" => ").append(pos.getValue()).append("\n");
                    count++;
                }
                sendMessage.setText(builder.toString());
            } else {
                sendMessage.setText("Количество активаций у " + messageText + ": " + referralDataBase.statistics(messageText));
            }
            ReplyKeyboardMarkup keyboardMarkup = KeyBoardManager.keyBoardBuilder("✅ Добавить%nb%\uD83D\uDCCA Статистика%nl%❌ Удалить%nl%↪️ Обратно");
            sendMessage.setReplyMarkup(keyboardMarkup);
            telegramBot.execute(sendMessage);
            adminPanel.setEditDirection(EditDirection.REFERRAL_MAIN);
            return;
        }

        if (adminPanel.getEditDirection() == EditDirection.REFERRAL_ADD) {
            ReferralDataBase referralDataBase = new ReferralDataBase(Config.getString("Url"), Config.getString("UserName"), Config.getString("Password"));
            referralDataBase.add(messageText);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Цель с именем " + messageText + " добавлена!");
            ReplyKeyboardMarkup keyboardMarkup = KeyBoardManager.keyBoardBuilder("✅ Добавить%nb%\uD83D\uDCCA Статистика%nl%❌ Удалить%nl%↪️ Обратно");
            sendMessage.setReplyMarkup(keyboardMarkup);
            telegramBot.execute(sendMessage);
            adminPanel.setEditDirection(EditDirection.REFERRAL_MAIN);
            return;
        }

        if (adminPanel.getEditDirection() == EditDirection.REFERRAL_REMOVE) {
            ReferralDataBase referralDataBase = new ReferralDataBase(Config.getString("Url"), Config.getString("UserName"), Config.getString("Password"));
            referralDataBase.remove(messageText);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            ReplyKeyboardMarkup keyboardMarkup = KeyBoardManager.keyBoardBuilder("✅ Добавить%nb%\uD83D\uDCCA Статистика%nl%❌ Удалить%nl%↪️ Обратно"    );
            sendMessage.setReplyMarkup(keyboardMarkup);
            sendMessage.setText("Цель с именем " + messageText + " удалена!");
            telegramBot.execute(sendMessage);
            adminPanel.setEditDirection(EditDirection.REFERRAL_MAIN);
            return;
        }

        if (adminPanel.getEditDirection() == EditDirection.CODES_ADD) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);

            CodesDataBase codesDataBase = new CodesDataBase(
                    Config.getString("Url"), Config.getString("UserName"), Config.getString("Password")
            );

            String[] parts = messageText.split(",");
            if (parts.length != 4) {
                sendMessage.setText("Используйте формат: количество,привилегия,время,дни-действия-кода\nПример: 3,vip,1mo,5 или unlim,premium,7d,5");
                try {
                    telegramBot.execute(sendMessage);
                } catch (TelegramApiException ex) {
                    throw new RuntimeException(ex);
                }
                return;
            }

            String countArg = parts[0].trim();
            String permission = parts[1].trim();
            String timeArg = parts[2].trim();
            String CodeTimeArg = parts[3].trim();

            Code code;
            try {
                boolean unlimited = countArg.equalsIgnoreCase("unlim");
                int activations = unlimited ? 100 : Integer.parseInt(countArg);

                long time = getTime(timeArg);
                int days = Integer.parseInt(CodeTimeArg);

                code = Code.generate(
                        unlimited,
                        activations,
                        permission,
                        time,
                        days
                );


                codesDataBase.addCode(code);
                sendMessage.setText(
                        "Вы создали новый код 💎!\n" +
                                "Публичный айди кода: " + code.getCode() + "\n" +
                                "Приватный айди: " + code.getId() + "\n" +
                                "Привилегия: " + code.getPermission() + "\n" +
                                "Действие кода закончится " + code.getTime() + "!"
                );
            } catch (Exception e) {
                sendMessage.setText("Используйте формат: количество,привилегия,время,дни-действия-кода\nПример: 3,vip,1mo,5 или unlim,premium,7d,5");
            }

            sendMessage.setReplyMarkup(KeyBoardManager.keyBoardBuilder("✅ Добавить%nb%❌ Удалить%nl%↪️ Обратно"));
            try {
                telegramBot.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            adminPanel.setEditDirection(EditDirection.CODES_MAIN);
        }

        if (adminPanel.getEditDirection() == EditDirection.CODES_REMOVE) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);

            CodesDataBase codesDataBase = new CodesDataBase(
                    Config.getString("Url"), Config.getString("UserName"), Config.getString("Password")
            );

            codesDataBase.deleteCode(messageText);
            sendMessage.setText("Код с айди " + messageText + " удалён!");
            sendMessage.setReplyMarkup(KeyBoardManager.keyBoardBuilder("✅ Добавить%nb%❌ Удалить%nl%↪️ Обратно"));
            try {
                telegramBot.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            adminPanel.setEditDirection(EditDirection.CODES_MAIN);
        }

        if (adminPanel.getEditDirection() == EditDirection.CODES_MAIN) {
            if (messageText.endsWith("Добавить")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Используйте формат: количество,привилегия,время \nПример: 3,vip,1mo или unlim,premium,7d");
                try {
                    telegramBot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                adminPanel.setEditDirection(EditDirection.CODES_ADD);
            }
            if (messageText.endsWith("Удалить")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Напишите приватный айди кода");
                try {
                    telegramBot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                adminPanel.setEditDirection(EditDirection.CODES_REMOVE);
            }
        }
    }

    private static long getTime(String timeArg) {
        long time = 0;
        if (timeArg.endsWith("d")) {
            int days = Integer.parseInt(timeArg.replace("d", ""));
            time+=days;
        } else if (timeArg.endsWith("mo")) {
            int months = Integer.parseInt(timeArg.replace("mo", ""));
            time+= months*30L;
        } else if (timeArg.endsWith("y")) {
            int years = Integer.parseInt(timeArg.replace("y", ""));
            time+=years*12L*30;
        } else {
            throw new IllegalArgumentException("Неверный формат времени.");
        }
        return time;
    }

    private static Pair<String, ReplyKeyboardMarkup> adminPanel(String messageText, String chatId) {
        switch (messageText) {
            case "Рефералы":
            case "Рефералы \uD83C\uDF1F":
            {
                AdminPanel adminPanel = APanelManager.editor(Long.parseLong(chatId));
                if (adminPanel != null) {
                    adminPanel.setEditDirection(EditDirection.REFERRAL_MAIN);
                }
                return new Pair<>(MessageProvider.admin_panel_referral_choose(), KeyBoardManager.keyBoardBuilder("✅ Добавить%nb%\uD83D\uDCCA Статистика%nl%❌ Удалить%nl%↪️ Обратно"));
            }
            case "Коды \uD83D\uDC8E":
            case "Коды":
            {
                AdminPanel adminPanel = APanelManager.editor(Long.parseLong(chatId));
                if (adminPanel != null) {
                    adminPanel.setEditDirection(EditDirection.CODES_MAIN);
                }
                return new Pair<>(MessageProvider.admin_panel_codes_main(), KeyBoardManager.keyBoardBuilder("✅ Добавить%nb%❌ Удалить%nl%↪️ Обратно"));
            }
        }

        return new Pair<>("\uD83D\uDC8E Вы в режиме админ-панели. Выберите действие ниже. Чтобы выйти, нажмите или напишите \"Выйти из админки\"", KeyBoardManager.keyBoardBuilder("Рефералы \uD83C\uDF1F%nb%Коды \uD83D\uDC8E%nl%Выйти из админки \uD83D\uDEAA"));
    }
}
