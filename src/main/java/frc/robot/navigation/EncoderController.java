package frc.robot.navigation;

import frc.robot.subsystems.DriveSubsystem;

public class EncoderController{

    private Path genPath;
    private Point robotPosition = new Point(0, 0);
    private Point targetPoint = new Point(0, 0);
    private int targetIndex = 0;
    private double targetX, targetY, targetDistance, targetHeading;
    private double xLocation;
    private double yLocation;
    private DriveSubsystem m_drive;
    private boolean isFinished = false;


    public EncoderController(Point[] points, double weight_smooth){
        //setting up the path
        double tol = 0.001;
        double a = 1 - weight_smooth;
        Path path = new Path(points);
        //initialize the path, and insert a point every eighteen inches
        this.genPath = new Path(path.generatePath(path.numPointForArray(18)));
        //smooth the path
        this.genPath = this.genPath.smoother(a, weight_smooth, tol);
    }

    public Boolean controlLoop(DriveSubsystem drive, double lDistance, double rDistance, double heading){

        //initialize the drive
        this.m_drive = drive;
        //find the robot's current location
        this.getRobotPosition(lDistance, rDistance, heading);
        //every time the control loop is run, the targetIndex incresaes by one
        this.targetIndex += 1;
        //set the targetPoint to the new Index
        this.targetPoint = this.genPath.get(targetIndex);
        //determine the change in X and Y that the robot will have to achieve
        this.targetX = targetPoint.getX() - robotPosition.getX();
        this.targetY = targetPoint.getY() - robotPosition.getY();
        //calculate the change in heading, this may be subject to change, depending on how to encoder's are written
        this.targetHeading = Math.atan(this.targetX/this.targetY) - heading;
        //how far the robot needs to move, once it has turned
        this.targetDistance = this.robotPosition.distFrom(this.targetPoint);

        //this is the actual drive command, I do not know what the actual method names will be, however this will come later.
        //this.m_drive.encoderTurn(this.targetHeading);
        //this.m_drive.encoderDrive(this.targetDistance);

        //checking to see if we are at the end of the path
        if(this.robotPosition.equals(this.genPath.get(this.genPath.size() - 1))){
            //if it is, set the finish variable to true, meaning that the loop is complete
            this.isFinished = true;
        }

        //return either false for not finshed, or true if the path is complete
        return this.isFinished;
    }

    public void getRobotPosition(double lDistance, double rDistance, double heading){
        //determining the robot's current location
        double distance = Math.abs((6 * 3.14) * (lDistance + rDistance / 2) / 360);
        this.xLocation += distance * Math.cos(heading);
        this.yLocation += distance * Math.sin(heading);
        this.robotPosition = new Point(this.xLocation, this.yLocation);
    }
}