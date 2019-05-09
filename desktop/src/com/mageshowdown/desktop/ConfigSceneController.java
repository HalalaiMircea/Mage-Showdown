package com.mageshowdown.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.mageshowdown.gameclient.ClientAssetLoader.prefs;

public class ConfigSceneController implements Initializable {
    @FXML
    private ChoiceBox<String> modeChoiceBox;
    @FXML
    private ChoiceBox<String> rezChoiceBox;
    @FXML
    private CheckBox vsyncCheckBox;
    @FXML
    private CheckBox glCheckBox;
    @FXML
    private TextField backFpsField;
    @FXML
    private TextField foreFpsField;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    @FXML
    void okBtnClicked(ActionEvent actionEvent) {
        String[] str = rezChoiceBox.getValue().split("x");
        prefs.putString("width", str[0]);
        prefs.putString("height", str[1]);
        if (modeChoiceBox.getValue().equals("Fullscreen"))
            prefs.putBoolean("fullscreen", true);
        else if (modeChoiceBox.getValue().equals("Windowed"))
            prefs.putBoolean("fullscreen", false);
        prefs.putInteger("foregroundFPS", Integer.parseInt(foreFpsField.getText()));
        prefs.putInteger("backgroundFPS", Integer.parseInt(backFpsField.getText()));
        prefs.putBoolean("vSyncEnabled", vsyncCheckBox.isSelected());
        prefs.putBoolean("useGL30", glCheckBox.isSelected());
        prefs.flush();
        DesktopClientLauncher.mainStage.setScene(DesktopClientLauncher.mainScene);
    }

    @FXML
    void cancelBtnClicked(ActionEvent actionEvent) {
        DesktopClientLauncher.mainStage.setScene(DesktopClientLauncher.mainScene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Graphics.DisplayMode> optimalDisplayModes = new ArrayList<Graphics.DisplayMode>();
        for (Graphics.DisplayMode each : LwjglApplicationConfiguration.getDisplayModes()) {
            float aspectNum = ((float) each.width / (float) each.height) * 9f;
            if (each.width >= 1280 && each.height >= 720 && aspectNum >= 15.9f && aspectNum <= 16.1f)
                optimalDisplayModes.add(each);
        }
        ArrayList<String> strResolutions = new ArrayList<String>();
        for (Graphics.DisplayMode each : optimalDisplayModes) {
            strResolutions.add(each.width + "x" + each.height);
        }
        rezChoiceBox.getItems().setAll(strResolutions);
        modeChoiceBox.getItems().setAll("Fullscreen", "Windowed");

        rezChoiceBox.setValue(prefs.getInteger("width") + "x" + prefs.getInteger("height"));
        if (prefs.getBoolean("fullscreen"))
            modeChoiceBox.setValue("Fullscreen");
        else if (!prefs.getBoolean("fullscreen"))
            modeChoiceBox.setValue("Windowed");

        foreFpsField.setText(String.valueOf(prefs.getInteger("foregroundFPS")));
        backFpsField.setText(String.valueOf(prefs.getInteger("backgroundFPS")));
        vsyncCheckBox.setSelected(prefs.getBoolean("vSyncEnabled"));
        glCheckBox.setSelected(prefs.getBoolean("useGL30"));
    }
}
