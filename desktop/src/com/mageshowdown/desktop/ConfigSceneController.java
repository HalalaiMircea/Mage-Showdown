package com.mageshowdown.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mageshowdown.utils.PrefsKeys;
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
import java.util.TreeSet;

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
        prefs.putString(PrefsKeys.WIDTH, str[0]);
        prefs.putString(PrefsKeys.HEIGHT, str[1]);
        if (modeChoiceBox.getValue().equals("Fullscreen"))
            prefs.putBoolean(PrefsKeys.FULLSCREEN, true);
        else if (modeChoiceBox.getValue().equals("Windowed"))
            prefs.putBoolean(PrefsKeys.FULLSCREEN, false);
        prefs.putInteger(PrefsKeys.FOREGROUNDFPS, Integer.parseInt(foreFpsField.getText()));
        prefs.putInteger(PrefsKeys.BACKGROUNDFPS, Integer.parseInt(backFpsField.getText()));
        prefs.putBoolean(PrefsKeys.VSYNC, vsyncCheckBox.isSelected());
        prefs.putBoolean(PrefsKeys.USEGL30, glCheckBox.isSelected());
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
        TreeSet<String> strResolutions = new TreeSet<String>();
        for (Graphics.DisplayMode each : optimalDisplayModes) {
            strResolutions.add(each.width + "x" + each.height);
        }
        rezChoiceBox.getItems().setAll(strResolutions);
        modeChoiceBox.getItems().setAll("Fullscreen", "Windowed");

        rezChoiceBox.setValue(prefs.getInteger(PrefsKeys.WIDTH) + "x" + prefs.getInteger(PrefsKeys.HEIGHT));
        if (prefs.getBoolean(PrefsKeys.FULLSCREEN))
            modeChoiceBox.setValue("Fullscreen");
        else
            modeChoiceBox.setValue("Windowed");

        foreFpsField.setText(String.valueOf(prefs.getInteger(PrefsKeys.FOREGROUNDFPS)));
        backFpsField.setText(String.valueOf(prefs.getInteger(PrefsKeys.BACKGROUNDFPS)));
        vsyncCheckBox.setSelected(prefs.getBoolean(PrefsKeys.VSYNC));
        glCheckBox.setSelected(prefs.getBoolean(PrefsKeys.USEGL30));
    }
}
