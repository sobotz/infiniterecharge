package frc.robot.common;

import edu.wpi.first.wpilibj.Preferences;

/**
 * Defines a set of utility classes for commonly used programming patterns.
 * 
 * @author Dowland Aiello
 */
public class Patterns {
    /**
     * Defines a type that can be constructed, taking into account an instance of
     * the robot preferences class.
     */
    public static interface PreferenceChangeable<T> {
        /**
         * Applies the preferences defined in the SmartDashboard preferences pane to the
         * configurable object.
         * 
         * @param prefs the instance of the preferences class that should be used to
         *              configure the configurable class from
         */
        T applyPreferences(Preferences prefs);
    }
}
