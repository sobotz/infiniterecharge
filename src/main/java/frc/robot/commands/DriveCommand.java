/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;

public class DriveCommand extends CommandBase {
  DriveSubsystem drivetrain; 

  /**
   * Creates a new DriveCommand.
   */
  public DriveCommand(DriveSubsystem subsystem) {
    this.drivetrain = subsystem;

    addRequirements(drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double xAxis = RobotContainer.m_leftDriverJoystick.getRawAxis(0);
    double yAxis = RobotContainer.m_leftDriverJoystick.getRawAxis(1);

    System.out.println(yAxis);

    if (Math.abs(yAxis) < .1){
      yAxis = 0;
    }

    if (Math.abs(xAxis) < .1){
      xAxis = 0;
    }

    drivetrain.manualDrive2(yAxis * yAxis, -(xAxis * xAxis));
   // drivetrain.manualDrive2(yAxis, -xAxis);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
