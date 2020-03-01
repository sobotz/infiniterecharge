/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.LauncherConstants;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class LauncherSubsystem extends SubsystemBase {

  /**
   * Creates a new Launcher.
   */

  public WPI_TalonFX launcherMotor;
  public WPI_TalonFX launcherMotor2;
  WPI_TalonSRX feedMotor;

  public LauncherSubsystem() {
    launcherMotor = new WPI_TalonFX(LauncherConstants.LAUNCHER_MOTOR_1);
    launcherMotor2 = new WPI_TalonFX(LauncherConstants.LAUNCHER_MOTOR_2);
    feedMotor = new WPI_TalonSRX(Constants.ROLLER_MOTOR);

    launcherMotor.configFactoryDefault();
    launcherMotor2.configFactoryDefault();
    feedMotor.configFactoryDefault();
  }

  public void startRollers() {
    feedMotor.set(ControlMode.PercentOutput, 1);
  }

  public void stopRollers() {
    feedMotor.set(ControlMode.PercentOutput, 0);
  }

  public void startLauncher() {
    launcherMotor.set(ControlMode.PercentOutput, 1);
    launcherMotor.setInverted(true);
    launcherMotor2.follow(launcherMotor);
  }

  public void stopLauncher() {
    launcherMotor.set(ControlMode.Velocity, 0);
    launcherMotor2.set(ControlMode.Velocity, 0);
  }
}