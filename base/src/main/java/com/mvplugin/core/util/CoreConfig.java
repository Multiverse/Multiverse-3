package com.mvplugin.core.util;

import org.jetbrains.annotations.Nullable;
import pluginbase.config.SerializationRegistrar;
import pluginbase.config.annotation.Comment;
import pluginbase.config.annotation.ValidateWith;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.config.field.Validator;
import pluginbase.plugin.Settings;

/**
 * This is where you will find all of the configuration options for the Multiverse-Core plugin and potentially some
 * options used here and in other Multiverse plugins.
 */
public class CoreConfig extends Settings {

    static {
        SerializationRegistrar.registerClass(Chat.class);
    }

    private Chat chat = new Chat();

    public boolean isFormattingChat() {
        return chat.formattingChat;
    }

    public void setFormattingChat(boolean formattingChat) {
        chat.formattingChat = formattingChat;
    }

    public String getChatFormatString() {
        return chat.chatFormatString;
    }

    public void setChatFormatString(String formatString) {
        chat.chatFormatString = formatString;
    }

    @Comment("Settings related to chat handling.")
    private static class Chat {

        @Comment({
                "If this is set to true, Multiverse will format ALL chat messages.",
                "If you would rather have a chat plugin handle chat message prefixes, make sure to set this to false."
        })
        private boolean formattingChat = true;
        @Comment({
                "When formattingChat is true, this is the message format that will be used.",
                ChatUtil.WORLD_MARKER + " - Replaced by the world's name/alias",
                ChatUtil.PLAYER_MARKER + " - Replaced by the player's name.",
                ChatUtil.MESSAGE_MARKER + " - Replaced by the chat message",
                ChatUtil.PLAYER_MARKER + " must ALWAYS come before " + ChatUtil.MESSAGE_MARKER + "!",
                "Please be aware that world aliases and player names are often colored and may affect any colors before the " + ChatUtil.WORLD_MARKER + " symbol.",
                "Color should be indicated with the " + ChatUtil.COLOR_CHAR + " symbol"
        })
        @ValidateWith(ChatFormatValidator.class)
        private String chatFormatString = ChatUtil.DEFAULT_CHAT_FORMAT_STRING;

        private static class ChatFormatValidator implements Validator<String> {
            @Nullable
            @Override
            public String validateChange(@Nullable String newValue, @Nullable String oldValue) throws PropertyVetoException {
                ChatUtil.validateChat(newValue != null ? newValue : "");
                return newValue;
            }
        }
    }
}
