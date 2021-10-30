package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Comparable;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.common.Patterns;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.DriveSubsystem.Type;

/**
 * Moves the robot to a reflective target, within a specified distance from the
 * target.
 *
 * @author Dowland Aiello
 */
public class MoveToReflectiveTargetCommand extends CommandBase {
    /* The drivetrain that we'll use to run the MoveToReflectiiveTarget command. */
    private final DriveSubsystem m_drivetrain;

    /* The vision subsystem that we'll use for targeting. */
    private final VisionSubsystem m_vision;

    /* A configuration for this command. */
    private final Configuration cfg;

    /* The state of the command. This includes all historical data. */
    private State state;

    /**
     * Axis represents a generic limelight vision axis.
     **/
    public static enum Axis {
        X, Y, Z
    }

    /**
     * State represents the current state of the command.
     *
     * @author Dowland Aiello
     **/
    private static class State {
        /* The offsets on each axis from the target. */
        ArrayList<Entry<Double>> targetOffsets;

        /* Whether or not a target is present in the camera's field of view. */
        Entry<Boolean> targetPresence;

        /* The number of frames until the state will restart. */
        private int maxFrames;

        /* The current frame number. */
        private int currentFrame;

        /* The current frame number in each group of frames. */
        private int frameInSession;

        /* Have we corrected rotationally at least once in the session? */
        public boolean hasInitialHeading;

        /*
         * The number of times the offset values have been sampled, and observed in a
         * consistent manner.
         */
        private int nCorrectionlessFrames;

        /*
         * The number of times the rotational offset values have been sampled, and
         * observed in a zero-value, consistent manner.
         */
        private int nRotationlessFrames;

        /**
         * Creates a new state with the given data sources.
         *
         * @param maxFrames the maximum number of frames until the state will be reset
         **/
        public State(int maxFrames) {
            // Make a new array to store historical offset data in
            this.targetOffsets = new ArrayList<Entry<Double>>() {
                private static final long serialVersionUID = 1L;

                {
                    add(new Entry<Double>(Entry.Type.AVERAGE_ROTATIONAL_OFFSET));
                    add(new Entry<Double>(Entry.Type.AVERAGE_Y_OFFSET));
                    add(new Entry<Double>(Entry.Type.AVERAGE_ZED_DISTANCE_OFFSET));
                }
            };

            this.targetPresence = new Entry<Boolean>(Entry.Type.AVERAGE_HAS_TARGET);
            this.maxFrames = maxFrames;
            this.currentFrame = 0;
            this.frameInSession = 0;
            this.nCorrectionlessFrames = 0;
            this.nRotationlessFrames = 0;
            this.hasInitialHeading = false;
        }

        /**
         * Inserts the given offsets into the state's history.
         *
         * @param offsets the offset values to use
         **/
        public void putValues(double[] offsets, boolean hasTarget) {
            // If we're past the max number of frames, reset the state
            if (this.currentFrame > this.maxFrames) {
                // Reset the index of the current frame so that we can start building off a new
                // head
                this.currentFrame--;
                this.frameInSession = 0;

                // Pop an entry from the presence state entry
                this.pop();
            }

            // Increment the current frame
            this.currentFrame++;
            this.frameInSession++;

            // Iterate through each of the historical target offset values
            for (int i = 0; i < this.targetOffsets.size() && i < offsets.length; i++) {
                // Insert each of the offset values
                this.targetOffsets.get(i).push(offsets[i]);
            }

            this.targetPresence.push(hasTarget);
        }

        /**
         * Pops an individual historical value entry from each of the data sources in
         * the state.
         */
        private void pop() {
            // Pop a single entry value from each of the target offsets
            for (Entry<Double> entry : this.targetOffsets) {
                // Pop a historical value from the botom of the queue
                double value = entry.historicalValues.remove(0);

                // Subtract one from the corresponding frequency
                entry.frequencies.put(value, entry.frequencies.get(value) - 1);
            }

            // Pop a historical value from the bottom of the target present queue
            boolean present = this.targetPresence.historicalValues.remove(0);

            // Remove the value from the presence frequencies hashmap so that it doesn't
            // contaminate the overall average anymore
            this.targetPresence.frequencies.put(present, this.targetPresence.frequencies.get(present) - 1);
        }

        /**
         * Gets the offset values contained inside the state.
         *
         * @return the offset values contained inside the state
         **/
        public double[] getOffsets() {
            return new double[] { this.targetOffsets.get(0).getAverage(), this.targetOffsets.get(1).getAverage(),
                    this.targetOffsets.get(2).getAverage() };
        }

        /**
         * Checks whether or not a target is present in the camera's field of view.
         *
         * @return whether or not a target exists in the camera's field of view
         **/
        public boolean hasTarget() {
            return !(this.frameInSession > this.maxFrames * 0.5 && !this.targetPresence.getMode());
        }

        /**
         * Checks whether or not the target has been acquired.
         **/
        public boolean hasFinished(double errorTolerance, boolean[] enabledAxes) {
            // Check if we need to correct for the x values
            boolean needsCorrectionX = needsCorrectionOnAxis(Axis.X, errorTolerance);
            boolean needsCorrectionZ = needsCorrectionOnAxis(Axis.Z, errorTolerance);

            if (!needsCorrectionX) {
                if (!needsCorrectionZ) {
                    this.nCorrectionlessFrames++;
                }

                this.nRotationlessFrames++;
            } else {
                this.nCorrectionlessFrames = 0;
                this.nRotationlessFrames = 0;
            }

            this.hasInitialHeading = (!needsCorrectionX && this.nRotationlessFrames > this.maxFrames * 0.75) ? true
                    : this.hasInitialHeading;

            return (enabledAxes[0] ? !needsCorrectionX : true) && (enabledAxes[2] ? !needsCorrectionZ : true)
                    && this.nCorrectionlessFrames > this.maxFrames * 0.75;
        }

        /**
         * Checks whether or not correction is necessary on the given axis.
         *
         * @return whether or not correction is necessary for a particular axis
         **/
        public boolean needsCorrectionOnAxis(Axis axis, double errorTolerance) {
            switch (axis) {
            case X:
                return Math.abs(this.targetOffsets.get(0).getAverage())
                        / Constants.VisionConstants.DEFAULT_BOUNDS[0] > errorTolerance;
            case Y:
                return Math.abs(this.targetOffsets.get(1).getAverage())
                        / Constants.VisionConstants.DEFAULT_BOUNDS[1] > errorTolerance;
            default:
                return Math.abs(
                        Constants.VisionConstants.DEFAULT_BOUNDS[2] - this.targetOffsets.get(2).getAverage()) > Math
                                .abs(Constants.VisionConstants.DEFAULT_BOUNDS[2]
                                        - ((1 + errorTolerance) * Constants.VisionConstants.DEFAULT_BOUNDS[2]));
            }
        }

        /**
         * Entry represents a generic state entry for the command. This used to
         * represent historical values for the command.
         **/
        private static class Entry<T extends Comparable<T>> {
            /* The type of the entry. */
            private Type entryType;

            /* Individual entries inside the entry at each frame. */
            private ArrayList<T> historicalValues;

            /* The number of times that each value occurs in the Entry. */
            private HashMap<T, Integer> frequencies;

            /* The most often occurring value in the historical values of the entry. */
            private T mode;

            /**
             * Initializes a new Entry.
             *
             * @param type the type of entry
             **/
            public Entry(Type type) {
                this.entryType = type;
                this.historicalValues = new ArrayList<>();
                this.frequencies = new HashMap<>();
                this.mode = null;
            }

            /**
             * Entry represents a generic type of entry stored in the command's state.
             **/
            static enum Type {
                /* Whether or not a target has been recorded in more than half of the frames. */
                AVERAGE_HAS_TARGET,

                /* The average offset along the X axis over the specified number of frames. */
                AVERAGE_ROTATIONAL_OFFSET,

                /* The average offset along the Z axis over the specified number of frames. */
                AVERAGE_ZED_DISTANCE_OFFSET,

                /* The average offset along the Y axis over the specified number of frames. */
                AVERAGE_Y_OFFSET,
            }

            /**
             * Gets the mode of the historical values stored in the entry.
             *
             * @return the most often occurring value stored in the entry
             **/
            private T getMode() {
                return this.mode;
            }

            /**
             * Gets the median of the historical values stored in the entry.
             *
             * @return the median value stored in the entry
             **/
            private T getMedian() {
                // Make sure that sorting the historical values won't change it
                ArrayList<T> history = new ArrayList<T>(Collections.unmodifiableList(this.historicalValues));

                // Sort each of the values contained in the set of historical values
                Collections.sort(history);

                // Return the middle value from the sorted historical values list
                return history.get(this.historicalValues.size() / 2);
            }

            /**
             * Gets the average of the historical values stored in the entry through the
             * respective average mode.
             *
             * @return the mean of the historical values stored in the entry
             **/
            public T getAverage() {
                switch (this.entryType) {
                case AVERAGE_HAS_TARGET:
                    return this.getMode();
                default:
                    return this.getMedian();
                }
            }

            /**
             * Pushes a value onto the state entry.
             *
             * @param value the value that will be pushed onto the entry
             **/
            public void push(T value) {
                // Increment the frequency for the value provided
                this.frequencies.put(value, this.frequencies.getOrDefault(value, 0) + 1);

                // Add the value into the set of historical values contained in the state entry
                this.historicalValues.add(value);

                // Recalculate the mode for the entry
                if (this.mode == null || this.frequencies.get(value) > this.frequencies.getOrDefault(mode, 0)) {
                    this.mode = value;
                }
            }
        }
    }

    /**
     * Configuration represents a configuration for the MoveToReflectiveTarget
     * command.
     */
    public static class Configuration implements Patterns.PreferenceChangeable<Configuration> {
        /*
         * The proportional value for the command's PID loop. This is the amount that
         * the input error value is multiplied by on each call.
         */
        DoubleSupplier kP;

        /* An amount that is added to the output value of the PID loop on each run. */
        DoubleSupplier kI;

        /*
         * A multiplier for the final output value of the command's PID loop. Applied as
         * output^kChange.
         */
        DoubleSupplier kChange;

        /*
         * The tolerance to error in this command's PID loop. This is the amount that
         * the error vlaue can deviate from the target value.
         */
        DoubleSupplier errorTolerance;

        /*
         * The maximum speed that the robot will run during the execution of this
         * command.
         */
        DoubleSupplier maximumSpeed;

        /*
         * The maximum speed that the robot will move forward and backwards during the
         * execution of this command.
         */
        DoubleSupplier maximumForwardSpeed;

        /* The set of axes that are supported by the vision command. */
        boolean[] supportedAxes;

        /**
         * Initializes a new Configuration for the MoveToReflectiveTarget command with
         * the given parameters.
         *
         * @param kP                  the proportional value for this command's PID loop
         * @param kChange             a multiplier for the final output value of the
         *                            command's PID loop
         * @param errorTolerance      the acceptable range that the output of this
         *                            command will be from the target value
         * @param maximumSpeed        the maximum speed that the robot will run during
         *                            the execution of this command
         * @param maximumForwardSpeed the maximum speed that the robot will move forward
         *                            and backwards
         */
        public Configuration(DoubleSupplier kP, DoubleSupplier kI, DoubleSupplier kChange,
                DoubleSupplier errorTolerance, DoubleSupplier maximumSpeed, DoubleSupplier maximumForwardSpeed) {
            // Set up the configuration using the given constraints
            this.kP = kP;
            this.kI = kI;
            this.kChange = kChange;
            this.errorTolerance = errorTolerance;
            this.maximumSpeed = maximumSpeed;
            this.maximumForwardSpeed = maximumForwardSpeed;
            this.supportedAxes = new boolean[] { true, true, true };
        }

        /**
         * Constructs an empty instance of the command configuration. Should be
         * configured with the robot's preferences via applyPreferences(prefs).
         */
        public static Configuration getDefault() {
            // Create a new configuration with the variables provided in the global
            // constants class
            return new Configuration(() -> Constants.VisionConstants.DEFAULT_KP,
                    () -> Constants.VisionConstants.DEFAULT_KI, () -> Constants.VisionConstants.DEFAULT_KCHANGE,
                    () -> Constants.VisionConstants.DEFAULT_ERROR_TOLERANCE,
                    () -> Constants.VisionConstants.DEFAULT_MAX_SPEED,
                    () -> Constants.VisionConstants.DEFAULT_MAX_FORWARD_SPEED);
        }

        /**
         * Applies the preferences defined in the SmartDashboard preferences pane to the
         * configurable object.
         * 
         * @param prefs the instance of the preferences class that should be used to
         *              configure the configurable class from
         */
        public Configuration applyPreferences(Preferences prefs) {
            // Initialize a default-value vision subsystem so that we can generate a bunch
            // of preference keys from which
            // we will extract the information for the configuration of the command
            VisionSubsystem preferencesBuilder = VisionSubsystem.getDefault();

            // Get a default instane of the command config so that we can fallback to some
            // values
            Configuration defaultConfig = Configuration.getDefault();

            // Use all default values for each of the configuration fields. Or, if we can
            // get override values from the preferences instance, use those.
            this.kP = () -> prefs.getDouble(preferencesBuilder.preferencesKey("kP").toString(),
                    defaultConfig.kP.getAsDouble());
            this.kI = () -> prefs.getDouble(preferencesBuilder.preferencesKey("kI").toString(),
                    defaultConfig.kI.getAsDouble());
            this.kChange = () -> prefs.getDouble(preferencesBuilder.preferencesKey("kChange").toString(),
                    defaultConfig.kChange.getAsDouble());
            this.errorTolerance = () -> prefs.getDouble(preferencesBuilder.preferencesKey("errorTolerance").toString(),
                    defaultConfig.errorTolerance.getAsDouble());
            this.maximumSpeed = () -> prefs.getDouble(preferencesBuilder.preferencesKey("maximumSpeed").toString(),
                    defaultConfig.maximumSpeed.getAsDouble());
            this.maximumForwardSpeed = () -> prefs.getDouble(
                    preferencesBuilder.preferencesKey("maximumForwardSpeed").toString(),
                    defaultConfig.maximumForwardSpeed.getAsDouble());

            return this;
        }

        /**
         * Disables correction on a particular axis. The available axes are: x, y, z. X
         * represents the rotational offset about the robot, Y represents secondary
         * horizontal offset (measured in terms of vertical offset along the height of
         * the screen), and Z represents horizontal offset (how far forward or
         * backwards?) in terms of % area. Of the two methods of horizontal correction,
         * Z correction is given precedence.
         *
         * @param axis the axis that should be disabled for correction capability
         * @return a copy of the configuration
         */
        public Configuration disableCorrectionOnAxis(int axis) {
            this.supportedAxes[axis] = false;

            return this;
        }

        /**
         * Enables correction on a particular axis. By default, all (X, Y, Z) axes are
         * enabled.
         *
         * @param axis the axis that should be enabled
         * @return a copy of the configuration
         */
        public Configuration enableCorrectionOnAxis(int axis) {
            this.supportedAxes[axis] = true;

            return this;
        }

        /**
         * Gets the kP of the configuration.
         *
         * @return the kP of the configuration
         */
        public double getKp() {
            // Return the configuration's current kP
            return this.kP.getAsDouble();
        }

        /**
         * Gets the kI of the configuration.
         * 
         * @return the kI of the configuration
         */
        public double getKi() {
            // Return the configuration's current kI
            return this.kI.getAsDouble();
        }

        /**
         * Gets the error tolerance of the configuration.
         *
         * @return the error tolerance of the configuration
         */
        public double getErrorTolerance() {
            // Return the configuration's current error tolerance variable
            return this.errorTolerance.getAsDouble();
        }

        /**
         * Gets the maximum speed that this command will run at.
         *
         * @return the maximum speed at which the robot will run during the execution of
         *         this command
         */
        public double getMaximumSpeed() {
            // Return the configuration's maximum speed variable
            return this.maximumSpeed.getAsDouble();
        }

        /**
         * Gets the maximum speed that the command will allow the robot to move forward
         * or backwards at.
         * 
         * @return the maximum speed forwards or backwards that the robot will run at
         *         during the execution of this command
         */
        public double getMaximumForwardSpeed() {
            // Return the configuration's maximum forward speed variable
            return this.maximumForwardSpeed.getAsDouble();
        }

        /**
         * Gets the kChange value for this command's config.
         *
         * @return the kChange for the command
         */
        public double getkChange() {
            // Return the kChange
            return this.kChange.getAsDouble();
        }
    }

    /**
     * Initializes a new MoveToReflectiveTargetCommand with the given constraints.
     */
    public MoveToReflectiveTargetCommand(DriveSubsystem drivetrain, VisionSubsystem vision, Configuration cfg) {
        // Use the provided drive train, vision subsystem, and configuration classes
        this.m_drivetrain = drivetrain;
        this.m_vision = vision;
        this.cfg = cfg;
        this.state = new State(Constants.VisionConstants.TARGETLESS_FRAMES_TO_STOP);

        // Keep the limelight's light off until the command is scheduled to run
        this.m_vision.disableLimelight();

        // Drivetrain and vision subsystems need to be established in order for this
        // command to work
        addRequirements(drivetrain, vision);
    }

    @Override
    public void initialize() {
        // Turn the limelight light on once the command is scheduled
        this.m_vision.enableLimelight();
    }

    /**
     * Calculates the percentage value of each offset along an assumed X, Y, and Z
     * axis.
     * 
     * @param offsets each of the X, Y, and Z offset values
     * @return the corrected offset values expressed as percentages of the maximum
     *         boundary
     */
    private double[] normalizeOffsets(double[] offsets) {
        Axis[] axes = { Axis.X, Axis.Y, Axis.Z };

        // Normalize each of the provided offsets
        for (int i = 0; i < offsets.length && i < axes.length; i++) {
            // Normalize the offset
            offsets[i] = this.normalizeOffset(offsets[i], axes[i]);
        }

        return offsets;
    }

    /**
     * Calculates a target offset, considering a given maximum offset from the
     * center of the limelight view, in conjunction with consideration to the
     * value's status as a negative or positive value.
     */
    private double normalizeOffset(double offset, Axis axis) {
        // The original offset value
        double original = offset;

        // Convert the raw offset to a percentage of the boundary definition
        switch (axis) {
        case X:
            offset = Math.abs(offset) / Constants.VisionConstants.DEFAULT_BOUNDS[0];

            break;
        case Y:
            offset = Math.abs(offset) / Constants.VisionConstants.DEFAULT_BOUNDS[1];

            break;
        case Z:
            offset = Constants.VisionConstants.DEFAULT_BOUNDS[2] - offset;
            original = offset;

            break;
        }

        // If the offset is negative, compare it against the negative max number of
        // degrees. Otherwise, compare it against
        // the positive version.
        return original < 0 ? -1 * Math.pow(Math.abs(offset), this.cfg.getkChange()) : Math.pow(offset, this.cfg.getkChange());
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        // Update the state of the command
        this.state.putValues(new double[] { this.m_vision.tx(), this.m_vision.ty(), this.m_vision.ta() },
                this.m_vision.hasTarget());

        // Get each of the offset values from the limelight
        double[] offsets = this.normalizeOffsets(this.state.getOffsets());

        // Check if we have a target
        boolean hasTarget = this.state.hasTarget();

        // If we don't have a target to lock on to, we can stop execution
        if (!hasTarget) {
            return;
        }

        double tolerance = this.state.hasInitialHeading ? this.cfg.getErrorTolerance() * 2.5
                : this.cfg.getErrorTolerance();

        // Check that we need to correct for Z axis error
        if (this.state.needsCorrectionOnAxis(Axis.Z, this.cfg.getErrorTolerance()) && this.state.hasInitialHeading
                && this.cfg.supportedAxes[2]) {
            // Calculate the gain with the z offset
            double gain = this.cfg.getKp() * offsets[2] * this.cfg.getMaximumForwardSpeed() + this.cfg.getKi();

            // gain += gain < 0 ? -this.cfg.getKi() : this.cfg.getKi();

            // Move forward and back using the gain variable
            //this.m_drivetrain.drive(Type.RHINO, new double[] { -gain, -gain });
            System.out.println("gain = " + gain);
            System.out.println("correcting z");
        } else if (this.state.needsCorrectionOnAxis(Axis.X, tolerance) && this.cfg.supportedAxes[0]) {
            // Calculate the gain with the x offset
            double gain = this.cfg.getKp() * offsets[0] * this.cfg.getMaximumSpeed();
            gain += gain < 0 ? -this.cfg.getKi() : this.cfg.getKi();

            // Spin in one spot using the provided gain variable
            //this.m_drivetrain.drive(Type.RHINO, new double[] { -gain, gain });
            System.out.println("gain = " + gain);
            System.out.println("correcting x");
            
        } else if (this.state.needsCorrectionOnAxis(Axis.Y, this.cfg.getErrorTolerance())
                && this.state.hasInitialHeading && this.cfg.supportedAxes[2]) {
            // Calculate the gain with the y offset
            double gain = this.cfg.getKp() * offsets[1] * this.cfg.getMaximumForwardSpeed() + this.cfg.getKi();
            gain += gain < 0 ? -this.cfg.getKi() : this.cfg.getKi();

            // Move forward and back using the gain variable
            //this.m_drivetrain.drive(Type.RHINO, new double[] { gain, gain });
            System.out.println("gain = " + gain);

            System.out.println("correcting y");
        }
    }

    /**
     * Returns whether or not the command has finished executing.
     *
     * @return whether or not the command has completed
     */
    @Override
    public boolean isFinished() {
        // Check that the command should have finished, and that we have a target
        boolean hasFinished = this.state.hasFinished(this.cfg.getErrorTolerance(), this.cfg.supportedAxes);
        boolean hasTarget = this.state.hasTarget();

        // If there aren't any targets, we're done. Or, if we've moved to the target,
        // we're done.
        return hasFinished || !hasTarget;
    }

    /**
     * Terminates the vision command
     */
    @Override
    public void end(boolean interrupted) {
        // Reset the command's state
        this.state = new State(Constants.VisionConstants.TARGETLESS_FRAMES_TO_STOP);
        this.state.hasInitialHeading = false;

        // Once the command has terminated, disable the limelight's light
        this.m_vision.disableLimelight();
    }
}
