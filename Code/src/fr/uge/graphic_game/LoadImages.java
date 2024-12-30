package fr.uge.graphic_game;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import fr.uge.Cascadia.Animal;
import fr.uge.Cascadia.Habitat;

public class LoadImages {

	public Map<String, Image> loadAllImages() {
		Map<String, Image> map = new HashMap<String, Image>();
		List<List<String>> liste = listOfGameImages();
		for (var lst : liste) {
			for (var elem : lst) {
				map.put(elem, LoadImage(elem));
			}
		}
		return map;
	}

	public static List<List<String>> listOfGameImages() {
		List<String> animals = Stream.of(Animal.values()).map(Enum::name).collect(Collectors.toList());
		List<String> habitats = Stream.of(Habitat.values()).map(Enum::name).collect(Collectors.toList());
		return List.of(animals, habitats);
	}

	public static Image LoadImage(String name) {
		String path = "images\\";
		var userDir = System.getProperty("user.dir");
		Path pathDir = Path.of(userDir).resolve(path);

		File input = new File(pathDir.toString() + "\\" + name + ".png");
		System.out.println("--->" + pathDir.toString() + "\\" + name + ".png");
		try {
			return ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
