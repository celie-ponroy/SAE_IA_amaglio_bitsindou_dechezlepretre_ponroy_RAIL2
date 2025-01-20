import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Etiquette {
    private int etiquette;

    public Etiquette(int etiquette) {
        this.etiquette = etiquette;
    }

    public static Etiquette[] charger(String nom) throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(nom));
        int typeFichier = dis.readInt();
        int nbElements = dis.readInt();
        Etiquette[] etiquettes = new Etiquette[nbElements];
        System.out.println(typeFichier+" "+nbElements);
        for(int i = 0; i < nbElements; i++) {
            etiquettes[i] = (new Etiquette(dis.readUnsignedByte()));
        }
        return etiquettes;
    }

    public int getEtiquette() {
        return etiquette;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Etiquette etiquette1 = (Etiquette) o;
        return etiquette == etiquette1.etiquette;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(etiquette);
    }

    @Override
    public String toString() {
        return "Etiquette{" +
                "etiquette=" + etiquette +
                '}';
    }
}
