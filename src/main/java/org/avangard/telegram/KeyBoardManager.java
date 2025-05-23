package org.avangard.telegram;

import org.avangard.Main;
import org.avangard.profile.Group;
import org.avangard.profile.ProfileDatabase;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyBoardManager {
    // keyBoardBuilder("Рефералы \uD83C\uDF1F%nb%Коды \uD83D\uDC8E%nl%Выйти из админки \uD83D\uDEAA");
    public static ReplyKeyboardMarkup keyBoardBuilder(String keyboardStr) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        for (String line : keyboardStr.split("%nl%")) {
            KeyboardRow row = new KeyboardRow();
            for (String button : line.split("%nb%")) {
                row.add(button.trim());
            }
            keyboard.add(row);
        }

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup getUserPanel(String chatId) {
        String keyboardStr = "Профиль \uD83D\uDCBC%nb%Активировать код \uD83D\uDC8E%nl%Купить подписку \uD83D\uDCB5";
        ProfileDatabase database = Main.profileDatabase;
        if (database.getProfile(Long.parseLong(chatId)).getGroup().equals(Group.ADMIN)) {
            keyboardStr += "%nb%Админ-панель \uD83D\uDC68\u200D\uD83D\uDCBB";
        }
        return keyBoardBuilder(keyboardStr);
    }
}
