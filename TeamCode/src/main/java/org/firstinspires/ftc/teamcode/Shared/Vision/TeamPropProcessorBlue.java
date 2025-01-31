package org.firstinspires.ftc.teamcode.Shared.Vision;

import android.graphics.Canvas;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.*;
public class TeamPropProcessorBlue
        implements VisionProcessor {
    Mat testMat = new Mat();
    Mat highMat = new Mat();
    Mat lowMat = new Mat();
    Mat finalMat = new Mat();
    double redThreshold = 0.5;

    String outStr = "left"; //Set a default value in case vision does not work

    static final Rect LEFT_RECTANGLE = new Rect(
            new Point(0, 0),
            new Point(0, 0)
    );

    static final Rect RIGHT_RECTANGLE = new Rect(
            new Point(0, 0),
            new Point(0, 0)
    );

    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {

        Scalar BlueUpper = new Scalar(218, 96, 62);
        Scalar BlueLower = new Scalar(195, 100, 100);

        Core.inRange(testMat, BlueLower, BlueUpper, finalMat);

        testMat.release();

        Core.bitwise_or(lowMat, highMat, finalMat);

        lowMat.release();
        highMat.release();

        double leftBox = Core.sumElems(finalMat.submat(LEFT_RECTANGLE)).val[0];
        double rightBox = Core.sumElems(finalMat.submat(RIGHT_RECTANGLE)).val[0];

        double averagedLeftBox = leftBox / LEFT_RECTANGLE.area() / 255;
        double averagedRightBox = rightBox / RIGHT_RECTANGLE.area() / 255; //Makes value [0,1]


        if (averagedLeftBox > redThreshold) {        //Must Tune Red Threshold
            outStr = "left";
        } else if (averagedRightBox > redThreshold) {
            outStr = "center";
        } else {
            outStr = "right";
        }

        finalMat.copyTo(frame); /*This line should only be added in when you want to see your custom pipeline
                                  on the driver station stream, do not use this permanently in your code as
                                  you use the "frame" mat for all of your pipelines, such as April Tags*/
        return null;            //You do not return the original mat anymore, instead return null
    }


    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }

    public String getPropPosition() {  //Returns postion of the prop in a String
        return outStr;
    }
}

