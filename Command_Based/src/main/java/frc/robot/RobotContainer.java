package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Arm.ArmPosition;

import static edu.wpi.first.units.Units.Degrees;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {
  private final Intake m_Intake;
  private final Arm m_Arm;
  private final CommandXboxController m_Control;


  public RobotContainer() {
    this.m_Intake = new Intake();
    this.m_Arm = new Arm();
    this.m_Control = new CommandXboxController(OperatorConstants.kDriverControllerPort);

    configureBindings();
  }
  
  private void configureBindings() {
    m_Control.a().onTrue(m_Intake.setMotorInCommand());
    m_Control.a().onFalse(m_Intake.stopMotorCommand());

    m_Control.b().onTrue(m_Intake.guardarPieza());
    m_Control.b().onFalse(m_Intake.stopMotorCommand());

    m_Control.povUp().onTrue(m_Arm.setSetpointCommand(ArmPosition.HIGH.getPosition()));
    m_Control.povLeft().onTrue(m_Arm.setSetpointCommand(ArmPosition.MIDDLE.getPosition()));
    m_Control.povDown().onTrue(m_Arm.setSetpointCommand(ArmPosition.HORIZONTAL.getPosition()));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
