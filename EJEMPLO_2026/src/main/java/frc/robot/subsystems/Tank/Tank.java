// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Tank;

import org.littletonrobotics.junction.Logger;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.SpeedAlterator.SpeedAlterator;
import frc.robot.subsystems.Tank.Constants.TankConstants;;

public class Tank extends SubsystemBase {

  private final SparkMax leftMotorLead;
  private final SparkMax rightMotorLead;
  private final SparkMax leftMotorFollow;
  private final SparkMax rightMotorFollow;

  private final SparkMaxConfig leftMotorLeadConfig = new SparkMaxConfig();
  private final SparkMaxConfig rightMotorLeadConfig = new SparkMaxConfig();
  private final SparkMaxConfig leftMotorFollowConfig = new SparkMaxConfig();
  private final SparkMaxConfig rightMotorFollowConfig = new SparkMaxConfig();

  private final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
  
  private ChassisSpeeds speeds = new ChassisSpeeds();
  private SpeedAlterator speedAlterator = null;

  /** Creates a new Tank. */
  public Tank() {
    this.leftMotorLead = new SparkMax(TankConstants.LEFT_LEAD_ID, SparkMax.MotorType.kBrushless);
    this.rightMotorLead = new SparkMax(TankConstants.RIGHT_LEAD_ID, SparkMax.MotorType.kBrushless);
    this.leftMotorFollow = new SparkMax(TankConstants.LEFT_FOLLOW_ID, SparkMax.MotorType.kBrushless);
    this.rightMotorFollow = new SparkMax(TankConstants.RIGHT_FOLLOW_ID, SparkMax.MotorType.kBrushless);

    this.leftMotorLeadConfig
    .smartCurrentLimit(40)
    .inverted(false)
    .idleMode(IdleMode.kBrake);

    this.rightMotorLeadConfig
    .smartCurrentLimit(40)
    .inverted(true)
    .idleMode(IdleMode.kBrake);

    this.leftMotorFollowConfig
    .smartCurrentLimit(40)
    .inverted(false)
    .idleMode(IdleMode.kBrake)
    .follow(this.leftMotorLead);

    this.rightMotorFollowConfig
    .smartCurrentLimit(40)
    .inverted(true)
    .idleMode(IdleMode.kBrake)
    .follow(this.rightMotorLead);

  }

  public Rotation2d getHeading() {
    return Rotation2d.fromDegrees(gyro.getAngle() % 360);
  }

  public void zeroHeading() {
      this.gyro.reset();
  }

  public Command zeroHeadingCommand() {
    return runOnce(this::zeroHeading).ignoringDisable(true);
  }

  public void drive(ChassisSpeeds speeds) {
    Logger.recordOutput("TankDrive/SpeedsUnaltered", speeds);
    if (speedAlterator != null) {
        this.speeds = speedAlterator.alterSpeed(speeds, true);
    } else {
        this.speeds = speeds;
    }

    DifferentialDriveWheelSpeeds wheelSpeeds = TankConstants.kinematics.toWheelSpeeds(this.speeds);

    leftMotorLead.setVoltage(wheelSpeeds.leftMetersPerSecond / TankConstants.kMaxSpeedsMetersPerSecond * 12);
    rightMotorLead.setVoltage(wheelSpeeds.rightMetersPerSecond / TankConstants.kMaxSpeedsMetersPerSecond * 12);
  }

  public void driveArcade(double forward, double rotation) {
    ChassisSpeeds Speeds = new ChassisSpeeds(forward, 0.0, rotation);
    this.drive(Speeds);
  }

  public void enableSpeedAlterator(SpeedAlterator alterator) {
    if (this.speedAlterator != alterator) alterator.onEnable();
    if (this.speedAlterator != null) this.speedAlterator.onDisable();
    this.speedAlterator = alterator;
}

public Command enableSpeedAlteratorCommand(SpeedAlterator alterator) {
    return runOnce(() -> this.enableSpeedAlterator(alterator));
}

public void disableSpeedAlterator() {
    if(this.speedAlterator != null) this.speedAlterator.onDisable();
    this.speedAlterator = null;
}

public Command disableSpeedAlteratorCommand() {
    return runOnce(() -> this.disableSpeedAlterator());
}

  public void stop() {
    this.leftMotorLead.stopMotor();
    this.rightMotorLead.stopMotor();
    this.leftMotorFollow.stopMotor();
    this.rightMotorFollow.stopMotor();
  }

  public double getLeftDistance() {
    return this.leftMotorLead.getEncoder().getPosition() * TankConstants.kWheelCircumferenceMeters / TankConstants.kEncoderCPR;
  }

  public double getRightDistance() {
    return this.rightMotorLead.getEncoder().getPosition() * TankConstants.kWheelCircumferenceMeters / TankConstants.kEncoderCPR;
  }
  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
