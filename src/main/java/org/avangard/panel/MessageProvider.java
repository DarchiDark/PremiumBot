package org.avangard.panel;

import lombok.Getter;
import org.avangard.Main;
import org.avangard.codes.Code;
import org.avangard.codes.CodesDataBase;
import org.avangard.profile.Group;
import org.avangard.profile.Profile;
import org.avangard.profile.ProfileDatabase;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MessageProvider {

    public static List<Long> waitingForActivation = new ArrayList<>();

    public static String executeCode(String id, String code_id) {
        CodesDataBase codesDataBase = Main.codesDataBase;
        Code code = codesDataBase.getCode(code_id);
        if(code == null) {
            waitingForActivation.remove(Long.valueOf(id));
            return "❌ Код не верный\\!";
        }
        if(code.getTime().isBefore(LocalDate.now())) {
            waitingForActivation.remove(Long.valueOf(id));
            codesDataBase.deleteCode(code.getId());
            return "❌ Время жизни кода \\(\\3 дня\\) закончилось\\!";
        }
        if(code.getActivations() <= 0) {
            waitingForActivation.remove(Long.valueOf(id));
            codesDataBase.deleteCode(code.getId());
            return "❌ Активации закончились\\!";
        }
        if(code.isUnlimited()) {
            ProfileDatabase database = Main.profileDatabase;
            Group group = Group.PREMIUM;
            try {
                group = Group.valueOf(code.getPermission().toUpperCase());
            } catch (IllegalArgumentException ignored) {}
            database.setGroup(Long.parseLong(id), group, Date.valueOf(LocalDate.now().plusDays(code.getPermissionTime())));
            waitingForActivation.remove(Long.valueOf(id));
            return "✅ Код верный\\! Вы активировали "+code.getPermission()+" \\(" + code.getPermissionTime() + " дней\\)";
        }
        else {
            ProfileDatabase database = Main.profileDatabase;
            Group group = Group.PREMIUM;
            try {
                group = Group.valueOf(code.getPermission().toUpperCase());
            } catch (IllegalArgumentException ignored) {}
            database.setGroup(Long.parseLong(id), group, Date.valueOf(LocalDate.now().plusDays(code.getPermissionTime())));
            codesDataBase.decrementActivations(code.getId());
            waitingForActivation.remove(Long.valueOf(id));
            return "✅ Код верный\\! Вы активировали "+code.getPermission()+"\\(до " + code.getPermissionTime() + "\\)";
        }
    }

    public static String activate(String id) {
        waitingForActivation.add(Long.valueOf(id));
        return "\uD83D\uDC8E Напишите сюда код, данный админом, для активации подписки.";
    }

    public static String admin_panel() {
        return "\uD83D\uDC8E Вы вошли в режим админ-панели. Выберите действие ниже. Чтобы выйти, нажмите или напишите \"Выйти из админки\"";
    }

    public static String admin_panel_quit() {
        return "\uD83D\uDC8E Вы вышли из админ-панели.";
    }

    public static String admin_panel_referral_choose() {
        return "\uD83C\uDF1F Рефералы. Выберите следующие действие: \"✅ Добавить\", \"\uD83D\uDCCA Статистика\", \" ❌ Удалить\" ";
    }

    public static String admin_panel_codes_main() {
        return "\uD83D\uDC8E Коды. Выберите следующие действие: \"✅ Добавить\", \"❌ Удалить\"";
    }

    public static String onStart() {
        return "Добро пожаловать в Avangard Premium bot \uD83D\uDC4B!";
    }

    public static String onProfile(String chatId) {
        ProfileDatabase database = Main.profileDatabase;
        Profile profile = database.getProfile(Long.parseLong(chatId));
        if(profile.getExpireTime() != null) {
            LocalDate expireDate = profile.getExpireTime().toLocalDate();

            String formatted = expireDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                    .replaceAll("\\d", "\\\\$0") // экранируем цифры
                    .replace(".", "\\.");      // экранируем точки
            return "Ваш профиль \uD83D\uDCBC \nВаш айди \uD83C\uDD94\\: " + chatId +
                    "\nВаша подписка \uD83C\uDFA9\\: " + profile.getGroup().name() + (expireDate.getYear() < 4000 ? "\nПодписка истекает\\: " + formatted : "\nПодписка истекает\\: Навсегда");
        }
        return "Ваш профиль \uD83D\uDCBC \nВаш айди \uD83C\uDD94\\: " + chatId +
                "\nВаша подписка \uD83C\uDFA9\\: " + profile.getGroup().name();
    }

    // TODO
    public static String onContent(String chatId) {
        ProfileDatabase database = Main.profileDatabase;
        Profile profile = database.getProfile(Long.parseLong(chatId));
        if(profile.getExpireTime() != null) {
            LocalDate expireDate = profile.getExpireTime().toLocalDate();

            String formatted = expireDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                    .replaceAll("\\d", "\\\\$0") // экранируем цифры
                    .replace(".", "\\.");      // экранируем точки
            return "Ваш профиль \uD83D\uDCBC \nВаш айди \uD83C\uDD94\\: " + chatId +
                    "\nВаша подписка \uD83C\uDFA9\\: " + profile.getGroup().name() + (expireDate.getYear() < 4000 ? "\nПодписка истекает\\: " + formatted : "\nПодписка истекает\\: Навсегда");
        }
        return "Ваш профиль \uD83D\uDCBC \nВаш айди \uD83C\uDD94\\: " + chatId +
                "\nВаша подписка \uD83C\uDFA9\\: " + profile.getGroup().name();
    }

    public static String onBuy(String chatId) {
        return "\uD83D\uDCB5 Напишите на аккаунт @test. Ваш личный код: " + chatId;
    }
}
