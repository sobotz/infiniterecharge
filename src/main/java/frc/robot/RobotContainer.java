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
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.TurnDegrees;
import frc.robot.commands.TurnToAngle;
import frc.robot.subsystems.Drive;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  public static Joystick joystick;
  public static Joystick operatorJoystick;

  private final Drive m_driveSubsystem;

  private final DriveCommand m_driveCommand;
  private final TurnToAngle m_navxCommand;
  private final TurnDegrees m_turnDegreesCommand;
  //private final ShiftGearCommand m_gearCommand;


  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    joystick = new Joystick(0);
    operatorJoystick = new Joystick(1);

    m_driveSubsystem = new Drive();

    m_driveCommand = new DriveCommand(m_driveSubsystem);
    m_navxCommand = new TurnToAngle(180, m_driveSubsystem);
    m_turnDegreesCommand = new TurnDegrees(180, m_driveSubsystem);
    //m_gearCommand = new ShiftGearCommand(m_driveSubsystem);
    configureButtonBindings();

    m_driveSubsystem.setDefaultCommand(m_driveCommand);

  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    JoystickButton turnToAngle = new JoystickButton(joystick, 2);
    turnToAngle.whenPressed(m_navxCommand);
    JoystickButton turnDegrees = new JoystickButton(joystick, 8);
    turnDegrees.whenPressed(m_turnDegreesCommand);
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  //public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    //return m_autoCommand;
//  }
}
