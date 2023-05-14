# PROJECT NEWSUPERCHESSDELUXEDEFINITIVEEDITION++:

Notre code a été compilé avec Java 17 il suffit donc d'avoir Java17 sur votre machine, de cloner le dépôt git et ensuite il y a deux options:
- ouvir IntilliJ et ouvrir le projet dans IntelliJ, puis faire clic droit puis "Run 'ChessMain.main()'" sur "ChessMain.java" qui est dans le dossier
"src/main/java/fr.chess.deluxe".
- éxécuter le fichier "chess.jar" se trouvant dans le dossier "out/artifacts/chess_jar".

Pour faire le fichier écécutable .jar nous avons fait les étpaes suivantes dans IntelliJ (tutoriel trouvé ici: https://www.youtube.com/watch?v=F8ahBtXkQzU&feature=youtu.be):
- File -> Project Structure -> Add -> JAR -> From modules with dependencies
- - Puis mettre "JavaFxMain"(présent dans "src/main/java/fr.chess.deluxe") dans le champ Main Class, puis ok.
- - Télécharger JavaFX17 dans le type SDK (à l'adresse https://gluonhq.com/products/javafx/) si ce n'est pas déjà fait
- - Ajouter tous les fichiers dans le fichier bin de JavaFX17 puis faire "Apply" et "OK"
- Build -> Build Artifacts -> Build

Suite à cela ça devrait mettre le fichier "chess.jar" dans le dossier "out/artifacts/chess_jar".
