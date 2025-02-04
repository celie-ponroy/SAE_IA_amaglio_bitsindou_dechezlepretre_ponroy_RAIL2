import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Imagette {
    private int type;
    private int width;
    private int height;
    private double distance;
    private double[][] niveauGris;
    private Etiquette etiquette;

    public Imagette(double[][] niveauGris) {
        this.niveauGris = niveauGris;
    }

    public Imagette(int type, int width, int height, double[][] niveauGris) {
        this.type = type;
        this.width = width;
        this.height = height;
        this.niveauGris = niveauGris;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double[][] getNiveauGris() {
        return niveauGris;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Etiquette getEtiquette() {
        return etiquette;
    }

    public void setEtiquette(Etiquette etiquette) {
        this.etiquette = etiquette;
    }

    public static Imagette[] charger(String nom) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(nom));
        int typeFichier = dis.readInt();
        int nbImg = dis.readInt();
        int nbLignes = dis.readInt();
        int nbColonnes = dis.readInt();
        int tailleImg = nbColonnes*nbLignes;
        double[][] img = new double[nbColonnes][nbLignes];
        Imagette[] imagettes = new Imagette[nbImg];
        System.out.println(typeFichier+" "+nbImg+" "+nbLignes+" "+nbColonnes);
        for(int i = 0; i < nbImg; i++) {
            for (int colonnes = 0; colonnes < nbColonnes; colonnes++) {
                for (int lignes = 0; lignes < nbLignes; lignes++) {
                    img[colonnes][lignes] = (double)dis.readUnsignedByte()/255;
                }
            }
            imagettes[i] = (new Imagette(typeFichier, nbLignes, nbColonnes, img));
            System.out.println(i);
            img = new double[nbColonnes][nbLignes];
        }
        return imagettes;
    }

    public static int getColorIntFromRGB(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    public static int getColorGrayIntFromRGB(int i){
        return getColorIntFromRGB(i, i, i);
    }
    public static Imagette[] melanger(Imagette[] tab){
        List<Imagette> list = new ArrayList<>(Arrays.stream(tab).toList());
        Collections.shuffle(list);
        Imagette[] res = new Imagette[list.size()];
        for(int i = 0; i < res.length; i++){
            res[i] = list.get(i);
        }
        return res;
    }
}
