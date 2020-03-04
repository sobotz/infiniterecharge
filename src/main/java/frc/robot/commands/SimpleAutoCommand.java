/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.subsystems.SerializerSubsystem;

public class SimpleAutoCommand extends CommandBase {
  private final DriveSubsystem m_drive;
  private final SerializerSubsystem m_serializer;
  private LauncherSubsystem m_launcher;
  private boolean isFinished = false;
  private int nFramesRun;
  
  public SimpleAutoCommand(DriveSubsystem drive, SerializerSubsystem serial, LauncherSubsystem launch) {
    this.m_drive = drive;
    this.m_serializer = serial;
    this.m_launcher = launch;

    this.nFramesRun = 0;

    addRequirements(this.m_drive, this.m_launcher, this.m_serializer);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    this.nFramesRun = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
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
