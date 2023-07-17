package me.cocos.cocosmines.language.impl;

import me.cocos.cocosmines.language.Language;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EnglishLanguage implements Language {

    private final Map<String, Object> translations;

    public EnglishLanguage() {
        this.translations = new HashMap<>();
        // modifications
        this.translations.put("cancel", "cancel");
        this.translations.put("confirm", "confirm");
        this.translations.put("modification-percentage-set", "&7Write &a% &7in chat!");
        this.translations.put("modification-actionbar", "&8● &7Type &c\"Cancel\" &7to cancel &8●");
        this.translations.put("modification-first-location", "&7Type &a\"confirm\" &7to confirm the first location");
        this.translations.put("modification-second-location", "&7Type &a\"confirm\" &7to confirm the second location");
        this.translations.put("modification-change-name", "&7Write the &aname &7in chat!");
        this.translations.put("modification-time-regeneration", "&7Write the &aregeneration time &7in chat");
        // edit menu
        this.translations.put("editing", "&8>> &7Editing &e&l");
        this.translations.put("edit-change-name", "&8● &7Change name");
        this.translations.put("edit-change-icon", "&8● &7Set icon");
        this.translations.put("edit-change-time-regeneration", "&8● &7Set regeneration time");
        this.translations.put("edit-change-blocks", "&8● &7Change blocks");
        this.translations.put("edit-change-location", "&8● &7Change location");
        this.translations.put("edit-reset-mine", "&8● &7Reset mine");
        this.translations.put("edit-teleport", "&8● &7Teleport");
        // errors
        this.translations.put("cannot-find-mine", "&cCannot find a mine with the name &l");
        this.translations.put("must-be-number", "&cYou must enter a number!");
        this.translations.put("regeneration-time-error", "&8>> &7Regeneration time cannot be less than 1!");
        this.translations.put("percent-error", "&c% cannot be greater than 100 or less than 0");
        // hologram
        this.translations.put("hologram-lines", List.of(
                "&8● &a&lGenerator {NAME} &8●",
                "",
                "&a⌚ &fTime until regeneration: &a{TIME}")
        );
        // lores
        this.translations.put("block-action-lore", List.of(
                "",
                "&8[&fLMB&8] &8- &7Remove block from generator",
                "&8[&fRMB&8] &8- &7Set % for generation",
                "",
                "&8● &7Current %&8: &6{CHANCE}%"
        ));
        this.translations.put("block-info-lore", List.of(
                "",
                "&8● &7Creator: &e{OWNER}",
                "&8● &7Creation date: &e{CREATION-TIME}",
                "&8● &7Coordinates: &e{LOCATION}",
                ""
        ));
        // commands
        this.translations.put("correct-usage", "&8>> &7Correct usage&8: ");
        this.translations.put("no-permission", "&8>> &cYou don't have permission (cocosmines.admin)");

        this.translations.put("create-description", "creates a mine");
        this.translations.put("create-arguments", " <name> <regeneration-time>");

        this.translations.put("panel-no-mines", "&cThere are no mines created!");
        this.translations.put("panel-description", "opens the mine panel");

        this.translations.put("teleport-description", "teleports to a mine");
        this.translations.put("teleport-arguments", " <name>");

        this.translations.put("remove-mine-message", "&aSuccessfully removed mine &l");
        this.translations.put("remove-description", "removes a mine");
        this.translations.put("remove-arguments", " <name>");

        this.translations.put("list-message", "&7List of mines&8:");
        this.translations.put("list-description", "list of mines");

        this.translations.put("actionbar-regeneration", "&8● &7Time until regeneration&8: &f{TIME} &8●");
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

