package src.Logic;

public interface SlotMaschineObserver {
    public void updateSpielErgebnis(int symbol1, int symbol2, int symbol3, double gewinn);
    public void fehler(String fehlermeldung);
}
