package fr.uge.graphic_game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import javax.imageio.ImageIO;

public class ImageLoader {
    private final BufferedImage[] animalImages, habitatImages, blankImages, userImages, numberImages, playerImages;

    public ImageLoader(String dir, String[] animals, String[] habitats, String blank, String[] userAnimals, String [] numbers, String [] players) {
        Objects.requireNonNull(animals);
        Objects.requireNonNull(habitats);
        Objects.requireNonNull(blank);
        Objects.requireNonNull(userAnimals);
        Objects.requireNonNull(numbers);
        Objects.requireNonNull(players);
        animalImages = new BufferedImage[animals.length];
        habitatImages = new BufferedImage[habitats.length];
        userImages = new BufferedImage[userAnimals.length];
        numberImages = new BufferedImage[numbers.length];
        playerImages = new BufferedImage[players.length];
        blankImages = new BufferedImage[1];
        for (var i = 0; i < animals.length; i++) {
            setImage(animalImages, i, dir, animals[i]);
        }
        for (var i = 0; i < habitats.length; i++) {
            setImage(habitatImages, i, dir, habitats[i]);
        }
        for (var i = 0; i < userAnimals.length; i++) {
            setImage(userImages, i, dir, userAnimals[i]);
        }
        for (var i = 0; i < numbers.length; i++) {
            setImage(numberImages, i, dir, numbers[i]);
        }
        for (var i = 0; i < players.length; i++) {
            setImage(playerImages, i, dir, players[i]);
        }
        setImage(blankImages, 0, dir, blank);
    }

    private void setImage(BufferedImage[] images, int position, String dirPath, String imagePath) {
        Objects.requireNonNull(dirPath);
        Objects.requireNonNull(imagePath);
        var path = Path.of(dirPath + "/" + imagePath);
        try (var input = Files.newInputStream(path)) {
            images[position] = ImageIO.read(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public BufferedImage animalImage(int id) {
        if (id < 0 || id >= animalImages.length) {
            return blankImg(); // Retorna la imagen en blanco si el índice es inválido
        }
        return animalImages[id];
    }
    public BufferedImage userImage(int id) {
        if (id < 0 || id >= animalImages.length) {
            return blankImg(); // Retorna la imagen en blanco si el índice es inválido
        }
        return userImages[id];
    }


    public BufferedImage habitatImage(int id) {
        if (id < 0 || id >= habitatImages.length) {
            return blankImg();
        }
        return habitatImages[id];
    }

    public BufferedImage blankImg() {
        return blankImages[0];
    }
    
    public BufferedImage numberImage(int id) {
        return numberImages[id];
    }
    
    public BufferedImage playerImage(int id) {
        return playerImages[id];
    } 

}