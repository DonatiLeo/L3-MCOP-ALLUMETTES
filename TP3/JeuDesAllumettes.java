package fr.unice.l3.allumettes.engine;

import fr.unice.l3.allumettes.player.Joueur;

public class JeuDesAllumettes {
    // État du jeu

    // - Les joueurs
    Joueur[] joueurs = new Joueur[2];
    int joueurCourant = -1;
    int joueurGagnant = -1;

    // - Le plateau de jeu (les allumettes)
    protected static final int NB_PAR_DEFAUT = 21;
    private int nombreAllumettesTotal = NB_PAR_DEFAUT;
    private int nombreAllumettesVisible = NB_PAR_DEFAUT;

    // Constructeur : on passe en argument le nombre désiré d'allumettes pour le jeu
    public JeuDesAllumettes(int nombreAllumettesTotal) {
        this.nombreAllumettesTotal = nombreAllumettesTotal;
    }

    /////////////////// Méthodes pour définir les joueurs ///////////////////

    /**
     * Ajoute un joueur à la partie
     * @param j  le joueur à ajouter
     * @return   true si succès, false si la partie est déjà complète
     */
    public boolean ajouterJoueur(Joueur j)
    {
        if (partieComplete())
            return false;

        if (joueurs[0] == null) joueurs[0] = j;
        else joueurs[1] = j;
        return true;
    }

    /**
     * Renvoie true si la partie est complète
     * @return
     */
    public boolean partieComplete()  // pour savoir s’il y a suffisamment de joueurs à la partie
    {
        return ((joueurs[0] != null) && (joueurs[1] != null));
    }

    /////////////////// Méthodes pour contrôler la partie ///////////////////

    /**
     * Réinitialise la partie de jeu d'allumettes
     */
    public void démarrerNouvellePartie()
    {
        nombreAllumettesVisible = nombreAllumettesTotal;
        joueurCourant = 0;
        joueurGagnant = -1;
    }

    /**
     * Renvoie le joueur dont c'est actuellement le tour
     * @return j le joueur
     */
    public Joueur aQuiDeJouer()
    {
        Joueur j = null;
        if (partieComplete() && joueurCourant >= 0 && joueurCourant < 2)
            j = joueurs[joueurCourant];
        return j;
    }

    /**
     * Fait joueur le joueur courant et avance l'état du jeu en conséquence de ce coup
     * @return le nombre d'allumettes choisies à ce coup
     * @throws InterruptedException
     */
    public int jouerUnTour() throws InterruptedException // pour faire jouer le joueur courant, déterminer s’il y a un gagnant, sinon changer de joueur courant. Retourne le nombre d’allumettes prises
    {
        // Le joueur courant joue en indiquant un nombre d'allumettes à sélectionner
        Joueur j = aQuiDeJouer();

        if (j == null)
            return 0;

        int nbAllumettesChoisies = j.jouer(nombreAllumettesVisible);

        // On met à jour l'état du jeu
        nombreAllumettesVisible = nombreAllumettesVisible - nbAllumettesChoisies;

        // Le jeu est-il fini ?
        if (nombreAllumettesVisible > 0) {
            // Pas fini, on passe au joueur suivant
            joueurCourant = (joueurCourant + 1) % 2;
        }
        else {
            // Fini, on désigne le gagnant
            joueurGagnant = (joueurCourant + 1) % 2; // celui qui prend en dernier perd
            joueurCourant = -1;
        }

        return nbAllumettesChoisies;
    }


    /////////////////// Méthodes pour observer la partie ///////////////////

    /**
     * Renvoie le nombre total d'allumettes dans la partie
     * @return
     */
    public int getNombreAllumettesTotal()
    {
        return nombreAllumettesTotal;
    }

    /**
     * Renvoie le nombre d'alumettes encore visibles dans la partie
     * @return
     */
    public int getNombreAllumettesVisible() // pour savoir combien il en reste
    {
        return nombreAllumettesVisible;
    }

    /**
     * Renvoie le gagnant de la partie, ou null s'il n'y en a pas encore
     * @return
     */
    public Joueur getGagnant() // pour savoir qui est le gagnant. Retourne null s’il n’y en a pas (encore)
    {
        if (joueurGagnant > -1)
            return joueurs[joueurGagnant];
        return null;
    }

    /**
     * Renvoie true si une partie est en cours
     * @return
     */
    public boolean partieEnCours() {
        return getGagnant() == null;
    }

}
