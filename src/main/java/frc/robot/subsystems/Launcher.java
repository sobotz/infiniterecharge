/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class Launcher extends SubsystemBase {

  /**
   * Creates a new Launcher.
   */

  public WPI_TalonFX launcherMotor;
  WPI_TalonSRX rollerMotor;

  public Launcher() {
    launcherMotor = new WPI_TalonFX(Constants.LAUNCHER_MOTOR);
    rollerMotor = new WPI_TalonSRX(Constants.ROLLER_MOTOR);

    launcherMotor.set(ControlMode.Velocity, 28.2);

    launcherMotor.configFactoryDefault();
    rollerMotor.configFactoryDefault();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void startRollers() {
    rollerMotor.set(ControlMode.Velocity, Constants.LAUNCHER_VELOCITY_MS);

  }

  public void stopRollers() {
    rollerMotor.set(ControlMode.Velocity, 0);

  }

  public void startLauncher() {
    launcherMotor.set(ControlMode.Velocity, Constants.LAUNCHER_VELOCITY_MS);

  }

  public void stopLauncher() {
    launcherMotor.set(ControlMode.Velocity, 0);
  }
}