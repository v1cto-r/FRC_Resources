// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Rotations;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.SubsystemConstants;
import frc.robot.Constants.SubsystemConstants.ArmConstants;
import frc.robot.Constants.SubsystemConstants.IntakeConstants;

public class Arm extends SubsystemBase {

  public static enum ArmPosition {
    HORIZONTAL(Degrees.of(8)),
    MIDDLE(Degrees.of(25)),
    HIGH(Degrees.of(45));

    private Angle angle;
    private ArmPosition(Angle angle) {
      this.angle = angle;
    }

    public Angle getPosition() {
      return angle;
    }
  }

  private final SparkMax m_Motor1;
  private final SparkMax m_Motor2;
  private final SparkMax m_Motor3;

  private final SparkMaxConfig m_ConfiguracionMotor1;
  private final SparkMaxConfig m_ConfiguracionMotor2;
  private final SparkMaxConfig m_ConfiguracionMotor3;

  private final CANcoder m_encoder;
  private Angle setPoint;


  public Arm() {
    this.m_Motor1 = new SparkMax(ArmConstants.kArmControllerID1, MotorType.kBrushless);
    this.m_Motor2 = new SparkMax(ArmConstants.kArmControllerID2, MotorType.kBrushless);
    this.m_Motor3 = new SparkMax(ArmConstants.kArmControllerID3, MotorType.kBrushless);

    this.m_ConfiguracionMotor1 = new SparkMaxConfig();
    this.m_ConfiguracionMotor2 = new SparkMaxConfig();
    this.m_ConfiguracionMotor3 = new SparkMaxConfig();

    this.m_ConfiguracionMotor1
    .smartCurrentLimit(40)
    .inverted(false) // Podrian agregar esta configuracion
    .idleMode(IdleMode.kBrake);

    this.m_ConfiguracionMotor2
    .smartCurrentLimit(40)
    .inverted(false) // Podrian agregar esta configuracion
    .idleMode(IdleMode.kBrake)
    .follow(m_Motor1);

    this.m_ConfiguracionMotor3
    .smartCurrentLimit(40)
    .inverted(false) // Podrian agregar esta configuracion
    .idleMode(IdleMode.kBrake)
    .follow(m_Motor1);

    this.m_Motor1.configure(m_ConfiguracionMotor1, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    this.m_Motor2.configure(m_ConfiguracionMotor2, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    this.m_Motor3.configure(m_ConfiguracionMotor3, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    this.m_encoder = new CANcoder(ArmConstants.kEncoderID, SubsystemConstants.kRioBus);

    this.setPoint = ArmPosition.HIGH.getPosition();
  }

  public Angle getAngle() {
    return Rotations.of(this.m_encoder.getAbsolutePosition().getValueAsDouble());
  }

  public boolean enPosicion() {
    return ArmConstants.kArmPIDController.atSetpoint();
  }

  public void setSetpoint (Angle angulo) {
    this.setPoint = angulo;
  }

  public Command setSetpointCommand(Angle angulo) {
    return runOnce(() -> setSetpoint(angulo)).until(this::enPosicion);
  }


  @Override
  public void periodic() {
    double setPointGrados = this.setPoint.in(Degrees);
    double anguloActualGrados = this.getAngle().in(Degrees);

    double voltajeResultante = ArmConstants.kArmPIDController.calculate(anguloActualGrados, setPointGrados);
    
    m_Motor1.setVoltage(voltajeResultante);
  }
}
