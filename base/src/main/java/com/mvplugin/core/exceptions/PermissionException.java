package com.mvplugin.core.exceptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.messages.BundledMessage;
import pluginbase.permission.Perm;

public class PermissionException extends MultiverseException {

    @Nullable
    private Perm missingPermission;

    public PermissionException(@NotNull final BundledMessage languageMessage) {
        this(languageMessage, (Perm) null);
    }

    public PermissionException(@NotNull final BundledMessage languageMessage, @NotNull final Throwable throwable) {
        this(languageMessage, null, throwable);
    }

    public PermissionException(@NotNull final BundledMessage languageMessage, @NotNull final MultiverseException mvException) {
        this(languageMessage, null, mvException);
    }

    public PermissionException(@NotNull final BundledMessage languageMessage, @Nullable Perm missingPerm) {
        super(languageMessage);
        this.missingPermission = missingPerm;
    }

    public PermissionException(@NotNull final BundledMessage languageMessage, @Nullable Perm missingPerm, @NotNull final Throwable throwable) {
        super(languageMessage, throwable);
        this.missingPermission = missingPerm;
    }

    public PermissionException(@NotNull final BundledMessage languageMessage, @Nullable Perm missingPerm, @NotNull final MultiverseException mvException) {
        super(languageMessage, mvException);
        this.missingPermission = missingPerm;
    }

    public PermissionException(@Nullable Perm missingPerm, @NotNull final MultiverseException e) {
        super(e);
        this.missingPermission = missingPerm;
    }

    @Nullable
    public Perm getMissingPermission() {
        return missingPermission;
    }
}
