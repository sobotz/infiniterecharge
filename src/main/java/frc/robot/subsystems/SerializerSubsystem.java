/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SerializerConstants;
import frc.robot.common.Preferences;

public class SerializerSubsystem extends SubsystemBase implements Preferences.Group {
    WPI_TalonFX serializerMotor;

    DigitalInput serializerSensor1, serializerSensor2, serializerSensor3;

    int counter = 0;

    /**
     * Creates a new ExampleSubsystem.
     */

    public SerializerSubsystem() {
        serializerMotor = new WPI_TalonFX(SerializerConstants.SERIALIZER_MOTOR);

        serializerSensor1 = new DigitalInput(SerializerConstants.SERIALIZER_SENSOR_1);
        serializerSensor2 = new DigitalInput(SerializerConstants.SERIALIZER_SENSOR_2);
        serializerSensor3 = new DigitalInput(SerializerConstants.SERIALIZER_SENSOR_3);
    }

    /**
     * Gets the name of the preferences group.
     * 
     * @return the name of the preferences group
     */
    @Override
    public String groupName() {
        return "serializer";
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

    }
}
