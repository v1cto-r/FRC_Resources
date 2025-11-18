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


  private final SparkMax m_ArmMotor1;
  private final SparkMax m_ArmMotor2;
  private final SparkMax m_ArmMotor3;

  private final SparkMaxConfig m_LeaderConfig;
  private final SparkMaxConfig m_Follower1Config;
  private final SparkMaxConfig m_Follower2Config;

  private final CANcoder m_encoder;
  private Angle setPoint;
  private boolean pidEnabled = false;


  public Arm() {
    this.m_ArmMotor1 = new SparkMax(ArmConstants.kArmControllerID1, MotorType.kBrushless);
    this.m_ArmMotor2 = new SparkMax(ArmConstants.kArmControllerID2, MotorType.kBrushless);
    this.m_ArmMotor3 = new SparkMax(ArmConstants.kArmControllerID3, MotorType.kBrushless);

    this.m_LeaderConfig = new SparkMaxConfig();
    this.m_Follower1Config = new SparkMaxConfig();
    this.m_Follower2Config = new SparkMaxConfig();

    this.m_LeaderConfig
    .smartCurrentLimit(40)
    .idleMode(IdleMode.kBrake)
    .inverted(false);

    this.m_Follower1Config
    .smartCurrentLimit(40)
    .idleMode(IdleMode.kBrake)
    .inverted(true)
    .follow(m_ArmMotor1);

    this.m_Follower2Config
    .smartCurrentLimit(40)
    .idleMode(IdleMode.kBrake)
    .inverted(false)
    .follow(m_ArmMotor1);

    m_ArmMotor1.configure(m_LeaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    m_ArmMotor2.configure(m_Follower1Config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    m_ArmMotor3.configure(m_Follower2Config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    this.m_encoder = new CANcoder(ArmConstants.kEncoderID, SubsystemConstants.kCanivoreBus);

    this.setPoint = ArmPosition.HIGH.getPosition();
  }

  public Angle getAngle() {
    return Rotations.of(m_encoder.getAbsolutePosition().getValueAsDouble());
  }

  public boolean atSetpoint() {
    return ArmConstants.kArmPIDController.atSetpoint();
  }

  private void setSetpoint(ArmPosition angle) {
    this.setPoint = Radians.of(
      MathUtil.clamp(
        angle.getPosition().in(Radians), 
        ArmConstants.kArmLowerLimit.in(Radians), 
        ArmConstants.kArmUpperLimit.in(Radians)
      )
    );
  }

  private void setSetpoint(Angle angle) {
    this.setPoint = Radians.of(
      MathUtil.clamp(
        angle.in(Radians), 
        ArmConstants.kArmLowerLimit.in(Radians), 
        ArmConstants.kArmUpperLimit.in(Radians)
      )
    );
  }

  public Command setSetpointCommand(Angle angle) {
    return runOnce(() -> setSetpoint(angle)).until(this::atSetpoint);
  }

  public Command setSetpointCommand(ArmPosition angle) {
    return runOnce(() -> setSetpoint(angle)).until(this::atSetpoint);
  }

  @Override
  public void periodic() {
    double setPointRadians = this.setPoint.in(Radians);
    double currentAngleRadians = this.getAngle().in(Radians);

    double resultVoltage = ArmConstants.kArmPIDController.calculate(setPointRadians, currentAngleRadians);

    if (pidEnabled) m_ArmMotor1.setVoltage(resultVoltage);
    else m_ArmMotor1.setVoltage(0);

    SmartDashboard.putBoolean("Arm/enSetpoint", this.atSetpoint());
    SmartDashboard.putNumber("Arm/setpointGrados", this.setPoint.in(Degrees));
    SmartDashboard.putNumber("Arm/anguloActualGrados", this.getAngle().in(Degrees));
    SmartDashboard.putNumber("Arm/voltajeSalida", resultVoltage);

  }
}
