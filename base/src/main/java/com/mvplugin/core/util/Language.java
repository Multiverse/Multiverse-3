package com.mvplugin.core.util;

import pluginbase.messages.Message;

public final class Language {
    private Language() { }

    public static final class Command {
        private Command() { }

        public static final Message MUST_SPECIFY_WORLD = Message.createMessage("command.general.console_must_specify_world",
                "$-You must specify a world when using this command when not in game!");

        public static final class Create {
            private Create() { }

            public static final Message HELP = Message.createMessage("command.create.help",
                    "$hCreates a new world on your server with the given name."
                    + "\n$hYou must specify a world environment such as $0NORMAL $hor $1NETHER$h."
                    + "\n$hYou may specify a world seed."
                    + "\n$hYou may also specify a generator to use along with an optional generator ID."
                    + "\n$hThe generator name is case sensitive!"
                    + "\n$hYou may specify a world type such as FLAT or LARGE_BIOMES"
                    + "\n$hYou may specify if $tMultiverse $hshould declare to generate structures or not."
                    + "\n$hFlags:"
                    + "\n$f  -s $r{SEED} $hSpecify a world seed to use."
                    + "\n$f  -g $r{GENERATOR$o[:ID]$r} $hSpecify a generator."
                    + "\n$f  -t $r{TYPE} $hSpecify a world type."
                    + "\n$f  -a $r{true|false} $hSpecify whether or not to generate structures."
                    + "\n$hExamples:"
                    + "\n$c  /mv create $rgargamel $0normal"
                    + "\n$c  /mv create $r\"hell world\" $1nether"
                    + "\n$c  /mv create $rspace $0normal $f-g $rCleanroomGenerator$o:.");
            public static final Message FAILED = Message.createMessage("command.create.failed",
                    "$-Create failed!");
            public static final Message CREATING_WORLD = Message.createMessage("command.create.creating",
                    "$wCreating new world, please wait...");
            public static final Message SUCCESS = Message.createMessage("command.create.success",
                    "$+Successfully created world $!%s$+!");
        }

        public static final class Delete {
            private Delete() { }

            public static final Message HELP = Message.createMessage("command.delete.help",
                    "$hDeletes a world from the server, removing it from $tMultiverse's $hmanagement."
                            + "\n$hThe world must be managed by Multiverse to use this command."
                            + "\n$hExamples:"
                            + "\n$c  /mv delete $rgargamel");
            public static final Message DELETING_WORLD = Message.createMessage("command.delete.deleting_world",
                    "$wDeleting world '$v%s$w', please wait...");
            public static final Message DELETED_WORLD = Message.createMessage("command.delete.deleted_world",
                    "$+Successfully deleted world '$v%s$+'!");
        }

        public static final class Import {
            private Import() { }

            public static final Message HELP = Message.createMessage("command.import.help",
                    "$hImports a world into the server from a folder with the given name."
                    + "\n$hThe folder must exist in the location where worlds are normally located and must contain Minecraft world data."
                    + "\n$hYou must specify a world environment such as $0NORMAL $hor $1NETHER$h."
                    + "\n$hYou may also specify a generator to use along with an optional generator ID."
                    + "\n$hThe generator name is case sensitive!"
                    + "\n$hFlags:"
                    + "\n$f  -g $r{GENERATOR$o[:ID]$r} $hSpecify a generator."
                    + "\n$f  -n $hDo not adjust spawn"
                    + "\n$hExamples:"
                    + "\n$c  /mv import $rgargamel $0normal"
                    + "\n$c  /mv import $r\"hell world\" $1nether"
                    + "\n$c  /mv import $rspace $0normal $f-g $rCleanroomGenerator$o:.");
            public static final Message POTENTIAL_WORLD_LIST = Message.createMessage("command.import.potential_world_list",
                    "$=====[ These look like worlds ]====\n%s");
            public static final Message NO_POTENTIAL_WORLDS = Message.createMessage("command.import.no_potential_worlds",
                    "$$No potential worlds found. Sorry!");
            public static final Message STARTING_IMPORT = Message.createMessage("command.import.starting_import",
                    "$wStarting import of world '%s'...");
            public static final Message IMPORT_COMPLETE = Message.createMessage("command.import.import_complete",
                    "$+Import complete!");
            public static final Message IMPORT_FAILED = Message.createMessage("command.import.import_failed",
                    "$-Import failed!");
            public static final Message NON_EXISTENT_FOLDER = Message.createMessage("command.import.non_existent_folder",
                    "$^That world folder does not exist."
                    + "\n$iThese look like worlds to me: \n%s");
        }

        public static final class List {
            private List() { }

            public static final Message HELP = Message.createMessage("command.list.help",
                    "$hLists all worlds managed by $tMultiverse$h."
                    + "\n$hOnly the worlds you may access will be shown.");
            public static final Message LIST_WORLDS = Message.createMessage("command.list.list",
                    "$=====[ Multiverse World List ]====\n%s");
        }

        public static final class Modify {
            private Modify() { }

            public static final Message ALL_PROPERTIES_LIST = Message.createMessage("command.modify.property_list",
                    "$iAvailable properties: \n%s");

            public static final Message NO_SUCH_PROPERTY = Message.createMessage("command.modify.no_such_property",
                    "$-There is no property '$v%s$-'");

            public static final Message PROPERTY_DESCRIPTION = Message.createMessage("command.modify.property_description",
                    "$hDescription for property '$v%s$h': \n$i%s");

            public static final Message NO_MODIFY_PERMISSION = Message.createMessage("command.modify.no_perm",
                    "$-You do not have permission to modify world '$v%s$-'!");

            public static final Message PROBABLY_INVALID_VALUE = Message.createMessage("command.modify.probably_invalid_value",
                    "$-That doesn't seem like a valid value for '$v%s$-'."
                            + "\nMore details will follow.");

            public static final class Set {
                private Set() { }

                public static final Message HELP = Message.createMessage("command.modify.set.help",
                        "$hSets the value of a property for a world managed by $tMultiverse$h."
                                + "\n$hYou may type this command with no args to see a list of available properties."
                                + "\n$hYou may type this command with only a property name to see a description of the property.");

                public static final Message PROPERTY_CANNOT_BE_SET = Message.createMessage("command.modify.set.cannot_set",
                        "$-'$v%s$-' cannot be set!");

                public static final Message SUCCESS = Message.createMessage("command.modify.set.success",
                        "$+You have successfully set '$v%s$+' to '$v%s$+'!");
            }
        }

        public static final class Load {
            private Load() { }

            public static final Message HELP = Message.createMessage("command.load.help",
                    "$hLoads a world that has previously been imported but is currently unloaded."
                    + "\n$hExamples:"
                    + "\n$c  /mv load $rgargamel");
            public static final Message SUCCESS = Message.createMessage("command.load.success",
                    "$+Successfully loaded the world '$v%s$+'!");
        }

        public static class Teleport {
            private Teleport() { }

            public static final Message HELP = Message.createMessage("command.teleport.help",
                    "$hTeleports a player to a specified destination."
                    + "\n$hIf no player is given, the user will be teleported.");
            public static final Message NEED_PLAYER = Message.createMessage("command.teleport.needplayer",
                            "$-$*You must specify a player to teleport!");
            public static final Message NO_SUCH_PLAYER = Message.createMessage("command.teleport.nosuch.player",
                                    "$-$*The player '$v%s$-$*' was not found!");
            public static final Message NO_SUCH_DESTINATION = Message.createMessage("command.teleport.nosuch.destination",
                                            "$-$*The destination '$v%s$-$*' was not found!");
        }

        public static class Unload {
            private Unload() { }

            public static final Message HELP = Message.createMessage("command.unload.help",
                    "$hUnloads a world that has previously been imported and is currently loaded."
                    + "\n$hThis will remove the world from memory but not delete anything."
                    + "\n$hExamples:"
                    + "\n$c  /mv unload $rgargamel");
            public static final Message SUCCESS = Message.createMessage("command.unload.success",
                    "$+'$v%s$+' has been unloaded successfully!");
            public static final Message FAILURE = Message.createMessage("command.unload.failure",
                    "$-'$v%s$-' could not be unloaded!");
        }
    }

    public static final class Destination {
        private Destination() { }

        public static final class Player {
            private Player() { }

            public static final Message NOT_FOUND = Message.createMessage("destination.player.notfound",
                    "$-$*Multiverse could not find the destination player '$v%s$-$*'!");
            public static final Message OFFLINE = Message.createMessage("destination.player.offline",
                            "$-$*The destination player '$v%s$-$*' is offline right now!");
        }

        public static final class Unknown {
            private Unknown() { }

            public static final Message UNKNOWN_DESTINATION = Message.createMessage("destination.unknown",
                    "$-$*Multiverse could not teleport '$v%s$-$*' to the destination '$v%s$-$*' because it could" +
                            "not be resolved. Did you recently remove a plugin?");
        }

        public static final class World {
            private World() { }

            public static final Message CANT_LOAD = Message.createMessage("destination.world.cantload",
                    "$-$*Multiverse could not load the destination world '$v%s$-$*'!");
            public static final Message NOT_LOADED = Message.createMessage("destination.world.notloaded",
                            "$-$*The destination world '$v%s$-$*' is not loaded!");
        }
    }

    public static final class DefaultSafeTeleporter {
        private DefaultSafeTeleporter() { }

        public static final Message NO_SAFE_LOCATION = Message.createMessage("teleporter.no_safe_location",
                "$-$*Multiverse could not find a safe location near '$v%s$-$*' for teleporting '$v%s$-$*'.");
        public static final Message TELEPORT_FAILED = Message.createMessage("teleporter.failed",
                        "$-$*Multiverse could not teleport '$v%s$-$*' to safe location '$v%s$-$*'.");
    }

    public static final class Properties {
        public static final Message VALID_SPAWN_STRING = Message.createMessage("world_properties.spawn_location.valid_string",
                "$iA valid spawn string is in the form '$vx,y,z$i'."
                +"\n$cExample: 503,67,-22");
    }

    public static final Message WORLD_ALREADY_EXISTS = Message.createMessage("worlds.world_exists",
            "$tMultiverse $-already knows about '$v%s$-'.  $?Perhaps it needs to be loaded? ($~$C/mv load$?)");

    public static final Message WORLD_ALREADY_LOADED = Message.createMessage("world.already_loaded",
            "$-The world '$v%s$-' is already loaded!");

    public static final Message WORLD_ALREADY_UNLOADED = Message.createMessage("world.already_unloaded",
            "$-The world '$v%s$-' is already unloaded!");

    public static final Message WORLD_NOT_MANAGED = Message.createMessage("world.not_managed",
            "$tMultiverse $-is unaware of the world '$v%s$-'.  $?Has it been imported? ($~$C/mv import$?)");

    public static final Message WORLD_LOAD_ERROR = Message.createMessage("world.load.error",
            "$tMultiverse $-experienced a problem while attempting to load '$v%s$-'!");

    public static final Message WORLD_UNLOAD_ERROR = Message.createMessage("world.unload.error",
            "$tMultiverse $-experienced a problem while attempting to unload '$v%s$-'.");

    public static final Message WORLD_COULD_NOT_UNLOAD_FROM_SERVER = Message.createMessage("world.unload.could_not_unload_from_server",
            "$-World '$v%s$-' could not be unloaded.  $?Is it a default world?");

    public static final Message WORLD_REMOVE_ERROR = Message.createMessage("world.remove.error",
            "$tMultiverse $-experienced a problem while attempting to remove '$v%s$-'.");

    public static final Message CANNOT_DELETE_UNMANAGED = Message.createMessage("world.delete.must_be_managed",
             "$$You may only delete worlds that Multiverse manages.");

    public static final Message CANNOT_DELETE_NONWORLD = Message.createMessage("world.delete.not_a_world",
            "$$Sorry, $v%s$$ does not appear to be a world.");

    public static final Message WORLD_DELETE_ERROR = Message.createMessage("world.delete.error",
            "$tMultiverse $-experienced a problem while attempting to delete '$v%s$-'!");

    public static final Message WORLD_DELETE_PERSISTENCE_ERROR = Message.createMessage("world.delete.persistence_error",
            "$tMultiverse $-experienced a problem while attempting to delete the $tMultiverse$- information for '$v%s$-'!"
            + "\n$iYou may attempt to remove this with $~$C/mv remove");

    public static final Message WORLD_DELETE_FAILED = Message.createMessage("world.delete.failed",
            "$tMultiverse $-was unable to fully delete world '$v%s$-'."
            + "\n$i$*This probably means the files are in use somewhere else."
            + "\n$,You may attempt to delete it again with Multiverse but you will probably need to delete it manually.");

    public static final Message INVALID_ENVIRONMENT = Message.createMessage("worlds.invalid_environment",
            "$v%s $-is not a valid world environment."
            + "\n$iFor a list of available world environments, $~type $C/mvenv");
    public static final Message INVALID_WORLD_TYPE = Message.createMessage("worlds.invalid_type",
            "$v%s $-is not a valid world type."
            + "\n$iFor a list of available world types, $~type $C/mvtype");
}
