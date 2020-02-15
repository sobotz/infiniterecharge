package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.common.Preferences;

/**
 * DriveSubsystem is a subsystem that handles control of the drivetrain.
 */
public class DriveSubsystem extends SubsystemBase implements Preferences.Group {
    /**
     * Specifies a type of drive for the subsystem. Either differential (arcade) or
     * rhino.
     */
    public static enum Type {
        DIFFERENTIAL, RHINO
    }

    public static class MotorControllerConfiguration {
        /* The front left motor controller */
        WPI_TalonFX frontLeftController, frontRightController, backLeftController, backRightController;

        /* Controller groups for the left and right sides of the robot */
        SpeedControllerGroup right, left;

        /**
         * Initializes a new MotorControllerConfiguration with the given ports.
         *
         * @param frontLeftControllerPort  the port of the front left motor controller
         * @param frontRightControllerPort the port of the front right motor controller
         * @param backLeftControllerPort   the port of the back left motor controller
         * @param backRightControllerPort  the port of the back right motor controller
         */
        public MotorControllerConfiguration(int frontLeftControllerPort, int frontRightControllerPort,
                int backLeftControllerPort, int backRightControllerPort) {
            // Initialize each of the talons
            this.frontLeftController = new WPI_TalonFX(frontLeftControllerPort);
            this.frontRightController = new WPI_TalonFX(frontRightControllerPort);
            this.backLeftController = new WPI_TalonFX(backLeftControllerPort);
            this.backRightController = new WPI_TalonFX(backRightControllerPort);
        }

        /**
         * Drives the robot according to a left and right percentage speed.
         *
         * @param driveType            the type of drive to use
         * @param leftPercentageSpeed  the desired speed of the left motor controllers
         * @param rightPercentageSpeed the desired speed of the right motor controllers
         */
        void drive(Type driveType, double leftPercentageSpeed, double rightPercentageSpeed) {
            if (driveType.equals(Type.DIFFERENTIAL)) {
                // Treat the rightPercentageSpeed as a rotational parameter
                this.frontLeftController.set(ControlMode.PercentOutput, leftPercentageSpeed,
                        DemandType.ArbitraryFeedForward, rightPercentageSpeed);
                this.backLeftController.follow(this.frontLeftController);

                this.frontRightController.set(ControlMode.PercentOutput, leftPercentageSpeed,
                        DemandType.ArbitraryFeedForward, -rightPercentageSpeed);
                this.backRightController.follow(this.frontRightController);

                return;
            }

            this.frontLeftController.set(-leftPercentageSpeed);
            this.backLeftController.follow(this.frontLeftController);

            this.frontRightController.set(rightPercentageSpeed);
            this.backRightController.follow(this.frontRightController);
        }
    }

    /* The motor controllers that will be used in the drive subsystem. */
    private final MotorControllerConfiguration motorControllers;

    /* Whether or not the pneumatic gear-shift has been activated. */
    private boolean hasShifted;

    /* The pneumatic doulbe solenoid used to shift gears. */
    private DoubleSolenoid gearShifter;

    /**
     * Initializes a new DriveSubsystem.
     */
    public DriveSubsystem(MotorControllerConfiguration motorConfig) {
        // Use the motor controller configuration provided to us by the calling command
        this.motorControllers = motorConfig;
        this.hasShifted = false;
        this.gearShifter = new DoubleSolenoid(Constants.DriveConstants.GEAR_SHIFT_DEPLOY,
                Constants.DriveConstants.GEAR_SHIFT_RETRACT);
    }

    /**
     * Gets the name of the preferences group.
     * 
     * @return the name of the preferences group
     */
    @Override
    public String groupName() {
        return "drive";
    }

    /**
     * Drives the robot with the given percentage speed values.
     *
     * @param driveType        the manner in which the robot should drive
     * @param percentageSpeeds the percentage speed values to drive with
     */
    public void drive(Type driveType, double[] percentageSpeeds) {
        if (driveType.equals(Type.RHINO)) {
            // Drive the robot
            this.motorControllers.drive(Type.RHINO, percentageSpeeds[0], percentageSpeeds[1]);

            return;
        }

        // Use differential drive to drive the robot
        this.motorControllers.drive(Type.DIFFERENTIAL, percentageSpeeds[0], percentageSpeeds[1]);
    }

    /**
     * Shifts the gear value associated with the drivetrain pneumatically.
     * 
     * @return the previous value (shifted or not)
     */
    public boolean shiftGear() {
        this.hasShifted = !this.hasShifted;
        this.gearShifter.set(this.hasShifted ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);

        return !this.hasShifted;
    }
}