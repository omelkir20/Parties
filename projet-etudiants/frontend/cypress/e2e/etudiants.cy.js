describe('Gestion des etudiants', () => {
  beforeEach(() => {
    cy.visit('/etudiants');
  });

  it('affiche la page liste des etudiants', () => {
    cy.contains('Etudiants').should('be.visible');
    cy.get('table').should('exist');
  });

  it('affiche au moins un etudiant', () => {
    cy.get('tbody tr').should('have.length.greaterThan', 0);
  });

  it('navigue vers le formulaire de creation', () => {
    cy.contains('Nouvel etudiant').click();
    cy.url().should('include', '/etudiants/nouveau');
    cy.get('form').should('be.visible');
  });

  it('cree un nouvel etudiant et le retrouve dans la liste', () => {
    cy.visit('/etudiants/nouveau');
    cy.get('input[type="text"]').first().type('CIN999');
    cy.get('input').eq(1).type('Test Cypress');
    cy.get('input[type="email"]').type('cypress@test.com');
    cy.get('input[type="date"]').type('2000-01-01');
    cy.get('input[type="number"]').type('2022');
    cy.get('button[type="submit"]').click();
    cy.url().should('include', '/etudiants');
    cy.contains('Test Cypress').should('be.visible');
  });
});

describe('Gestion des departements', () => {
  it('affiche la page departements', () => {
    cy.visit('/departements');
    cy.contains('Departements').should('be.visible');
    cy.get('form').should('be.visible');
  });

  it('cree un departement', () => {
    cy.visit('/departements');
    cy.get('input[placeholder*="Informatique"]').clear().type('Physique Quantique');
    cy.get('button[type="submit"]').click();
    cy.contains('Physique Quantique').should('be.visible');
  });
});
