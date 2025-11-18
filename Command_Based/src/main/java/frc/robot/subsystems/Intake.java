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
  private final SparkMax m_IntakeMotor;
  private final SparkMaxConfig m_IntakeMotorConfig;
  private final DigitalInput m_pieceDetector;
  private boolean hasPiece;


  public Intake() {
    this.m_IntakeMotor = new SparkMax(IntakeConstants.kIntakeControllerID, MotorType.kBrushless);
    this.m_IntakeMotorConfig = new SparkMaxConfig();
    this.m_pieceDetector  = new DigitalInput(IntakeConstants.kIntakeLimitSwitchID);

    this.m_IntakeMotorConfig
    .smartCurrentLimit(40)
    .idleMode(IdleMode.kBrake);

    this.m_IntakeMotor.configure(this.m_IntakeMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public boolean getHasPiece() {
    return this.hasPiece;
  }

  public void setIntakeMotorVoltage(int voltage) {
    this.m_IntakeMotor.setVoltage(voltage);
  }

  public void setMotorIn() {
    this.setIntakeMotorVoltage(8);
  }

  public void setMotorOut() {
    this.setIntakeMotorVoltage(-4);
  }

  public void stopMotor() {
    this.setIntakeMotorVoltage(0);
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

  public Command intakePieceCommand() {
    return new RunCommand(this::setMotorIn, this).until(this::getHasPiece).andThen(this::stopMotor);
  }

  @Override
  public void periodic() {
    this.hasPiece = this.m_pieceDetector.get();

    SmartDashboard.putBoolean("Intake/tienePieza", hasPiece);
  }
}
