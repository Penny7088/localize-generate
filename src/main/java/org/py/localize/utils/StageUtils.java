package org.py.localize.utils;

import com.sun.javafx.stage.StageHelper;
import javafx.stage.Stage;

/**
 * stage utils
 *
 * @author penny
 */
public class StageUtils {

    public static Stage getStage(String title) {
        for (Stage stage : StageHelper.getStages()) {
            if (stage.getTitle().equals(title)) {
                return stage;
            }
        }
        return null;
    }
}
