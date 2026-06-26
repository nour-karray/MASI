# Mini Projet MASI

Application JavaFX de dessin utilisant des Design Patterns.

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

## Execution

Depuis le dossier du projet :

```powershell
mvn clean javafx:run
```

Ou dans Eclipse : Run As > Maven build... puis goal :

```text
clean javafx:run
```
