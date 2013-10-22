package com.mvplugin.core.util;

import com.mvplugin.core.util.Language.Config;
import org.jetbrains.annotations.NotNull;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.messages.Message;

public class ChatUtil {

    public static final char COLOR_CHAR = '&';
    public static final String PLAYER_MARKER = "%p";
    public static final String WORLD_MARKER = "%w";
    public static final String MESSAGE_MARKER = "%m";

    public static final String DEFAULT_CHAT_FORMAT_STRING = COLOR_CHAR + "f[" + WORLD_MARKER + COLOR_CHAR + "f]<" + PLAYER_MARKER + COLOR_CHAR + "f> " + MESSAGE_MARKER;

    public static void validateChat(@NotNull String chatFormat) throws PropertyVetoException {
        int playerIndex = chatFormat.indexOf("%p");
        int messageIndex = chatFormat.indexOf("%m");
        if (playerIndex == -1) {
            throw new PropertyVetoException(Message.bundleMessage(Config.CHAT_FORMAT_MISSING_PLAYER));
        }
        if (messageIndex == -1) {
            throw new PropertyVetoException(Message.bundleMessage(Config.CHAT_FORMAT_MISSING_MESSAGE));
        }
        if (playerIndex > messageIndex) {
            throw new PropertyVetoException(Message.bundleMessage(Config.CHAT_FORMAT_INVALID_ORDER));
        }
    }
}
