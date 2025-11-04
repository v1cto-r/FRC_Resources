// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.DutyCycleOut;

/**
 * Este es un ejemplo de control básico de código para motores en FRC
 */
public class Robot extends TimedRobot {
  /// Ejemplo de Kraken
  private final CANBus Canivore = new CANBus("rio"); // WHATEVER NAME

  private final TalonFX m_LeftMotor1 = new TalonFX(0, Canivore);
  private final TalonFX m_LeftMotor2 = new TalonFX(1, Canivore);

  private final TalonFX m_RightMotor1 = new TalonFX(2, Canivore);
  private final TalonFX m_RightMotor2 = new TalonFX(3, Canivore);

  private final DutyCycleOut m_LeftRequest = new DutyCycleOut(0);
  private final DutyCycleOut m_RightRequest = new DutyCycleOut(0);

  /// Ambos
  private final Joystick m_stick = new Joystick(0);

  /** Se ejecuta al inicio del programa. */
  public Robot() {
    /// Ejemplo de Kraken
    m_LeftMotor2.setControl(new Follower(m_LeftMotor1.getDeviceID(), false));
    m_RightMotor2.setControl(new Follower(m_RightMotor1.getDeviceID(), false));
  }

  // Se ejecuta constantemente mientras el robot se encuentra en teleoperado
  @Override
  public void teleopPeriodic() {
    /// Ejemplo de Kraken
    var enfrente = m_stick.getY();
    var rotacion = m_stick.getX();

    var leftOut = enfrente + rotacion;
    var rightOut = enfrente - rotacion;

    m_LeftMotor1.setControl(m_LeftRequest.withOutput(leftOut));
    m_RightMotor1.setControl(m_RightRequest.withOutput(rightOut));

  }
}