package org.example;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class ValidPosition {


    // Utiliser un ensemble pour stocker les positions valides
    private Set<Position> validPositions;

    public ValidPosition() {
        this.validPositions = new HashSet<>();
        // Initialiser les positions valides (peut-être avec des valeurs par défaut)
        initializeValidPositions();
    }

    // Méthode pour initialiser les positions valides
    private void initializeValidPositions() {
        // Ajouter les positions valides à l'ensemble
        // Exemple d'ajout de quelques positions valides
        validPositions.add(new Position(1, 0));
        validPositions.add(new Position(1, 1));
        validPositions.add(new Position(1, 2));
        validPositions.add(new Position(1, 3));
        validPositions.add(new Position(1, 4));
        validPositions.add(new Position(1, 5));
        validPositions.add(new Position(1, 6));
        validPositions.add(new Position(2, 6));
        validPositions.add(new Position(3, 6));
        validPositions.add(new Position(4, 6));
        validPositions.add(new Position(4, 5));
        validPositions.add(new Position(4, 4));
        validPositions.add(new Position(4, 3));
        validPositions.add(new Position(5, 3));
        validPositions.add(new Position(6, 3));
        validPositions.add(new Position(7, 3));
        validPositions.add(new Position(7, 4));
        validPositions.add(new Position(7, 5));
        validPositions.add(new Position(8, 5));
        validPositions.add(new Position(8, 6));
        validPositions.add(new Position(8, 7));
        validPositions.add(new Position(8, 8));
        validPositions.add(new Position(8, 9));

    }

    // Méthode pour vérifier si une position donnée est valide
    public boolean isValidPosition(Position position) {
        return validPositions.contains(position);
    }
}
