/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SerializerSubsystem;

public class PurgeCommand extends CommandBase {
  /**
   * Creates a new backCommand.
   */
  private SerializerSubsystem serializer;
  private IntakeSubsystem intake;

  public PurgeCommand(SerializerSubsystem s, IntakeSubsystem i) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.serializer = s;
    this.intake = i;
    addRequirements(serializer, intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    this.serializer.acceptingBalls = false;
    if (this.intake.hasDeployed)
      this.intake.toggleIntake();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    this.serializer.reverseBelts();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    this.serializer.stopBelts();
    this.serializer.acceptingBalls = true;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
