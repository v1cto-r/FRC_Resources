// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private final SparkMax m_MotorSpark = new SparkMax(0, MotorType.kBrushed); // Brushed para CIM/Redline/Motor de ventana
  // private final SparkMax m_MotorSpark = new SparkMax(0, MotorType.kBrushless); // Brushless para Neos

  private final SparkMaxConfig m_MotorSparkConfig = new SparkMaxConfig(); // Inicializar la configuración

  private final DigitalInput m_LimitSwitch = new DigitalInput(1); // Inicializar el limit switch conectado al puerto 1

  /**
   * Esta función se va a ejecutar cuando se inicia el robot, aqui se inicializan motores, sensores o cualquier componente
   * Código de inicialización.
   */
  public Robot() {
    m_MotorSparkConfig
    .inverted(false) // Se configura si se invierte el motor
    .idleMode(IdleMode.kBrake) // Su modo de reposo, Break hace que el motor esté duro y se oponga al movimiento
                               // Idle hace que el eje gire libremente
    .smartCurrentLimit(40); // Importante siempre agregar un limite de corriente, porque puede causar problemas
                                       // Tanto de acabarse la batería como terminar dañando un mecanismo
  }

  /** Esta funcion se ejecuta siempre, constantemente. */
  @Override
  public void robotPeriodic() {}

  /** Esta funcion se ejecuta cuando se inicia autonomo. */
  @Override
  public void autonomousInit() {
    System.out.println("Iniciando autónomo");
  }

  /** Esta funcion se ejecuta constantemente durante autonomo. */
  @Override
  public void autonomousPeriodic() {
    // Todo este código se ejecuta durante el autónomo
  }

  /** Esta funcion se ejecuta cuando se inicia teleoperado. */
  @Override
  public void teleopInit() {}

  /**
   * Esta función se va a ejecutar constantemente mientras el robot esté en teleoperado.
   * 
  */
  @Override
  public void teleopPeriodic() {
    if (m_LimitSwitch.get()) {
      m_MotorSpark.setVoltage(0);
    } else {
      m_MotorSpark.setVoltage(5.0);
    }
  }

  /** Esta funcion se ejecuta cuando se desabilita el robot. */
  @Override
  public void disabledInit() {}

  /** Esta funcion se ejecuta cuando se desabilita el robot, constantemente. */
  @Override
  public void disabledPeriodic() {}

  /** Esta funcion se ejecuta cuando se inicia test. */
  @Override
  public void testInit() {}

  /** Esta funcion se ejecuta constantemente durante test. */
  @Override
  public void testPeriodic() {}

  /** Esta funcion se ejecuta cuando se inicia simulación. */
  @Override
  public void simulationInit() {}

  /** Esta funcion se ejecuta constantemente durante simulación. */
  @Override
  public void simulationPeriodic() {}
}
