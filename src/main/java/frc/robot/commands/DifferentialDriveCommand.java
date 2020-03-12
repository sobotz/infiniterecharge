package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.lang.Math;

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
    private final DoubleSupplier xInput, yInput;
    private DoubleSupplier finalX, finalY;

    /* A speed limitation variable provided through smart dashboard. */
    private DoubleSupplier maximumSpeed;

    /* The number by which input from the joystick should be ^d. */
    private DoubleSupplier amplificationFactor;

    /**
     * Crates a new DifferentialDriveCommand.
     * 
     * @param drivetrain the subsystem used by this command
     * @param xInput     input from the x axis of the input joystick
     * @param zInput     rotational input from the z axis of the input joystick
     */
    public DifferentialDriveCommand(DriveSubsystem drivetrain, DoubleSupplier xInput, DoubleSupplier yInput) {
        // Use the provided drivetrain
        this.m_drivetrain = drivetrain;

        // By default, we'll set out speed to 1.0
        this.maximumSpeed = () -> 1.0;

        // By default, we'll set amp factor to 1.0
        this.amplificationFactor = () -> 1.0;

        // Use the axis inputs that the user provided
        this.xInput = xInput;
        this.yInput = yInput;
        this.finalX = this.xInput;
        this.finalY = this.yInput;
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

        this.amplificationFactor = () -> prefs
                .getDouble(this.m_drivetrain.preferencesKey("inputAmplificationFactor").toString(), 1.0);

        this.finalX = () -> this.maximumSpeed.getAsDouble() * this.xInput.getAsDouble();
        this.finalY = () -> this.maximumSpeed.getAsDouble() * this.yInput.getAsDouble();

        // Use all of the previously provided configuration variables, aside from the
        // maximum speed
        return this;
    }

    /**
     * Executes the command.
     */
    @Override
    public void execute() {
        // Get the factor by which the input should be amplified
        double inputAmplificationFactor = this.amplificationFactor.getAsDouble();

        // Get the readings from the joystick input
        double[] inputs = { this.finalX.getAsDouble(), this.finalY.getAsDouble() };

        // Normalize each of the inputs, with consideration to the provided
        // amplifciation factor
        for (int i = 0; i < inputs.length; i++) {
            // Normalize the input
            double normalized = Math.pow(inputs[i], inputAmplificationFactor);

            // Reapply a negative sign, if it exists in the original input
            if (inputs[i] < 0 && normalized >= 0) {
                normalized *= -1;
            }

            // Put the normalized input back into the array
            inputs[i] = normalized;
        }

        // Drive the drivetrain with a differential drive config
        this.m_drivetrain.drive(Type.DIFFERENTIAL, inputs);
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
