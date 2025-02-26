CREATE TABLE User (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    role VARCHAR(255)
);

CREATE TABLE SystemeFeedback (
    idfeedback INT PRIMARY KEY AUTO_INCREMENT,
    Commentaire VARCHAR(255),
    date DATE,
    type_feedback VARCHAR(255),
    idUser INT,
    FOREIGN KEY (idUser) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Ressource (
    idresource INT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(255),
    lien VARCHAR(255)
);



CREATE TABLE ProgrammeBienEtre (
    idProgramme INT PRIMARY KEY AUTO_INCREMENT,
    titre VARCHAR(255),
    type VARCHAR(255),
    description VARCHAR(255),
    idUser INT,
    FOREIGN KEY (idUser) REFERENCES User(id) ON DELETE CASCADE
);



CREATE TABLE Quiz (
    idQuiz INT PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE Score (
    idQuiz INT,
    idUser INT,
    score DOUBLE,
    PRIMARY KEY (idQuiz, idUser),
    FOREIGN KEY (idQuiz) REFERENCES Quiz(idQuiz) ON DELETE CASCADE,
    FOREIGN KEY (idUser) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Question (
    idQuestion INT PRIMARY KEY AUTO_INCREMENT,
    Question VARCHAR(255),
    bonneReponse VARCHAR(255),
    idQuiz INT,
    FOREIGN KEY (idQuiz) REFERENCES Quiz(idQuiz) ON DELETE CASCADE
);

CREATE TABLE Reponse (
    idReponse INT PRIMARY KEY AUTO_INCREMENT,
    Response VARCHAR(255),
    idQuestion INT,
    FOREIGN KEY (idQuestion) REFERENCES Question(idQuestion) ON DELETE CASCADE
);

CREATE TABLE Question_Repense (
    question_id INT NOT NULL,
    repense_id INT NOT NULL,
    PRIMARY KEY (question_id, repense_id),
    FOREIGN KEY (question_id) REFERENCES Question(idQuestion) ON DELETE CASCADE,
    FOREIGN KEY (repense_id) REFERENCES Reponse(idReponse) ON DELETE CASCADE
);

CREATE TABLE ModuledeFormation (
    idModule INT PRIMARY KEY AUTO_INCREMENT,
    titre VARCHAR(255),
    contenu VARCHAR(255),
    sujet VARCHAR(255)
);

CREATE TABLE ModuledeFormation_Quiz (
    idModule INT,
    idQuiz INT,
    PRIMARY KEY (idModule, idQuiz),
    FOREIGN KEY (idModule) REFERENCES ModuledeFormation(idModule) ON DELETE CASCADE,
    FOREIGN KEY (idQuiz) REFERENCES Quiz(idQuiz) ON DELETE CASCADE
);

CREATE TABLE Projet (
    idProjet INT PRIMARY KEY AUTO_INCREMENT,
    titre VARCHAR(255),
    Description VARCHAR(255),
    idChefProjet INT,
    FOREIGN KEY (idChefProjet) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Tache (
    idTache INT PRIMARY KEY AUTO_INCREMENT,
    titre VARCHAR(255),
    Description VARCHAR(255),
    idProjet INT,
    FOREIGN KEY (idProjet) REFERENCES Projet(idProjet) ON DELETE CASCADE,
    idUser INT,
    FOREIGN KEY (idUser) REFERENCES User(id) ON DELETE CASCADE
);