/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.SerializerConstants;

public class SerializerSubsystem extends SubsystemBase {
  /**
   * Creates a new Serializer.
   */
  WPI_TalonSRX serializerMotor1;
  //WPI_TalonSRX serializerMotor2;
  // WPI_TalonFX serializerMotor; For use with Falcon 500

  // Initializes the sensors in the serializer and launcher
  private AnalogInput serializerSensor1;
  private AnalogInput serializerSensor2;
  private AnalogInput launcherSensor;

  // Initializes variables that wiil be used in the program
  public boolean acceptingBalls = true;

  public SerializerSubsystem() {
    // instantiates sensor values with respect to the contants method
    serializerSensor1 = new AnalogInput(Constants.PHOTOELECTRIC_SENSOR_1);
    serializerSensor2 = new AnalogInput(Constants.PHOTOELECTRIC_SENSOR_2);
    launcherSensor = new AnalogInput(Constants.PHOTOELECTRIC_SENSOR_3);
    
    this.serializerMotor1 = new WPI_TalonSRX(SerializerConstants.SERIALIZER_MOTOR);
    serializerMotor1.configFactoryDefault();
    
    // serializerMotor = new WPI_TalonFX(Constants.SERIALIZER_MOTOR);
  }

  
  // Called every time the Command Scheduler runs (every 20 miliseconds)
  public void periodic() {
    serializerMotor1.set(ControlMode.PercentOutput, ((serializerSensor1.getVoltage() < .85 || serializerSensor2.getVoltage() < .85) && launcherSensor.getVoltage() > .85 && this.acceptingBalls) ? -SerializerConstants.SERIALIZER_SPEED : 0);
  }
  
  public void runBelts() {
    serializerMotor1.set(ControlMode.PercentOutput, -SerializerConstants.SERIALIZER_SPEED);
  }

  public void stopBelts() {
    serializerMotor1.set(ControlMode.PercentOutput, 0);
  }

  public void reverseBelts() {
    serializerMotor1.set(ControlMode.PercentOutput, SerializerConstants.SERIALIZER_SPEED);
  }

}
