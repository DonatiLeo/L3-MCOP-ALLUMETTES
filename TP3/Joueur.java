package fr.unice.l3.allumettes.player;

import java.util.concurrent.TimeUnit;

public class Joueur {

    private String name = "Joueur de base";

    /**
     * Change le nom du joueur
     * @param n nom
     */
    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }

    /**
     * Renvoie le nombre d'allumettes sélectionnées par le joueur à ce tour
     * On fournit au joueur l'information du nombre d'allumettes visibles
     * @param nbAllumettesVisibles le nombre d'allumettes visibles sur le plateau
     * @return
     * @throws InterruptedException
     */
    public int jouer(int nbAllumettesVisibles) throws InterruptedException {
        return 1; // Ce joueur par défaut a un comportement "naïf" et choisit toujours 1 allumette
    }

    /**
     * Met en pause l'exécution pour 1 seconde. Peut être utiliser par le Contrôleur
     * pour simuler le temps de jeu d'un tour pour un joueur IA.
     * @throws InterruptedException
     */
    public void attendre() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1000);
    }
}
