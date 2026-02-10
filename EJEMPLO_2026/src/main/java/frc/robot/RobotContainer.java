package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.DriveSwerve;
import frc.robot.subsystems.Auto.Auto;
import frc.robot.subsystems.Gyro.Gyro;
import frc.robot.subsystems.Gyro.GyroIOPigeon;
import frc.robot.subsystems.Swerve.SwerveDrive;
import frc.robot.subsystems.Swerve.SwerveModule;
import frc.robot.subsystems.Swerve.Constants.SwerveDriveConstants;
import frc.robot.subsystems.Swerve.Constants.SwerveModuleConstants;
import frc.robot.util.Odometry.Odometry;
import frc.robot.util.Odometry.Camera.LimelightOdometryCamera;
import frc.robot.util.Odometry.Camera.VisionOdometryFilters;
import frc.robot.util.SpeedAlterator.LookAtHub;
import frc.robot.util.SpeedAlterator.SpeedAlterator;
import choreo.auto.AutoChooser;
import choreo.auto.AutoFactory;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {

  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  private final SwerveDrive m_swerveDrive;

  private final SpeedAlterator m_speedAlterator_LookAtHub;

  private final LimelightOdometryCamera m_limelight2;
  private final Odometry m_odometry;
  private final Auto m_auto;
  private final AutoFactory autoFactory;
  private final AutoChooser autoChooser = new AutoChooser();
  private final AutoRoutines autoRoutines;


  public RobotContainer() {
    this.m_swerveDrive = new SwerveDrive(
        new SwerveModule(SwerveModuleConstants.kFrontLeftOptions),
        new SwerveModule(SwerveModuleConstants.kFrontRightOptions),
        new SwerveModule(SwerveModuleConstants.kBackLeftOptions),
        new SwerveModule(SwerveModuleConstants.kBackRightOptions),
        new Gyro(new GyroIOPigeon(SwerveDriveConstants.kGyroDeviceId))
    );

    this.m_limelight2 = new LimelightOdometryCamera(Constants.Vision.Limelight2.kName, true, true, VisionOdometryFilters::visionFilter);


    this.m_odometry = new Odometry(
      SwerveDriveConstants.PhysicalModel.kDriveKinematics,
      m_swerveDrive::getHeading,
      m_swerveDrive::getModulePositions,
      new Pose2d(),
      0.02,
      m_limelight2
    );
    
    this.m_limelight2.enable();
    this.m_odometry.startVision();

    this.m_speedAlterator_LookAtHub = new LookAtHub(m_odometry::getEstimatedPosition);

    this.m_auto = new Auto(m_swerveDrive, m_odometry);

    autoFactory = m_auto.getAutoFactory();
    autoRoutines = new AutoRoutines(autoFactory);

    autoChooser.addRoutine("Inicio Izquierda - Anotar Centro - Agarrar Pelotas - Anotar Centro", autoRoutines::InicioIzquierdaAnotarAgarrarAnotar);
    SmartDashboard.putData("Autonomo", autoChooser);

    configureBindings();
  }

  
  private void configureBindings() {
    this.m_swerveDrive.setDefaultCommand(new DriveSwerve(
        m_swerveDrive,
        () -> -m_driverController.getLeftY(),
        () -> -m_driverController.getLeftX(),
        () -> m_driverController.getLeftTriggerAxis() - m_driverController.getRightTriggerAxis(),
        () -> !m_driverController.a().getAsBoolean()
      )
    );

    this.m_driverController.rightStick().onTrue(this.m_swerveDrive.zeroHeadingCommand());

    this.m_driverController.b().whileTrue(m_swerveDrive.enableSpeedAlteratorCommand(m_speedAlterator_LookAtHub));
    this.m_driverController.b().onFalse(m_swerveDrive.disableSpeedAlteratorCommand());

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.selectedCommand();
  }
}
