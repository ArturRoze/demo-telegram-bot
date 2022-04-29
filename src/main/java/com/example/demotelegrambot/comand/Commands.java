package com.example.demotelegrambot.comand;

public enum Commands {

    START("/start"),
    HELP("/help"),
    CONTACT("/contact"),
    EMAIL("/email");

    private final String command;

    Commands(String command) {
        this.command = command;
    }

    public String getCommandText() {
        return command;
    }

}
