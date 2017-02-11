package org.usfirst.frc.team5938.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotDrive myRobot;
	int autoLoopCounter;
	Joystick xLeftStick; // set to ID 1 in DriverStation
	Joystick xRightStick; // set to ID 2 in DriverStation
	Joystick Logitech;
	CameraServer Camera;
	SpeedController intake1; // the motor to directly control with a joystick
	SpeedController intake2;
	SpeedController sprocket;
	SpeedController flicker;

	public Robot() {
		myRobot = new RobotDrive(0, 1);
		myRobot.setExpiration(0.1);
		xLeftStick = new Joystick(1);
		xRightStick = new Joystick(1);
		Logitech = new Joystick(0);

		Camera = CameraServer.getInstance();
		Camera.setQuality(35); // set the quality of the camera
		Camera.startAutomaticCapture("cam2"); // the camera name (ex "cam0") can
												// be found through the roborio
												// web interface

		intake2 = new VictorSP(2); // initialize the second intake motor as a
									// VictorSP on channel 2
		intake1 = new VictorSP(3); // initialize the first intake motor as a
									// VictorSP on channel 3
		sprocket = new VictorSP(4); // initialize the Sprocket on channel 4
		flicker = new VictorSP(5);
	}

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
	}

	/**
	 * This function is run once each time the robot enters autonomous mode
	 */
	public void autonomousInit() {
		autoLoopCounter = 0;
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {

		if (autoLoopCounter < 100) // Check if we've completed 100 loops
									// (approximately 2 seconds)
		{
			myRobot.drive(-0.65, 0.00); // drive forwards half speed
			autoLoopCounter++;
		} else {
			myRobot.drive(0.0, 0.0); // stop robot
		}

	}

	/**
	 * This function is called once each time the robot enters tele-operated
	 * mode
	 */
	public void teleopInit() {

	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		myRobot.setSafetyEnabled(true);
		while (isOperatorControl() && isEnabled()) {

			myRobot.tankDrive(xLeftStick.getRawAxis(5) * 0.75, xRightStick.getRawAxis(1) * 0.75);
			// The right joystick actually controls the left side of the robot
			// The right side of the robot seems to be accidently built slightly
			// tighter, so I compensated by slowing the left side a bit

			if (Logitech.getPOV() == 0) {
				intake1.set(-0.75);
				intake2.set(0.75);
			} else if (Logitech.getPOV() != 180) {
				intake1.set(0);
				intake2.set(0);
			}

			if (Logitech.getPOV() == 180) {
				intake1.set(0.75);
				intake2.set(-0.75);
				flicker.set(0.75);
			} else if (Logitech.getPOV() != 0 && Logitech.getRawButton(11) == false) {
				intake1.set(0);
				intake2.set(0);
				flicker.set(0);
			}

			if (Logitech.getRawButton(2) == true) {
				intake1.set(-0.75);
				intake2.set(0.75);
			} else if (Logitech.getPOV() != 180 && Logitech.getPOV() != 0) {
				intake1.set(0);
				intake2.set(0);
			}

			if (Logitech.getRawButton(1) == true) {
				sprocket.set(.08);
			} else {
				sprocket.set(Logitech.getY() * 0.45);
			}

			if (Logitech.getRawButton(11) == true) {
				flicker.set(-1);
			} else {
				flicker.set(0);
			}

		}

		Timer.delay(0.005); // wait for a motor update time

	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

		LiveWindow.run();
	}

} // 10/21/16 8:44 AM Uploaded (Current Version)
