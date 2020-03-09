/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SerializerSubsystem;

public class PurgeSerializerCommand extends CommandBase {
  /**
   * Creates a new PurgeSerializerCommand.
   */
  IntakeSubsystem intake;
  SerializerSubsystem serializer;
  boolean intakeUp = false;

  public PurgeSerializerCommand(SerializerSubsystem serializer, IntakeSubsystem intake) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.serializer = serializer;
    this.intake = intake;
    addRequirements(serializer, intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    this.serializer.serializerState = 1;
    if (!intakeUp) {
      this.intake.retractIntake();
      intakeUp = true;
    }
    this.serializer.runSerializer(1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    this.intake.deliverIntake();
    intakeUp = false;
    this.serializer.stopSerializer();
    this.serializer.serializerState = 0;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
