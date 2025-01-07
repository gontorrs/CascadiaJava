package fr.uge.graphic_game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.uge.DataGame.GamingBoard;
import fr.uge.DataGame.Animal;
import fr.uge.DataGame.Habitat;
import fr.uge.DataGame.Player;

public class GraphicalGame {

    private GamingBoard gameBoard;

    public GraphicalGame(GamingBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void createAndShowGUI() {
        // Create a window (JFrame)
        JFrame frame = new JFrame("Animal Collage");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the panel that will hold the images
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCollage(g);
            }
        };

        frame.add(panel);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    private void drawCollage(Graphics g) {
        // Seleccionamos un animal y un hábitat aleatorio
        Animal animal = Animal.FOX; // O puedes seleccionar un animal aleatorio
        Habitat habitat = Habitat.FOREST; // O seleccionar un hábitat aleatorio

        // Obtener las imágenes del collage
        List<Image> images = gameBoard.getCollageImages(animal, habitat);

        // Dibujar las imágenes una encima de la otra (apiladas verticalmente)
        int y = 10; // Coordenada inicial Y
        for (Image image : images) {
            g.drawImage(image, 10, y, 100, 100, null);
            y += 110; // Asegúrate de que cada imagen esté separada
        }
    }

    public static void main(String[] args) {
        // Crear la instancia de GamingBoard
        GamingBoard gameBoard = new GamingBoard();

        // Crear la interfaz gráfica
        GraphicalGame graphicalGame = new GraphicalGame(gameBoard);
        graphicalGame.createAndShowGUI();
    }
}
