/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.common.Preferences;

public class NavigationSubsystem extends SubsystemBase implements Preferences.Group {

    /**
     * Creates a new ExampleSubsystem.
     */

    public NavigationSubsystem() {
    }

    /**
     * Gets the name of the preferences group.
     * 
     * @return the name of the preferences group
     */
    @Override
    public String groupName() {
        return "navigation";
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

    }
}
