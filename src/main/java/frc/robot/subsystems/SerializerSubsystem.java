/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.SerializerConstants;

import java.util.HashMap;
import java.util.ArrayList;

public class SerializerSubsystem extends SubsystemBase {
    /* The motor used to control the belts. */
    WPI_TalonSRX serializerMotor;

    /* The sensors triggered in the serializer. */
    private AnalogInput[] sensors;

    /* Whether or not the serializer can accept any more balls (is it at capacity?). */
    private boolean acceptingBalls = true;

    /* The balls that have passed into the serializer. */
    private ArrayList<Ball> balls;

    /**
     * Ball is an abstract representation of an object that might occupy a sensor at a point in time.
     **/
    private static class Ball {
        /* Whether or not a particular sensors has been tripped by the ball. */
        HashMap<Integer, Boolean> sensorTripped;

        /* The last sensor tripped by the ball. */
        int currentSensor;

        /**
         * Initializes a new Ball.
         **/
        Ball() {
            this.sensorTripped = new HashMap<Integer, Boolean>();

            // Since the ball has been detected, it must have been detected by the first sensor at least
            this.currentSensor = 0;
            this.sensorTripped.put(0, true);
        }

        /**
         * Registers that the ball has tripped the given sensor. If the sensor is out of range of the 3 serializer sensors, false is returned.
         *
         * @param sensor the sensor that should be tripped
         * @return whether or not the provided sensor is in range of the 3 serializer sensors
         **/
        boolean tripSensor(int sensor) {
            // If the sensor is out of range, return false
            if (sensor > 2) {
                return false;
            }

            // Trip the sensor
            this.sensorTripped.put(sensor, true);

            return true;
        }
    }

    /**
     * Creates a new Serializer.
     */
    public SerializerSubsystem() {
        // instantiates sensor values with respect to the contants method
        this.sensors = new AnalogInput[] { new AnalogInput(Constants.PHOTOELECTRIC_SENSOR_1), new AnalogInput(Constants.PHOTOELECTRIC_SENSOR_2), new AnalogInput(Constants.PHOTOELECTRIC_SENSOR_3) };

        // Create a new motor that we'll use to move the projectiles with, considering a port provided in the constants file
        this.serializerMotor = new WPI_TalonSRX(SerializerConstants.SERIALIZER_MOTOR);
        this.serializerMotor.configFactoryDefault();

        // Create a new ArrayList to store the balls that are inside the serializer.
        this.balls = new ArrayList<Ball>();
    }

    // Called every time the Command Scheduler runs (every 20 miliseconds)
    public void periodic() {
        // The index of the ball that is furthest lodged in the serializer
        int ball = this.balls.size() - 1;

        // Work our way down from the last sensor in the serializer
        for (int i = this.sensors.length - 1; i >= 0; i--) {
            // If there is no ball in front of the sensor, this means that there:
            // a. was no ball, and continues to be no ball in front of the sensor
            // or that there
            // b. was a ball in front of the sensor, but no longer
            if (!this.sensorTriggered(i)) {
                if (ball >= 0 && ball < this.balls.size()) {
                    // Get the index of the ball that would have been in this spot
                    Ball formerBall = this.balls.get(ball);

                    // If there was in fact a ball most ready to pop out, and it was once in front of this sensor, that means it has moved past this sensor
                    if (formerBall != null && formerBall.sensorTripped.get(i)) {
                        // Trip the next sensor, and pop the ball from the queue if necessary
                        if (!formerBall.tripSensor(i + 1)) {
                            this.balls.remove(this.balls.size() - 1);
                        }
                    }
                }
            } else {
                if (ball >= 0 && ball < this.balls.size()) {
                    // Trip the sensor
                    this.balls.get(ball).tripSensor(i);
                } else {
                    // This ball hasn't been seen before, and is entering for the first time
                    Ball newBall = new Ball();
                    this.balls.add(newBall);
                }
            }

            ball--;
        }

        // If there are balls in the serializer, start moving the belts
        if (this.hasBalls()) {
            this.moveBeltsForward();
        }
    }

    /**
     * Runs the serializer belt.
     **/
    public void moveBeltsForward() {
        // turns serializer motor on
        serializerMotor.set(ControlMode.PercentOutput, -0.5);
    }

    /**
     * Determines whether or not the serializer can accept more balls.
     *
     * @return whether or not the serializer can accept more balls
     **/
    public boolean canAcceptBalls() {
        // The serializer can hold 5 balls, at max
        return this.balls.size() < 5;
    }

    /**
     * Whether or not there are any balls inside the serializer.
     *
     * @return whether or not there are any balls inside the serializer
     **/
    public boolean hasBalls() {
        return this.balls.size() > 0;
    }

    /**
     * Determines whether or not a particular sensor has been triggered.
     *
     * @return whether or not the sensor has been triggered
     **/
    public boolean sensorTriggered(int sensor) {
        // The sensor is only triggered if its voltage is less than the provided threshold
        return this.sensors[sensor].getVoltage() < .85;
    }
}
