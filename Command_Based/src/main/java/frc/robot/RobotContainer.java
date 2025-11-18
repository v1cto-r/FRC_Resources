package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Arm.ArmPosition;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {
  private final Intake m_Intake = new Intake();
  private final Arm m_Arm = new Arm();

  private final CommandXboxController m_control =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  public RobotContainer() {
    configureBindings();
  }
  
  private void configureBindings() {
    // Control manual de encendido y apagado
    m_control.b().onTrue(m_Intake.setMotorInCommand());
    m_control.b().onFalse(m_Intake.stopMotorCommand());

    // Solo activa el comando y este se detiene solo
    m_control.a().onTrue(m_Intake.intakePieceCommand());

    m_control.povUp().onTrue(m_Arm.setSetpointCommand(ArmPosition.HIGH));
    m_control.povDown().onTrue(m_Arm.setSetpointCommand(ArmPosition.HORIZONTAL));
    m_control.povLeft().onTrue(m_Arm.setSetpointCommand(ArmPosition.MIDDLE));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
