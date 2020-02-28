/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.DriveSubsystem.Type;
import frc.robot.common.Patterns;

/**
 * An example command that uses an example subsystem.
 */
public class RhinoDriveCommand extends CommandBase implements Patterns.PreferenceChangeable<RhinoDriveCommand> {
    /* The drivetrain that we'll use to run the Rhino drive command. */
    private final DriveSubsystem m_drivetrain;

    /* The right and left joystick inputs. */
    private final DoubleSupplier leftJoystickInput, rightJoystickInput;

    /* A speed limitation variable provided through smart dashboard. */
    private DoubleSupplier maximumSpeed;

    /**
     * Creates a new RhinoDriveCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public RhinoDriveCommand(DriveSubsystem drivetrain, DoubleSupplier leftJoystickInput,
            DoubleSupplier rightJoystickInput) {
        // Use the provided drive train
        this.m_drivetrain = drivetrain;

        // By default, we'll set out speed to 1.0
        this.maximumSpeed = () -> 1.0;

        // Set the joystick inputs of the RhinoDriveCommand
        this.leftJoystickInput = () -> this.maximumSpeed.getAsDouble() * leftJoystickInput.getAsDouble();
        this.rightJoystickInput = () -> this.maximumSpeed.getAsDouble() * rightJoystickInput.getAsDouble();

        // The RhinoDriveCommand doesn't work without a drivetrain
        addRequirements(drivetrain);
    }

    /**
     * Applies the preferences defined in the SmartDashboard preferences pane to the
     * configurable object.
     * 
     * @param prefs the instance of the preferences class that should be used to
     *              configure the configurable class from
     */
    public RhinoDriveCommand applyPreferences(Preferences prefs) {
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
        // Drive
        this.m_drivetrain.drive(Type.RHINO,
                new double[] { this.leftJoystickInput.getAsDouble(), this.rightJoystickInput.getAsDouble() });
    }

    /**
     * Returns whether or not the command has finished executing.
     * 
     * @return whether or not the command has completed
     */
    @Override
    public boolean isFinished() {
        return false;
    }
}
