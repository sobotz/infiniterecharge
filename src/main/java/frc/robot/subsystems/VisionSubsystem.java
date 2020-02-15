package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.common.Patterns;
import frc.robot.common.Preferences.Group;

/**
 * VisionSubsystem implements a connector to the limelight over a given
 * transport, namely NetworkTables.
 *
 * @author Dowland Aiello
 */
public class VisionSubsystem extends SubsystemBase implements Group {

    /**
     * LEDMode represents one of 4 LED modes available to the limelight.
     */
    public static enum LEDMode {
        DEFAULT(1), BLINK(2), ON(3), OFF(4);

        /* The value corresponding to the LEDMode. */
        private final int intVal;

        /**
         * Initializies a new LEDMode with the given corresponding integer value.
         *
         * @param value the value corresponding to the LEDMode
         */
        private LEDMode(int value) {
            this.intVal = value;
        }

        /**
         * Initializes a new LEDMode with the given value.
         * 
         * @param mode the mode that the mode should use
         */
        public static LEDMode fromString(String mode) {
            switch (mode.toLowerCase()) {
            case "default":
                return DEFAULT;
            case "blink":
                return BLINK;
            case "on":
                return ON;
            case "off":
                return OFF;
            }

            return DEFAULT;
        }

        /**
         * Converts the LEDMode to an integer.
         *
         * @return the value of the LEDMode
         */
        public int value() {
            // Return the value of this mode
            return this.intVal;
        }
    }

    /**
     * LimelightConfiguration defines a standard configuration for the connected
     * limelight.
     */
    public static class LimelightConfiguration implements Patterns.PreferenceChangeable<LimelightConfiguration> {
        /* The mode that the limelight's LED will use. */
        LEDMode ledMode;

        /**
         * Initializes a new LimelightConfiguration with the given paramters.
         *
         * @param ledMode the mode that the limelight's LED should operate in
         */
        public LimelightConfiguration(LEDMode ledMode) {
            // Set the limelight's ledMode
            this.ledMode = ledMode;
        }

        /**
         * Constructs a new configuration for the limelight, considering default values
         * defined in Constants.java
         *
         * @return the constructed limelight configuration
         */
        public static LimelightConfiguration getDefault() {
            // Make a limelight configuration with the default mode provided in the
            // constants file
            return new LimelightConfiguration(Constants.VisionConstants.DEFAULT_LIMELIGHT_MODE);
        }

        /**
         * Applies the preferences defined in the SmartDashboard preferences pane to the
         * limelight config object.
         * 
         * @param prefs the instance of the preferences class that should be used to
         *              configure the limeliight configuration class from
         */
        public LimelightConfiguration applyPreferences(Preferences prefs) {
            // We'll use an empty vision subsystem in order to configure the preferences
            VisionSubsystem visionSubsystem = VisionSubsystem.getDefault();

            this.ledMode = LEDMode
                    .fromString(prefs.getString(visionSubsystem.preferencesKey("ledMode").toString(), "default"));

            return this;
        }

        /**
         * Applies the Limelight configuration to the limelight through the given
         * network tables connector.
         *
         * @param limelightTable the NetworkTable connector to the limelight
         */
        public void applySettings(NetworkTable limelightTable) {
            // Apply the ledMode to the limelight
            limelightTable.getEntry("ledMode").setNumber(this.ledMode.value());
        }
    }

    /* A NetworkTables tabel for the limelight. */
    private NetworkTable limelightTable;

    /* A configuration for the LimelightConfiguratioin. */
    private LimelightConfiguration limelightConfiguratiion;

    /**
     * Initializes the vision subsystem with the given network tables configuration.
     *
     * @param limelightConfiguration a configuration for the limelight
     */
    public VisionSubsystem(LimelightConfiguration limelightConfiguration) {
        // Set up the limelight tables API
        this.limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

        // Use the user-provided limelight configuration class
        this.limelightConfiguratiion = limelightConfiguration;

        // Apply all of our setttings
        this.limelightConfiguratiion.applySettings(this.limelightTable);
    }

    /**
     * Constructs a new VisionSubsystem without any parameters, and, thus without
     * any formal fields.
     * 
     * @return the initialized, default vision subsystem
     */
    public static VisionSubsystem getDefault() {
        return new VisionSubsystem(null);
    }

    /**
     * Gets the name of the preferences group.
     * 
     * @return the name of the preferences group
     */
    public String groupName() {
        return "vision";
    }

    /**
     * Whether or not the limelight has a vision target.
     *
     * @return
     */
    public boolean hasTarget() {
        return this.tv() > 0;
    }

    /**
     * Gets the volume variable from the limelight.
     */
    public double tv() {
        return this.limelightTable.getEntry("tv").getDouble(0.0);
    }

    /**
     * Gets the x offset from the limelight.
     *
     * @return the offset of the crosshair from the target regiion
     */
    public double tx() {
        return this.limelightTable.getEntry("tx").getDouble(0.0);
    }

    /**
     * Gets the y offset from the limelight.
     *
     * @return the x offset of the crosshair from the target region
     */
    public double ty() {
        return this.limelightTable.getEntry("ty").getDouble(0.0);
    }

    /**
     * Gets the size of the target from the limelight.
     *
     * @return the size of any target contained inside the bounds of the limelight
     **/
    public double ta() {
        return this.limelightTable.getEntry("ta").getDouble(0.0);
    }
}
