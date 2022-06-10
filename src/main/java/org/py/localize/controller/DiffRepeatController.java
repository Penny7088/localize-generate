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
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.py.localize.utils.*;
import org.py.localize.widget.Progress;
import org.py.localize.model.RepeatEntity;
import org.py.localize.widget.Toast;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 去重控制器
 */
public class DiffRepeatController {

    private double xOffset = 0;
    private double yOffset = 0;


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

    private static final String ios_notes = "/* */";
    private static final String android_notes = "<!-- -->";

    ObservableList<RepeatEntity> data = FXCollections.observableArrayList(
            new RepeatEntity("KEY", "Value", "文件路径", "文件名称", "文件路径"));


    private Progress mProgress;
    private Disposable disposable;

    public void init() {

        repeatBox.setOnMouseDragged((MouseEvent event) -> {
            event.consume();
            Objects.requireNonNull(StageUtils.getStage("检测重复key值")).setX(event.getScreenX() - xOffset);

            //根据自己的需求，做不同的判断
            if (event.getScreenY() - yOffset < 0) {
                Objects.requireNonNull(StageUtils.getStage("检测重复key值")).setY(0);
            } else {
                Objects.requireNonNull(StageUtils.getStage("检测重复key值")).setY(event.getScreenY() - yOffset);
            }
        });

        mProgress = new Progress(StageUtils.getStage("检测重复key值"));

        repeatBox.setOnDragDropped(new DragDroppedEvent(filePathLabel));
        repeatBox.setOnDragOver(new DragOverEvent());

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
        boolean hasData = data.size() >= 2;
        if (hasData) {
            data.remove(1, data.size());
        }
        String text = filePathLabel.getText();
        if (text == null || text.length() == 0) {
            Toast.makeText(StageUtils.getStage("检测重复key值"), "文件夹路径为空");
            return;
        }
        // 开始解析文件夹
        mProgress.activateProgressBar();
        if (!FileUtils.isDir(text)) {
            mProgress.cancelProgressBar();
            Toast.makeText(StageUtils.getStage("检测重复key值"), "当前路径不是文件夹");
        }

        Observable<RepeatResponse> observable = Observable.create(new ObservableOnSubscribe<RepeatResponse>() {
            @Override
            public void subscribe(ObservableEmitter<RepeatResponse> observableEmitter) throws Exception {
                RepeatResponse response = new RepeatResponse();
                ArrayList<RepeatEntity> entities = new ArrayList<>();
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
                        observableEmitter.onNext(parseIOSFile(ios, response, entities));
                    }
                } else {
                    observableEmitter.onNext(parseAndroidFile(fileList, response, entities));
                }


            }
        });
        disposable = observable.subscribeOn(Schedulers.io())
                .subscribe(new Consumer<RepeatResponse>() {
                    @Override
                    public void accept(RepeatResponse repeatResponse) throws Exception {
                        Platform.runLater(() -> {
                            System.out.println("rsp = " + repeatResponse);
                            mProgress.cancelProgressBar();
                            if (repeatResponse.getErrorMsg() != null) {
                                String message = repeatResponse.getErrorMsg();
                                Toast.makeText(StageUtils.getStage("检测重复key值"), message);
                            } else {
                                data.addAll(repeatResponse.getData());
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println(throwable);
                        Platform.runLater(() -> {
                            String message = throwable.getMessage();
                            Toast.makeText(StageUtils.getStage("检测重复key值"), message);
                            mProgress.cancelProgressBar();
                        });

                    }
                });

    }

    private RepeatResponse parseIOSFile(List<File> fileList, RepeatResponse response, ArrayList<RepeatEntity> entities) {
        fileList.forEach(file -> {
            System.out.println(file.getAbsolutePath());
            List<String> stringList = FileIOUtils.readFile2List(file, "utf-8", ios_notes);
            ArrayList<RepeatEntity> repeatEntities = filterFileContent(stringList, "ios", file);
            if (repeatEntities.size() > 0) {
                System.out.println(repeatEntities);
                entities.addAll(repeatEntities);
            }
        });

        if (entities.size() == 0) {
            response.setErrorMsg("没有重复的值");
        }
        response.setData(entities);

        return response;
    }

    private ArrayList<RepeatEntity> filterFileContent(List<String> result, String platfrom, File file) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        ArrayList<RepeatEntity> entities = new ArrayList<>();
        String regex;
        List<String> ignore = new ArrayList<>();
        if (platfrom.equals("android")) {
            regex = "<string name=(.+?)>(.+?)</string>";
            ignore.add("<resources>");
            ignore.add("</resources>");
        } else {
            regex = "=";
        }

        result.forEach(s -> {

            if (ignore.size() > 0) {
                boolean contains = ignore.contains(s);
                if (contains) {
                    return;
                }
            }

            String[] split = matchLine(s, regex, platfrom);
            System.out.println("split = " + split);


            if (split.length > 0) {
                String key = split[0];
                String value = split[1];
                if (key != null && value != null) {
                    key = key.trim();
                    value = value.trim();
                    if (hashMap.get(key) != null) {
                        System.out.println("有重复的值");
                        RepeatEntity repeatEntity = new RepeatEntity(key, value, file.getParent(), file.getName(), file.getParentFile().getName());
                        entities.add(repeatEntity);
                    } else {
                        hashMap.put(key, 1);
                    }
                }
            }
        });

        return entities;
    }

    private String[] matchLine(String s, String regex, String platform) {
        String[] result = new String[2];
        if (platform.equals("ios")) {
            result = s.split(regex);
        } else {
            Pattern compile = Pattern.compile(regex, Pattern.MULTILINE);
            Matcher matcher = compile.matcher(s);
            while (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String group = matcher.group(i);
                    if (!group.isEmpty()) {
                        if (i == 1) {
                            result[0] = group;
                        } else {
                            result[1] = group;
                        }
                    }
                }
            }
        }
        return result;
    }

    private RepeatResponse parseAndroidFile(List<File> fileList, RepeatResponse response, ArrayList<RepeatEntity> entities) {
        fileList.forEach(file -> {
            System.out.println(file.getAbsolutePath());
            List<String> stringList = FileIOUtils.readFile2List(file, "utf-8", android_notes);
            ArrayList<RepeatEntity> repeatEntities = filterFileContent(stringList, "android", file);
            if (repeatEntities.size() > 0) {
                System.out.println(repeatEntities);
                entities.addAll(repeatEntities);
            }
        });

        if (entities.size() == 0) {
            response.setErrorMsg("没有重复的值");
        }
        response.setData(entities);

        return response;
    }

    private void initListView() {
        listView.setCellFactory(new Callback<ListView<RepeatEntity>, ListCell<RepeatEntity>>() {
            @Override
            public ListCell<RepeatEntity> call(ListView<RepeatEntity> param) {

                return new ListCell<RepeatEntity>() {
                    @Override
                    protected void updateItem(RepeatEntity item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            HBox hBox = new HBox();
                            if (item.getFilePath().equals("文件路径")) {
                                Label filePath = new Label(item.getFilePath());
                                filePath.setPrefWidth(300);
                                filePath.setPrefHeight(50);
                                Label fileName = new Label(item.getFileName());
                                fileName.setPrefWidth(150);
                                fileName.setPrefHeight(50);
                                Label key = new Label(item.getKey());
                                key.setPrefWidth(200);
                                key.setPrefHeight(50);
                                Label value = new Label(item.getValue());
                                value.setPrefWidth(450);
                                value.setPrefHeight(50);
                                hBox.getChildren().addAll(filePath, fileName, key, value);
                            } else {
                                Label filePath = new Label(item.getFileParentName());
                                filePath.setPrefWidth(300);
                                filePath.setPrefHeight(80);
                                Label fileName = new Label(item.getFileName());
                                fileName.setPrefWidth(150);
                                fileName.setPrefHeight(80);
                                Label key = new Label(item.getKey());
                                key.setPrefWidth(200);
                                key.setPrefHeight(80);
                                Label value = new Label(item.getValue());
                                value.setPrefWidth(450);
                                value.setPrefHeight(80);
                                hBox.getChildren().addAll(filePath, fileName, key, value);
                            }
                            this.setGraphic(hBox);
                        } else if (empty) {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        listView.setOnMouseClicked(event -> {
            int clickCount = event.getClickCount();
            System.out.println("count" + clickCount);
            if (clickCount == 2) {
                RepeatEntity entity = listView.getSelectionModel().getSelectedItem();
                System.out.println("click =" + entity);
                if (entity.getFilePath() != null) {

                    FileUtils.openDir(entity.getFilePath());
                }
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
        if (disposable != null) {
            disposable.dispose();
        }
        Stage stage = StageUtils.getStage("检测重复key值");
        Event.fireEvent(Objects.requireNonNull(stage), new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
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


    public static class DragOverEvent implements EventHandler<DragEvent> {
        public void handle(DragEvent event) {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                for (int i = 0; i < dragboard.getFiles().size(); i++) {
                    File file = dragboard.getFiles().get(i);
                    if (file.isDirectory()) {
                        event.acceptTransferModes(TransferMode.ANY);
                    }
                }
            }

        }
    }


}
