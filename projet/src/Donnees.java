public class Donnees {
    private Imagette[] imagettes;

    public Donnees(Imagette[] imgs) {
        this.imagettes = imgs;
    }

    public void etiquetter(Etiquette[] etiquette) {
        for(int i = 0; i < imagettes.length; i++) {
            this.imagettes[i].setEtiquette(etiquette[i]);
        }
    }

    public Imagette getImagette(int id) {
        return imagettes[id];
    }

    public Imagette[] getImagettes() {
        return imagettes;
    }
}
