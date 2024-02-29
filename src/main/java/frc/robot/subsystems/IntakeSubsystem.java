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
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
import frc.robot.common.Preferences;

public class IntakeSubsystem extends SubsystemBase implements Preferences.Group {
    WPI_TalonSRX intakeTalon;

    private DoubleSolenoid intakeDelivery;

    public boolean hasDeployed;

    /**
     * Creates a new ExampleSubsystem.
     */

    public IntakeSubsystem() {
        intakeTalon = new WPI_TalonSRX(IntakeConstants.INTAKE_MOTOR);

        intakeDelivery = new DoubleSolenoid(IntakeConstants.INTAKE_SOLENOID_DEPLOY, IntakeConstants.INTAKE_SOLENOID_RETRACT);

        intakeTalon.configFactoryDefault();

        hasDeployed = false;
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
        this.runIntake(0.0);
        this.intakeDelivery.set(Value.kForward);
    }

    public void runIntake(double speed) {
        this.intakeTalon.set(ControlMode.PercentOutput, speed * IntakeConstants.MAXIMUM_INTAKE_SPEED);
    }

    public void retractIntake() {
        this.intakeDelivery.set(Value.kReverse);
    }

    public boolean toggleIntake() {
        if (this.hasDeployed) {
            intakeDelivery.set(DoubleSolenoid.Value.kForward);
            this.runIntake(0);
            this.hasDeployed = false;
        } else {
            intakeDelivery.set(DoubleSolenoid.Value.kReverse);
            this.runIntake(1);
            this.hasDeployed = true;
        }

        return !this.hasDeployed;
    }

    /*
     * public void reverseMotors(){ rightIntakeMotor.stopMotor();
     * leftIntakeMotor.stopMotor(); Timer.delay(2);
     * rightIntakeMotor.set(ControlMode.PercentOutput, -.1);
     * leftIntakeMotor.set(ControlMode.PercentOutput, -.1); Timer.delay(5);
     * rightIntakeMotor.stopMotor(); leftIntakeMotor.stopMotor(); Timer.delay(2);
     * rightIntakeMotor.set(ControlMode.PercentOutput, .1);
     * leftIntakeMotor.set(ControlMode.PercentOutput, .1); }
     */

}
