# Mini Projet MASI

Petit projet pédagogique Java 17 permettant de dessiner, sauvegarder et rouvrir des formes avec SQLite.

## Fonctionnalites

- Dessiner rectangle, cercle et ligne
- Choisir une couleur
- Enregistrer et ouvrir les dessins avec SQLite
- Journaliser les actions en console, fichier ou base de donnees
- Annuler la derniere action

## Design Patterns utilises

- Singleton : gestionnaire unique de connexion SQLite
- Factory : creation des formes
- Decorator : ajout de couleur/style aux formes
- Command : annulation des actions
- Strategy : choix du type de journalisation
- Observer : notification des changements du dessin

## Stack

Java 17, JavaFX, SQLite, Maven et JUnit 5.

## Lancement

Depuis le dossier du projet :

```powershell
mvn javafx:run
```

## Tests

```powershell
mvn test
```
