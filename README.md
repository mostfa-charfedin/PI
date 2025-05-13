
# Onboardify - Application JavaFX d’Onboarding et de Gestion de Projets

## 📝 Présentation

**Onboardify** est une application JavaFX développée dans le cadre du module **PIDEV 3A** à **Esprit School of Engineering**.  
Elle vise à optimiser l'intégration des nouveaux employés grâce à un onboarding **gamifié**, tout en assurant une gestion efficace des **projets**, des **réclamations** et des **interactions utilisateurs**.

---

## 🚀 Fonctionnalités principales

- Gestion des utilisateurs
- Gestion des réclamations et des publications
- Gestion des quiz
- Gestion des parcours d’intégration
- Gestion des programmes bien-être
- Gestion des ressources et des évaluations

---

## 🛠️ Stack technique

- **Langage** : Java 17+
- **Framework** : JavaFX
- **Base de données** : MySQL
- **Interface graphique** : FXML (SceneBuilder), CSS
- **Outils** : IntelliJ IDEA, SceneBuilder, JDBC, Git

---

## 📁 Structure du projet

```
onboardify-javafx/
├── src/
│   ├── controller/
│   ├── entity/
│   ├── service/
│   ├── utils/
│   ├── view/
│   │   └── *.fxml
│   └── Main.java
├── resources/
│   ├── styles/
│   ├── images/
│   └── i18n/
├── lib/
├── .gitignore
└── README.md
```

---

## ▶️ Installation et démarrage

1. **Cloner le projet :**
   ```bash
   git clone https://github.com/votre-utilisateur/onboardify-javafx.git
   cd onboardify-javafx
   ```

2. **Ouvrir le projet** avec IntelliJ IDEA ou un IDE compatible.

3. **Ajouter les dépendances nécessaires** (ex: MySQL JDBC Driver) dans le dossier `lib/` et les inclure dans le module.

4. **Configurer la base de données** dans `DBConnection.java` :
   ```java
   String url = "jdbc:mysql://localhost:3306/onboardify";
   String user = "root";
   String password = "votre_mot_de_passe";
   ```

5. **Exécuter la classe** `Main.java`.

---

## 🏷️ Mots-clés GitHub

`javafx`, `java`, `onboarding`, `reclamations`, `pidev`, `esprit`, `project-management`, `education`, `desktop-app`

---

## 🙏 Remerciements

Projet réalisé sous la supervision des enseignants encadrants dans le cadre du module **PIDEV 3A** à **Esprit School of Engineering**.  
Merci à l’équipe **CodeRockers** pour leur collaboration et leur engagement.

---

## 🔑 Mots-clés

JavaFX, Java, Onboarding, Gamification, Gestion de projets, Réclamations, Esprit School of Engineering, Application Desktop
