/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.Vision.LEDMode;

/**
 * Constants holds a list of robot-wide constant definitions.
 */
public final class Constants {
	public static final class ControlPanelConstants {
		public static final int CONTROL_PANEL_MOTOR = 4;
	}

	public static final class DriveConstants {
		public static final int LEFT_FRONT_MOTOR = 0;
		public static final int LEFT_BACK_MOTOR = 1;
		public static final int RIGHT_FRONT_MOTOR = 2;
		public static final int RIGHT_BACK_MOTOR = 3;
		public static final int GEAR_SHIFT_DEPLOY = 0;
		public static final int GEAR_SHIFT_RETRACT = 1;
	}

	public static final class IntakeConstants {
		public static final int INTAKE_SOLENOID_DEPLOY = 2;
		public static final int INTAKE_SOLENOID_RETRACT = 3;
		public static final int LEFT_INTAKE_MOTOR = 5;
		public static final int RIGHT_INTAKE_MOTOR = 6;
	}

	public static final class LauncherConstants {
		public static final int LAUNCHER_MOTOR_1 = 7;
		public static final int LAUNCHER_MOTOR_2 = 8;
	}

	public static final class LiftConstants {
	}

	public static final class NavigationConstants {
	}

	public static final class SerializerConstants {
		public static final int SERIALIZER_SENSOR_1 = 0;
		public static final int SERIALIZER_SENSOR_2 = 1;
		public static final int SERIALIZER_SENSOR_3 = 2;
		public static final int SERIALIZER_MOTOR = 0;
		public static final double SERIALIZER_SPEED = 0;
	}

	/**
	 * VisionConstants defines useful constant values for the vision subsystem
	 * class.
	 */
	public static final class VisionConstants {
		/* By default, the limelight should remain on, constantly. */
		public static final LEDMode DEFAULT_LIMELIGHT_MODE = LEDMode.ON;
	}

	public static final class OIConstants {
	}
}
