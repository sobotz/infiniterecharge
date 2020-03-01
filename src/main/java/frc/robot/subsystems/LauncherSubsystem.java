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
    /* The two motors used to launch projectiles from the launcher. */
    private WPI_TalonFX launcherMotor;
    private WPI_TalonFX launcherMotor2;

    /* The motor used to feed projectiles from the serializer into the launcher. */
    private WPI_TalonSRX feedMotor;

    /**
     * Creates a new Launcher.
     */
    public LauncherSubsystem() {
        this.launcherMotor = new WPI_TalonFX(LauncherConstants.LAUNCHER_MOTOR_1);
        this.launcherMotor2 = new WPI_TalonFX(LauncherConstants.LAUNCHER_MOTOR_2);
        this.feedMotor = new WPI_TalonSRX(Constants.ROLLER_MOTOR);

        this.launcherMotor.configFactoryDefault();
        this.launcherMotor2.configFactoryDefault();
        this.feedMotor.configFactoryDefault();
    }

    /**
     * Runs the single SRX motor used to feed projectiles from the serializer into the launcher.
     *
     * @param speed the speed at which the feed motor will run
     */
    public void runFeed(double speed) {
        feedMotor.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Sets the speed of the Launcher's feed motor to 0%. This is the same as calling `m_launcherSubsystem.runFeed(0.0)`.
     */
    public void stopFeed() {
        feedMotor.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Runs the two FX motors used to launch projectiles from the launcher at the given speed.
     *
     * @param speed the speed at which projectiles will be launched from the shooter.
     */
    public void runLauncher(double speed) {
        launcherMotor.set(ControlMode.PercentOutput, speed);
        launcherMotor2.follow(launcherMotor);
        launcherMotor2.setInverted(true);
    }

    /**
     * Runs the two FX motors at 0% speed, causing them to stop.
     */
    public void stopLauncher() {
        launcherMotor.set(ControlMode.PercentOutput, 0);
        launcherMotor2.set(ControlMode.PercentOutput, 0);
    }
}
