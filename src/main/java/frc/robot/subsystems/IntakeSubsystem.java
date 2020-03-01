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
    /* U|_|U ~ Yes. Thy field hath bestowed upon my broken, dry soul thy yellough bahl. I shall suck it up, if I must. */
    WPI_TalonSRX intakeTalon;

    /* OvO ~ Sire, shall I dispatch the bed upon which you may "suck up" thy yellough bahl? */
    private DoubleSolenoid intakePiston;

    /**
     * Creates a new ExampleSubsystem.
     */

    public IntakeSubsystem() {
        // Initialize a talon to suck the ball up with, and a solenoid to extend and retract the intake with
        this.intakeTalon = new WPI_TalonSRX(IntakeConstants.INTAKE_MOTOR);
        this.intakePiston = new DoubleSolenoid(IntakeConstants.INTAKE_SOLENOID_DEPLOY,
                IntakeConstants.INTAKE_SOLENOID_RETRACT);

        intakeTalon.configFactoryDefault();
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

    /**
     * Pulls the intake arm up and stops the intake motor.
     */
    public void pullIntakeUp() {
        this.runIntake(0.0);
        this.intakePiston.set(Value.kForward);
    }

    /**
     * Starts the intake motor at the given speed.
     *
     * @param speed the percentage of the maximum speed defined in Constants that the intake
     * motor will run at.
     */
    public void runIntake(double speed) {
        this.intakeTalon.set(ControlMode.PercentOutput, speed * IntakeConstants.MAXIMUM_INTAKE_SPEED);
    }

    /**
     * Drops the intake arm and starts the intake motor at full speed, with consideration to
     * the maximum speed defined in Constants.java.
     */
    public void dropIntake() {
        this.intakePiston.set(Value.kReverse);
        this.runIntake(1.0);
    }
}
