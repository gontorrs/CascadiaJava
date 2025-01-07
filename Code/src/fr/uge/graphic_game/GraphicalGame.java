package fr.uge.graphic_game;
import fr.uge.controllerGame.*;

import zen.Zen;
import zen.display.ZenFrame;
import zen.display.ZenPanel;
import zen.text.ZenGraphics;
import fr.uge.DataGame.Tile;
import fr.uge.DataGame.GamingBoard;
import fr.uge.DataGame.Player;
import fr.uge.DataGame.Position;
import java.util.List;

public class GraphicalGame {

    private GamingBoard gb1;
    private GamingBoard gb2;

    public GraphicalGame(GamingBoard gb1, GamingBoard gb2) {
        this.gb1 = gb1;
        this.gb2 = gb2;
    }

    // Método para iniciar la ventana gráfica
    public void start() {
        Zen.init(); // Inicializa Zen

        ZenFrame frame = new ZenFrame("Cascadia Game - Graphical Mode");
        ZenPanel panel = new ZenPanel();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setVisible(true);

        // Llamada al método para dibujar el tablero
        panel.draw(() -> {
            drawBoard(gb1, gb2, 50, 50); // Muestra los azulejos en el panel
        });
    }

    // Dibuja el tablero
    public void drawBoard(GamingBoard gb1, GamingBoard gb2, int startX, int startY) {
        ZenGraphics g = ZenGraphics.getGraphics();

        // Obtén los azulejos del tablero
        List<Tile> tiles = gb1.getBoardMap().values().stream().toList();

        // Dibuja cada azulejo en el tablero
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            int x = startX + (i % 5) * 100; // Ajusta la posición horizontal
            int y = startY + (i / 5) * 100; // Ajusta la posición vertical

            // Dibuja el fondo del tile
            g.setColor("lightgray");
            g.fillRect(x, y, 80, 80); // Dibuja un rectángulo para el tile

            // Dibuja la representación textual del tile
            String tileString = tile.toString();
            g.setColor("black");
            g.drawString(tileString, x + 10, y + 10); // Dibuja el texto dentro del tile
        }
    }
}
