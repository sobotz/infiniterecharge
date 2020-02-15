package frc.robot.common;

/**
 * Defines a type that must be both able to generate a default instance, and a
 * modifiable instance through the preferences utility class.
 */
public abstract class Buildable<T> implements Default<T>, PreferenceChangeable<T> {
    /**
     * Completes the building process, returning the contents of the buildable
     * object.
     * 
     * @return the contents of the buildable object
     */
    public Buildable<T> finish() {
        return this;
    }
}