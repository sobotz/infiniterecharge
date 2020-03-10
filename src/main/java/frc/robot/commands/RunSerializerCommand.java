/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SerializerSubsystem;

public class RunSerializerCommand extends CommandBase {
  SerializerSubsystem m_serializer;
  boolean direction;

  /**
   * Creates a new RunSerializerCommand.
   */
  public RunSerializerCommand(SerializerSubsystem subsystem, boolean direction) {
    this.m_serializer = subsystem;
    this.direction = direction;

    addRequirements(m_serializer);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(this.direction){
      this.m_serializer.runSerializer(false);
    }else{
      this.m_serializer.runSerializer(true);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    this.m_serializer.stopSerializer();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
