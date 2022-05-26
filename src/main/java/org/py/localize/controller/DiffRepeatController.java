package org.py.localize.controller;

import com.sun.javafx.stage.StageHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.py.localize.utils.*;
import org.py.localize.widget.Progress;
import org.py.localize.model.RepeatEntity;
import org.yzr.poi.utils.*;
import org.py.localize.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * 去重控制器
 */
public class DiffRepeatController {


    @FXML
    private FlowPane repeatBox;

    @FXML
    private Button openFileRepeatButton;

    @FXML
    private Button executeButton;

    @FXML
    private Label filePathLabel;

    @FXML
    private ListView<RepeatEntity> listView;

    private static final String ios_notes = "/*";
    private static final String android_notes = "<--";

    ObservableList<RepeatEntity> data = FXCollections.observableArrayList(
            new RepeatEntity("KEY", "Value", "文件路径", "文件名称"),
            new RepeatEntity("ss", "vv", "/mac", "value-en"),
            new RepeatEntity("ss", "vv", "/mac", "value-en"),
            new RepeatEntity("ss", "vv", "/mac", "value-en"),
            new RepeatEntity("ss", "vv", "/mac", "value-en"),
            new RepeatEntity("ss", "vv", "/mac", "value-en"),
            new RepeatEntity("ss", "vv", "/mac", "value-en"),
            new RepeatEntity("ss", "vv", "/mac", "value-en"),
            new RepeatEntity("ss", "vv", "/mac", "value-en"));


    private Progress mProgress;

    public void init() {

        mProgress = new Progress(getRepeatStage());

        repeatBox.setOnDragDropped(new DragDroppedEvent(filePathLabel));

        openFileRepeatButton.setOnAction(event -> {
            String dir = FileChooser.getDirChooser();
            filePathLabel.setText(dir);
        });

        executeButton.setOnMouseClicked(event -> executeFile());

        anim();

        initListView();

        listView.setItems(data);
    }

    private void executeFile() {
        String text = filePathLabel.getText();
        if (text == null || text.length() == 0) {
            Toast.makeText(getRepeatStage(), "文件夹路径为空");
            return;
        }
        // 开始解析文件夹
        mProgress.activateProgressBar();
        if (!FileUtils.isDir(text)) {
            mProgress.cancelProgressBar();
            Toast.makeText(getRepeatStage(), "当前路径不是文件夹");
        }

        Observable<RepeatResponse> observable = Observable.create(new ObservableOnSubscribe<RepeatResponse>() {
            @Override
            public void subscribe(ObservableEmitter<RepeatResponse> observableEmitter) throws Exception {
                RepeatResponse response = new RepeatResponse();
                List<File> files = FileUtils.listFilesInDir(text);
                if (files == null || files.size() == 0) {
                    observableEmitter.onError(new Throwable("文件夹内为空"));
                }

                List<File> fileList = FileUtils.listFilesInDirWithFilter(new File(text), ".xml", true);
                if (fileList.size() == 0) {
                    List<File> ios = FileUtils.listFilesInDirWithFilter(new File(text), ".strings", true);
                    if (ios.size() == 0) {
                        observableEmitter.onError(new Throwable("文件夹内为空"));
                    } else {
                        parseIOSFile(ios, response);
                    }
                } else {
                    parseAndroidFile(fileList, response);
                }


            }
        });
        Disposable subscribe = observable.subscribeOn(Schedulers.io())
                .subscribe(new Consumer<RepeatResponse>() {
                    @Override
                    public void accept(RepeatResponse repeatResponse) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Platform.runLater(() -> {
                            String message = throwable.getMessage();
                            Toast.makeText(getRepeatStage(), message);
                            mProgress.cancelProgressBar();
                        });

                    }
                });

    }

    private void parseIOSFile(List<File> fileList, RepeatResponse response) {
        fileList.forEach(new java.util.function.Consumer<File>() {
            @Override
            public void accept(File file) {
                System.out.println(file.getParent());
                System.out.println(file.getName());
                System.out.println(file.getAbsolutePath());
                List<String> stringList = FileIOUtils.readFile2List(file, "utf-8");
                System.out.println(stringList);
            }
        });
    }

    private void parseAndroidFile(List<File> fileList, RepeatResponse response) {

    }

    private void initListView() {
        listView.setCellFactory(new Callback<ListView<RepeatEntity>, ListCell<RepeatEntity>>() {
            @Override
            public ListCell<RepeatEntity> call(ListView<RepeatEntity> param) {
                ListCell<RepeatEntity> listCell = new ListCell<RepeatEntity>() {
                    @Override
                    protected void updateItem(RepeatEntity item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            if (item.getFilePath().equals("文件路径")) {
                                HBox hBox = new HBox();
                                Label filePath = new Label(item.getFilePath());
                                filePath.setPrefWidth(200);
                                filePath.setPrefHeight(50);
                                Label fileName = new Label(item.getFileName());
                                fileName.setPrefWidth(150);
                                fileName.setPrefHeight(50);
                                Label key = new Label(item.getKey());
                                key.setPrefWidth(300);
                                key.setPrefHeight(50);
                                Label value = new Label(item.getValue());
                                value.setPrefWidth(450);
                                value.setPrefHeight(50);
                                hBox.getChildren().addAll(filePath, fileName, key, value);
                                this.setGraphic(hBox);
                            } else {
                                HBox hBox = new HBox();
                                Label filePath = new Label(item.getFilePath());
                                filePath.setPrefWidth(200);
                                filePath.setPrefHeight(80);
                                Label fileName = new Label(item.getFileName());
                                fileName.setPrefWidth(150);
                                fileName.setPrefHeight(80);
                                Label key = new Label(item.getKey());
                                key.setPrefWidth(300);
                                key.setPrefHeight(80);
                                Label value = new Label(item.getValue());
                                value.setPrefWidth(450);
                                value.setPrefHeight(80);
                                hBox.getChildren().addAll(filePath, fileName, key, value);
                                this.setGraphic(hBox);
                            }
                        }
                    }
                };


                return listCell;
            }


        });
    }

    private void anim() {
        openFileRepeatButton.setOnMouseEntered(event -> AnimationUtil.mouseAnimationIn(openFileRepeatButton));

        openFileRepeatButton.setOnMouseExited(event -> AnimationUtil.mouseAnimationOut(openFileRepeatButton));

        executeButton.setOnMouseEntered(event -> AnimationUtil.mouseAnimationIn(executeButton));

        executeButton.setOnMouseExited(event -> AnimationUtil.mouseAnimationOut(executeButton));
    }

    @FXML
    public void close(MouseEvent mouseEvent) {
        Event.fireEvent(Objects.requireNonNull(getRepeatStage()), new WindowEvent(getRepeatStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }


    public static Stage getRepeatStage() {
        for (Stage stage : StageHelper.getStages()) {
            if (stage.getTitle().equals("检测重复key值")) {
                return stage;
            }
        }
        return null;
    }


    private static class DragDroppedEvent implements EventHandler<DragEvent> {

        private Label filePathLabel;

        public DragDroppedEvent(Label filePathLabel) {
            this.filePathLabel = filePathLabel;
        }

        public void handle(DragEvent event) {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                try {
                    File file = dragboard.getFiles().get(0);
                    if (file != null) {
                        filePathLabel.setText(file.getAbsolutePath());
                    }
                } catch (Exception ignored) {

                }
            }
        }
    }


}
