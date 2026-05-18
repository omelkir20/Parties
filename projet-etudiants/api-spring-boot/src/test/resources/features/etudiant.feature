Feature: Calcul de l'âge d'un étudiant

  Scenario: Étudiant né il y a 22 ans
    Given un étudiant avec la date de naissance "2002-04-07"
    When on calcule son âge
    Then l'âge retourné doit être supérieur ou égal à 22

  Scenario: Étudiant né en 2000
    Given un étudiant avec la date de naissance "2000-01-01"
    When on calcule son âge
    Then l'âge retourné doit être supérieur ou égal à 24

  Scenario: Étudiant né récemment
    Given un étudiant avec la date de naissance "2005-06-15"
    When on calcule son âge
    Then l'âge retourné doit être supérieur ou égal à 18
