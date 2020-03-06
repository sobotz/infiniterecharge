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
import edu.wpi.first.wpilibj.Timer;
//import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.SerializerConstants;

public class SerializerSubsystem extends SubsystemBase {
  /**
   * Creates a new Serializer.
   */
  WPI_TalonSRX serializerMotor1;
  // WPI_TalonSRX serializerMotor2;
  // WPI_TalonFX serializerMotor; For use with Falcon 500

  // Initializes the sensors in the serializer and launcher
  public AnalogInput serializerSensor1;
  public AnalogInput serializerSensor2;
  public AnalogInput launcherSensor;

  // Initializes variables that wiil be used in the program
  public double ballCount = 3.0;
  public boolean acceptingBalls = true;
  public boolean previousLSValue = false; // previous launcher sensor value
  public boolean previousSSValue = false; // previous serializer sensor value
  public boolean hasBeenMovedForward = false;
  public boolean isVomiting = false;
  public double previousBallCount = 3.0;

  public SerializerSubsystem() {
    // instantiates sensor values with respect to the contants method
    serializerSensor1 = new AnalogInput(Constants.PHOTOELECTRIC_SENSOR_1);
    serializerSensor2 = new AnalogInput(Constants.PHOTOELECTRIC_SENSOR_2);
    launcherSensor = new AnalogInput(Constants.PHOTOELECTRIC_SENSOR_3);
    SmartDashboard.putNumber("Ball Count: ", ballCount);

    this.serializerMotor1 = new WPI_TalonSRX(SerializerConstants.SERIALIZER_MOTOR);
    serializerMotor1.configFactoryDefault();

    // serializerMotor = new WPI_TalonFX(Constants.SERIALIZER_MOTOR);
  }

  // Called every time the Command Scheduler runs (every 20 miliseconds)
  public void periodic() {
    if (!isVomiting) {
      // Recieves possible user input from the smart dashboard
      // ballCount = SmartDashboard.getNumber("Ball Count", ballCount);
      // outputs current value to the smart dashboard
      // System.out.println("running periodic: " + serializerSensor1.getVoltage());
      // Puts sensor voltage values on the Smart dashboard
      // SmartDashboard.putNumber("Sensor 1: ", serializerSensor1.getVoltage()); //
      // true
      // SmartDashboard.putNumber("Sensor 2: ", serializerSensor2.getVoltage()); //
      // true

      if (!previousSSValue && serializerSensor2.getVoltage() < .85) {
        // update ballCount
        ballCount++;
        SmartDashboard.putNumber("Ball Count: ", ballCount);
      }
      previousSSValue = serializerSensor2.getVoltage() < .85;

      acceptingBalls = ballCount < 5; // Took out >= 0 because we should still be able to accept balls even when
                                      // negative
      /*
       * if (acceptingBalls) { if (serializerSensor2Value) { if (previousBallCount ==
       * ballCount) { // adds one to the ball count ballCount++; // updates ballcount
       * on smart dashboard ballCount = SmartDashboard.getNumber("Ball Count",
       * ballCount); SmartDashboard.putNumber("Ball Count", ballCount); } } else {
       * previousBallCount = ballCount; } }
       */

      /*
       * if (launcherSensor.getVoltage() < 0.85) {
       * 
       * if (ballCount > 0 && !previousLSValue) { // decrement ballCount by 1
       * ballCount--; // update ballCount //SmartDashboard.putNumber("Ball Count: ",
       * ballCount); previousLSValue = true; } ballCount =
       * SmartDashboard.getNumber("Ball Count: ", ballCount); }
       */
      if (serializerSensor1.getVoltage() < .85 && acceptingBalls) {
        serializerMotor1.set(ControlMode.PercentOutput, -SerializerConstants.SERIALIZER_SPEED);
        // SmartDashboard.putBoolean("Belts On: ", true);
      } else {
        serializerMotor1.set(ControlMode.PercentOutput, 0);
        // SmartDashboard.putBoolean("Belts On: ", false);
      }

      SmartDashboard.putNumber("Ball Count", ballCount);
      previousBallCount = ballCount;
    } else {
      if (!previousSSValue && serializerSensor2.getVoltage() < .85) {
        // update ballCount
        ballCount--;
        SmartDashboard.putNumber("Ball Count: ", ballCount);
      }
      previousSSValue = serializerSensor2.getVoltage() < .85;
    }

  }

  public void moveBeltsForward() {
    // accepting balls is set to false to stop incorrect ball placement in the
    // serializer
    acceptingBalls = false;
    // turns serializer motor on
    serializerMotor1.set(ControlMode.PercentOutput, -0.5);
    // lets us know if the belts are running
    // SmartDashboard.putBoolean("Belts On: ", true);
    // changes the amount of time moved forward based on the ball count
    Timer.delay(0.5); // check
    // turns serializer motor on
    serializerMotor1.set(ControlMode.PercentOutput, 0);
    // outputs belt state to the smart dashboard
    // SmartDashboard.putBoolean("Belts On: ", false);
  }

  public void moveBack() {
    // runs belts until sensor at the start of the serializer is triggered
    if (serializerSensor2.getVoltage() < .85) {
      // starts belts in inverse
      serializerMotor1.set(ControlMode.PercentOutput, SerializerConstants.SERIALIZER_SPEED);
      // SmartDashboard.putBoolean("Belts On: ", true);
    } else {
      // stops belts
      serializerMotor1.set(ControlMode.PercentOutput, 0);
      // outputs belt states to the smart dashboard
      // SmartDashboard.putBoolean("Belts On: ", false);
      // allows balls to be intaken again
      acceptingBalls = true;
    }
  }

  public void stopSerializer() {
    serializerMotor1.set(ControlMode.PercentOutput, 0);
  }

  public void runSerializer(int posOrNeg) {
    serializerMotor1.set(ControlMode.PercentOutput, posOrNeg * SerializerConstants.SERIALIZER_SPEED);
  }
}
