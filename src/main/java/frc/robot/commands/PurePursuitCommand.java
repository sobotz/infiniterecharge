/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.navigation.*;
import frc.robot.subsystems.DriveSubsystem;

public class PurePursuitCommand extends CommandBase {
  private final DriveSubsystem m_driveSubsystem;
  Point[] purePursuitPath;
  EncoderController purePursuitE;
  private boolean isFinished = false;
  TalonFX frontLeft, frontRight;

  public PurePursuitCommand(Point[] points, DriveSubsystem subsystem, TalonFX lTalon, TalonFX rTalon) {
    this.m_driveSubsystem = subsystem;
    this.purePursuitPath = points;
    this.purePursuitE = new EncoderController(this.purePursuitPath, 0.8);
    this.frontLeft = lTalon;
    this.frontRight = rTalon;

    addRequirements(m_driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    this.isFinished = this.purePursuitE.controlLoop(this.m_driveSubsystem, this.m_driveSubsystem.talonToInches(frontLeft), 
      this.m_driveSubsystem.talonToInches(frontRight), m_driveSubsystem.getHeading());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return this.isFinished;
  }
}
