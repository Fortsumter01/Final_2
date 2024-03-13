// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;


import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagDetector;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.UsbCameraInfo;
import edu.wpi.first.cscore.VideoMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.ArrayList;
import java.util.List;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

public class ImageDetect extends SubsystemBase {
    private static final int CAMERA_WIDTH = 320;
    private static final int CAMERA_HEIGHT = 240;
    private static final int CAMERA_FPS = 30;
    private static final int CAMERA_EXPOSURE = 50;
    private static final List<Integer> TARGET_TAG_IDS = List.of(3, 4, 7, 8); // Replace with the IDs of your target AprilTags
    private static final double MOTOR_ACTIVATION_DELAY = 2.0; // Delay to keep the motor active after tag detection (in seconds)

    private final UsbCamera camera;
    private final AprilTagDetector detector;
    private final Timer timer;

    public static final CANSparkLowLevel.MotorType kBrushed = MotorType.kBrushed;
    public static final CANSparkLowLevel.MotorType kBrushless = MotorType.kBrushless;
    private final CANSparkMax shooter1 = new CANSparkMax(2, kBrushed);
    private final CANSparkMax shooter2 = new CANSparkMax(1, kBrushed);

    public ImageDetect() {
        VideoMode videoMode = new VideoMode(null, CAMERA_WIDTH, CAMERA_HEIGHT, CAMERA_FPS);
        camera = new UsbCamera("Camera", 0);
        camera.setVideoMode(videoMode);
        camera.setExposureManual(CAMERA_EXPOSURE);

        detector = new AprilTagDetector();

       

        timer = new Timer();
    }

    @Override
    public void periodic() {
        // Continuously process vision
        processVision();
    }

    private void processVision() {
        // Capture an image from the camera
        Image image = Image.fromMat(camera.grabFrame());

        // Detect AprilTags in the image
        List<AprilTag> results = detector.detect(image);

        // Check if any of the detected AprilTags match the target IDs
        boolean tagDetected = false;
        for (AprilTag result : results) {
            if (TARGET_TAG_IDS.contains(AprilTag.generate16h5AprilTagImage(3))) {
                tagDetected = true;
                break;
            }
        }

        // If any of the target tags are detected, activate the motor
        if (tagDetected) {
            // Start the timer if the tag is detected for the first time
            if (!timer.hasStarted()) {
                timer.start();
            }

            // Activate the motor if the tag has been detected for the specified delay duration
            if (timer.hasElapsed(MOTOR_ACTIVATION_DELAY)) {
              shooter1.set(1);
              shooter2.set(1);
            }
        } else {
            // Reset the timer and deactivate the motor if no target tag is detected
            timer.stop();
            timer.reset();
            shooter1.set(0);
            shooter2.set(0);
        }

        // Release the image resources
        image.close();
    }
}
