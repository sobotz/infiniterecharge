/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.DeliverIntakeCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.IntakeControl;
import frc.robot.commands.ShiftGearCommand;
import frc.robot.subsystems.ControlPanel;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Launcher;
import frc.robot.subsystems.LiftSubsystem;
import frc.robot.subsystems.Navigation;
import frc.robot.subsystems.Serializer;
import frc.robot.subsystems.Vision;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  public static Joystick joystick;
  public static Joystick operatorJoystick;

// The robot's subsystems and commands are defined here...
  private final ControlPanel m_controlPanel;
  private final Drive m_driveSubsystem;
  private final Intake m_intakeSubsystem;
  private final Launcher m_launcherSubsystem;
  private final LiftSubsystem m_liftSubsystem;
  private final Navigation m_navigationSubsystem;
  private final Serializer m_serializerSubsystem;
  private final Vision m_visionSubsystem;

  private final DriveCommand m_driveCommand;
  private final ShiftGearCommand m_gearCommand;
  private final DeliverIntakeCommand m_deliverIntakeCommand;
  private final IntakeControl m_intakeControlCommand;
  
  public RobotContainer() {
    joystick = new Joystick(0);
    operatorJoystick = new Joystick(1);

    m_controlPanel = new ControlPanel();
    m_driveSubsystem = new Drive();
    m_intakeSubsystem = new Intake();
    m_launcherSubsystem = new Launcher();
    m_liftSubsystem = new LiftSubsystem();
    m_navigationSubsystem = new Navigation();
    m_serializerSubsystem = new Serializer();
    m_visionSubsystem = new Vision();


    this.m_driveCommand = new DriveCommand(m_driveSubsystem);
    this.m_gearCommand = new ShiftGearCommand(m_driveSubsystem);
    this.m_deliverIntakeCommand = new DeliverIntakeCommand(m_intakeSubsystem);
    this.m_intakeControlCommand = new IntakeControl(m_intakeSubsystem);

    // Configure the button bindings
    configureButtonBindings();

    //m_driveSubsystem.setDefaultCommand(new DriveCommand(m_driveSubsystem));
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    JoystickButton gearShiftButton = new JoystickButton(operatorJoystick, 1);
    JoystickButton deliverIntakeButton = new JoystickButton(operatorJoystick, 2);
    JoystickButton controlIntakeButton = new JoystickButton(operatorJoystick, 3);

    gearShiftButton.toggleWhenPressed(m_gearCommand);
    deliverIntakeButton.toggleWhenPressed(m_deliverIntakeCommand);
    controlIntakeButton.whenPressed(m_intakeControlCommand);
    m_intakeSubsystem.setDefaultCommand(m_intakeControlCommand);
  }


  public Command getTeleopCommand() {
    // An ExampleCommand will run in teleop
    return m_driveCommand;
  }
}
