/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.subsystems.SerializerSubsystem;
import frc.robot.Constants;

public class TestLaunchCommand extends CommandBase {

  private SerializerSubsystem serializer;
  private LauncherSubsystem launcher;

  public TestLaunchCommand(SerializerSubsystem serializer1, LauncherSubsystem launcher1) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.serializer = serializer1;
    this.launcher = launcher1;
    addRequirements(serializer);
    addRequirements(launcher);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // movebeltsforward + starts launcher
    this.serializer.moveBeltsForward();
    this.launcher.startLauncher();
    while (this.serializer.previousBallCount != this.serializer.ballCount) {
      this.launcher.stopRollers();
      while (this.launcher.launcherMotor.getSelectedSensorVelocity() != Constants.LAUNCHER_VELOCITY_MS) {
        Timer.delay(0.01);//check
      }
      this.launcher.startRollers();
      Timer.delay(0.2);//check
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    this.launcher.stopRollers();
    this.launcher.stopLauncher();    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(this.serializer.ballCount <= 0){
    return true;  
    }
    return false;
  }
}
