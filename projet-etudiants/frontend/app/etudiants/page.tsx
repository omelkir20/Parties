import Link from "next/link";

const API = process.env.API_GATEWAY_URL || "http://localhost:8080";

interface Etudiant {
  id: number;
  cin: string;
  nom: string;
  dateNaissance: string;
  email: string;
  age: number;
  anneePremiereInscription: number;
  departementNom?: string;
}

async function getEtudiants(): Promise<Etudiant[]> {
  const res = await fetch(`${API}/api/etudiants`, { cache: "no-store" });
  if (!res.ok) throw new Error("Erreur lors du chargement des étudiants");
  return res.json();
}

export default async function EtudiantsPage() {
  let etudiants: Etudiant[] = [];
  let error: string | null = null;

  try {
    etudiants = await getEtudiants();
  } catch (e) {
    error = (e as Error).message;
  }

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-800">Étudiants</h1>
        <Link href="/etudiants/nouveau"
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors text-sm font-medium">
          + Nouvel étudiant
        </Link>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-4">
          {error}
        </div>
      )}

      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50 border-b border-gray-100">
            <tr>
              <th className="text-left px-4 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">CIN</th>
              <th className="text-left px-4 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">Nom</th>
              <th className="text-left px-4 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">Date de naissance</th>
              <th className="text-left px-4 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">Âge</th>
              <th className="text-left px-4 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">Département</th>
              <th className="text-left px-4 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">Inscription</th>
              <th className="text-left px-4 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-50">
            {etudiants.map((e) => (
              <tr key={e.id} className="hover:bg-gray-50 transition-colors">
                <td className="px-4 py-3 text-sm font-mono text-gray-600">{e.cin}</td>
                <td className="px-4 py-3 text-sm font-medium text-gray-800">{e.nom}</td>
                <td className="px-4 py-3 text-sm text-gray-600">{e.dateNaissance}</td>
                <td className="px-4 py-3 text-sm text-gray-600">{e.age} ans</td>
                <td className="px-4 py-3 text-sm">
                  {e.departementNom ? (
                    <span className="inline-block bg-blue-100 text-blue-700 text-xs px-2 py-1 rounded-full font-medium">
                      {e.departementNom}
                    </span>
                  ) : "—"}
                </td>
                <td className="px-4 py-3 text-sm text-gray-600">{e.anneePremiereInscription}</td>
                <td className="px-4 py-3">
                  <Link href={`/etudiants/${e.id}`}
                    className="text-blue-600 hover:text-blue-800 text-sm font-medium">
                    Modifier
                  </Link>
                </td>
              </tr>
            ))}
            {etudiants.length === 0 && !error && (
              <tr>
                <td colSpan={7} className="px-4 py-8 text-center text-gray-400 text-sm">
                  Aucun étudiant trouvé.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
