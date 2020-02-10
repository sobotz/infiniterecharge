/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Serializer extends SubsystemBase {
  /**
   * Creates a new Serializer.
   */
  WPI_TalonSRX serializerMotor1;
  WPI_TalonSRX serializerMotor2;
  // WPI_TalonFX serializerMotor; For use with Falcon 500
  public DigitalInput serializerSensor1;
  public DigitalInput serializerSensor2;
  public DigitalInput launcherSensor;
  public double ballCount = 0;
  public boolean acceptingBalls = true;
  public boolean previousLSValue = false;
  public boolean previousSSValue = false;

  public Serializer() {
    serializerSensor1 = new DigitalInput(Constants.PHOTOELECTRIC_SENSOR_1);
    serializerSensor2 = new DigitalInput(Constants.PHOTOELECTRIC_SENSOR_2);
    launcherSensor = new DigitalInput(Constants.PHOTOELECTRIC_SENSOR_3);

    /*
     * serializerMotor1 = new WPI_TalonSRX(Constants.TOP_SERIALIZER_MOTOR);
     * serializerMotor2 = new WPI_TalonSRX(Constants.BOTTOM_SERIALIZER_MOTOR);
     * serializerMotor1.configFactoryDefault();
     * serializerMotor2.configFactoryDefault();
     */
    // serializerMotor = new WPI_TalonFX(Constants.SERIALIZER_MOTOR);
    /*
     * serializerMotor2.follow(serializerMotor1);
     * serializerMotor1.setInverted(false);
     * serializerMotor2.setInverted(InvertType.FollowMaster);
     */
  }

  public void periodic() {
    SmartDashboard.putNumber("Ball Count: ", ballCount);
    if ((!previousLSValue && serializerSensor2.get()) && acceptingBalls) {
      ballCount = SmartDashboard.getNumber("Ball Count: ", ballCount);
      ballCount++;
      SmartDashboard.putNumber("Ball Count: ", ballCount);
      System.out.println(ballCount);
    }
    System.out.println("hello world");
    if ((!serializerSensor1.get() || serializerSensor2.get()) && acceptingBalls) {

      // serializerMotor1.set(ControlMode.PercentOutput, 0.5);
      // else don't run
      previousLSValue = serializerSensor2.get();
      SmartDashboard.putBoolean("Belts On: ", true);
    } else {
      // serializerMotor1.set(ControlMode.PercentOutput, 0);
      SmartDashboard.putBoolean("Belts On: ", false);
    }
    if (ballCount <= 5) {
      acceptingBalls = false;
    }
  }

  public void moveBeltsForward() {
    acceptingBalls = false;
    while (launcherSensor.get()) {
      // serializerMotor1.set(ControlMode.PercentOutput, 0.5);
      SmartDashboard.putBoolean("Belts On: ", true);
    }
    // serializerMotor1.set(ControlMode.PercentOutput, 0);
    SmartDashboard.putBoolean("Belts On: ", false);
  }

  public void moveBack() {
    while (serializerSensor2.get()) {
      // serializerMotor1.set(ControlMode.PercentOutput, -0.5);
      SmartDashboard.putBoolean("Belts On: ", true);
    }
    // serializerMotor1.set(ControlMode.PercentOutput, 0);
    SmartDashboard.putBoolean("Belts On: ", false);
    acceptingBalls = true;
  }

}
