package io.github.et.reminder;


import io.github.et.reminder.component.WrongCfgException;
import io.github.et.reminder.threads.ServerStream;
import io.github.et.reminder.threads.TimedOperation;
import io.github.et.reminder.util.JsonReader;
import io.github.et.reminder.util.TTS;
import io.github.et.reminder.util.XlsxReaderKt;
import io.github.et.reminder.util.component.concrete.AppearancePane;
import io.github.et.reminder.util.component.concrete.ContentPane;
import io.github.et.reminder.util.component.concrete.UpdatePane;
import io.github.et.reminder.util.component.concrete.VoicePane;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

import static io.github.et.reminder.threads.ServerStream.pw;
import static io.github.et.reminder.util.JsonReader.*;

public class MainApplication extends Application {
    public static javafx.scene.control.Label lastOn = null;
    public String serverAddress = "127.0.0.1";
    public final int portNumber = 5738;

    public Socket socket = null;
    public BufferedReader in = null;
    public PrintWriter out = null;

    public static ImageView imgView = new ImageView();
    public static ImageView imgView1 = new ImageView();
    public static javafx.scene.control.Label mover=new javafx.scene.control.Label("≡");
    public static double X=0;
    public static double Y=0;
    @Override
    public void start(Stage stage) throws IOException {
        socket = new Socket(serverAddress, portNumber);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        stage.setAlwaysOnTop(true);
        stage.initStyle(StageStyle.TRANSPARENT);
        AnchorPane root = new AnchorPane();
        AnchorPane root1 = new AnchorPane();
        AnchorPane root2 = new AnchorPane();
        UpdatePane updatePane = new UpdatePane(stage);
        VoicePane voicePane = new VoicePane();
        ContentPane contentPane = new ContentPane(stage);
        AppearancePane appearancePane = new AppearancePane(stage);
        root.setOnMouseClicked(event -> {
            if(getAlwaysOnTop()) {
                stage.setAlwaysOnTop(true);
            }
        });
        root.setPrefHeight(700);
        root.setPrefWidth(900);
        root1.setPrefWidth(900);
        root1.setPrefHeight(700);
        root2.setPrefWidth(900);
        root2.setPrefHeight(700);
        stage.setHeight(700);
        stage.setWidth(900);
        javafx.scene.control.Button setting = new javafx.scene.control.Button("设置");
        javafx.scene.control.Button play = new javafx.scene.control.Button("播放提醒");
        ImageView exit = new ImageView(new javafx.scene.image.Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/io/github/et/reminder/exit.jpeg"))));
        exit.setFitHeight(30d);
        exit.setFitWidth(30d);
        exit.setOnMouseClicked(event -> {
            stage.hide();
            try {
                socket = new Socket(serverAddress, portNumber);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                String serverResponse;
                while ((serverResponse = in.readLine()) != null) {
                    if (serverResponse.equals("exit")) {
                        stage.show();
                        break;
                    }
                }
            } catch (IOException ignored) {
            }

        });
        play.setFont(javafx.scene.text.Font.loadFont(new FileInputStream("./zh-cn.ttf"), 30));
        AnchorPane.setBottomAnchor(play, 110d);
        AnchorPane.setTopAnchor(play, 600d);
        AnchorPane.setRightAnchor(play, 30d);
        AnchorPane.setLeftAnchor(play, stage.getWidth() / 2);
        play.setOnAction(e -> {
            int num=XlsxReaderKt.getContent().toArray().length;
            StringBuilder a = new StringBuilder();
            ArrayList<String> temp = XlsxReaderKt.getContent();
            for (int i = 0; i < num; i++) {
                a.append(temp.get(i)).append("；");
            }
            try {
                TTS.playTextToSpeech(a.toString().replace("\t", ":"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        setting.setFont(javafx.scene.text.Font.loadFont(new FileInputStream("./zh-cn.ttf"), 30));
        AnchorPane.setTopAnchor(setting, 600d);
        AnchorPane.setBottomAnchor(setting, 110d);
        AnchorPane.setLeftAnchor(setting, 30d);
        AnchorPane.setRightAnchor(setting, stage.getWidth() / 2);

        AnchorPane inner = new AnchorPane();
        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(inner);
        Text text = new Text();
        root1.getChildren().add(scrollPane);
        scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefHeight(500d);
        text.setFont(javafx.scene.text.Font.loadFont(new FileInputStream("./zh-cn.ttf"), 50));
        inner.getChildren().add(text);
        AnchorPane.setLeftAnchor(text, 10d);
        AnchorPane.setTopAnchor(text, 0d);
        AnchorPane.setTopAnchor(scrollPane, 75d);
        AnchorPane.setLeftAnchor(scrollPane, 50d);
        AnchorPane.setRightAnchor(scrollPane, 50d);


        AnchorPane sidePane = new AnchorPane();
        javafx.scene.control.ScrollPane settingsPane = new javafx.scene.control.ScrollPane();
        settingsPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
        settingsPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
        AnchorPane.setLeftAnchor(sidePane, 0d);
        AnchorPane.setRightAnchor(sidePane, 700d);
        AnchorPane.setTopAnchor(sidePane, 100d);
        AnchorPane.setBottomAnchor(sidePane, 120d);
        AnchorPane.setLeftAnchor(settingsPane, 200d);
        AnchorPane.setTopAnchor(settingsPane, 100d);
        AnchorPane.setRightAnchor(settingsPane, 30d);
        AnchorPane.setBottomAnchor(settingsPane, 120d);
        javafx.scene.control.Label back = new javafx.scene.control.Label("< 返回");
        AnchorPane.setLeftAnchor(back, 30d);
        AnchorPane.setTopAnchor(back, 30d);
        javafx.scene.control.Label content = new javafx.scene.control.Label("内容");
        javafx.scene.control.Label tts = new javafx.scene.control.Label("语音");
        javafx.scene.control.Label appearance = new javafx.scene.control.Label("外观");
        javafx.scene.control.Label update = new javafx.scene.control.Label("通用");
        content.setFont(javafx.scene.text.Font.loadFont(new FileInputStream("./zh-cn.ttf"), 40));
        back.setFont(javafx.scene.text.Font.loadFont(new FileInputStream("./zh-cn.ttf"), 20));
        tts.setFont(javafx.scene.text.Font.loadFont(new FileInputStream("./zh-cn.ttf"), 40));
        appearance.setFont(javafx.scene.text.Font.loadFont(new FileInputStream("./zh-cn.ttf"), 40));
        update.setFont(javafx.scene.text.Font.loadFont(new FileInputStream("./zh-cn.ttf"), 40));
        content.setAlignment(Pos.CENTER);
        tts.setAlignment(Pos.CENTER);
        appearance.setAlignment(Pos.CENTER);
        update.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(content, 150d);
        AnchorPane.setLeftAnchor(content, 0d);
        AnchorPane.setRightAnchor(content, 0d);
        AnchorPane.setTopAnchor(tts, 50d);
        AnchorPane.setLeftAnchor(tts, 0d);
        AnchorPane.setRightAnchor(tts, 0d);
        AnchorPane.setTopAnchor(appearance, 100d);
        AnchorPane.setLeftAnchor(appearance, 0d);
        AnchorPane.setRightAnchor(appearance, 0d);
        AnchorPane.setTopAnchor(update, 0d);
        AnchorPane.setLeftAnchor(update, 0d);
        AnchorPane.setRightAnchor(update, 0d);
        back.setAlignment(Pos.CENTER);

        javafx.scene.control.Button confirm = new javafx.scene.control.Button("确定");
        javafx.scene.control.Button cancel = new javafx.scene.control.Button("取消");
        javafx.scene.control.Label msg = new javafx.scene.control.Label();
        AnchorPane.setTopAnchor(msg, 630d);
        AnchorPane.setLeftAnchor(msg, 10d);
        msg.setFont(javafx.scene.text.Font.loadFont(new FileInputStream("./zh-cn.ttf"), 20));
        settingsPane.setStyle(
                "-fx-background-color:white;" +
                        "-fx-border-style:solid"
        );
        confirm.setStyle(
                "-fx-background-color:#00BFFF;" +
                        "-fx-background-radius:20;" +
                        "-fx-text-fill:white;" +
                        "-fx-border-radius:20"
        );
        cancel.setStyle(
                "-fx-background-color:black;" +
                        "-fx-background-radius:20;" +
                        "-fx-text-fill:white;" +
                        "-fx-border-radius:20"
        );
        sidePane.setStyle(
                "-fx-background-color:white"
        );
        content.setStyle(
                "-fx-text-fill:#00BFFF;" +
                        "-fx-background-color:white"
        );
        tts.setStyle(
                "-fx-text-fill:#00BFFF;" +
                        "-fx-background-color:white"
        );
        appearance.setStyle(
                "-fx-text-fill:#00BFFF;" +
                        "-fx-background-color:white"
        );
        update.setStyle(
                "-fx-text-fill:#00BFFF;" +
                        "-fx-background-color:white"
        );


        lastOn = update;
        update.setStyle(
                "-fx-background-color:#00BFFF;" +
                        "-fx-text-fill:white"
        );
        settingsPane.setContent(updatePane);
        content.setOnMouseClicked(event -> {
            content.setStyle(
                    "-fx-background-color:#00BFFF;" +
                            "-fx-text-fill:white"
            );
            if (!lastOn.equals(content)) {
                lastOn.setStyle(
                        "-fx-text-fill:#00BFFF;" +
                                "-fx-background-color:white"
                );
                lastOn = content;
                settingsPane.setContent(contentPane);
            }

        });
        tts.setOnMouseClicked(event -> {
            tts.setStyle(
                    "-fx-background-color:#00BFFF;" +
                            "-fx-text-fill:white"
            );
            if (!lastOn.equals(tts)) {
                lastOn.setStyle(
                        "-fx-text-fill:#00BFFF;" +
                                "-fx-background-color:white"
                );
                lastOn = tts;
                settingsPane.setContent(voicePane);
            }

        });
        appearance.setOnMouseClicked(event -> {
            appearance.setStyle(
                    "-fx-background-color:#00BFFF;" +
                            "-fx-text-fill:white"
            );
            if (!lastOn.equals(appearance)) {
                lastOn.setStyle(
                        "-fx-text-fill:#00BFFF;" +
                                "-fx-background-color:white"
                );
                lastOn = appearance;
                settingsPane.setContent(appearancePane);
            }

        });
        update.setOnMouseClicked(event -> {
            update.setStyle(
                    "-fx-background-color:#00BFFF;" +
                            "-fx-text-fill:white"
            );
            if (!lastOn.equals(update)) {
                lastOn.setStyle(
                        "-fx-text-fill:#00BFFF;" +
                                "-fx-background-color:white"
                );
                lastOn = update;
                settingsPane.setContent(updatePane);
            }
        });
        confirm.setFont(javafx.scene.text.Font.loadFont(new FileInputStream("./zh-cn.ttf"), 30));
        cancel.setFont(Font.loadFont(new FileInputStream("./zh-cn.ttf"), 30));
        AnchorPane.setBottomAnchor(confirm, 30d);
        AnchorPane.setTopAnchor(confirm, 600d);
        AnchorPane.setRightAnchor(confirm, 180d);
        AnchorPane.setBottomAnchor(cancel, 30d);
        AnchorPane.setTopAnchor(cancel, 600d);
        AnchorPane.setRightAnchor(cancel, 60d);


        sidePane.getChildren().add(content);
        sidePane.getChildren().add(tts);
        sidePane.getChildren().add(appearance);
        sidePane.getChildren().add(update);


        root.getChildren().add(root2);
        root2.getChildren().add(sidePane);
        root2.getChildren().add(settingsPane);
        root.getChildren().add(root1);
        root2.getChildren().add(back);
        AnchorPane.setLeftAnchor(root1, 0d);
        AnchorPane.setTopAnchor(root1, 0d);
        AnchorPane.setLeftAnchor(root1, 0d);
        AnchorPane.setRightAnchor(root1, 0d);
        root2.setDisable(true);

        TranslateTransition tt1 = new TranslateTransition(new Duration(300d), root1);
        TranslateTransition tt2 = new TranslateTransition(new Duration(300d), root2);
        setting.setOnAction(event -> {
            root1.setDisable(true);
            root2.setDisable(false);
            tt1.setByX(-1118);
            tt2.setByX(-1118);
            tt1.play();
            tt2.play();
        });
        back.setOnMouseClicked(event -> {
            root2.setDisable(true);
            root1.setDisable(false);
            tt1.setByX(1118);
            tt2.setByX(1118);
            tt1.play();
            tt2.play();
        });
        cancel.setOnAction(event -> {
            root2.setDisable(true);
            root1.setDisable(false);
            tt1.setByX(1118);
            tt2.setByX(1118);
            tt1.play();
            tt2.play();
        });
        confirm.setOnAction(event -> {
            try {
                try {
                    msg.setTextFill(javafx.scene.paint.Color.BLACK);
                    msg.setText("更新配置中...");
                    updatePane.updateCfg();
                    appearancePane.updateCfg();
                } catch (WrongCfgException e) {
                    appearance.setStyle(
                            "-fx-background-color:#00BFFF;" +
                                    "-fx-text-fill:white"
                    );
                    if (!lastOn.equals(appearance)) {
                        lastOn.setStyle(
                                "-fx-text-fill:#00BFFF;" +
                                        "-fx-background-color:white"
                        );
                        lastOn = appearance;
                        settingsPane.setContent(appearancePane);
                    }
                    msg.setTextFill(javafx.scene.paint.Color.RED);
                    msg.setText(e.getMessage());

                    throw e;
                }
                try {
                    contentPane.updateCfg();
                } catch (WrongCfgException e) {
                    content.setStyle(
                            "-fx-background-color:#00BFFF;" +
                                    "-fx-text-fill:white"
                    );
                    if (!lastOn.equals(content)) {
                        lastOn.setStyle(
                                "-fx-text-fill:#00BFFF;" +
                                        "-fx-background-color:white"
                        );
                        lastOn = content;
                        settingsPane.setContent(contentPane);
                    }
                    msg.setTextFill(javafx.scene.paint.Color.RED);
                    msg.setText(e.getMessage());

                    throw e;
                }
                try {
                    voicePane.updateCfg();
                } catch (WrongCfgException e) {
                    tts.setStyle(
                            "-fx-background-color:#00BFFF;" +
                                    "-fx-text-fill:white"
                    );
                    if (!lastOn.equals(tts)) {
                        lastOn.setStyle(
                                "-fx-text-fill:#00BFFF;" +
                                        "-fx-background-color:white"
                        );
                        lastOn = tts;
                        settingsPane.setContent(voicePane);
                    }
                    msg.setTextFill(javafx.scene.paint.Color.RED);
                    msg.setText(e.getMessage());

                    throw e;
                }
                update();
                msg.setTextFill(Color.BLACK);
                msg.setText("配置更新完成！");
            } catch (Exception ignored) {
            }

        });

        try {
            String bgImagePath = io.github.et.reminder.util.BackgroundImage.getPath();
            if (!bgImagePath.equals("")) {
                javafx.scene.image.Image img = new javafx.scene.image.Image(bgImagePath);
                imgView.setImage(img);
                imgView.setOpacity(0.2);
                final double ratio = img.getWidth() / img.getHeight();
                imgView.setPreserveRatio(true);
                imgView.setFitHeight(stage.getHeight());
                imgView.setFitWidth(stage.getHeight() * ratio);
                imgView.setMouseTransparent(true);
                AnchorPane.setLeftAnchor(imgView, stage.getWidth() / 2 - imgView.getFitWidth() / 2 - 30);

                imgView1.setImage(img);
                imgView1.setOpacity(0.2);
                imgView1.setPreserveRatio(true);
                imgView1.setFitHeight(stage.getHeight());
                AnchorPane.setLeftAnchor(imgView1, stage.getWidth() / 2 - imgView.getFitWidth() / 2 - 30);
                imgView1.setFitWidth(stage.getHeight() * ratio);
                imgView1.setMouseTransparent(true);


            }
        } catch (Exception ignored) {
        }
        AnchorPane.setRightAnchor(exit, 30d);
        AnchorPane.setTopAnchor(exit, 30d);
        AnchorPane.setLeftAnchor(root2, 1118d);
        AnchorPane.setRightAnchor(root2, -1118d);
        AnchorPane.setTopAnchor(root2, 0d);
        AnchorPane.setBottomAnchor(root2, 0d);
        root1.getChildren().add(imgView);
        root2.getChildren().add(imgView1);
        root1.getChildren().add(setting);
        root1.getChildren().add(play);
        root.getChildren().add(exit);
        root2.getChildren().add(confirm);
        root2.getChildren().add(cancel);
        root2.getChildren().add(msg);
        Scene scene = new Scene(root);
        stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/io/github/et/reminder/OIP-C.jpeg"))));
        stage.setScene(scene);
        stage.setOnShown(event -> {
            if(!getAlwaysOnTop()) {
                stage.setAlwaysOnTop(false);
            }
            int num=XlsxReaderKt.getContent().toArray().length;
            msg.setText("");
            StringBuilder a = new StringBuilder();
            ArrayList<String> temp = XlsxReaderKt.getContent();
            for (int i = 0; i < num; i++) {
                if (i == num - 1) {
                    a.append(temp.get(i));
                } else {
                    a.append(temp.get(i)).append("\n");
                }
            }
            text.setText(a.toString());
            play.setDisable(a.toString().equals("今日无值日生安排"));
            settingsPane.setContent(updatePane);
            update.setStyle(
                    "-fx-background-color:#00BFFF;" +
                            "-fx-text-fill:white"
            );
            if (!lastOn.equals(update)) {
                lastOn.setStyle(
                        "-fx-text-fill:#00BFFF;" +
                                "-fx-background-color:white"
                );
                lastOn = update;
            }
            root1.setTranslateX(0d);
            root2.setTranslateX(0d);
            root2.setDisable(true);
            root1.setDisable(false);
        });

        AnchorPane.setLeftAnchor(mover,200d);
        AnchorPane.setTopAnchor(mover,10d);
        AnchorPane.setRightAnchor(mover,200d);
        mover.setFont(Font.loadFont(new FileInputStream("./zh-cn.ttf"), 30));
        mover.setAlignment(Pos.CENTER);
        root.getChildren().add(mover);
        if(!JsonReader.isMovable()){
            mover.setVisible(false);
        }
        mover.addEventHandler(MouseEvent.ANY,e->{
            e.consume();
            if(e.getEventType()==MouseEvent.MOUSE_PRESSED){
                X=e.getSceneX();
                Y = e.getSceneY();
            }else if(e.getEventType()==MouseEvent.MOUSE_DRAGGED){
                stage.setX(e.getScreenX()-X);
                if(e.getScreenY()-Y<0){
                    stage.setY(0);
                }else {
                    stage.setY(e.getScreenY() - Y);
                }
            }
        });

        try {
            String serverResponse;
            while ((serverResponse = in.readLine()) != null) {
                if (serverResponse.equals("exit")) {
                    stage.show();
                    break;
                }
            }
        } catch (IOException ignored) {
        }
    }





    public static void main(String[] args) throws IOException {
        new Thread(new ServerStream()).start();
        new Thread(new TimedOperation()).start();
        createSystemTrayIcon();
        launch(args);

    }

    private static void createSystemTrayIcon() throws IOException {
        MenuItem exitItem=new MenuItem("Exit");
        MenuItem showItem=new MenuItem("Show");
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("./OIP-C.jpeg");
            ActionListener showListener = e -> {
                pw.println("exit");
            };
            ActionListener exitListener = e -> {
                System.exit(0);
            };
            TrayIcon trayIcon = new TrayIcon(image, "Reminder");
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(showListener);
            showItem.addActionListener(showListener);
            exitItem.addActionListener(exitListener);
            PopupMenu pop=new PopupMenu();
            pop.add(showItem);
            pop.add(exitItem);
            trayIcon.setPopupMenu(pop);


            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

}
