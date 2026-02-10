package frc.robot.subsystems.Swerve;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.units.measure.AngularAcceleration;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearAcceleration;
import edu.wpi.first.units.measure.LinearVelocity;
import static edu.wpi.first.units.Units.*;

import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.path.PathConstraints;
import frc.robot.util.PIDFConstants;

public class Constants {
    public static Translation2d[] sizeToModulePositions(double trackWidth, double wheelBase) {
        return new Translation2d[] {
            // Front left
            new Translation2d(wheelBase / 2, trackWidth / 2),
            // Front right
            new Translation2d(wheelBase / 2, -trackWidth / 2),
            // Back left
            new Translation2d(-wheelBase / 2, trackWidth / 2),
            // Back right
            new Translation2d(-wheelBase / 2, -trackWidth / 2)
        };
    }

    public static final class SwerveDriveConstants {
        public static final class PoseControllers {
            public static final ProfiledPIDController rotationPID = new ProfiledPIDController(4, 0, 0, new TrapezoidProfile.Constraints(100, 80));
            public static final ProfiledPIDController translationXPID = new ProfiledPIDController(0.25, 0, 0, new TrapezoidProfile.Constraints(2.0, 0.25));
            public static final ProfiledPIDController translationYPID = new ProfiledPIDController(0.25, 0, 0, new TrapezoidProfile.Constraints(2.0, 0.25));

            public static final Distance kOffsetBoxHeight = Meters.of(0.25);
            public static final Distance kOffsetBoxWidth = Meters.of(0.25);

            public static final double epsilon = 0.05;
            public static final double rotEpsilon = 1.;
        }

        //* Gyroscope (Pigeon 2.0)
        public static final int kGyroDeviceId = 34;

        public static final double kJoystickDeadband = 0.09;
        //* Physical model of the robot
        public static final class PhysicalModel {
            //* MAX DISPLACEMENT SPEED (and acceleration)
            public static final LinearVelocity kMaxSpeed = MetersPerSecond.of(4.0);
            public static final LinearAcceleration kMaxAcceleration = MetersPerSecond.per(Second).of(5.0);
            public static final LinearAcceleration kMaxDeceleration = MetersPerSecond.per(Second).of(-5.0);

            //* MAX ROTATIONAL SPEED (and acceleration)
            public static final AngularVelocity kMaxAngularSpeed = DegreesPerSecond.of(270.0);
            public static final AngularAcceleration kMaxAngularAcceleration = DegreesPerSecond.per(Second).of(360.0);
            public static final AngularAcceleration kMaxAngularDeceleration = DegreesPerSecond.per(Second).of(-360.0);

            //* Slew rate limiters
            public static final SlewRateLimiter yLimiter = new SlewRateLimiter(Constants.SwerveDriveConstants.PhysicalModel.kMaxAcceleration.in(MetersPerSecondPerSecond), Constants.SwerveDriveConstants.PhysicalModel.kMaxDeceleration.in(MetersPerSecondPerSecond), 0);
            public static final SlewRateLimiter xLimiter = new SlewRateLimiter(Constants.SwerveDriveConstants.PhysicalModel.kMaxAcceleration.in(MetersPerSecondPerSecond), Constants.SwerveDriveConstants.PhysicalModel.kMaxDeceleration.in(MetersPerSecondPerSecond), 0);
            public static final SlewRateLimiter rotLimiter = new SlewRateLimiter(Constants.SwerveDriveConstants.PhysicalModel.kMaxAngularAcceleration.in(RadiansPerSecond.per(Second)), Constants.SwerveDriveConstants.PhysicalModel.kMaxAngularDeceleration.in(RadiansPerSecond.per(Second)), 0);

            // Drive wheel diameter
            public static final Distance kWheelDiameter = Inches.of(4);

            // Gear ratios
            public static final double kDriveMotorGearRatio = 1.0 / 6.75; // 6.12:1 Drive
            public static final double kTurningMotorGearRatio = 1.0 / (150/7); // 12.8:1 Steering

            // Conversion factors (Drive Motor)
            public static final double kDriveEncoder_RotationToMeter = kDriveMotorGearRatio * kWheelDiameter.in(Meters);
            public static final double kDriveEncoder_RPMToMeterPerSecond = kDriveEncoder_RotationToMeter / 60.0;

            // Conversion factors (Turning Motor)
            public static final double kTurningEncoder_Rotation = kTurningMotorGearRatio;
            public static final double kTurningEncoder_RPS = kTurningEncoder_Rotation / 60.0;

            // Robot Without bumpers measures
            public static final Distance kTrackWidth = Inches.of(26);
            public static final Distance kWheelBase = Inches.of(26);
    
            // Create a kinematics instance with the positions of the swerve modules
            public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(sizeToModulePositions(kTrackWidth.in(Meters), kWheelBase.in(Meters)));

            // Path constraints
            public static final PathConstraints kPathConstraints = new PathConstraints(kMaxSpeed, kMaxAcceleration, kMaxAngularSpeed, kMaxAngularAcceleration);
        }
    }

    public static final class SwerveModuleConstants {
        // * Motor config
        // Ramp rates
        public static final double kDriveMotorRampRate = 0;
        public static final double kTurningMotorRampRate = 0;

        // Current limits
        public static final int kDriveMotorCurrentLimit = 40;
        public static final int kDriveMotorLowerCurrentLimit = 30;
        public static final int kTurningMotorCurrentLimit = 30;

        //* PID
        public static final PIDFConstants kTurningPIDConstants = new PIDFConstants(1.57);

        //* Swerve modules options
        public static final SwerveModuleOptions kFrontLeftOptions = new SwerveModuleOptions()
            .setDriveMotorID(11)
            .setTurningMotorID(12)
            .setAbsoluteEncoderCANDevice(21)
            .setName("Front Left");

        public static final SwerveModuleOptions kFrontRightOptions = new SwerveModuleOptions()
            .setDriveMotorID(13)
            .setTurningMotorID(14)
            .setAbsoluteEncoderCANDevice(22)
            .setName("Front Right");

        public static final SwerveModuleOptions kBackLeftOptions = new SwerveModuleOptions()
            .setDriveMotorID(15)
            .setTurningMotorID(16)
            .setAbsoluteEncoderCANDevice(23)
            .setName("Back Left");

        public static final SwerveModuleOptions kBackRightOptions = new SwerveModuleOptions()
            .setDriveMotorID(17)
            .setTurningMotorID(18)
            .setAbsoluteEncoderCANDevice(24)
            .setName("Back Right");
    }


    public static class AutoConstants {
        public static RobotConfig config = getAutoConfig();

    }

    public static RobotConfig getAutoConfig() {
        try{
            RobotConfig config = RobotConfig.fromGUISettings();
            return config;
        } catch (Exception e) {
            // Handle exception as needed
            e.printStackTrace();
            return null;
        }
    }
}