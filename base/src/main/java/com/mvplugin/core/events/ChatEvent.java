package com.mvplugin.core.events;

public class ChatEvent {

    private String worldName;
    private String format;

    public ChatEvent(String sendersWorldName, String format) {
        this.worldName = sendersWorldName;
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getWorldName() {
        return worldName;
    }
}
