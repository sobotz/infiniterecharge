package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DriveConstants;
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

        /* Config objets for motor ontrollers */
        TalonFXConfiguration leftConfig, rightConfig;

        /**
         * Initializes a new MotorControllerConfiguration with the given ports.
         *
         * @param frontLeftControllerPort  the port of the front left motor controller
         * @param frontRightControllerPort the port of the front right motor controller
         * @param backLeftControllerPort   the port of the back left motor controller
         * @param backRightControllerPort  the port of the back right motor controller
         */
        public MotorControllerConfiguration(final int frontLeftControllerPort, final int frontRightControllerPort,
                final int backLeftControllerPort, final int backRightControllerPort) {
            // Initialize each of the talons
            this.frontLeftController = new WPI_TalonFX(frontLeftControllerPort);
            this.frontRightController = new WPI_TalonFX(frontRightControllerPort);
            this.backLeftController = new WPI_TalonFX(backLeftControllerPort);
            this.backRightController = new WPI_TalonFX(backRightControllerPort);

            final TalonFXInvertType leftInvert = TalonFXInvertType.CounterClockwise; //Same as invert = "fasle"
	        final TalonFXInvertType rightInvert = TalonFXInvertType.Clockwise; //Same as invert = "true"

            this.leftConfig = new TalonFXConfiguration();
            this.rightConfig = new TalonFXConfiguration();

            this.frontLeftController.setInverted(leftInvert);
            this.backLeftController.setInverted(leftInvert);
            this.frontRightController.setInverted(rightInvert);
            this.backRightController.setInverted(rightInvert);
            

            /* local feedbak source */
            leftConfig.primaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice();

            /*Configure the front left talon sensor as the remote sensor for the right talons */ 
            rightConfig.remoteFilter1.remoteSensorDeviceID = this.frontLeftController.getDeviceID();
            rightConfig.remoteFilter1.remoteSensorSource =  RemoteSensorSource.TalonFX_SelectedSensor;

            setRobotTurnConfigs(rightInvert, rightConfig);

            leftConfig.peakOutputForward = +1.0;
            leftConfig.peakOutputReverse = -1.0;
            rightConfig.peakOutputForward = 1.0;
            rightConfig.peakOutputReverse = -1.0;

            // rightConfig.slot0.kF = DriveConstants.DISTANCE_F;
            rightConfig.slot0.kP = DriveConstants.DISTANCE_P;
            rightConfig.slot0.kI = DriveConstants.DISTANCE_I;
            rightConfig.slot0.kD = DriveConstants.DISTANCE_D;
            rightConfig.slot0.integralZone = DriveConstants.DISTANCE_Iz;
            rightConfig.slot0.closedLoopPeakOutput = DriveConstants.DISTANCE_PEAK_OUTPUT;

            /* FPID Gains for turn servo */
            // rightConfig.slot1.kF = DriveConstants.DRIVE_STRAIGHT_F;
            rightConfig.slot1.kP = DriveConstants.DRIVE_STRAIGHT_P;
            rightConfig.slot1.kI = DriveConstants.DRIVE_STRAIGHT_I;
            rightConfig.slot1.kD = DriveConstants.DRIVE_STRAIGHT_D;
            rightConfig.slot1.integralZone = DriveConstants.DRIVE_STRAIGHT_IZ;
            rightConfig.slot1.closedLoopPeakOutput = DriveConstants.DRIVE_STRAIGHT_PEAK_OUTPUT;

            final int closedLoopTimeMs = 1;
            rightConfig.slot0.closedLoopPeriod = closedLoopTimeMs;
            rightConfig.slot1.closedLoopPeriod = closedLoopTimeMs;

            this.frontLeftController.configAllSettings(leftConfig);
            this.backLeftController.configAllSettings(leftConfig);
            this.frontRightController.configAllSettings(rightConfig);
            this.backRightController.configAllSettings(rightConfig);

            this.frontRightController.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20,
                    DriveConstants.kTimeoutMs);
            this.frontRightController.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20,
                    DriveConstants.kTimeoutMs);
            this.frontRightController.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20,
                    DriveConstants.kTimeoutMs);
            this.frontLeftController.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, DriveConstants.kTimeoutMs);

            // 44 to 30, 2.92 spread

            // zeroSensors();

            // Reset the configuration of each of the talons
            this.frontLeftController.configFactoryDefault();
            this.frontRightController.configFactoryDefault();
            this.backLeftController.configFactoryDefault();
            this.backRightController.configFactoryDefault();
        }

        /**
         * Drives the robot according to a left and right percentage speed.
         *
         * @param driveType            the type of drive to use
         * @param leftPercentageSpeed  the desired speed of the left motor controllers
         * @param rightPercentageSpeed the desired speed of the right motor controllers
         */
        void drive(final Type driveType, final double leftPercentageSpeed, final double rightPercentageSpeed) {
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
    private final DoubleSolenoid gearShifter;

    /* The navx interface. */
    private final AHRS ahrs;

    private double lockedDistance = 0;

    private double targetAngle;

    /**
     * Initializes a new DriveSubsystem.
     */
    public DriveSubsystem(final MotorControllerConfiguration motorConfig) {
        // Use the motor controller configuration provided to us by the calling command
        this.motorControllers = motorConfig;
        this.hasShifted = false;
        this.gearShifter = new DoubleSolenoid(Constants.DriveConstants.GEAR_SHIFT_DEPLOY,
                Constants.DriveConstants.GEAR_SHIFT_RETRACT);
        this.ahrs = new AHRS(SPI.Port.kMXP);
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
    public void drive(final Type driveType, final double[] percentageSpeeds) {
        // Use the preferred drive to drive the robot
        this.motorControllers.drive(driveType, percentageSpeeds[0], percentageSpeeds[1]);
    }

    /**
     * Shifts the gear value associated with the drivetrain pneumatically.
     * 
     * @return the previous value (shifted or not)
     */
    public boolean shiftGear() {
        if (this.hasShifted) {
            gearShifter.set(DoubleSolenoid.Value.kForward);
            this.hasShifted = false;
        } else {
            gearShifter.set(DoubleSolenoid.Value.kReverse);
            this.hasShifted = true;
        }

        return !this.hasShifted;
    }

    public double getHeading() {
        return Math.IEEEremainder(ahrs.getAngle(), 360) * (DriveConstants.GYRO_REVERSED ? -1.0 : 1.0);
    }

    public void zeroHeading() {
        ahrs.zeroYaw();
    }

    public double inchesToTalonUnits(final double inches) {
        return (inches / (Math.PI * DriveConstants.WHEEL_DIAMETER)) * 2048.0;
    }

    public void setDriveToTargetValues() {
        this.lockedDistance = motorControllers.frontRightController.getSelectedSensorPosition(0);
        this.targetAngle = motorControllers.frontRightController.getSelectedSensorPosition(1);

        this.motorControllers.frontRightController.selectProfileSlot(DriveConstants.kSlot_Distanc,
                DriveConstants.PID_PRIMARY);
        this.motorControllers.frontRightController.selectProfileSlot(DriveConstants.kSlot_Turning,
                DriveConstants.PID_DRIVE_STRAIGHT);

    }

    public boolean driveToTarget(double distance) {

        final double targetPositionRotations = inchesToTalonUnits(distance);

        // this.motorControllers.frontLeftController.set(ControlMode.Position,
        // targetPositionRotations);
        // this.motorControllers.frontRightController.set(ControlMode.Position,
        // targetPositionRotations);

        final double target_sensorUnits = targetPositionRotations + lockedDistance;
        final double target_turn = targetAngle;

        this.motorControllers.frontRightController.set(TalonFXControlMode.Position, target_sensorUnits,
                DemandType.AuxPID, target_turn);
        this.motorControllers.frontLeftController.follow(this.motorControllers.frontRightController,
                FollowerType.AuxOutput1);
        // this.motorControllers.frontLeftController.set(TalonFXControlMode.Position,
        // target_sensorUnits, DemandType.AuxPID, target_turn);

        this.motorControllers.backLeftController.follow(this.motorControllers.frontLeftController);
        this.motorControllers.backRightController.follow(this.motorControllers.frontRightController);

        // this.motorControllers.frontLeftController.follow(motorControllers.frontRightController,
        // FollowerType.AuxOutput1);

        System.out.println(this.motorControllers.frontRightController.getSelectedSensorPosition(0));
        return ((this.motorControllers.frontRightController.getSelectedSensorPosition(0) >= target_sensorUnits - 100)
                && this.motorControllers.frontRightController.getSelectedSensorPosition(0) <= target_sensorUnits + 100);

    }

    public void zeroSensors() {
        motorControllers.frontLeftController.getSensorCollection().setIntegratedSensorPosition(0,
                DriveConstants.TIMEOUT_MS);
        motorControllers.frontRightController.getSensorCollection().setIntegratedSensorPosition(0,
                DriveConstants.TIMEOUT_MS);
        System.out.println("[Integrated Sensors] All sensors are zeroed.\n");
    }

    public static void setRobotTurnConfigs(TalonFXInvertType masterInvertType, TalonFXConfiguration masterConfig) {

		/* Check if we're inverted */
		if (masterInvertType == TalonFXInvertType.Clockwise){
			/* 
				If master is inverted, that means the integrated sensor
				will be negative in the forward direction.
				If master is inverted, the final sum/diff result will also be inverted.
				This is how Talon FX corrects the sensor phase when inverting 
				the motor direction.  This inversion applies to the *Selected Sensor*,
				not the native value.
				Will a sensor sum or difference give us a positive heading?
				Remember the Master is one side of your drivetrain distance and 
				Auxiliary is the other side's distance.
					Phase | Term 0   |   Term 1  | Result
				Sum:  -1 *((-)Master + (+)Aux   )| OK - magnitude will cancel each other out
				Diff: -1 *((-)Master - (+)Aux   )| NOT OK - magnitude increases with forward distance.
				Diff: -1 *((+)Aux    - (-)Master)| NOT OK - magnitude decreases with forward distance
			*/

			masterConfig.sum0Term = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice(); //Local Integrated Sensor
			masterConfig.sum1Term = TalonFXFeedbackDevice.RemoteSensor1.toFeedbackDevice();   //Aux Selected Sensor
			masterConfig.auxiliaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.SensorSum.toFeedbackDevice(); //Sum0 + Sum1

			/*
				PID Polarity
				With the sensor phasing taken care of, we now need to determine if the PID polarity is in the correct direction
				This is important because if the PID polarity is incorrect, we will run away while trying to correct
				Will inverting the polarity give us a positive counterclockwise heading?
				If we're moving counterclockwise(+), and the master is on the right side and inverted,
				it will have a negative velocity and the auxiliary will have a negative velocity
				 heading = right + left
				 heading = (-) + (-)
				 heading = (-)
				Let's assume a setpoint of 0 heading.
				This produces a positive error, in order to cancel the error, the right master needs to
				drive backwards. This means the PID polarity needs to be inverted to handle this
				
				Conversely, if we're moving counterclwise(+), and the master is on the left side and inverted,
				it will have a positive velocity and the auxiliary will have a positive velocity.
				 heading = right + left
				 heading = (+) + (+)
				 heading = (+)
				Let's assume a setpoint of 0 heading.
				This produces a negative error, in order to cancel the error, the left master needs to
				drive forwards. This means the PID polarity needs to be inverted to handle this
			*/
			masterConfig.auxPIDPolarity = true;
		} else {
			/* Master is not inverted, both sides are positive so we can diff them. */
			masterConfig.diff0Term = TalonFXFeedbackDevice.RemoteSensor1.toFeedbackDevice();    //Aux Selected Sensor
			masterConfig.diff1Term = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice(); //Local IntegratedSensor
			masterConfig.auxiliaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.SensorDifference.toFeedbackDevice(); //Sum0 + Sum1
			/* With current diff terms, a counterclockwise rotation results in negative heading with a right master */
			masterConfig.auxPIDPolarity = true;
		}
		/**
		 * Heading units should be scaled to ~4000 per 360 deg, due to the following limitations...
		 * - Target param for aux PID1 is 18bits with a range of [-131072,+131072] units.
		 * - Target for aux PID1 in motion profile is 14bits with a range of [-8192,+8192] units.
		 *  ... so at 3600 units per 360', that ensures 0.1 degree precision in firmware closed-loop
		 *  and motion profile trajectory points can range +-2 rotations.
		 */
		masterConfig.auxiliaryPID.selectedFeedbackCoefficient = DriveConstants.kTurnTravelUnitsPerRotation / DriveConstants.kEncoderUnitsPerRotation;
	}
   
}
