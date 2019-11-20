package jvision;

import exceptions.NoBehaviourDefined;
import exceptions.NoDetectionsDefined;
import exceptions.NoSourceDefined;
import interfaces.BehaviourDetection;
import interfaces.Detection;
import interfaces.Source;
import models.*;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author 8130031
 *
 * Handles the detection mode, receive desired detections defined by {@link Detection}
 * Call the methods of respective detections
 * Sends the output with images processed to {@link BehaviourDetection}
 *
 */
public  class JVisionDetection extends JVision {

    private static JVisionDetection instance = new JVisionDetection();

    //mandatory configs
    private List<Detection> detections = null;
    private BehaviourDetection behaviour = null;

    //service
    private ScheduledExecutorService timer;

    //detection
    private ArrayList<Rect[]> objects = new ArrayList<>();
    private OutputColorContour contoursOutput = new OutputColorContour();
    private OutputOpticalFlow opticalFlowOutput = new OutputOpticalFlow();
    private OutputShape shapeOutput = new OutputShape();

    private JVisionDetectionFactory visionFactory;
    private OutputJVisionImage imageOut = new OutputJVisionImage();


    private JVisionDetection() {
    }

    public static JVisionDetection getInstance() {
        return JVisionDetection.instance;
    }

    public void setDetections(List<Detection> detections) {
        this.detections = detections;
    }

    public void setBehaviour(BehaviourDetection behaviour) {
        this.behaviour = behaviour;
    }

    public void setSource(Source source) {
        super.setSource(source);
    }

    public void setVisionConfig(JVisionConfig config) {
        super.setVisionConfig(config);
    }

    public void updateSourceBuffered(BufferedImage bufferedImage){
        super.updateSourceBuffered(bufferedImage);
    }

    @Override
    public void start() {
        boolean error = false;
        try {
            checkExceptions();
        } catch (NoBehaviourDefined | NoDetectionsDefined | NoSourceDefined e) {
            e.printStackTrace();
            error = true;
        }

        if (!error) {
            super.initSource();

            this.visionFactory = new JVisionDetectionFactory(super.getVisionConfig());

            Runnable frameGrabber = () -> {
                if (!super.getFrame().empty()) {

                    this.objects = new ArrayList<>();
                    this.shapeOutput = null;
                    this.contoursOutput = null;
                    this.opticalFlowOutput = null;

                    for (Detection detection : this.detections) {

                        if (detection instanceof DetectionObject) {
                            Rect[] obj = ((DetectionObject) detection).detectObject(super.getFrame());
                            this.objects.add(obj);

                        } else if (detection instanceof DetectionContour) {
                            this.contoursOutput = ((DetectionContour) detection).detectContours(super.getFrame());

                        } else if (detection instanceof DetectionOpticalFlow) {
                            this.opticalFlowOutput = ((DetectionOpticalFlow) detection).detectFlow(super.getFrame());

                        } else if (detection instanceof DetectionShape) {
                            this.shapeOutput = ((DetectionShape) detection).detectShape(super.getFrame());
                        }

                    }

                    imageOut = visionFactory.buildImage(super.getFrame(), objects, shapeOutput, contoursOutput, opticalFlowOutput);

                    //stop at first target found - actionTargetFound
                    try {
                        if (imageOut.isTargetsFound()) {
                            this.behaviour.actionTargetFound(imageOut);
                        }
                    } catch (UnsupportedOperationException ue) {
                        System.out.println(ue.toString());
                    }

                    //continuous action
                    try {
                        this.behaviour.actionContinuous(imageOut);
                    } catch (UnsupportedOperationException ue) {
                        System.out.println(ue.toString());
                    }
                }
            };

            this.timer = Executors.newSingleThreadScheduledExecutor();
            this.timer.scheduleAtFixedRate(frameGrabber, 0, super.getVisionConfig().getFrameRate(), super.getVisionConfig().getTimeUnit());
        }
    }

    @Override
    public void stop() {
        if (!this.timer.isShutdown() || !this.timer.isTerminated()) {
            try {
                this.timer.shutdown();
                this.timer.awaitTermination(super.getVisionConfig().getFrameRate(), super.getVisionConfig().getTimeUnit());
            } catch (InterruptedException e) {
                // log the exception
                System.err.println("Exception in stopping the executor service... " + e);
            }
        }
    }

    private void checkExceptions() throws NoSourceDefined, NoBehaviourDefined, NoDetectionsDefined {
        if (super.getSource() == null) {
            throw new NoSourceDefined("No Source Image Defined");
        }

        if (behaviour == null) {
            throw new NoBehaviourDefined("No Behaviour Defined");
        }

        if (detections == null) {
            throw new NoDetectionsDefined("No Detections Defined");
        }
    }
}
