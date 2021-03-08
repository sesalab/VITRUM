package data;

public class ClassCKInfo {

    private int loc;
    private int rfc;
    private int nom;
    private int wmc;

    public ClassCKInfo (int loc, int rfc, int nom, int wmc) {
        this.loc = loc;
        this.rfc = rfc;
        this.nom = nom;
        this.wmc = wmc;
    }

    public ClassCKInfo(){

    }



    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public int getRfc() {
        return rfc;
    }

    public void setRfc(int rfc) {
        this.rfc = rfc;
    }

    public int getNom() {
        return nom;
    }

    public void setNom(int nom) {
        this.nom = nom;
    }

    public int getWmc() {
        return wmc;
    }

    public void setWmc(int wmc) {
        this.wmc = wmc;
    }


    @Override
    public String toString() {
        return "ClassCKInfo{" +
                "loc=" + loc +
                ", rfc=" + rfc +
                ", nom=" + nom +
                ", wmc=" + wmc +
                '}';
    }
}
