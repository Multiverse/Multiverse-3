package com.mvplugin.core.command;

import com.mvplugin.core.MultiverseWorld;
import com.mvplugin.core.plugin.MultiverseCore;
import com.mvplugin.core.util.Language;
import org.jetbrains.annotations.NotNull;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.Message;
import pluginbase.messages.Theme;
import pluginbase.minecraft.BasePlayer;

public abstract class ModifyCommandBase extends MultiverseCommand {

    protected ModifyCommandBase(@NotNull final MultiverseCore plugin) {
        super(plugin);
    }

    protected void showPropertyList(BasePlayer sender) {
        getMessager().message(sender, Language.Command.Modify.ALL_PROPERTIES_LIST, getAllPropertyNames());
    }

    private String getAllPropertyNames() {
        String[] properties = MultiverseWorld.getAllPropertyNames();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < properties.length; i++) {
            buffer.append(Theme.INFO);
            if (i != 0) {
                buffer.append(", ");
            }
            if (i % 2 == 0) {
                buffer.append(Theme.LIST_EVEN);
            } else {
                buffer.append(Theme.LIST_ODD);
            }
            buffer.append(properties[i]);
        }
        return buffer.toString();
    }

    protected void showPropertyDescription(BasePlayer sender, String propertyName) {
        getMessager().message(sender, getPropertyDescription(propertyName));
    }

    private BundledMessage getPropertyDescription(String propertyName) {
        try {
            String descriptionKey = MultiverseWorld.getPropertyDescriptionKey(propertyName);
            String description = getMessager().getLocalizedMessage(descriptionKey != null ? descriptionKey : "");
            return Message.bundleMessage(Language.Command.Modify.PROPERTY_DESCRIPTION, propertyName, description);
        } catch (NoSuchFieldException e) {
            return Message.bundleMessage(Language.Command.Modify.NO_SUCH_PROPERTY, propertyName);
        }
    }
}
