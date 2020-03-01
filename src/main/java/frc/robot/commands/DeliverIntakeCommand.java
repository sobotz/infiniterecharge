/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

public class DeliverIntakeCommand extends CommandBase {
    private final IntakeSubsystem m_intake;
    private boolean done;
    private boolean direction;

    /**
     * Creates a new DeliverIntakeCommand.
     */
    public DeliverIntakeCommand(IntakeSubsystem subsystem) {
        m_intake = subsystem;
        this.done = false;
        this.direction = true;

        addRequirements(m_intake);
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        this.done = false;
        this.direction = true;
        this.m_intake.dropIntake();
    }

    public void setDirection(boolean direction) {
        // Wait half a second to reverse the direction
        Timer.delay(0.5);

        this.direction = direction;
    }

    public void execute() {
        this.m_intake.runIntake(this.direction ? -1.0 : 1.0);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        this.m_intake.pullIntakeUp();
        this.done = true;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return this.done;
    }
}
