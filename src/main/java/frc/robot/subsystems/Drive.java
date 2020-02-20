/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.Constants.DriveConstants;

public class Drive extends SubsystemBase {
  /**
   * Creates a new Drive.
   */
  public WPI_TalonFX leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor;

  AHRS ahrs;
  private DoubleSolenoid gearShifter;
  private boolean gearShifterState = false;

  private SpeedControllerGroup leftMotors, rightMotors;
  public DifferentialDrive differentialDrive;

  public Drive() {
    leftFrontMotor = new WPI_TalonFX(DriveConstants.LEFT_FRONT_MOTOR);
    leftBackMotor = new WPI_TalonFX(DriveConstants.LEFT_BACK_MOTOR);
    rightFrontMotor = new WPI_TalonFX(DriveConstants.RIGHT_FRONT_MOTOR);
    rightBackMotor = new WPI_TalonFX(DriveConstants.RIGHT_BACK_MOTOR);

    gearShifter = new DoubleSolenoid(DriveConstants.GEAR_SHIFT_DEPLOY, DriveConstants.GEAR_SHIFT_RETRACT);

    rightFrontMotor.setInverted(true);
    rightBackMotor.setInverted(true);

    leftFrontMotor.configFactoryDefault();
    leftBackMotor.configFactoryDefault();
    rightFrontMotor.configFactoryDefault();
    rightBackMotor.configFactoryDefault();

    leftFrontMotor.configPeakOutputReverse(-.2);
    leftFrontMotor.configPeakOutputForward(.2);
    rightFrontMotor.configPeakOutputReverse(-.2);
    rightFrontMotor.configPeakOutputForward(.2);

    ahrs = new AHRS(SPI.Port.kMXP);
    
    //leftMotors = new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
    //rightMotors = new SpeedControllerGroup(rightFrontMotor, rightBackMotor);
    
    //differentialDrive = new DifferentialDrive(leftMotors, rightMotors);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Angle", getHeading());
    SmartDashboard.putNumber("PIDGet", ahrs.pidGet());
    // This method will be called once per scheduler run
  }

  public void manualDrive(double speed, double rotation) {

    if (!RobotContainer.joystick.getRawButton(11) & !RobotContainer.joystick.getRawButton(12)) {
        leftFrontMotor.set(ControlMode.PercentOutput, -speed, DemandType.ArbitraryFeedForward, rotation);
        rightFrontMotor.set(ControlMode.PercentOutput, -speed, DemandType.ArbitraryFeedForward, -rotation);
        leftBackMotor.follow(leftFrontMotor);
        rightBackMotor.follow(rightFrontMotor);
    }

    if (RobotContainer.joystick.getRawButton(11)) {
        leftFrontMotor.set(ControlMode.PercentOutput, -1);
        rightFrontMotor.set(ControlMode.PercentOutput, -1);
        leftBackMotor.follow(leftFrontMotor);
        rightBackMotor.follow(rightFrontMotor);
    }

    if (RobotContainer.joystick.getRawButton(12)) {
        leftFrontMotor.set(ControlMode.PercentOutput, 1);
        rightFrontMotor.set(ControlMode.PercentOutput, 1);
        leftBackMotor.follow(leftFrontMotor);
        rightBackMotor.follow(rightFrontMotor);
    }

  }

  /*public void arcadeDrive(double speed, double rotation){
      differentialDrive.arcadeDrive(speed, rotation);
  }
  */
  


  public void shiftGear() {
    if (gearShifterState) {
        gearShifter.set(DoubleSolenoid.Value.kForward);
        gearShifterState = false;
    } else {
        gearShifter.set(DoubleSolenoid.Value.kReverse);
        gearShifterState = true;
    }
  }

  public void zeroHeading() {
    ahrs.zeroYaw();
  }

  public double getHeading() {
    return Math.IEEEremainder(ahrs.getAngle(), 360) * (DriveConstants.GYRO_REVERSED ? -1.0 : 1.0);
  }

  public double getTurnRate() {
    return ahrs.getRate() * (DriveConstants.GYRO_REVERSED ? -1.0 : 1.0);
  }

  public void reset(){
    leftFrontMotor.stopMotor();
    rightFrontMotor.stopMotor();
    Timer.delay(.002);
  }


}
