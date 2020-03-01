/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeDirectionControl extends CommandBase {
  IntakeSubsystem m_intake;

  //true is full speed forward, false is full speed reverse
  boolean speedDirection; 
  /**
   * Creates a new IntakeDirectionControl.
   */
  public IntakeDirectionControl(IntakeSubsystem subsystem, boolean direction) {
    m_intake = subsystem;
    speedDirection = direction;
    addRequirements(m_intake);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(speedDirection){
      this.m_intake.runIntake(1.0);
    }else{
      this.m_intake.runIntake(-1.0);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    this.m_intake.runIntake(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  public void reverseIntake(){
    this.m_intake.runIntake(0.0);
    speedDirection = !speedDirection;
  }
}
