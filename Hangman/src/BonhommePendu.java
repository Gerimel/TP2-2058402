import java.util.*;

//Nom du programme.
public class BonhommePendu {

    //Enlève la couleur.
    public static final String ANSI_RESET = "\u001b[0m";

    //Couleur rouge.
    public static final String ANSI_RED = "\u001b[31m";

    //Couleur bleu.
    public static final String ANSI_BLUE = "\u001b[34m";

    //
    public static void main(String[] args) {

        // Créez un objet Scanner pour lire l'entrée standard (clavier).
        Scanner stdin = new Scanner(System.in);
        // Déclarez une variable pour déterminer si une nouvelle partie doit être jouée.
        boolean nouvellePartie;

        // Utilisez une boucle "do-while" pour permettre au joueur de jouer plusieurs parties.
        do {
            // Appelez la fonction validerRejouer pour demander à l'utilisateur s'il souhaite rejouer.
            nouvellePartie = validerRejouer(stdin);
        }
        while (nouvellePartie);// La boucle continue tant que nouvellePartie est vrai (true).

        // Fermez le Scanner après avoir terminé la saisie.
        stdin.close();

        // Affichez un message de remerciement à la console losreque le joueur a terminé de jouer.
        System.out.println("Merci d'avoir joué !");
    }

    public static boolean validerRejouer(Scanner stdin) {
        // Liste de mots secrets.
        String[] secretWordsList = {"algorithme", "boucle", "variable", "fonction", "condition", "classe",
                "objet", "debogage", "compilation", "syntaxe", "instruction", "bibliotheque", "developpeur",
                "constante", "norme", "scanner", "switch", "exception", "string", "tableaux"};

        // Constantes et compteurs
        int maxBadGuesses = 7;// Nombre maximum d'erreurs autorisées.
        int nbEssaye = 0;// Compteur du nombre d'essais.
        int nbPartie = 0;// Compteur du nombre de parties jouées.
        int win = 0;// Compteur du nombre de victoires.



        // Déclaration et initialisation de tableaux et variables
        String secretWord = motAleatoire(secretWordsList);// Sélection d'un mot secret aléatoire.
        char[] correctLetters = creerEtRemplir(secretWord.length(), '_');// Création d'un tableau pour les lettres correctement devinées.
        char[] allLettersCount = new char[0];// Tableau pour toutes les lettres déjà essayées.
        char[] entreDuJoueur = new char[nbEssaye];// Tableau pour les entrées du joueur (lettres essayées).
        int badGuessesCount = 0;// Compteur du nombre de mauvais essais.

        copieTableau(entreDuJoueur, allLettersCount);// Copie les éléments du tableau entreDuJoueur dans le tableau allLettersCount

        // Boucle principale du jeu
        while (!gameWon(correctLetters) && badGuessesCount < maxBadGuesses) {
            // Affichage du dessin du pendu en fonction du nombre d'erreurs.
            dessiner(badGuessesCount);

            // Affichage des lettres correctement devinées dans le mot secret.
            afficherTableau(correctLetters);

            // Affichage des lettres déjà essayées par le joueur.
            if (nbEssaye >= 1) {
                System.out.print("Lettres essayées : ");
                for (int i = 0; i < nbEssaye; i++) {
                    if (allLettersCount[i] != '\0') {
                        System.out.print(allLettersCount[i] + " ");// Affiche les lettres déjà essayées suivies d'un espace.
                    }
                }
            }

            // Saisie et lecture de la prochaine lettre proposée par le joueur.
            System.out.print("\n\n-> Entrez une lettre : ");
            char guess = 0;// Initialise la variable guess pour stocker la lettre devinée.

            try {
                String input = stdin.nextLine();// Lit l'entrée du joueur.
                if (!input.isEmpty() && input.length() == 1) {
                    guess = input.charAt(0);// Stocke la première lettre de l'entrée dans la variable guess.
                    if (allLettersCount.length > 0){
                        allLettersCount = ajouterElement(allLettersCount, guess);// Ajoute la lettre à la liste des lettres déjà essayées.
                    } else {
                        allLettersCount = new char[]{guess};// Crée une nouvelle liste de lettres essayées si elle est vide.
                    }
                    System.out.println("Lettres essayées : " + Arrays.toString(allLettersCount));// Affiche les lettres déjà essayées.
                }
            }
            catch (StringIndexOutOfBoundsException e) {
                // Capture une exception si l'entrée ne contient pas une seule lettre.
                System.out.println(ANSI_RED + "Caractère invalide" + ANSI_RESET);// Affiche un message d'erreur.
                stdin.nextLine();// Vide la ligne d'entrée incorrecte.
            }

            // ... La boucle continue de gérer les entrées, les vérifications et le déroulement du jeu.

            boolean found = estDansTableau(secretWord, guess);// Vérifie si la lettre devinée est dans le mot secret.

            remplacer(correctLetters, secretWord, guess);// Remplace les occurrences de la lettre devinée dans le mot secret par le caractère correct.

            if (guess != 0) {
                found = estDansTableau(secretWord, guess);// Vérifie à nouveau si la lettre devinée est dans le mot secret.
                remplacer(correctLetters, secretWord, guess);// Remplace à nouveau les occurrences de la lettre devinée dans le mot secret.

            }

            if (guess >= 'a' && guess <= 'z') {
                // Vérifie si la lettre devinée (guess) est une lettre minuscule valide.
                if (found) {
                    // Si la lettre devinée est trouvée dans le mot secret.
                    System.out.println(ANSI_BLUE + "Trouvé" + ANSI_RESET);
                    nbEssaye++;// Incrémente le nombre d'essais.
                } else {
                    // Si la lettre devinée n'est pas trouvée dans le mot secret.
                    badGuessesCount++;// Incrémente le compteur des lettres incorrectes.
                    nbEssaye++;// Incrémente le nombre d'essais.
                    System.out.println(ANSI_RED + "Non" + ANSI_RESET);
                }
            }else if (guess == '\r') {
                // Si le caractère deviné est un retour à la ligne (Enter).
                // Cela peut se produire si l'utilisateur appuie simplement sur "Entrée" sans entrer de lettre.
            } else {
                // Si le caractère deviné n'est pas une lettre minuscule valide.
                System.out.println(ANSI_RED + "Caractère invalide" + ANSI_RESET);
                nbEssaye++;// Incrémente le nombre d'essais.
            }
        }



        // Message de fin de partie
        System.out.println("\n********************************************");
        if (gameWon(correctLetters)) {
            // Si le joueur a gagné la partie (toutes les lettres devinées).
            System.out.println(ANSI_BLUE + "Trouvé" + ANSI_RESET);// Affiche "Trouvé" en bleu.
            dessiner(badGuessesCount);// Affiche l'état final du dessin du pendu.
            System.out.println("-> Bravo vous avez gagné!");// Affiche un message de victoire.
            win++;// Incrémente le compteur de victoires.
        } else {
            // Si le joueur a perdu la partie (toutes les tentatives épuisées).
            System.out.println(ANSI_RED + "Non" + ANSI_RESET);// Affiche "Non" en rouge.
            System.out.println("Vous avez perdu, le mot était : " + secretWord);// Affiche le mot secret.
            dessiner(badGuessesCount);// Affiche l'état final du dessin du pendu.
        }

        nbPartie++;// Incrémente le compteur du nombre de parties jouées.

        // Calcule le pourcentage de parties gagnées et l'affiche.
        double pourcentageGagne = ((double) win / nbPartie) * 100.0;
        System.out.println(ANSI_BLUE + "Pourcentage de parties gagnées: " + pourcentageGagne + "%" + ANSI_RESET);



        // Demande au joueur s'il souhaite rejouer.
        System.out.print("Voulez-vous rejouer (o/n) ? ");// Invite le joueur à répondre "o" (oui) ou "n" (non).
        String playAgainResponse = stdin.nextLine().toLowerCase();// Lit la réponse du joueur et la convertit en minuscules.

        while (!playAgainResponse.equals("o") && !playAgainResponse.equals("n")) {
            // Tant que la réponse n'est ni "o" (oui) ni "n" (non).
            System.out.print("Voulez-vous rejouer (o/n) ? ");// Invite le joueur à répondre à nouveau.
            playAgainResponse = stdin.nextLine().toLowerCase();// Lit la nouvelle réponse du joueur et la convertit en minuscules.
        }

        // Retourne true si le joueur veut rejouer (a répondu "o"), sinon retourne false (a répondu "n").
        return playAgainResponse.equals("o");
    }

    /**
     * Sélectionne aléatoirement un mot secret parmi une liste de mots.
     *
     * @param secretWordsList Une liste de mots parmi lesquels choisir un mot secret.
     * @return Le mot secret sélectionné de manière aléatoire.
     */
    public static String motAleatoire(String[] secretWordsList) {
        Random random = new Random();// Initialise un générateur de nombres aléatoires.
        int index = random.nextInt(secretWordsList.length);// Génère un index aléatoire dans la plage de la liste.
        return secretWordsList[index];// Renvoie le mot secret sélectionné de manière aléatoire.
    }

    /**
     * Vérifie si le joueur a gagné la partie en ayant correctement deviné toutes les lettres du mot secret.
     *
     * @param correctLetters Un tableau de caractères représentant les lettres correctement devinées.
     * @return true si toutes les lettres du mot secret ont été correctement devinées, sinon false.
     */
    public static boolean gameWon(char[] correctLetters) {
        for (char letter : correctLetters) {
            if (letter == '_') {
                return false;// S'il y a encore au moins une lettre non devinée, le jeu n'est pas gagné.
            }
        }
        return true;// Si toutes les lettres ont été correctement devinées, le jeu est gagné.
    }

    /**
     * Affiche un dessin du pendu en fonction du nombre d'erreurs (mauvaises devinettes) du joueur.
     *
     * @param badGuessesCount Le nombre d'erreurs du joueur.
     */
    public static void dessiner(int badGuessesCount) {
        int poleLines = 6;// Le nombre de lignes pour le poteau de pendaison.
        System.out.println("  ____");
        System.out.println("  |  |");

        if (badGuessesCount == 7) {
            System.out.println("  |  |");
            System.out.println("  |  |");
        }
        if (badGuessesCount > 0) {
            System.out.println("  |  O");
            poleLines = 5;
        }
        if (badGuessesCount > 1) {
            poleLines = 4;
            if (badGuessesCount == 2) {
                System.out.println("  |  |");
            } else if (badGuessesCount == 3) {
                System.out.println("  | \\|");
            } else if (badGuessesCount >= 4) {
                System.out.println("  | \\|/");
            }
        }
        if (badGuessesCount > 4) {
            poleLines = 3;
            System.out.println("  |  |");
        }
        if (badGuessesCount == 6) {
            poleLines = 2;
            System.out.println("  | /");
        } else if (badGuessesCount >= 7) {
            poleLines = 0;
            System.out.println("  | / \\");
        }
        for (int k = 0; k < poleLines; k++) {
            System.out.println("  |");
        }
        System.out.println("__|__");
        System.out.println();
    }

    /**
     * Remplace les occurrences d'une lettre spécifique dans un tableau par la lettre correcte.
     *
     * @param tableau Un tableau de caractères à mettre à jour.
     * @param mot Le mot secret dans lequel rechercher les occurrences de la lettre.
     * @param lettre La lettre à insérer dans le tableau en remplacement des occurrences trouvées.
     */
    public static void remplacer(char[] tableau, String mot, char lettre) {
        for (int i = 0; i < mot.length(); i++) {
            if (mot.charAt(i) == lettre) {
                tableau[i] = lettre;// Remplace les occurrences de la lettre dans le tableau par la lettre correcte.
            }
        }
    }

    /**
     * Affiche le mot secret masqué en utilisant un tableau de caractères.
     *
     * @param tableau Un tableau de caractères représentant le mot secret avec des lettres correctement devinées visibles.
     */
    public static void afficherTableau(char[] tableau) {
        System.out.println("Mot secret: " + new String(tableau));
    }

    /**
     * Vérifie si une lettre donnée est présente dans un mot secret.
     *
     * @param mot Le mot secret dans lequel rechercher la lettre.
     * @param lettre La lettre à vérifier si elle est présente dans le mot secret.
     * @return true si la lettre est présente dans le mot secret, sinon false.
     */
    public static boolean estDansTableau(String mot, char lettre) {
        for (int i = 0; i < mot.length(); i++) {
            if (mot.charAt(i) == lettre) {
                return true;// Si la lettre est trouvée dans le mot secret, retourne true.
            }
        }
        return false;// Si la lettre n'est pas trouvée dans le mot secret, retourne false.
    }

    /**
     * Crée un tableau de caractères de la longueur spécifiée et le remplit avec le caractère donné.
     *
     * @param longueur La longueur du tableau à créer.
     * @param caractere Le caractère avec lequel remplir le tableau.
     * @return Un tableau de caractères rempli du caractère spécifié.
     */
    public static char[] creerEtRemplir(int longueur, char caractere) {
        char[] correctletters = new char[longueur];// Crée un tableau de caractères de la longueur spécifiée.
        Arrays.fill(correctletters, caractere);// Remplit le tableau avec le caractère donné.
        return correctletters;// Renvoie le tableau rempli du caractère spécifié.
    }

    /**
     * Ajoute un nouvel élément à un tableau de caractères existant.
     *
     * @param tableau Le tableau auquel ajouter un nouvel élément.
     * @param nouvelElement Le caractère à ajouter au tableau.
     * @return Un nouveau tableau de caractères avec le nouvel élément ajouté à la fin.
     */
    public static char[] ajouterElement(char[] tableau, char nouvelElement) {
        int longueur = tableau.length;// Obtient la longueur actuelle du tableau.
        char[] nouveauTableau = Arrays.copyOf(tableau, longueur + 1);// Crée un nouveau tableau de caractères d'une taille plus grande.
        nouveauTableau[longueur] = nouvelElement;// Ajoute le nouvel élément à la fin du tableau.
        return nouveauTableau;// Renvoie le nouveau tableau avec le nouvel élément ajouté.
    }

    /**
     * Copie les éléments d'un tableau source dans un tableau de destination.
     *
     * @param source Le tableau dont les éléments doivent être copiés.
     * @param destination Le tableau où les éléments doivent être copiés.
     * @return Le tableau de destination avec les éléments copiés depuis le tableau source.
     */
    public static char[] copieTableau(char[] source, char[] destination) {
        int lenght = Math.min(source.length, destination.length);// Détermine la longueur de la copie en fonction des tailles des tableaux.
        for (int i = 0; i < lenght; i++) {
            destination[i] = source[i];// Copie chaque élément du tableau source dans le tableau de destination.
        }
        return destination;// Renvoie le tableau de destination avec les éléments copiés depuis le tableau source.
    }
}