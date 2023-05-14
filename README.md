# **PROJECT NEWSUPERCHESSDELUXEDEFINITIVEEDITION++**

Notre code a été compilé avec Java 17. Voici comment le faire fonctionner.

## **Prérequis**

- Java 17 installé sur votre machine
- Git pour cloner notre dépôt

## **Installation**

1. Clonez le dépôt git sur votre machine.

2. Vous avez ensuite deux options pour exécuter le projet :

   a. Ouvrez IntelliJ et ouvrez le projet. Faites un clic droit puis "Run 'ChessMain.main()'" sur "ChessMain.java" qui se trouve dans le dossier "src/main/java/fr.chess.deluxe".

   b. Exécutez le fichier "chess.jar" qui se trouve dans le dossier "out/artifacts/chess_jar".

## **Création du fichier .jar**

Si vous souhaitez créer le fichier exécutable .jar, voici les étapes que nous avons suivies dans IntelliJ (vous pouvez trouver un tutoriel ici : [lien](https://www.youtube.com/watch?v=F8ahBtXkQzU&feature=youtu.be)) :

1. File -> Project Structure -> Add -> JAR -> From modules with dependencies
2. Entrez "JavaFxMain" (qui se trouve dans "src/main/java/fr.chess.deluxe") dans le champ Main Class, puis cliquez sur OK.
3. Téléchargez JavaFX17 dans le type SDK à l'adresse suivante si ce n'est pas déjà fait : [JavaFX](https://gluonhq.com/products/javafx/)
4. Ajoutez tous les fichiers dans le fichier bin de JavaFX17, puis faites "Apply" et "OK".
5. Enfin, Build -> Build Artifacts -> Build

Après avoir suivi ces étapes, le fichier "chess.jar" devrait être créé dans le dossier "out/artifacts/chess_jar".

Bonne chance et amusez-vous bien avec NewSuperChessDeluxeDefinitiveEdition++ !