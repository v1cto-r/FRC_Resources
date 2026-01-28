package frc.robot.subsystems.Tank;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.util.Units;

public class Constants {
    public static final class TankConstants {

        public static final int LEFT_LEAD_ID = 1;
        public static final int LEFT_FOLLOW_ID = 2;
        public static final int RIGHT_LEAD_ID = 3;
        public static final int RIGHT_FOLLOW_ID = 4;

        public static final DifferentialDriveKinematics kinematics =
            new DifferentialDriveKinematics(Units.inchesToMeters(27.0));

        public static final double kMaxSpeedsMetersPerSecond = 2.0;

        public static final double kWheelCircumferenceMeters = Units.inchesToMeters(6.0) * Math.PI;
        public static final double kEncoderCPR = 42.0;
    }
}
