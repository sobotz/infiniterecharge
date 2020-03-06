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
import frc.robot.commands.IntakeDirectionControl;
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

    /* The robot's serializer subsystem. */
    private final SerializerSubsystem m_serializer;

    /*The robot's launcher subsystem. */
    private final LauncherSubsystem m_launcher;

    /* END SUBSYSTEMS */

    /* The robot's settings. */
    private final Preferences m_preferences;

    /* The driver's joystick. */
    public static Joystick m_leftDriverJoystick;

    /*The operator's joystick*/
    private Joystick m_operatorJoystick;
    
    /*The button box*/
    public Joystick m_buttonBox;

    /* BEGIN COMMANDS */

    /* A fallback teleOp command for the robot (arcade drive). */
    private final DifferentialDriveCommand fallbackTeleopCommand;

    /* The current autonomous command for the robot. */
    private final MoveToReflectiveTargetCommand visionCommand;

    /* A command used to control the intake. */
    private final DeliverIntakeCommand deliverIntakeCommand;

    /* The command used to launch each of the power cells. */
    private final LaunchAllCommand launchCommand;

    /* The command used to run the intake forward. */
    private final IntakeDirectionControl intakeForwardCommand;

    /* The command used to run the intake forward. */
    private final IntakeDirectionControl intakeReverseCommand;

    

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
        this.m_serializer = new SerializerSubsystem();
        this.m_launcher = new LauncherSubsystem();

        // Set up the controllers for the teleop command
        this.m_leftDriverJoystick = new Joystick(0);
        this.m_operatorJoystick = new Joystick(1);
        this.m_buttonBox = new Joystick(2);

        // Set up an alternative teleop command that uses arcade drive; use just one
        // joystick
        this.fallbackTeleopCommand = new DifferentialDriveCommand(this.m_drivetrain,
                () -> m_leftDriverJoystick.getRawAxis(0), () -> -m_leftDriverJoystick.getRawAxis(1),
                () -> m_leftDriverJoystick.getRawAxis(2)).applyPreferences(this.m_preferences);


        // SmartDashboard in order to override default values
        this.visionCommand = new MoveToReflectiveTargetCommand(this.m_drivetrain, this.m_vision,
                MoveToReflectiveTargetCommand.Configuration.getDefault().applyPreferences(this.m_preferences));

        // Setup a command to control the intake subsystem from, using the left driver
        // joystick
        this.deliverIntakeCommand = new DeliverIntakeCommand(this.m_intake);
        
        /* Set up two commands that control the diretion of the intake. */
        this.intakeForwardCommand = new IntakeDirectionControl(this.m_intake, false);
        this.intakeReverseCommand = new IntakeDirectionControl(this.m_intake, true);
        
        /* Set up a command that launches balls from the serializer */
        this.launchCommand = new LaunchAllCommand(this.m_serializer, this.m_launcher);

        //this.m_testLaunchCommand = new TestLaunchCommand(this.m_serializer, this.m_launcher);

        // Configure the button bindings
        this.configureButtonBindings();
    }

    /**
     * Activates bindings to the autonomous command from the button box.
     */
    private void configureButtonBindings() {
        JoystickButton gearShiftButton = new JoystickButton(m_leftDriverJoystick, 1);
        JoystickButton activateVisionButton = new JoystickButton(this.m_operatorJoystick, 3);
        JoystickButton deliverIntakeButton = new JoystickButton(this.m_operatorJoystick, 1);
        JoystickButton forwardIntakeButton = new JoystickButton(this.m_operatorJoystick, 5);
        JoystickButton reverseIntakeButton = new JoystickButton(this.m_buttonBox, 1);
        JoystickButton launchBallsButton = new JoystickButton(this.m_operatorJoystick, 6);
        JoystickButton serializerForwardButton = new JoystickButton(this.m_buttonBox, 2);
        JoystickButton serializerReverseButton = new JoystickButton(this.m_buttonBox,3);
        
        /* Shifts between low and high gear when the trigger on the driver joystick is pressed*/
        gearShiftButton.toggleWhenPressed(new ShiftGearCommand(this.m_drivetrain));

        /* Shifts between intake up and intake down when A is pressed on the operator joystick */
        deliverIntakeButton.toggleWhenPressed(this.deliverIntakeCommand);

        /* When the left bumper button is pressed, run the intake forward */
        forwardIntakeButton.toggleWhenPressed(this.intakeForwardCommand); 

        /* When the first button on the button box is pressed, run the intake in reverse */
        reverseIntakeButton.toggleWhenPressed(this.intakeReverseCommand);

        /* Runs the vision command when X is pressed */
        activateVisionButton.toggleWhenPressed(this.visionCommand);

        /*When the right bumper is held, run the serializer, feed, and launcher to shoot balls */
        launchBallsButton.whenHeld(this.launchCommand);

        //serializerForwardButton.toggleWhenPressed(this. )

        //JoystickButton bob = new JoystickButton(m_driveController, 0);
        //JoystickButton ballPrep = new JoystickButton(this.m_leftDriverJoystick, 1);
        //ballPrep.toggleWhenPressed(this.m_testLaunchCommand);
        // this.m_intake.setDefaultCommand(intakeControlCommand);
    }

    public Command getTeleopCommand() {
        // Use differential drive
        return this.fallbackTeleopCommand;
    }
}
