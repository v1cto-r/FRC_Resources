// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.Degrees;

import com.ctre.phoenix6.CANBus;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.units.measure.Angle;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class SubsystemConstants {
    public static final CANBus kRioBus = new CANBus("rio");
    public static final CANBus kCanivoreBus = new CANBus("canivore");


    public static class IntakeConstants {
      public static final int kIntakeControllerID = 2;
      public static final int kIntakeLimitSwitchID = 0;
    }

    public static class ArmConstants {
      public static final int kArmControllerID1 = 3;
      public static final int kArmControllerID2 = 4;
      public static final int kArmControllerID3 = 5;

      public static final int kEncoderID = 0;

      public static final TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(0,0);
      public static final ProfiledPIDController kArmPIDController = new ProfiledPIDController(
        0,0,0, constraints
      );
    }
  }

  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
}
