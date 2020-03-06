/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.subsystems.SerializerSubsystem;

public class LaunchAllCommand extends CommandBase {
  private SerializerSubsystem serializer;
  private LauncherSubsystem launcher;
  // private int nFramesRun;

  /**
   * Creates a new LaunchAllCommand.
   */
  public LaunchAllCommand(SerializerSubsystem serializer1, LauncherSubsystem launcher1) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.serializer = serializer1;
    this.launcher = launcher1;
    // this.nFramesRun = 0;
    addRequirements(serializer, launcher);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // this.nFramesRun = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!this.serializer.hasBeenMovedForward) {
      // moves belts forward
      this.serializer.moveBeltsForward();
      this.serializer.hasBeenMovedForward = true;
    }
    this.launcher.startLauncher();
    // checks if launcher Motor is up to speed
    if (this.launcher.launcherMotor.getActiveTrajectoryVelocity() > 7
        && this.launcher.launcherMotor2.getActiveTrajectoryVelocity() > 7) {
      // starts rollers and serializer
      this.serializer.runSerializer(-1);
      this.launcher.startRollers();
    }

    // this.nFramesRun++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    this.launcher.stopRollers();
    this.launcher.stopLauncher();
    this.serializer.stopSerializer();
    // this.nFramesRun = 0;
    this.serializer.hasBeenMovedForward = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // return this.nFramesRun > 500;
    return this.serializer.ballCount == 0;
  }
}
