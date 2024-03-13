// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;


public class Shooter extends Command {


  private final Timer timer;
  //Shooter
  public static final CANSparkLowLevel.MotorType kBrushed = MotorType.kBrushed;
  public static final CANSparkLowLevel.MotorType kBrushless = MotorType.kBrushless;
  private final CANSparkMax shooter1 = new CANSparkMax(2, kBrushed);
  private final CANSparkMax shooter2 = new CANSparkMax(1, kBrushed);
  //Servo_Flicker
  private final Servo exampleServo1 = new Servo(1);
  /** Creates a new Shooter. */
  public Shooter() {
    timer = new Timer();

    shooter1.setSmartCurrentLimit(30, 20);
    shooter2.setSmartCurrentLimit(30, 20);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(timer.get() < 3.0){
      shooter1.set(1);
      shooter2.set(1);

    }
    if (timer.get() >= 2.0 && timer.get() <3.0) {
      exampleServo1.setPosition(-0.4);
    }
    if (timer.hasElapsed(3.0)) {
      shooter1.set(0);
      shooter2.set(0);
      exampleServo1.setPosition(1);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

    shooter1.set(0);
      shooter2.set(0);
      exampleServo1.setPosition(1);
      timer.stop();
      timer.reset();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.hasElapsed(3.0);
  }
}
