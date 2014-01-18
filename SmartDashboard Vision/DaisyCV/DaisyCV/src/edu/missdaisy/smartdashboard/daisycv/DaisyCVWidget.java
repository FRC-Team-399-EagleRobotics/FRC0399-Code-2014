package edu.missdaisy.smartdashboard.daisycv;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_imgproc;
import com.googlecode.javacv.cpp.opencv_imgproc.*;
import edu.wpi.first.smartdashboard.camera.WPICameraExtension;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.wpijavacv.DaisyExtensions;
import edu.wpi.first.wpijavacv.WPIBinaryImage;
import edu.wpi.first.wpijavacv.WPIColor;
import edu.wpi.first.wpijavacv.WPIColorImage;
import edu.wpi.first.wpijavacv.WPIContour;
import edu.wpi.first.wpijavacv.WPIImage;
import edu.wpi.first.wpijavacv.WPIPoint;
import edu.wpi.first.wpijavacv.WPIPolygon;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.imageio.ImageIO;

/* HOW TO GET THIS COMPILING IN NETBEANS:
 *  1. Install the SmartDashboard using the installer (if on Windows)
 *      1a. Verify that the OpenCV libraries are in your PATH (on Windows)
 *  2. Add the following libraries to the project:
 *     SmartDashboard.jar
 * 
 *     extensions/WPICameraExtension.jar
 *     lib/NetworkTable_Client.jar
 *     extensions/lib/javacpp.jar
 *     extensions/lib/javacv-*your environment*.jar
 *     extensions/lib/javacv.jar
 *     extensions/lib/WPIJavaCV.jar
 *
 */
/**
 *
 * @author jrussell
 */
public class DaisyCVWidget extends WPICameraExtension {

    public static final String NAME = "FRC399 2014 Target Tracker"; //Plugin name
    private WPIColor displayColor = new WPIColor(0, 0, 255);        //Heads up display color
    // Constants that need to be tuned
    private static final double kNearlyHorizontalSlope = Math.tan(Math.toRadians(45));
    private static final double kNearlyVerticalSlope = Math.tan(Math.toRadians(90 - 45));
    private static final int kMinWidth = 0;
    private static final int kMaxWidth = 320;
    private static int kHoleClosingIterations = 9;
    private static final double kShooterOffsetDeg = -1.55;
    private static final double kHorizontalFOVDeg = 47.0;
    private boolean m_debugMode = false;
    // Store JavaCV temporaries as members to reduce memory management during processing
    private CvSize size = null;
    private WPIContour[] contours;
    private ArrayList<WPIPolygon> polygons;
    private IplConvKernel morphKernel;
    private IplImage bin;
    private IplImage hsv;
    private IplImage hue;
    private IplImage sat;
    private IplImage val;
    private WPIPoint linePt1;
    private WPIPoint linePt2;
    private int horizontalOffsetPixels;

    public DaisyCVWidget() {
        this(false);
    }

    public DaisyCVWidget(boolean debug) {
        m_debugMode = debug;
        morphKernel = IplConvKernel.create(3, 3, 1, 1, opencv_imgproc.CV_SHAPE_RECT, null);

        DaisyExtensions.init();
    }

    @Override
    public WPIImage processImage(WPIColorImage rawImage) {

        //Values from robot:
        double yaw = 0.0;   //Yaw for azimuth calculations
        double h_low = 0.0;
        double h_high = 0.0;
        double s_low = 0.0;
        double s_high = 0.0;
        double v_low = 0.0;
        double v_high = 0.0;

        int howManyTargets = 0;

        if (!m_debugMode) {
            try {
                yaw = Robot.getTable().getNumber("yaw");
            } catch (NoSuchElementException e) {
            } catch (IllegalArgumentException e) {
            }

            try {
                h_low = Robot.getTable().getNumber("h_low");
            } catch (NoSuchElementException e) {
            } catch (IllegalArgumentException e) {
            }

            try {
                h_high = Robot.getTable().getNumber("h_high");
            } catch (NoSuchElementException e) {
            } catch (IllegalArgumentException e) {
            }

            try {
                s_low = Robot.getTable().getNumber("s_low");
            } catch (NoSuchElementException e) {
            } catch (IllegalArgumentException e) {
            }

            try {
                s_high = Robot.getTable().getNumber("s_high");
            } catch (NoSuchElementException e) {
            } catch (IllegalArgumentException e) {
            }

            try {
                v_low = Robot.getTable().getNumber("v_low");
            } catch (NoSuchElementException e) {
            } catch (IllegalArgumentException e) {
            }

            try {
                v_high = Robot.getTable().getNumber("v_high");
            } catch (NoSuchElementException e) {
            } catch (IllegalArgumentException e) {
            }

            try {
                kHoleClosingIterations = (int) Robot.getTable().getNumber("kHoleClosingIterations", 0);
            } catch (NoSuchElementException e) {
            } catch (IllegalArgumentException e) {
            }
        }

        if (size == null || size.width() != rawImage.getWidth() || size.height() != rawImage.getHeight()) {
            size = opencv_core.cvSize(rawImage.getWidth(), rawImage.getHeight());
            bin = IplImage.create(size, 8, 1);
            hsv = IplImage.create(size, 8, 3);
            hue = IplImage.create(size, 8, 1);
            sat = IplImage.create(size, 8, 1);
            val = IplImage.create(size, 8, 1);
            horizontalOffsetPixels = (int) Math.round(kShooterOffsetDeg * (size.width() / kHorizontalFOVDeg));
            linePt1 = new WPIPoint(size.width() / 2 + horizontalOffsetPixels, size.height() - 1);
            linePt2 = new WPIPoint(size.width() / 2 + horizontalOffsetPixels, 0);
        }
        // Get the raw IplImages for OpenCV
        IplImage input = DaisyExtensions.getIplImage(rawImage);

        // Convert to HSV color space
        opencv_imgproc.cvCvtColor(input, hsv, opencv_imgproc.CV_BGR2HSV);
        opencv_core.cvSplit(hsv, hue, sat, val, null);

        // Threshold each component separately
        // Hue
        // NOTE: Red is at the end of the color space, so you need to OR together
        // a thresh and inverted thresh in order to get points that are red
        opencv_imgproc.cvThreshold(hue, bin, h_low, 255, opencv_imgproc.CV_THRESH_BINARY);
        opencv_imgproc.cvThreshold(hue, hue, h_high, 255, opencv_imgproc.CV_THRESH_BINARY_INV);

        // Saturation
        opencv_imgproc.cvThreshold(sat, sat, s_low, s_high, opencv_imgproc.CV_THRESH_BINARY);

        // Value
        opencv_imgproc.cvThreshold(val, val, v_low, v_high, opencv_imgproc.CV_THRESH_BINARY);

        // Combine the results to obtain our binary image which should for the most
        // part only contain pixels that we care about
        opencv_core.cvAnd(hue, bin, bin, null);
        opencv_core.cvAnd(bin, sat, bin, null);
        opencv_core.cvAnd(bin, val, bin, null);

        // Uncomment the next two lines to see the raw binary image
        //CanvasFrame result = new CanvasFrame("binary");
        //result.showImage(bin.getBufferedImage());
        // Fill in any gaps using binary morphology
        opencv_imgproc.cvMorphologyEx(bin, bin, null, morphKernel, opencv_imgproc.CV_MOP_CLOSE, kHoleClosingIterations);

        // Uncomment the next two lines to see the image post-morphology
        //CanvasFrame result2 = new CanvasFrame("morph");
        //result2.showImage(bin.getBufferedImage());
        // Find contours
        WPIBinaryImage binWpi = DaisyExtensions.makeWPIBinaryImage(bin);
        contours = DaisyExtensions.findConvexContours(binWpi);

        howManyTargets = contours.length;

        polygons = new ArrayList<WPIPolygon>();
        for (WPIContour c : contours) {

            if (c.getWidth() > kMinWidth && c.getWidth() < kMaxWidth) {
                polygons.add(c.approxPolygon(20));
            }
        }

        WPIPolygon square = null;
        int highest = Integer.MAX_VALUE;

        for (WPIPolygon p : polygons) {
            if (p.isConvex() && p.getNumVertices() == 4) {
                // We passed the first test...we fit a rectangle to the polygon
                // Now do some more tests

                WPIPoint[] points = p.getPoints();
                // We expect to see a top line that is nearly horizontal, and two side lines that are nearly vertical
                int numNearlyHorizontal = 0;
                int numNearlyVertical = 0;
                for (int i = 0; i < 4; i++) {
                    double dy = points[i].getY() - points[(i + 1) % 4].getY();
                    double dx = points[i].getX() - points[(i + 1) % 4].getX();
                    double slope = Double.MAX_VALUE;
                    if (dx != 0) {
                        slope = Math.abs(dy / dx);
                    }

                    if (slope < kNearlyHorizontalSlope) {
                        ++numNearlyHorizontal;
                    } else if (slope > kNearlyVerticalSlope) {
                        ++numNearlyVertical;
                    }
                }

                if (numNearlyHorizontal >= 1 && numNearlyVertical == 2) {
                    rawImage.drawPolygon(p, WPIColor.BLUE, 2);

                    int pCenterX = (p.getX() + (p.getWidth() / 2));
                    int pCenterY = (p.getY() + (p.getHeight() / 2));

                    rawImage.drawPoint(new WPIPoint(pCenterX, pCenterY), displayColor, 5);
                    if (pCenterY < highest) // Because coord system is funny
                    {
                        square = p;
                        highest = pCenterY;
                    }
                }
            } else {
                rawImage.drawPolygon(p, WPIColor.YELLOW, 1);
            }
        }

        if (square != null) {
            double x = square.getX() + (square.getWidth() / 2);
            x = (2 * (x / size.width())) - 1;
            double y = square.getY() + (square.getHeight() / 2);
            y = -((2 * (y / size.height())) - 1);

            /*double range = (kTopTargetHeightIn - kCameraHeightIn) / Math.tan((y * kVerticalFOVDeg / 2.0 + kCameraPitchDeg) * Math.PI / 180.0);
             double azimuth = this.boundAngle0to360Degrees(x*kHorizontalFOVDeg/2.0 + yaw - kShooterOffsetDeg);
             double altitude = this.boundAngle0to360Degrees(y*kVerticalFOVDeg/2.0 + pitch);*/
            if (!m_debugMode) {
                //Robot.getTable().beginTransaction();
                Robot.getTable().putBoolean("found", true);
                Robot.getTable().putNumber("TargetX", x);
                Robot.getTable().putNumber("TargetY", y);
                Robot.getTable().putNumber("targetWidth", square.getWidth());
                Robot.getTable().putNumber("targetHeight", square.getHeight());
                //Robot.getTable().putNumber("TargetRange", range);
                Robot.getTable().putNumber("TargetArea", square.getArea());
                //Robot.getTable().putNumber("azimuth", azimuth);
                //Robot.getTable().putNumber("numberOfTargets", howManyTargets);
                //Robot.getTable().putNumber("altitude", altitude);
                //Robot.getTable().endTransaction();
            } else {
                System.out.println("DebugMode, target found");
            }
            rawImage.drawPolygon(square, displayColor, 7);
        } else {

            if (!m_debugMode) {
                Robot.getTable().putBoolean("found", false);
            } else {
                System.out.println("Target not found");
            }
        }

        // Draw a crosshair
        rawImage.drawLine(linePt1, linePt2, displayColor, 2);

        DaisyExtensions.releaseMemory();

        //System.gc();
        return rawImage;
    }

    private double boundAngle0to360Degrees(double angle) {
        // Naive algorithm
        while (angle >= 360.0) {
            angle -= 360.0;
        }
        while (angle < 0.0) {
            angle += 360.0;
        }
        return angle;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Arguments are paths to image files to test the program on");
            return;
        }

        // Create the widget
        DaisyCVWidget widget = new DaisyCVWidget(true);

        long totalTime = 0;
        for (int i = 0; i < args.length; i++) {
            // Load the image
            WPIColorImage rawImage = null;
            try {
                rawImage = new WPIColorImage(ImageIO.read(new File(args[i % args.length])));
            } catch (IOException e) {
                System.err.println("Could not find file!");
                return;
            }

            //shows the raw image before processing to eliminate the possibility
            //that both may be the modified image.
            CanvasFrame original = new CanvasFrame("Raw");
            original.showImage(rawImage.getBufferedImage());

            WPIImage resultImage = null;

            // Process image
            long startTime, endTime;
            startTime = System.nanoTime();
            resultImage = widget.processImage(rawImage);
            endTime = System.nanoTime();

            // Display results
            totalTime += (endTime - startTime);
            double milliseconds = (double) (endTime - startTime) / 1000000.0;
            System.out.format("Processing took %.2f milliseconds%n", milliseconds);
            System.out.format("(%.2f frames per second)%n", 1000.0 / milliseconds);

            CanvasFrame result = new CanvasFrame("Result");
            result.showImage(resultImage.getBufferedImage());

            System.out.println("Waiting for ENTER to continue to next image or exit...");
            Scanner console = new Scanner(System.in);
            console.nextLine();

            if (original.isVisible()) {
                original.setVisible(false);
                original.dispose();
            }
            if (result.isVisible()) {
                result.setVisible(false);
                result.dispose();
            }
        }

        double milliseconds = (double) (totalTime) / 1000000.0 / (args.length);
        System.out.format("AVERAGE:%.2f milliseconds%n", milliseconds);
        System.out.format("(%.2f frames per second)%n", 1000.0 / milliseconds);
        System.exit(0);
    }
}
