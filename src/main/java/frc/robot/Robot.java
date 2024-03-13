// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.






// Code for arcade divre
// Code for April Tags
// Code for the Punematics
// Code for Autonoumus






package frc.robot;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// added imports below
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Commands.Intake;
import frc.robot.Commands.Shooter;
import edu.wpi.first.wpilibj.XboxController;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {

  

  private static final String kDefaultAuto = "Default"; //goes across line
  public static final CANSparkLowLevel.MotorType kBrushed = MotorType.kBrushed;
  public static final CANSparkLowLevel.MotorType kBrushless = MotorType.kBrushless;
  private static final String kspeakermiddle = "speakermiddle";
  private static final String kRedLongAuto = "Red Long Speaker";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();



  //Drivetrain
  //Left Drive Motor
  private final CANSparkMax leftDriverMotor1 = new CANSparkMax(8, kBrushless);
  private final CANSparkMax leftDriveMotor2 = new CANSparkMax(7, kBrushless);

  //Right Drive Motor
  private final CANSparkMax rightDriveMotor1 = new CANSparkMax(4, kBrushless);
  private final CANSparkMax rightDriveMotor2 = new CANSparkMax(3, kBrushless);

 
  




  private final Timer timer1 = new Timer();

  private final XboxController stick = new XboxController(0);

  private final DoubleSolenoid Armsolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 4, 5);

  private Shooter shooterCommand;
  private Intake intakeCommand;
  double driveLimit = 1;
  //private final XboxController operatorController = new XboxController(1);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {


    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("speakermiddle", kspeakermiddle);
    m_chooser.addOption("Red Long Speaker", kRedLongAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    leftDriverMotor1.setInverted(true);
    rightDriveMotor1.setInverted(false);
    
    
    timer1.start();

    shooterCommand = new Shooter();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    timer1.reset();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    if(stick.getRightStickButton() == true){
      driveLimit = 1;
    }else if(stick.getLeftStickButton()){
      driveLimit = .5;
    }
    //Shooter Command
    if(stick.getYButtonPressed()) {

      CommandScheduler.getInstance().schedule(shooterCommand);
    }
    // Intake Command
    if(stick.getRightBumperPressed()){

      CommandScheduler.getInstance().schedule(intakeCommand);
    }


    /*tank drive controls */
    //left side controls
    leftDriverMotor1.set(stick.getLeftY()*driveLimit); // change the control command to getRawAxis
    leftDriveMotor2.follow(leftDriverMotor1);
    //right side controls
    rightDriveMotor1.set(stick.getRightY()*driveLimit); // change the control command to getRawAxis
    rightDriveMotor2.follow(rightDriveMotor1);

  
  if(stick.getAButtonPressed()){

    Armsolenoid.set(Value.kForward);

}   else if (stick.getAButtonReleased()){

    Armsolenoid.set(Value.kReverse);
   
} else {
    Armsolenoid.set(Value.kOff);

  
   }
  } 
 

  

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}