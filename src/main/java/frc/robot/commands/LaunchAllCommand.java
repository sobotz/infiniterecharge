/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.subsystems.SerializerSubsystem;

public class LaunchAllCommand extends CommandBase {
    private SerializerSubsystem serializer;
    private LauncherSubsystem launcher;

    /**
     * Creates a new LaunchAllCommand.
     *
     * @param serializer the serializer that should be used by this command
     * @param launcher the launcher that should be used by this command
     */
    public LaunchAllCommand(SerializerSubsystem serializer, LauncherSubsystem launcher) {
        this.serializer = serializer;
        this.launcher = launcher;

        addRequirements(serializer, launcher);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Wait until the serializer is completely empty
        // by continuously running the belts
        if (this.serializer.hasBalls()) {
            this.serializer.moveBeltsForward();
        }

        // Launch any balls from the serializer to the feed to the power port
        this.launcher.runFeed(1.0);
        this.launcher.runLauncher(1.0);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        this.launcher.stopFeed();
        this.launcher.stopLauncher();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // Keep running until the serializer has been emptied
        return this.serializer.hasBalls();
    }
}
