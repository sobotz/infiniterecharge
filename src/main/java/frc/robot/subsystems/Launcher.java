/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LauncherConstants;


public class Launcher extends SubsystemBase {
  WPI_TalonFX launcherMotor1;
  WPI_TalonFX launcherMotor2;
  /**
   * Creates a new ExampleSubsystem.
   */

  public Launcher() {
  launcherMotor1 = new WPI_TalonFX(LauncherConstants.LAUNCHER_MOTOR_1);
  launcherMotor2 = new WPI_TalonFX(LauncherConstants.LAUNCHER_MOTOR_2);

  launcherMotor1.configFactoryDefault();
  launcherMotor2.configFactoryDefault();

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
  }

}
