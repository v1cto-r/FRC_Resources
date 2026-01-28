package frc.robot.util.SpeedAlterator;

import java.util.function.Supplier;

import frc.robot.subsystems.Swerve.Constants.SwerveDriveConstants;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class LookAtHub extends SpeedAlterator {
    private final Supplier<Pose2d> poseSupplier;
    private Alliance alliance = Alliance.Blue;

    public static final Pose2d BlueSideHub = new Pose2d(4.625, 4.02, new Rotation2d());
    public static final Pose2d RedSideHub = new Pose2d(11.9, 4.02, new Rotation2d());


    public LookAtHub(Supplier<Pose2d> poseSupplier) {
        this.poseSupplier = poseSupplier;
        
        SmartDashboard.putBoolean("Alterators/Look", false);
    }
    
    @Override
    public void onEnable() {
        Pose2d pose = poseSupplier.get();
        SwerveDriveConstants.PoseControllers.rotationPID.reset(pose.getRotation().getRotations());
        this.alliance = DriverStation.getAlliance().orElse(Alliance.Blue);
        SmartDashboard.putBoolean("Alterators/Look", true);
    }

    @Override
    public void onDisable() {
        SmartDashboard.putBoolean("Alterators/Look", false);
    }

    public ChassisSpeeds alterSpeed(ChassisSpeeds speeds, boolean robotRelative) {
        Pose2d pose = poseSupplier.get();
    
        // Get target hub based on alliance
        Pose2d targetHub = (alliance == Alliance.Blue) ? BlueSideHub : RedSideHub;
        
        // Calculate angle to target
        double deltaX = targetHub.getX() - pose.getX();
        double deltaY = targetHub.getY() - pose.getY();
        double targetAngleRadians = Math.atan2(deltaY, deltaX);
        double targetRotation = targetAngleRadians / (2 * Math.PI); // Convert to rotations
        
        // Use PID to calculate omega
        speeds.omegaRadiansPerSecond = SwerveDriveConstants.PoseControllers.rotationPID.calculate(
            pose.getRotation().getRotations(), 
            targetRotation
        );
                
        return speeds;
    }
}