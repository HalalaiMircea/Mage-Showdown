
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class ConfigSceneScratch extends AnchorPane {

    private final Label label;
    private final ChoiceBox modeChoiceBox;
    private final ChoiceBox rezChoiceBox;
    private final Button applyButton;
    private final Button cancelButton;
    private final Label label0;
    private final Label label1;
    private final CheckBox vsyncCheckBox;
    private final CheckBox glCheckBox;
    private final TextField backFpsField;
    private final TextField foreFpsField;
    private final Label label2;

    public ConfigSceneScratch() {

        label = new Label();
        modeChoiceBox = new ChoiceBox();
        rezChoiceBox = new ChoiceBox();
        applyButton = new Button();
        cancelButton = new Button();
        label0 = new Label();
        label1 = new Label();
        vsyncCheckBox = new CheckBox();
        glCheckBox = new CheckBox();
        backFpsField = new TextField();
        foreFpsField = new TextField();
        label2 = new Label();

        setPrefHeight(400.0);
        setPrefWidth(835.0);

        label.setLayoutX(401.0);
        label.setLayoutY(204.0);
        label.setText("BackgroundFPS");

        modeChoiceBox.setLayoutX(296.0);
        modeChoiceBox.setLayoutY(73.0);
        modeChoiceBox.setPrefHeight(25.0);
        modeChoiceBox.setPrefWidth(244.0);

        rezChoiceBox.setLayoutX(283.0);
        rezChoiceBox.setLayoutY(34.0);
        rezChoiceBox.setPrefHeight(25.0);
        rezChoiceBox.setPrefWidth(269.0);

        applyButton.setLayoutX(628.0);
        applyButton.setLayoutY(356.0);
        applyButton.setMnemonicParsing(false);
        applyButton.setPrefHeight(30.0);
        applyButton.setPrefWidth(90.0);
        applyButton.setText("Apply");

        cancelButton.setLayoutX(731.0);
        cancelButton.setLayoutY(356.0);
        cancelButton.setMnemonicParsing(false);
        cancelButton.setPrefHeight(30.0);
        cancelButton.setPrefWidth(90.0);
        cancelButton.setText("Cancel");

        label0.setLayoutX(210.0);
        label0.setLayoutY(29.0);
        label0.setPrefHeight(36.0);
        label0.setPrefWidth(68.0);
        label0.setText("Resolution");
        label0.setFont(new Font(14.0));

        label1.setLayoutX(210.0);
        label1.setLayoutY(77.0);
        label1.setText("Display Mode");

        vsyncCheckBox.setLayoutX(210.0);
        vsyncCheckBox.setLayoutY(161.0);
        vsyncCheckBox.setMnemonicParsing(false);
        vsyncCheckBox.setPrefHeight(25.0);
        vsyncCheckBox.setPrefWidth(119.0);
        vsyncCheckBox.setText("Vertical Sync");

        glCheckBox.setLayoutX(210.0);
        glCheckBox.setLayoutY(198.0);
        glCheckBox.setMnemonicParsing(false);
        glCheckBox.setPrefHeight(30.0);
        glCheckBox.setPrefWidth(182.0);
        glCheckBox.setText("UseGL30 (Experimental)");

        backFpsField.setLayoutX(499.0);
        backFpsField.setLayoutY(200.0);
        backFpsField.setPrefHeight(25.0);
        backFpsField.setPrefWidth(83.0);

        foreFpsField.setLayoutX(499.0);
        foreFpsField.setLayoutY(160.0);
        foreFpsField.setPrefHeight(25.0);
        foreFpsField.setPrefWidth(83.0);

        label2.setLayoutX(401.0);
        label2.setLayoutY(164.0);
        label2.setText("ForegroundFPS");

        getChildren().add(label);
        getChildren().add(modeChoiceBox);
        getChildren().add(rezChoiceBox);
        getChildren().add(applyButton);
        getChildren().add(cancelButton);
        getChildren().add(label0);
        getChildren().add(label1);
        getChildren().add(vsyncCheckBox);
        getChildren().add(glCheckBox);
        getChildren().add(backFpsField);
        getChildren().add(foreFpsField);
        getChildren().add(label2);

    }
}
