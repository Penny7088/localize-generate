package org.py.localize.utils;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class AnimationUtil {

    public static void mouseAnimationIn(Button bottom){
        ScaleTransition scale = new ScaleTransition(new Duration(250), bottom);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(1.1);
        scale.setToY(1.1);
        scale.setCycleCount(1);
        scale.play();
    }


    public static void mouseAnimationOut(Button bottom){
        ScaleTransition scale = new ScaleTransition(new Duration(250), bottom);
        scale.setFromX(1.1);
        scale.setFromY(1.1);
        scale.setToX(1);
        scale.setToY(1);
        scale.setCycleCount(1);
        scale.play();
    }

}
