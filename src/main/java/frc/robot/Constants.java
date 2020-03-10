/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.VisionSubsystem.LEDMode;

/**
 * Constants holds a list of robot-wide constant definitions.
 */
public final class Constants {
	public static final int MISERY_MOTOR = 9;

	public static final class ControlPanelConstants {
		public static final int CONTROL_PANEL_MOTOR = 4;
	}

	public static final class DriveConstants {
		// changed for d bot testing
		public static final int LEFT_FRONT_MOTOR = 0; // 2
		public static final int LEFT_BACK_MOTOR = 1; // 1
		public static final int RIGHT_FRONT_MOTOR = 2; // 4
		public static final int RIGHT_BACK_MOTOR = 3; // 3
		public static final int GEAR_SHIFT_DEPLOY = 1;
		public static final int GEAR_SHIFT_RETRACT = 0;

		public static final double TURN_P = 0.02;
		public static final double TURN_I = 0.0;
		public static final double TURN_D = 0.0;

		public static final double TURN_RATE_TOLERANCE = 4; // degrees
		public static final double TURN_TO_TOLERANCE = 4; // degrees per sec

		public static final boolean GYRO_REVERSED = false;

		public static final double DISTANCE_P = 0.1;
		public static final double DISTANCE_I = 0.0;
		public static final double DISTANCE_D = 0.0;
		public static final int DISTANCE_Iz = 100;
		public static final double DISTANCE_PEAK_OUTPUT = .5;

		// public static final double DRIVE_STRAIGHT_F = 0.0;
		public static final double DRIVE_STRAIGHT_P = 0.1;
		public static final double DRIVE_STRAIGHT_I = 0.0;
		public static final double DRIVE_STRAIGHT_D = 0.0;
		public static final int DRIVE_STRAIGHT_IZ = 200;
		public static final double DRIVE_STRAIGHT_PEAK_OUTPUT = 1.00;

		public final static int TIMEOUT_MS = 30;

		public final static int PID_PRIMARY = 0;
		public final static int PID_DRIVE_STRAIGHT = 1;

		public final static int SLOT_0 = 0;
		public final static int SLOT_1 = 1;

		public final static int kSlot_Distanc = SLOT_0;
		public final static int kSlot_Turning = SLOT_1;

		/**
		 * Number of rotations to drive when performing Distance Closed Loop
		 */
		public final static double kRotationsToTravel = 6;

		public static final double DISTANCE_PER_PULSE = (1 / 2048) * (6 * Math.PI);
		public static final double WHEEL_DIAMETER = 6;

		public static final int kTimeoutMs = 30;
		public static final int kTurnTravelUnitsPerRotation = 3600;

		public final static int kSensorUnitsPerRotation = 2048;

		/**
		 * Empirically measure what the difference between encoders per 360' Drive the
		 * robot in clockwise rotations and measure the units per rotation. Drive the
		 * robot in counter clockwise rotations and measure the units per rotation. Take
		 * the average of the two.
		 */
		public static final int kEncoderUnitsPerRotation = 51711;

		// (64910.5/100)/2048;
	}

	public static final class IntakeConstants {
		public static final int INTAKE_SOLENOID_DEPLOY = 2;
		public static final int INTAKE_SOLENOID_RETRACT = 3;
		public static final int INTAKE_MOTOR = 8;
		public static final double MAXIMUM_INTAKE_SPEED = 0.4;
	}

	public static final class LauncherConstants {
		public static final int LAUNCHER_MOTOR_1 = 5;
		public static final int LAUNCHER_MOTOR_2 = 6;
	}

	public static final class LiftConstants {
	}

	public static final class NavigationConstants {
	}

	public static final class SerializerConstants {
		public static final int SERIALIZER_SENSOR_1 = 0;
		public static final int SERIALIZER_SENSOR_2 = 1;
		public static final int SERIALIZER_SENSOR_3 = 2;
		public static final int SERIALIZER_MOTOR = 7;
		public static final int FEED_MOTOR = 10;
		public static final double SERIALIZER_SPEED = 0.4;
	}

	/**
	 * VisionConstants defines useful constant values for the vision subsystem
	 * class.
	 */
	public static final class VisionConstants {
		/* By default, the limelight should remain on, constantly. */
		public static final LEDMode DEFAULT_LIMELIGHT_MODE = LEDMode.ON;

		/*
		 * The default multiplier for the distance moved in the
		 * MoveToReflectiveTargetCommand.
		 */
		public static final double DEFAULT_KP = 1.0;

		/*
		 * The default amount added to the output of the vision subsystem command on
		 * each run.
		 */
		public static final double DEFAULT_KI = 0.01;

		/*
		 * A secondary multiplier for the distance moved in the
		 * MoveToReflectiveTargetCommand. This multiplier is applied in a logarithmic
		 * fashion. For example, a value of 0.4 causes the robot to slow down as it
		 * reaches the target output.
		 */
		public static final double DEFAULT_KCHANGE = 0.4;

		/*
		 * The default number of degrees that a target may be from the center of the
		 * vision camera's view.
		 */
		public static final double DEFAULT_ERROR_TOLERANCE = 0.25;

		/* The maximum speed of the vision command. */
		public static final double DEFAULT_MAX_SPEED = 0.75;

		/* The maximum forwards and backwards speed of the vision command. */
		public static final double DEFAULT_MAX_FORWARD_SPEED = 1;

		/* The degree radius of the vision camera. */
		public static final double[] DEFAULT_BOUNDS = new double[] { 27.0, 20.5, 0.7 };

		/*
		 * The number of calls to the vision command in which there must not be a target
		 * for the command to be stopped.
		 */
		public static int TARGETLESS_FRAMES_TO_STOP = 15;

		/*
		 * The number of calls to the vision command in which there must not be a target
		 * for the command to be stopped.
		 */
		public static int TARGETLESS_FRAMES_TO_Z_STOP = 10;
	}

	public static final class OIConstants {
	}

	// Serializer Motors
	public static final int ROLLER_MOTOR = 10; // For use with Falcon 500

	// Sensors
	public static final int PHOTOELECTRIC_SENSOR_1 = 0;
	public static final int PHOTOELECTRIC_SENSOR_2 = 1;
	public static final int PHOTOELECTRIC_SENSOR_3 = 2;
	// Controllers
	public static final int JOYSTICK = 0;
	public static final double LAUNCHER_VELOCITY_MS = 1.88;
}
