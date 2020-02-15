/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
import frc.robot.common.Preferences;

public class IntakeSubsystem extends SubsystemBase implements Preferences.Group {
    WPI_TalonSRX leftIntakeMotor;
    WPI_TalonSRX rightIntakeMotor;

    private DoubleSolenoid intakeDelivery;
    private boolean intakeDeliveryState = false;

    /**
     * Creates a new ExampleSubsystem.
     */

    public IntakeSubsystem() {
        leftIntakeMotor = new WPI_TalonSRX(IntakeConstants.LEFT_INTAKE_MOTOR);
        rightIntakeMotor = new WPI_TalonSRX(IntakeConstants.RIGHT_INTAKE_MOTOR);

        intakeDelivery = new DoubleSolenoid(IntakeConstants.INTAKE_SOLENOID_DEPLOY,
                IntakeConstants.INTAKE_SOLENOID_RETRACT);

        rightIntakeMotor.setInverted(true);

        leftIntakeMotor.configFactoryDefault();
        rightIntakeMotor.configFactoryDefault();
    }

    /**
     * Gets the name of the preferences group.
     * 
     * @return the name of the preferences group
     */
    @Override
    public String groupName() {
        return "intake";
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

    }

    public void deliverIntake() {
        if (intakeDeliveryState) {
            intakeDelivery.set(DoubleSolenoid.Value.kForward);
        } else {
            intakeDelivery.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public void changeMotorState() {
        if (intakeDeliveryState) {
            rightIntakeMotor.stopMotor();
            leftIntakeMotor.stopMotor();
        } else {
            rightIntakeMotor.set(ControlMode.PercentOutput, .1);
            leftIntakeMotor.set(ControlMode.PercentOutput, .1);
        }
    }

    public boolean getIntakeDeliveryState() {
        return intakeDeliveryState;
    }

    public void setIntakeDelivery(boolean state) {
        intakeDeliveryState = state;
    }

    public void reverseMotors() {
        rightIntakeMotor.stopMotor();
        leftIntakeMotor.stopMotor();
        Timer.delay(2);
        rightIntakeMotor.set(ControlMode.PercentOutput, -.1);
        leftIntakeMotor.set(ControlMode.PercentOutput, -.1);
        Timer.delay(5);
        rightIntakeMotor.stopMotor();
        leftIntakeMotor.stopMotor();
        Timer.delay(2);
        rightIntakeMotor.set(ControlMode.PercentOutput, .1);
        leftIntakeMotor.set(ControlMode.PercentOutput, .1);
    }
}
