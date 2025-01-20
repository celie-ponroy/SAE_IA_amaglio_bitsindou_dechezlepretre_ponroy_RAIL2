import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Imagette {
    private int type;
    private int width;
    private int height;
    private double distance;
    private int[][] niveauGris;
    private Etiquette etiquette;

    public Imagette(int[][] niveauGris) {
        this.niveauGris = niveauGris;
    }

    public Imagette(int type, int width, int height, int[][] niveauGris) {
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

    public int[][] getNiveauGris() {
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

    public void save(String nom) throws IOException {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int colonnes = 0; colonnes < this.height; colonnes++) {
            for (int lignes = 0; lignes < this.width; lignes++) {
                bi.setRGB(lignes, colonnes, getColorGrayIntFromRGB(niveauGris[colonnes][lignes]));
            }
        }
        File file = new File("image/"+nom);
        ImageIO.write(bi, "PNG", file);
    }

    public static Imagette[] charger(String nom) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(nom));
        int typeFichier = dis.readInt();
        int nbImg = dis.readInt();
        int nbLignes = dis.readInt();
        int nbColonnes = dis.readInt();
        int tailleImg = nbColonnes*nbLignes;
        int[][] img = new int[nbColonnes][nbLignes];
        Imagette[] imagettes = new Imagette[nbImg];
        System.out.println(typeFichier+" "+nbImg+" "+nbLignes+" "+nbColonnes);
        for(int i = 0; i < nbImg; i++) {
            for (int colonnes = 0; colonnes < nbColonnes; colonnes++) {
                for (int lignes = 0; lignes < nbLignes; lignes++) {
                    img[colonnes][lignes] = dis.readUnsignedByte();
                }
            }
            imagettes[i] = (new Imagette(typeFichier, nbLignes, nbColonnes, img));
            System.out.println(i);
            img = new int[nbColonnes][nbLignes];
        }
        return imagettes;
    }

    public static int getColorIntFromRGB(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    public static int getColorGrayIntFromRGB(int i){
        return getColorIntFromRGB(i, i, i);
    }
}
