package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.DriveSubsystem.Type;
import frc.robot.common.Patterns;

/**
 * Implements a differential drive driving command for the standard Drive
 * Subsystem.
 * 
 * @author Dowland Aiello
 */
public class DifferentialDriveCommand extends CommandBase
        implements Patterns.PreferenceChangeable<DifferentialDriveCommand> {
    /* The drivetrain that we'll use to run the Differential Drive command. */
    private final DriveSubsystem m_drivetrain;

    /* The x and y axis inputs from the joystick. */
    private final DoubleSupplier xInput, yInput, zInput;

    /* A speed limitation variable provided through smart dashboard. */
    private DoubleSupplier maximumSpeed;

    /**
     * Crates a new DifferentialDriveCommand.
     * 
     * @param drivetrain the subsystem used by this command
     * @param xInput     input from the x axis of the input joystick
     * @param zInput     rotational input from the z axis of the input joystick
     */
    public DifferentialDriveCommand(DriveSubsystem drivetrain, DoubleSupplier xInput, DoubleSupplier yInput,
            DoubleSupplier zInput) {
        // Use the provided drivetrain
        this.m_drivetrain = drivetrain;

        // By default, we'll set out speed to 1.0
        this.maximumSpeed = () -> 1.0;

        // Use the axis inputs that the user provided
        this.xInput = () -> this.maximumSpeed.getAsDouble() * xInput.getAsDouble();
        this.yInput = () -> this.maximumSpeed.getAsDouble() * yInput.getAsDouble();
        this.zInput = () -> this.maximumSpeed.getAsDouble() * zInput.getAsDouble();
    }

    /**
     * Applies the preferences defined in the SmartDashboard preferences pane to the
     * configurable object.
     * 
     * @param prefs the instance of the preferences class that should be used to
     *              configure the configurable class from
     */
    public DifferentialDriveCommand applyPreferences(Preferences prefs) {
        // Since we have a preferences instance to use, we can get the maximum speed
        // from there
        this.maximumSpeed = () -> prefs.getDouble(this.m_drivetrain.preferencesKey("maximumSpeed").toString(), 1.0);

        // Use all of the previously provided configuration variables, aside from the
        // maximum speed
        return this;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        // Drive the drivetrain with a differential drive config
        this.m_drivetrain.drive(Type.DIFFERENTIAL,
                new double[] { this.yInput.getAsDouble(), this.xInput.getAsDouble(), this.zInput.getAsDouble() });
    }

    /**
     * Returns whether or not the command has finished executing.
     * 
     * @return whether or not the command has completed
     */
    public boolean isFinished() {
        return false;
    }
}
