package frc.robot.common;

import edu.wpi.first.wpilibj.Preferences;

/**
 * Defines a type that can be constructed, taking into account an instance of
 * the robot preferences class.
 * 
 * @author Dowland Aiello
 */
public interface PreferenceChangeable<T> {
    /**
     * Applies the preferences defined in the SmartDashboard preferences pane to the
     * configurable object.
     * 
     * @param prefs the instance of the preferences class that should be used to
     *              configure the configurable class from
     */
    public T applyPreferences(Preferences prefs);
}