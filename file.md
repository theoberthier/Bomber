# UBomb Student 2021

### Authors
- Théo Morin-Vasseur : **theo.theo-morin-vasseur@etu.u-bordeaux.fr**
- Théo Berthier : **theo.berthier@etu.u-bordeaux.fr**

### Fonctionnalités
Toutes les fonctionnalités fonctionnent, seul le **Bonus : Faire en sorte que les monstres suivent le joueur au dernier niveau** n'a pas été réalisé.

### Spécificités des Ajouts/Modifications
- Nous avons décidé de modifier certains attributs défini en **"final"** afin de pouvoir les modifier notamment pour le changement de monde.
- Afin d'avoir une expérience de jeu optimal et que le joueur n'ait pas de temps de chargement entre les mondes nous avons choisi de charger tous les mondes au lancement de jeu.

- Nous avons fait en sorte que lorsque le joueur prend un dégat par un monstre, que celui-ci soit poussé.
- Pour un "gain" de classes nous avons décidé de traiter toutes les bombes dans la même classe mais de surcharger le constructeur selon le type de bombe.
- Afin d'avoir une StatusBar adapté aux mondes avec moins de cases nous avons décidé de réduire l'espace entre les cases de la barre au second niveau.