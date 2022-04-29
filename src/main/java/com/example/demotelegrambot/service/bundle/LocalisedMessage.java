package com.example.demotelegrambot.service.bundle;

public final class LocalisedMessage {

    private final String key;

    private final String text;

    public LocalisedMessage(String key, String text) {
        this.key = key;
        this.text = text;
    }

    public LocalisedMessage applyPlaceholder(String placeholder, Object replacement) {
        String placeholderValue = "%" + placeholder + "%";
        String replacementValue = replacement == null ? "" : replacement.toString();

        return new LocalisedMessage(key, text.replaceAll(placeholderValue, replacementValue));
    }

    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }
}
