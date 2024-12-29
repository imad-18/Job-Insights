package data_analyse;

// Déclaration de la classe Pair avec deux paramètres génériques T et U
public class Pair<T, U> {

    // Membres pour stocker les deux éléments
    private T first;  // Le premier élément de la paire
    private U second; // Le second élément de la paire

    // Constructeur pour initialiser les deux éléments
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    // Méthode pour obtenir le premier élément
    public T getFirst() {
        return first;
    }

    // Méthode pour obtenir le second élément
    public U getSecond() {
        return second;
    }

    // Méthode pour afficher les éléments de la paire
    public void printPair() {
        System.out.println("First: " + first + ", Second: " + second);
    }

    // Méthode pour définir le premier élément
    public void setFirst(T first) {
        this.first = first;
    }

    // Méthode pour définir le second élément
    public void setSecond(U second) {
        this.second = second;
    }
}

