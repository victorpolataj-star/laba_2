package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML private Pane gamePane;
    @FXML private Circle ball;
    @FXML private Label scoreLabel;
    @FXML private Label statusLabel;
    @FXML private Label speedLabel;
    @FXML private Button newGameButton;

    // Переменные игры
    private int score = 0;
    private boolean gameActive = true;
    private double speed = 200.0;
    private double vx = 100;
    private double vy = 100;
    private double x = 400;
    private double y = 300;
    private double fieldWidth = 800;
    private double fieldHeight = 600;
    private final double radius = 25;
    private int hitCounter = 0;

    private Random random = new Random();
    private AnimationTimer timer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Инициализация контроллера");

        // Устанавливаем начальное положение шарика
        x = gamePane.getWidth() / 2;
        y = gamePane.getHeight() / 2;
        ball.setCenterX(x);
        ball.setCenterY(y);

        // Случайное начальное направление (переменная создаётся ОДИН раз)
        double startAngle = Math.random() * 2 * Math.PI;
        vx = speed * Math.cos(startAngle);
        vy = speed * Math.sin(startAngle);

        // Обновляем метки
        updateUI();

        // ========== ОБРАБОТЧИК ДВОЙНОГО КЛИКА (ПАУЗА) ==========
        ball.setOnMouseClicked(event -> {
            System.out.println("Клик по шарику! Количество кликов: " + event.getClickCount());

            if (event.getClickCount() == 2) {
                // ДВОЙНОЙ КЛИК - ПАУЗА
                gameActive = !gameActive;
                updateUI();
                System.out.println("Пауза переключена! Активна: " + gameActive);
            } else if (event.getClickCount() == 1) {
                // ОДИНАРНЫЙ КЛИК - ПОПАДАНИЕ
                if (gameActive) {
                    score++;
                    hitCounter++;
                    System.out.println("Попадание! Счёт: " + score + ", попаданий: " + hitCounter);

                    // Каждые 5 попаданий увеличиваем скорость
                    if (hitCounter % 5 == 0) {
                        speed = speed * 1.1;
                        if (speed > 500) speed = 500;
                        double currentAngle = Math.atan2(vy, vx);
                        vx = speed * Math.cos(currentAngle);
                        vy = speed * Math.sin(currentAngle);
                        System.out.println("Скорость увеличена до: " + speed);
                    }

                    // Меняем цвет шарика
                    ball.setFill(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                    updateUI();
                } else {
                    System.out.println("Игра на паузе, попадание не засчитано");
                }
            }
        });

        // ========== УБЕГАНИЕ ОТ МЫШИ ==========
        gamePane.setOnMouseMoved(event -> {
            if (gameActive) {
                double mouseX = event.getX();
                double mouseY = event.getY();
                double dx = x - mouseX;
                double dy = y - mouseY;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist < 100) {
                    double escapeAngle = Math.atan2(dy, dx);
                    vx = speed * Math.cos(escapeAngle);
                    vy = speed * Math.sin(escapeAngle);
                    System.out.println("Убегаем от мыши! Новое направление");
                }
            }
        });

        // ========== НОВАЯ ИГРА ==========
        newGameButton.setOnAction(event -> {
            System.out.println("Новая игра");
            score = 0;
            hitCounter = 0;
            speed = 200.0;
            gameActive = true;

            x = gamePane.getWidth() / 2;
            y = gamePane.getHeight() / 2;

            double newStartAngle = Math.random() * 2 * Math.PI;
            vx = speed * Math.cos(newStartAngle);
            vy = speed * Math.sin(newStartAngle);

            ball.setFill(Color.RED);
            updateUI();
        });

        // ========== ЗАПУСК ТАЙМЕРА ==========
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameActive) {
                    updatePosition();
                }
            }
        };
        timer.start();

        // ========== ОТСЛЕЖИВАНИЕ РАЗМЕРА ОКНА ==========
        gamePane.widthProperty().addListener((obs, oldVal, newVal) -> {
            fieldWidth = newVal.doubleValue();
            fixPosition();
        });
        gamePane.heightProperty().addListener((obs, oldVal, newVal) -> {
            fieldHeight = newVal.doubleValue();
            fixPosition();
        });

        fieldWidth = gamePane.getWidth();
        fieldHeight = gamePane.getHeight();

        System.out.println("Инициализация завершена");
        System.out.println("Радиус шарика: " + radius);
        System.out.println("Размер поля: " + fieldWidth + "x" + fieldHeight);
    }

    // Обновление позиции шарика
    private void updatePosition() {
        double delta = 0.016; // примерно 60 FPS

        x = x + vx * delta;
        y = y + vy * delta;

        // Отскоки от границ
        if (x - radius < 0) {
            x = radius;
            vx = -vx;
        }
        if (x + radius > fieldWidth) {
            x = fieldWidth - radius;
            vx = -vx;
        }
        if (y - radius < 0) {
            y = radius;
            vy = -vy;
        }
        if (y + radius > fieldHeight) {
            y = fieldHeight - radius;
            vy = -vy;
        }

        // Обновляем отображение
        ball.setCenterX(x);
        ball.setCenterY(y);
    }

    // Корректировка позиции при изменении размера окна
    private void fixPosition() {
        if (x - radius < 0) x = radius;
        if (x + radius > fieldWidth) x = fieldWidth - radius;
        if (y - radius < 0) y = radius;
        if (y + radius > fieldHeight) y = fieldHeight - radius;
        ball.setCenterX(x);
        ball.setCenterY(y);
    }

    // Обновление интерфейса
    private void updateUI() {
        scoreLabel.setText("Счёт: " + score);
        statusLabel.setText("Статус: " + (gameActive ? "Активна" : "ПАУЗА"));
        speedLabel.setText(String.format("Скорость: %.1f px/сек", speed));
    }

    public void stopGame() {
        if (timer != null) {
            timer.stop();
        }
    }
}