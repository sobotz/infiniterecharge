/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final class DriveConstants{
		public static final int LEFT_FRONT_MOTOR = 0;
		public static final int LEFT_BACK_MOTOR = 1;
		public static final int RIGHT_FRONT_MOTOR = 2;
		public static final int RIGHT_BACK_MOTOR = 3;

		public static final int GEAR_SHIFT_DEPLOY = 0;
		public static final int GEAR_SHIFT_RETRACT = 1;

		public static final double TURN_P = .02;
		public static final double TURN_I = .015;
		public static final double TURN_D = 0;

		public static final double TURN_RATE_TOLERANCE = 4; // degrees
		public static final double TURN_TO_TOLERANCE = 4; // degrees per sec

		public static final boolean GYRO_REVERSED = false;

		//public static final double MAX_TURN_RATE = 100; //degrees per sec
		//public static final double MAX_TURN_ACCELERATION = 300;//degrees per sec squared
	
	}
}

