package interfaces;

import models.OutputJVisionImage;

/**
 * @author 8130031
 *
 * Behaviour to handle the result of {@link jvision.JVisionDetection}
 *
 */
public interface BehaviourDetection {

    void actionTargetFound(OutputJVisionImage imageOut);

    void actionContinuous(OutputJVisionImage imageOut);
}
