// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public class Intake extends Command {
  public static final CANSparkLowLevel.MotorType kBrushed = MotorType.kBrushed;
  public static final CANSparkLowLevel.MotorType kBrushless = MotorType.kBrushless;
  //Intake
  private final CANSparkMax intake1 = new CANSparkMax(6, kBrushed);
  private final CANSparkMax intake2 = new CANSparkMax(5, kBrushed);

  private final Timer timer;

  /** Creates a new Intake. */
  public Intake() {
    timer = new Timer();
    intake1.setSmartCurrentLimit(30, 10);
    intake2.setSmartCurrentLimit(30, 10);
    // Use addRequirements() here to declare subsystem dependencies.
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
    if (timer.get() < 3.0);{
      intake1.set(.8);
      intake2.set(.8);

    }
    if (timer.hasElapsed(3.0)) {
      intake1.set(0);
      intake2.set(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

    intake1.set(0);
    intake2.set(0);
    timer.stop();
    timer.reset();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.hasElapsed(3.0);
  }
}
