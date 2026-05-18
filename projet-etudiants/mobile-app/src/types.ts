export interface Etudiant {
  id: number;
  cin: string;
  nom: string;
  dateNaissance: string;
  email: string;
  age: number;
  anneePremiereInscription: number;
  departementId?: number;
  departementNom?: string;
}

export interface Departement {
  id: number;
  nom: string;
}
