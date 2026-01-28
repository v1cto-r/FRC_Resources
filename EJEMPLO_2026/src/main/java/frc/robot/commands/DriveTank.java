package frc.robot.commands;

import java.util.function.Supplier;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import frc.robot.subsystems.Swerve.Constants.SwerveDriveConstants;
import frc.robot.subsystems.Tank.Tank;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.Command;

public class DriveTank extends Command {
  //* The swerve drive subsystem
  private final Tank tankDrive;

  //* The suppliers for the joystick values
  private final Supplier<Double> xSpeed;
  private final Supplier<Double> rotSpeed;
  
  public double modifyAxis(double input, double scaleFactor, SlewRateLimiter slew) {
    // Get sign
    double sign = Math.signum(input);

    // Use absolute value for calculations (needed so slew rate limiter acts correctly against negative acceleration)
    input = Math.abs(input);

    // Scale input
    input *= scaleFactor;

    // Apply rate limit
    slew.reset(input);
    input = slew.calculate(input);

    // Reapply the sign
    input *= sign;

    // Return the result
    return input;
  }

  public DriveTank(Tank tankDrive, Supplier<Double> x, Supplier<Double> rot) {
    this.tankDrive = tankDrive;
    this.xSpeed = x;
    this.rotSpeed = rot;
    addRequirements(tankDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Get the joystick values
    double x = xSpeed.get();
    double rot = rotSpeed.get();
    
    rot = Math.abs(rot) < SwerveDriveConstants.kJoystickDeadband ? 0 : rot;

    // Modify the axis
    x = modifyAxis(x, SwerveDriveConstants.PhysicalModel.kMaxSpeed.in(MetersPerSecond), SwerveDriveConstants.PhysicalModel.xLimiter);
    rot = modifyAxis(rot, SwerveDriveConstants.PhysicalModel.kMaxAngularSpeed.in(RadiansPerSecond), SwerveDriveConstants.PhysicalModel.rotLimiter);
    
    // Drive the swerve drive
    tankDrive.driveArcade(x, rot);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    tankDrive.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
