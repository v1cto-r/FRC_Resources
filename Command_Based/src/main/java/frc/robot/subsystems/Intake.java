// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.Constants.SubsystemConstants.IntakeConstants;

public class Intake extends SubsystemBase {
  // Declarar el motor y el sensor
  private final SparkMax m_MotorIntake;
  private final SparkMaxConfig m_ConfiguracionMotorIntake;
  private final DigitalInput m_DetectorPieza;
  private boolean tienePieza;

  public Intake() {
    this.m_MotorIntake = new SparkMax(IntakeConstants.kIntakeControllerID, MotorType.kBrushless);
    this.m_ConfiguracionMotorIntake = new SparkMaxConfig();
    this.m_DetectorPieza = new DigitalInput(IntakeConstants.kIntakeLimitSwitchID);

    this.m_ConfiguracionMotorIntake
    .smartCurrentLimit(40)
    .inverted(false) // Podrian agregar esta configuracion
    .idleMode(IdleMode.kBrake);

    this.m_MotorIntake.configure(m_ConfiguracionMotorIntake, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public boolean getTienePieza() {
    return this.tienePieza;
  }

  public void setIntakeVoltage(int voltage) {
    this.m_MotorIntake.setVoltage(voltage);
  }

  public void setMotorIn() {
    setIntakeVoltage(8);
  }

  public void setMotorOut() {
    setIntakeVoltage(-6);
  }

  public void stopMotor() {
    setIntakeVoltage(0);
  }

  public Command setMotorInCommand() {
    return runOnce(this::setMotorIn);
  }

  public Command setMotorOutCommand() {
    return runOnce(this::setMotorOut);
  }

  public Command stopMotorCommand() {
    return runOnce(this::stopMotor);
  }

  public Command guardarPieza() {
    return new RunCommand(this::setMotorIn).until(this::getTienePieza).andThen(this::stopMotor);
  }

  // Funciones

  @Override
  public void periodic() {
    this.tienePieza = m_DetectorPieza.get();
  }
}
