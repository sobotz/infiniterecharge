package frc.robot.common;

/**
 * Defines a type that can be constructed using no configuration values. In
 * other words, any Default class must have a default constructor.
 */
public interface Default<T> {
    /**
     * Constructs an instance of the class, using no configuratiion values. The
     * produced value will be a "default" version of the class.
     * 
     * @return a default version of the class
     */
    public T getDefault();
}