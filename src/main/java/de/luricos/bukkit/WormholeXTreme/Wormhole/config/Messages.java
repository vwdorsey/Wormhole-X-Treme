package de.luricos.bukkit.WormholeXTreme.Wormhole.config;

public class Messages {
    
    public static final String MessageColor = "\u00A77";
    private static final String ErrorHeader = "\u00A73:: \u00A75error \u00A73:: \u00A77";
    private static final String NormalHeader = "\u00A73:: \u00A77";
    /**
     * The Enum StringTypes.
     */
    public enum Error {
        /** The permission no. */
        BAD_PERMISSIONS("You lack the permissions to do this."),
        /** The target is self. */
        DIALING_SELF("Can't dial own gate without solar flare"),
        /** The target invalid. */
        TARGET_INVALID("Invalid gate target."),
        /** The target is active. */
        TARGET_ACTIVE("Target gate %s is currently active."),
        /** The target is active. */
        TARGET_IN_USE("Target gate %s is currently in use by %s."),
        /** The gate not active. */
        GATE_NOT_ACTIVE("No gate activated to dial."),
        /** gate is invalid */
        GATE_SETUP_INVALID("Stargate has not a valid setup. Please check your log for errors."),
        /** no GateShape found */
        GATE_SHAPE_UNKNOWN("No valid Stargate shape was found. Type /wxbuild for build assistance."),
        /** bad GateShape given */
        GATE_SHAPE_INVALID("Specified GateShape doesn't exist: "),
        /** The construct name invalid. */
        GATE_NAME_INVALID("Gate name invalid or too long: "),
        /** The construct name taken. */
        GATE_ALREADY_EXISTS("Gate name already taken: "),
        /** The construct name taken. */
        GATE_DOESNT_EXIST("Specified gate does not exist: "),
        /** The gate not specified. */
        GATE_UNSPECIFIED("No gate name specified."),
        /** The gate not specified. */
        GATE_IRIS_LOCKED("The destination iris is locked!"),
        /** Request is invalid. */
        BAD_REQUEST("Invalid Request"),
        /** The player build count restricted. */
        BUILD_RESTRICTIONS("You are at your max number of built gates."),
        /** The player use cooldown restricted. */
        COOL_DOWN("You must wait longer before using a Stargate.");
        /** The m. */
        private final String m;

        /**
         * Instantiates a new string types.
         *
         * @param message
         *            the message
         */
        Error(final String message) {
            m = message;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return String.format("%s %s", ErrorHeader, m);
        }
    }
    
    public enum Info {
        /** The gate shutdown. */
        GATE_SHUTDOWN("Gate %s successfully shutdown."),
        /** The gate activated. */
        GATE_ACTIVATE("Gate %s successfully activated."),
        /** The gate deactivated. */
        GATE_DEACTIVATE("Gate %s successfully deactivated."),
        /** The gate remote active. */
        GATE_REMOTE_ACTIVATION("Gate %s remotely activated by %s."),
        /** The gate dialed. */
        GATE_CONNECT("Stargates connected."),
        /** The construct success. */
        BUILD_SUCCESS("Gate successfully constructed."),
        /** The player used a stargate and arrived at destination safely */
        STARGATE_WELCOME("Welcome to %s %s"),
        /** The player use cooldown wait time. */
        COOL_DOWN_WAIT_TIME("Current Wait (in seconds): %s"),
        /** Successful Wormhole Connection */
        CONNECTION_SUCCESSFUL("Stargate Connection Successful!");
        /** The m. */
        private final String m;

        /**
         * Instantiates a new string types.
         *
         * @param message
         *            the message
         */
        Info(final String message) {
            m = message;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return String.format("%s %s", NormalHeader, m);
        }
    }

    public static String createNormalMessage(String message) {
        return String.format("%s %s", NormalHeader, message);
    }

    public static String createErrorMessage(String message) {
        return String.format("%s %s", ErrorHeader, message);
    }
}
