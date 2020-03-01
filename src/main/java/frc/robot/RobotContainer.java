/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.DeliverIntakeCommand;
import frc.robot.commands.DifferentialDriveCommand;
import frc.robot.commands.LaunchAllCommand;
import frc.robot.commands.MoveToReflectiveTargetCommand;
import frc.robot.commands.ShiftGearCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.DriveSubsystem.MotorControllerConfiguration;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.subsystems.SerializerSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.VisionSubsystem.LimelightConfiguration;

/**
 * RobotContainer holds all of the robot's subsystems, commands, and utility
 * variables.
 */
public class RobotContainer {
    /* BEGIN SUBSYSTEMS */

    /* The robot's drivetrain. */
    private final DriveSubsystem m_drivetrain;

    /* The robot's intake subsystem. */
    private final IntakeSubsystem m_intake;

    /* The robot's vision subsystem. */
    private final VisionSubsystem m_vision;

    /* END SUBSYSTEMS */

    /* The robot's settings. */
    private final Preferences m_preferences;

    /* The driver's joystick. */
    private final Joystick m_leftDriverJoystick, m_operatorJoystick;

    /* BEGIN COMMANDS */

    /* A fallback teleOp command for the robot (arcade drive). */
    private final DifferentialDriveCommand fallbackTeleopCommand;

    /* The current autonomous command for the robot. */
    private final MoveToReflectiveTargetCommand visionCommand;

    /* A command used to control the intake. */
    private final DeliverIntakeCommand intakeControlCommand;

    /* The command used to launch each of the power cells. */
    private final LaunchAllCommand launchCommand;

    private SerializerSubsystem m_serializer;
    private LauncherSubsystem m_launcher;
    //private TestLaunchCommand m_testLaunchCommand;

    /* END COMMANDS */

    public RobotContainer() {
        // The preferences utility class is where we get ALL of our settings from. It is
        // a direct link to the SmartDashboard, where users can submit preferences to
        // the RIO
        this.m_preferences = Preferences.getInstance();

        // Make a motor controller config for the drivetrain
        MotorControllerConfiguration motorCfg = new MotorControllerConfiguration(
                Constants.DriveConstants.LEFT_FRONT_MOTOR, Constants.DriveConstants.RIGHT_FRONT_MOTOR,
                Constants.DriveConstants.LEFT_BACK_MOTOR, Constants.DriveConstants.RIGHT_BACK_MOTOR);

        // Keep the light on the limelight on at all times
        LimelightConfiguration visionCfg = new LimelightConfiguration(VisionSubsystem.LEDMode.ON)
                .applyPreferences(this.m_preferences);

        // Initialize each of the subsystems
        this.m_drivetrain = new DriveSubsystem(motorCfg);
        this.m_intake = new IntakeSubsystem();
        this.m_vision = new VisionSubsystem(visionCfg);

        // Set up the controllers for the teleop command
        this.m_leftDriverJoystick = new Joystick(0);
        this.m_operatorJoystick = new Joystick(1);

        // Set up an alternative teleop command that uses arcade drive; use just one
        // joystick
        this.fallbackTeleopCommand = new DifferentialDriveCommand(this.m_drivetrain,
                () -> this.m_leftDriverJoystick.getRawAxis(0), () -> -this.m_leftDriverJoystick.getRawAxis(1),
                () -> this.m_leftDriverJoystick.getRawAxis(2)).applyPreferences(this.m_preferences);

        // Setup the vision command, and use the provided preferences from the
        // SmartDashboard in order to override default values
        this.visionCommand = new MoveToReflectiveTargetCommand(this.m_drivetrain, this.m_vision,
                MoveToReflectiveTargetCommand.Configuration.getDefault().applyPreferences(this.m_preferences));

        // Setup a command to control the intake subsystem from, using the left driver
        // joystick
        this.intakeControlCommand = new DeliverIntakeCommand(this.m_intake);

        this.m_serializer = new SerializerSubsystem();
        this.m_launcher = new LauncherSubsystem();

        this.launchCommand = new LaunchAllCommand(this.m_serializer, this.m_launcher);
        //this.m_testLaunchCommand = new TestLaunchCommand(this.m_serializer, this.m_launcher);

        // Configure the button bindings
        this.configureButtonBindings();
    }

    /**
     * Activates bindings to the autonomous command from the button box.
     */
    private void configureButtonBindings() {
        JoystickButton gearShiftButton = new JoystickButton(this.m_operatorJoystick, 1);
        JoystickButton deliverIntakeButton = new JoystickButton(this.m_operatorJoystick, 2);
        JoystickButton activateVisionButton = new JoystickButton(this.m_operatorJoystick, 4);
        JoystickButton activateIntakeButton = new JoystickButton(this.m_operatorJoystick, 5);
        JoystickButton reverseIntakeButton = new JoystickButton(this.m_operatorJoystick, 6);

        gearShiftButton.toggleWhenPressed(new ShiftGearCommand(this.m_drivetrain));

        deliverIntakeButton.toggleWhenPressed(this.intakeControlCommand);

        // When the left bumper button is pressed, reverse the intake
        reverseIntakeButton.whenPressed(() -> this.intakeControlCommand.setDirection(false));
        activateIntakeButton.whenPressed(() -> this.intakeControlCommand.setDirection(true));

        // reverseIntakeButton.whenPressed(new ReverseIntakeCommand(this.m_intake));
        activateVisionButton.toggleWhenPressed(this.visionCommand);

        // JoystickButton bob = new JoystickButton(m_driveController, 0);
        //JoystickButton ballPrep = new JoystickButton(this.m_leftDriverJoystick, 1);
        //ballPrep.toggleWhenPressed(this.m_testLaunchCommand);

        JoystickButton ballsOut = new JoystickButton(this.m_operatorJoystick, 7);
        ballsOut.toggleWhenPressed(this.launchCommand);

        // this.m_intake.setDefaultCommand(intakeControlCommand);
    }

    public Command getTeleopCommand() {
        // Use differential drive
        return this.fallbackTeleopCommand;
    }
}
