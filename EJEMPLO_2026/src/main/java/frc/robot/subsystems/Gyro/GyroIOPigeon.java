package frc.robot.subsystems.Gyro;

import static edu.wpi.first.units.Units.Rotations;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.geometry.Rotation2d;

public class GyroIOPigeon implements GyroIO {
    public Pigeon2 gyro;

    public GyroIOPigeon(int deviceID) {
        gyro = new Pigeon2(deviceID, "*");
    }

    public double getPitch() {
        return gyro.getPitch().refresh().getValueAsDouble();
    }

    public double getYaw() {
        return -gyro.getYaw().refresh().getValue().in(Rotations);
    }

    public double getRoll() {
        return gyro.getRoll().refresh().getValueAsDouble();
    }

    public double getPitchVelocity() {
        return gyro.getAngularVelocityXWorld().refresh().getValueAsDouble();
    }

    public double getYawVelocity() {
        return gyro.getAngularVelocityZWorld().refresh().getValueAsDouble();
    }

    public double getRollVelocity() {
        return gyro.getAngularVelocityZWorld().refresh().getValueAsDouble();
    }

    public double getAccelerationX() {
        return gyro.getAccelerationX().refresh().getValueAsDouble();
    }

    public double getAccelerationY() {
        return gyro.getAccelerationY().refresh().getValueAsDouble();
    }

    public double getAccelerationZ() {
        return gyro.getAccelerationZ().refresh().getValueAsDouble();
    }

    public Rotation2d getHeading() {
        return gyro.getRotation2d();
    }
    
    public void reset() {
        gyro.reset();
    }

    public double getVelocityX() {
        return 0;
    }

    public double getVelocityY() {
        return 0;
    }

    public double getVelocityZ() {
        return 0;
    }
}
