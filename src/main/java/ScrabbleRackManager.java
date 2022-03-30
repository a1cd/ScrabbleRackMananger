import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static java.lang.Math.floor;
import static java.lang.Math.random;
import static java.lang.System.out;

/**
 * A way to manage a player's tiles and tile rack,
 * and display which words can be played using
 * said characters.
 * <p>
 *     extras: uses the {@code var} keyword, package-private member
 *     declarations, and the increment urinary operator, and finally,
 *     blank tiles
 * </p>
 * @author 24wilber
 * @version Mar 28, 2022
 */
public class ScrabbleRackManager {
    /**
     * the tile rack used to create words.
     * it represents the tile rack in front of the user
     * <p>extra: This is a Package-Private member declaration </p>
     */
    final ArrayList<String> tileRack = new ArrayList<>(100);
    private final ArrayList<String> tiles = new ArrayList<>(99);

    private void buildTileRack() {
        var dist = new Integer[]{2, 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
//        dist = new Integer[]{99,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        for (var i = '@'; i < 'Z'; i++)
            for (var j = 0; j < dist[i & 0x1F]; j++)
                tiles.add(String.valueOf((i=='@')?' ':i));
        // double-randomization algorithm
        Collections.shuffle(tiles);
        for (int i = 0; i < 7; i++) tileRack.add(tiles.get((int) floor(random() * 99)));
    }

    private void setupDictionary() {
        Words = new ArrayList<>();
        for (int i = 0; i < 27; i++) {
            Words.add(new ArrayList<>());
        }
    }

    /**
     * Populates the dictionary from the text file contents
     * The dictionary object should contain 26 buckets, each
     * bucket filled with an ArrayList<String>
     * The String objects in the buckets are sorted A-Z because
     * of the nature of the text file words.txt
     *
     * <p>extra: This is a Package-Private member declaration </p>
     */
    void populateDictionary() {
        {

        }
        setupDictionary();
        try {
            Scanner input = new Scanner(new File("./2019_collins_scrabble.txt"));
            while (input.hasNext()) {
                String word = input.nextLine();
                Words.get(word.charAt(0) & 0x1F).add(word);
            }
            input.close();
            for (ArrayList<String> strings : Words) Collections.sort(strings);
        } catch (FileNotFoundException e) {
            System.err.println("We cannot find the file '2019_collins_scrabble.txt' in the working directory!");
        }
    }

    private ArrayList<ArrayList<String>> Words;

    /**
     * A constructor to create a new Scrabble rack manager, builds
     * a new tile rack, and gets all the words and puts them in the
     * system in a nice way
     *
     */
    public ScrabbleRackManager() {
        Words = new ArrayList<>();
        buildTileRack();
        populateDictionary();
    }
    /**
     * displays the contents of the player's tile rack
     */
    public void printRack() {
        out.println("Letters in the rack: " + tileRack);
    }

    /**
     * With the current tile rack, is a word playable?
     * @param word the word to be tested against the tile rack
     * @return a boolean value indicating whether a word is playable
     */
    public boolean isPlayable(String word) {
        ArrayList<String> rackCopy = new ArrayList<>(tileRack);
        for (Character i: word.toCharArray())
            if (!rackCopy.remove(i.toString()))
                if (!rackCopy.remove(" ")) return false;
        return true;
    }

    /**
     * builds and returns an ArrayList of String objects that are values pulled from
     * the dictionary/database based on the available letters in the user's tile
     * rack
     * @return an ArrayList of playable words
     */
    public ArrayList<String> getPlaylist() {
        var words = new ArrayList<String>();
        for (var bucket: Words)
            if (bucket.size() != 0 && (tileRack.contains(String.valueOf(bucket.get(0).charAt(0))) || tileRack.contains(" ")))
                for (var word : bucket)
                    if (isPlayable(word))
                        words.add(word);
        return words;
    }

    /**
     * print all the playable words based on the letters in the tile rack
     */
    public void printMatches() {
        out.print("You can play the following words from the letters in your rack:");
        var plays = getPlaylist();
        out.println((plays.isEmpty())?"\nSorry, NO words can be played from those tiles.":"");
        var bingo = false;
        var times = 0.0;
        for (String word : plays) {
            /*
             I think the {@code times = ++times % 10} is pretty cool!
             it just increments times but once it reaches 10 sets it
             to 0 because `10 mod 10 = 0`
            */
            //extra: uses the urinary prefix increment operator
            for (times = ++times % 10; word.length() == 7; word += "*") bingo = true;
            out.printf("%-10s"+((times==0)?"\n":""), word);
        }
        if (bingo) out.println("\n* Denotes BINGO");
        else out.println();
    }

    /**
     * the main method with three lines displaying the program's use.
     * @param args the default main args to the program
     */
    public static void main(String[] args) {
        ScrabbleRackManager app = new ScrabbleRackManager();
        app.printRack();
        app.printMatches();
    }
}
