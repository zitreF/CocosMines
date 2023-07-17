package me.cocos.cocosmines.language.impl;

import me.cocos.cocosmines.language.Language;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PolishLanguage implements Language {

    private final Map<String, Object> translations;

    public PolishLanguage() {
        this.translations = new HashMap<>();
        // modifications
        this.translations.put("cancel", "anuluj");
        this.translations.put("confirm", "potwierdz");
        this.translations.put("modification-percentage-set", "&7Napisz &a% &7na chacie!");
        this.translations.put("modification-actionbar", "&8● &7Wpisz &c\"Anuluj\" &7aby anulowac &8●");
        this.translations.put("modification-first-location", "&7Napisz &a\"potwierdz\" &7aby potwierdzic pierwsza lokalizacje");
        this.translations.put("modification-second-location", "&7Napisz &a\"potwierdz\" &7aby potwierdzic druga lokalizacje");
        this.translations.put("modification-change-name", "&7Napisz &anazwe &7na chacie!");
        this.translations.put("modification-time-regeneration", "&7Napisz &aczas regeneracji &7na chacie");
        // edit menu
        this.translations.put("editing", "&8>> &7Edytowanie &e&l");
        this.translations.put("edit-change-name", "&8● &7Zmien nazwe");
        this.translations.put("edit-change-icon", "&8● &7Ustaw ikone");
        this.translations.put("edit-change-time-regeneration", "&8● &7Ustaw czas regeneracji");
        this.translations.put("edit-change-blocks", "&8● &7Zmien bloki");
        this.translations.put("edit-change-location", "&8● &7Zmien lokalizacje");
        this.translations.put("edit-reset-mine", "&8● &7Resetuj kopalnie");
        this.translations.put("edit-teleport", "&8● &7Przeteleportuj");
        // errors
        this.translations.put("cannot-find-mine", "&cNie znaleziono kopalni o nazwie &l");
        this.translations.put("must-be-number", "&cMusisz wpisac liczbe!");
        this.translations.put("regeneration-time-error", "&8>> &7Czas regeneracji nie moze byc mniejszy niz 1!");
        this.translations.put("percent-error", "&c% nie moze byc wiekszy niz 100 lub mniejszy niz 0");
        // hologram
        this.translations.put("hologram-lines", List.of(
                "&8● &a&lGenerator {NAME} &8●",
                "",
                "&a⌚ &fCzas do regeneracji: &a{TIME}")
        );
        // lores
        this.translations.put("block-action-lore", List.of(
                "",
                "&8[&fLPM&8] &8- &7Usuwa blok z generatora",
                "&8[&fPPM&8] &8- &7Ustawia % na wygenerowanie",
                "",
                "&8● &7Aktualny %&8: &6{CHANCE}%"
        ));
        this.translations.put("block-info-lore", List.of(
                "",
                "&8● &7Tworca: &e{OWNER}",
                "&8● &7Data zalozenia: &e{CREATION-TIME}",
                "&8● &7Kordy: &e{LOCATION}",
                ""
        ));
        // commands
        this.translations.put("correct-usage", "&8>> &7Poprawne uzycie&8: ");
        this.translations.put("no-permission", "&8>> &cNie posiadasz uprawnien (cocosmines.admin)");

        this.translations.put("create-description", "tworzy kopalnie");
        this.translations.put("create-arguments", " <nazwa> <czas-regeneracji>");

        this.translations.put("panel-no-mines", "&cNie ma stworzonych zadnych kopalni!");
        this.translations.put("panel-description", "otwiera panel z kopalniami");

        this.translations.put("teleport-description", "teleportuje do kopalni");
        this.translations.put("teleport-arguments", " <nazwa>");

        this.translations.put("remove-mine-message", "&aPomyslnie usunieto kopalnie &l");
        this.translations.put("remove-description", "usuwa kopalnie");
        this.translations.put("remove-arguments", " <nazwa>");

        this.translations.put("list-message", "&7Lista kopalni&8:");
        this.translations.put("list-description", "lista kopalni");

        this.translations.put("actionbar-regeneration", "&8● &7Czas do regeneracji&8: &f{TIME} &8●");
    }

    @Override
    public <T> T translate(String text, Class<T> type) {
        Object object = this.translations.getOrDefault(text, "");
        return type.cast(object);
    }

    @Override
    public Map<String, Object> getTranslations() {
        return Collections.unmodifiableMap(this.translations);
    }
}
