package calculator;

import java.util.Objects;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CalculatorApp extends Application {
    private static final String WINDOW_TITLE = "A11423011 Simple Calculator";
    private static final String APP_TITLE = "A11423011\nSimple Calculator";

    private final CalculatorModel calculator = new CalculatorModel();
    private Label displayLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.getStyleClass().add("calculator-root");
        root.setPadding(new Insets(14));

        Label titleLabel = new Label(APP_TITLE);
        titleLabel.getStyleClass().add("app-title");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);

        displayLabel = new Label(calculator.getDisplay());
        displayLabel.getStyleClass().add("display");
        displayLabel.setMaxWidth(Double.MAX_VALUE);
        displayLabel.setAlignment(Pos.CENTER_RIGHT);
        displayLabel.setTextOverrun(OverrunStyle.LEADING_ELLIPSIS);

        GridPane keypad = createKeypad();
        VBox.setVgrow(keypad, Priority.ALWAYS);

        root.getChildren().addAll(titleLabel, displayLabel, keypad);

        Scene scene = new Scene(root, 320, 480);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);

        stage.setTitle(WINDOW_TITLE);
        stage.setMinWidth(340);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.show();
    }

    private GridPane createKeypad() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("keypad");
        grid.setHgap(10);
        grid.setVgap(10);

        for (int column = 0; column < 4; column++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setHgrow(Priority.ALWAYS);
            constraints.setFillWidth(true);
            constraints.setPercentWidth(25);
            grid.getColumnConstraints().add(constraints);
        }

        for (int row = 0; row < 4; row++) {
            RowConstraints constraints = new RowConstraints();
            constraints.setVgrow(Priority.ALWAYS);
            constraints.setFillHeight(true);
            grid.getRowConstraints().add(constraints);
        }

        String[][] buttons = {
                {"7", "8", "9", "+"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "*"},
                {"C", "0", "=", "/"}
        };

        for (int row = 0; row < buttons.length; row++) {
            for (int column = 0; column < buttons[row].length; column++) {
                addButton(grid, buttons[row][column], column, row);
            }
        }

        return grid;
    }

    private void addButton(GridPane grid, String label, int column, int row) {
        Button button = new Button(label);
        button.getStyleClass().add("calculator-button");
        if ("C".equals(label)) {
            button.getStyleClass().add("clear-button");
        }
        button.setMnemonicParsing(false);
        button.setAlignment(Pos.CENTER);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setMinSize(58, 58);
        button.setFocusTraversable(false);
        button.setOnAction(event -> {
            handleInput(label);
            refreshDisplay();
        });
        grid.add(button, column, row);
    }

    private void handleInput(String input) {
        switch (input) {
            case "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> calculator.pressDigit(input.charAt(0));
            case "+", "-", "*", "/" -> calculator.chooseOperator(input);
            case "=" -> calculator.calculate();
            case "C" -> calculator.clear();
            default -> {
            }
        }
    }

    private void handleKeyPress(KeyEvent event) {
        String text = event.getText();

        if (text != null && text.length() == 1) {
            char value = text.charAt(0);
            if (Character.isDigit(value) || "+-*/=".indexOf(value) >= 0) {
                handleInput(String.valueOf(value));
                refreshDisplay();
                event.consume();
                return;
            }
        }

        if (event.getCode() == KeyCode.ENTER) {
            handleInput("=");
        } else if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.DELETE) {
            handleInput("C");
        } else {
            return;
        }

        refreshDisplay();
        event.consume();
    }

    private void refreshDisplay() {
        displayLabel.setText(calculator.getDisplay());
    }
}
