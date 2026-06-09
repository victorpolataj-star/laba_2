package sample;

import javafx.beans.property.*;

public class GameModel {
    public DoubleProperty x = new SimpleDoubleProperty();
    public DoubleProperty y = new SimpleDoubleProperty();
    public IntegerProperty score = new SimpleIntegerProperty(0);
    public BooleanProperty gameActive = new SimpleBooleanProperty(true);
    public DoubleProperty speed = new SimpleDoubleProperty(200.0);
}