/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

public class DeliverIntakeCommand extends CommandBase {
    private final IntakeSubsystem m_intake;

    /**
     * Creates a new DeliverIntakeCommand.
     */
    public DeliverIntakeCommand(IntakeSubsystem subsystem) {
        m_intake = subsystem;
        addRequirements(m_intake);
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        if (m_intake.getIntakeDeliveryState()) {
            m_intake.changeMotorState();
            m_intake.deliverIntake();
            m_intake.setIntakeDelivery(false);
        } else {
            m_intake.deliverIntake();
            m_intake.changeMotorState();
            m_intake.setIntakeDelivery(true);
        }
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
