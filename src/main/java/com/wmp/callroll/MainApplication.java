package com.wmp.callroll;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.wmp.callroll.tools.CallRollTool;
import com.wmp.callroll.tools.GetCRInfo;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class MainApplication extends GameApplication {

    private static Map<String, Integer> nameInfo = Map.of();
    private static ArrayList<String> punishInfo = new ArrayList<>();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initGame() {

        BorderPane borderPane = new BorderPane();

        Label title = new Label("点名器");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-size: 30px");
        //显示在北方
        borderPane.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);

        //System.out.println(getClass().getResource(""));
        InputStream resource = getClass().getResourceAsStream("image/wish.jpg");
        if (resource != null) {
            Image image = new Image(resource, 400, 400, true, true);
            ImageView imageView = new ImageView(image);
            borderPane.setCenter(imageView);
        }


        {//按钮组
            VBox buttonGroup = new VBox();


            HBox setsButtonGroup = new HBox();
            setsButtonGroup.setAlignment(Pos.CENTER);
            Button inputNameList = new Button("导入人员名单");
            inputNameList.setOnAction(e -> {
                System.out.println("导入人员名单");
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    try {
                        Files.copy(file.toPath(), new File("data.txt").toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    initNameMap(file.getPath());
                }
            });
            inputNameList.setStyle(getGenshinButtonStyle("#4d79c7", "#82a0d2"));

            Button inputPunish = new Button("导入惩罚名单");
            inputPunish.setOnAction(e -> {
                System.out.println("导入惩罚名单");
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    try {
                        Files.copy(file.toPath(), new File("punish.txt").toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    initPunishList(file.getPath());
                }
            });
            inputPunish.setStyle(getGenshinButtonStyle("#4d79c7", "#82a0d2"));
            setsButtonGroup.getChildren().addAll(inputNameList, inputPunish);
            buttonGroup.getChildren().add(setsButtonGroup);

            HBox callButtonGroup = new HBox();
            callButtonGroup.setAlignment(Pos.CENTER);
            Button button = new Button("祈愿1次");
            button.setOnAction(e -> {
                showWishVideo(1);
            });

            Button button10 = new Button("祈愿10次");
            button10.setOnAction(e -> {
                showWishVideo(10);
            });

            button.setStyle(getGenshinButtonStyle("#c79b4d", "#e8d282"));
            button10.setStyle(getGenshinButtonStyle("#4d79c7", "#82a0d2"));


            callButtonGroup.getChildren().addAll(button, button10);
            buttonGroup.getChildren().add(callButtonGroup);

            borderPane.setBottom(buttonGroup);
        }

        FXGL.addUINode(borderPane);
    }

    private void showWishVideo(int count) {
        System.out.printf("点名%s次", count);

        int star = 0;
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String name = CallRollTool.getName(nameInfo);
            names.add(name);
            if (nameInfo.get(name) > star) star = nameInfo.get(name);
        }
        String name = CallRollTool.getName(nameInfo);
        System.out.println(name);

        Media media;

        if (count ==1){
            if (star == 4){
                media = new Media(Objects.requireNonNull(getClass().getResource("video/1-4.mp4")).toString());
            } else if (star == 5) {
                media = new Media(Objects.requireNonNull(getClass().getResource("video/1-5.mp4")).toString());
            } else if (star == 6) {
                media = new Media(Objects.requireNonNull(getClass().getResource("video/1-6.mp4")).toString());
            } else{
                media = new Media(Objects.requireNonNull(getClass().getResource("video/1-3.mp4")).toString());
            }
        }else {
            if (star == 5) {
                media = new Media(Objects.requireNonNull(getClass().getResource("video/10-5.mp4")).toString());
            } else if (star == 6) {
                media = new Media(Objects.requireNonNull(getClass().getResource("video/1-6.mp4")).toString());
            } else{
                media = new Media(Objects.requireNonNull(getClass().getResource("video/10-4.mp4")).toString());
            }
        }

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        Pane pane = new Pane();
        pane.getChildren().add(mediaView);
        Scene scene = new Scene(pane);

        Stage view = new Stage();
        view.setTitle("祈愿");
        view.setResizable(false);
        view.setScene(scene);

        mediaPlayer.play();
        mediaPlayer.setOnReady(() -> {
            // 计算窗口装饰高度（标题栏高度）
            double decorationHeight = view.getHeight() - scene.getHeight();


            view.setWidth((double) media.getWidth() / 2);
            view.setHeight((media.getHeight() + decorationHeight) /2);

            mediaView.setFitWidth(view.getWidth());
            mediaView.setFitHeight(view.getHeight());
        });
        mediaPlayer.stop();


        view.setAlwaysOnTop(true);

        view.setOnCloseRequest(event -> {
            view.close();
            mediaPlayer.stop();
            showResult(names.toArray(new String[0]));
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            view.close();
            showResult(names.toArray(new String[0]));
        });

        mediaPlayer.play();
        view.show();
    }

    private static void showResult(String... names) {

        Stage showStage = new Stage();
        showStage.setAlwaysOnTop(true);
        showStage.setTitle("展示");

        StringBuilder sb = new StringBuilder();
        if (names.length > 1){
            TreeMap<String, Integer> temp = new TreeMap<>(((o1, o2) -> {
                int i = nameInfo.get(o1);
                int j = nameInfo.get(o2);
                if (i == j){
                    return -1;
                }else {
                    return j - i;
                }
            }));
            //设置temp的排序规则


            for (String name : names) {
                temp.put(name, nameInfo.get(name));
            }
            temp.forEach((key, value) -> {
                sb.append(value).append("-").append(key).append("\n");
            });


        }else{
            sb.append(nameInfo.get(names[0]).toString())
                    .append("-").append(Arrays.toString(names));
        }

        String punish = CallRollTool.getPunish(punishInfo.toArray(String[]::new));
        sb.append("\n\n").append(punish);

        messageDialog(sb.toString());
    }

    private static void messageDialog(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);// 设置标题
        alert.setContentText(message);
        alert.showAndWait();

    }

    private String getGenshinButtonStyle(String baseColor, String hoverColor) {
        String format = String.format(
                "* {" +
                        "-fx-font-family: 'Microsoft YaHei';" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-background-color: linear-gradient(to bottom, %s 0%%, %s 70%%);" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-radius: 5;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-color: derive(%s, 30%%);" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 8, 0, 0, 2);" +
                        "-fx-padding: 8 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-transition: all 0.3s;" +
                        "-fx-background-insets: 0;" +
                        "-fx-border-insets: 0;" +
                        "}" +
                        "*:hover {" +
                        "   -fx-background-color: linear-gradient(to bottom, derive(%s, 15%%) 0%%, derive(%s, 15%%) 70%%);" +
                        "   -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 3);" +
                        "}" +
                        "*:pressed {" +
                        "   -fx-background-color: linear-gradient(to bottom, %s 0%%, %s 70%%);" +
                        "   -fx-translate-y: 1;" +
                        "}",
                baseColor, baseColor, baseColor,
                baseColor, baseColor,
                hoverColor, hoverColor
        );
        return format;
         //format;
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("点名器");
        gameSettings.setWidth(400);
        gameSettings.setHeight(500);
        gameSettings.setVersion("1.0");
        //设置图标


        //gameSettings.setNative(true);// 设置为true，则使用本机平台，否则使用JavaFX平台


        initNameMap("data.txt");

        initPunishList("punish.txt");
    }

    @Override
    protected void initUI() {

        Platform.runLater(() -> {

            Stage stage = FXGL.getPrimaryStage();
            System.out.println(getClass().getResource("icon.png"));
            stage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            getClass().getResourceAsStream("icon.png"))));

        });

    }

    private void initNameMap(String path) {
        String[] names = GetCRInfo.getInfo(path);

        if (names != null) {
            nameInfo = GetCRInfo.getNameInfo(names);
        }
        System.out.println(nameInfo);
    }

    private void initPunishList(String path) {
        String[] names = GetCRInfo.getInfo(path);

        if (names != null) {
            punishInfo = GetCRInfo.getPunishInfo(names);
        }
        System.out.println(punishInfo);
    }
}
