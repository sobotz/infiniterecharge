package frc.robot.common;

/**
 * Defines a set of helper methods and classes for dealing with robot
 * preferences.
 * 
 * @author Dowland Aiello
 */
public class Preferences {
    /**
     * Defines a group of preferences, established in a subsystem class.
     */
    public static interface Group {
        /**
         * Gets the name of the preferences group.
         * 
         * @return the name of the preferences group
         */
        String groupName();

        /**
         * Generates a new instance of the preferences Key class using the provided name
         * for the
         * 
         * @param property
         * @return
         */
        public default Key preferencesKey(String property) {
            return new Key(this, property);
        }
    }

    /**
     * Key defines the name for a value stored in the robot preferences pane.
     */
    public static class Key {
        /*
         * The name under which the key will be registered, and the group that the key
         * will be contained in.
         */
        private String group, name;

        /**
         * Initializes a new Key using the provided group name and key name.
         * 
         * @param group the group that the key should be part of
         * @param name  the name that the key will be registered under
         */
        public Key(Group group, String name) {
            this.group = group.groupName();
            this.name = name;
        }

        /**
         * Converts the Key into a string suitable for the Robot's preferences.
         * 
         * @return a string representation of the robot preferences key
         */
        @Override
        public String toString() {
            return String.format("%s::%s", this.group, this.name);
        }
    }
}
