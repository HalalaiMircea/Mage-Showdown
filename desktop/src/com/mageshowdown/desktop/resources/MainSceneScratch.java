
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MainSceneScratch extends AnchorPane {

    private final VBox vBox;
    private final Button playButton;
    private final Button configButton;
    private final Button exitButton;

    public MainSceneScratch() {

        vBox = new VBox();
        playButton = new Button();
        configButton = new Button();
        exitButton = new Button();

        setPrefHeight(400.0);
        setPrefWidth(835.0);

        AnchorPane.setRightAnchor(vBox, 0.0);
        AnchorPane.setTopAnchor(vBox, 0.0);
        vBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        vBox.setLayoutX(735.0);
        vBox.setLayoutY(100.0);
        vBox.setPrefHeight(200.0);
        vBox.setPrefWidth(118.0);
        vBox.setSpacing(20.0);

        playButton.setMnemonicParsing(false);
        playButton.setText("PLAY");
        playButton.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        playButton.setFont(new Font("System Bold", 16.0));
        playButton.setCursor(Cursor.HAND);

        configButton.setMnemonicParsing(false);
        configButton.setText("CONFIGURE");

        exitButton.setMnemonicParsing(false);
        exitButton.setText("EXIT");
        vBox.setPadding(new Insets(20.0, 20.0, 0.0, 0.0));

        vBox.getChildren().add(playButton);
        vBox.getChildren().add(configButton);
        vBox.getChildren().add(exitButton);
        getChildren().add(vBox);

    }
}
