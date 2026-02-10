// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Auto;

import choreo.auto.AutoFactory;
import choreo.trajectory.SwerveSample;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Swerve.SwerveDrive;
import frc.robot.util.Odometry.Odometry;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class Auto extends SubsystemBase {
  /** Creates a new Auto. */

  SwerveDrive m_SwerveDrive;
  Odometry m_Odometry;

  private final PIDController xController = new PIDController(10.0, 0.0, 0.0); // MODIFICAR AQUI PARA LLEGAR A OBJETIVO
  private final PIDController yController = new PIDController(10.0, 0.0, 0.0); // MODIFICAR AQUI PARA LLEGAR A OBJETIVO
  private final PIDController headingController = new PIDController(7.5, 0.0, 0.0); // MODIFICAR AQUI PARA LLEGAR A OBJETIVO

  public Auto(SwerveDrive swerve, Odometry odometry) {
    this.m_SwerveDrive = swerve;
    this.m_Odometry = odometry;

    headingController.enableContinuousInput(-Math.PI, Math.PI);
  }

  public void followTrajectory(SwerveSample sample) {
    // Get the current pose of the robot
    //Pose2d pose = getPose();
    Pose2d pose = m_Odometry.getEstimatedPosition();

    // Generate the next speeds for the robot
    ChassisSpeeds speeds = new ChassisSpeeds(
        sample.vx + xController.calculate(pose.getX(), sample.x),
        sample.vy + yController.calculate(pose.getY(), sample.y),
        sample.omega + headingController.calculate(pose.getRotation().getRadians(), sample.heading)
    );

    // Apply the generated speeds
    this.m_SwerveDrive.driveRobotRelative(speeds);
  }

  public AutoFactory getAutoFactory() {
    AutoFactory autoFactory = new AutoFactory(
            m_Odometry::getEstimatedPosition, // A function that returns the current robot pose
            m_Odometry::resetPosition, // A function that resets the current robot pose to the provided Pose2d
            this::followTrajectory, // The drive subsystem trajectory follower 
            true, // If alliance flipping should be enabled 
            m_SwerveDrive // The drive subsystem
        );

    return autoFactory;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
