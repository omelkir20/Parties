"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import axios from "axios";

const API = process.env.NEXT_PUBLIC_API_GATEWAY_URL || "http://localhost:8080";

interface Departement {
  id: number;
  nom: string;
}

interface EtudiantForm {
  cin: string;
  nom: string;
  dateNaissance: string;
  email: string;
  anneePremiereInscription: number;
  departementId: number | null;
}

export default function EtudiantDetailPage() {
  const params = useParams();
  const router = useRouter();
  const isNew = params.id === "nouveau";

  const [form, setForm] = useState<EtudiantForm>({
    cin: "", nom: "", dateNaissance: "", email: "",
    anneePremiereInscription: new Date().getFullYear(),
    departementId: null,
  });
  const [departements, setDepartements] = useState<Departement[]>([]);
  const [loading, setLoading] = useState(!isNew);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    axios.get(`${API}/api/departements`).then(r => setDepartements(r.data));
    if (!isNew) {
      axios.get(`${API}/api/etudiants/${params.id}`)
        .then(r => {
          const e = r.data;
          setForm({
            cin: e.cin, nom: e.nom, dateNaissance: e.dateNaissance,
            email: e.email, anneePremiereInscription: e.anneePremiereInscription,
            departementId: e.departementId || null,
          });
        })
        .catch(() => setError("Étudiant non trouvé"))
        .finally(() => setLoading(false));
    }
  }, [params.id, isNew]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setError(null);
    try {
      if (isNew) {
        await axios.post(`${API}/api/etudiants`, form);
      } else {
        await axios.put(`${API}/api/etudiants/${params.id}`, form);
      }
      router.push("/etudiants");
    } catch (err: unknown) {
      const axiosErr = err as { response?: { data?: { message?: string } } };
      setError(axiosErr.response?.data?.message || "Une erreur est survenue");
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    if (!confirm("Confirmer la suppression ?")) return;
    await axios.delete(`${API}/api/etudiants/${params.id}`);
    router.push("/etudiants");
  };

  if (loading) return <div className="text-center py-10 text-gray-400">Chargement...</div>;

  return (
    <div className="max-w-xl mx-auto">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">
        {isNew ? "Nouvel étudiant" : "Modifier l'étudiant"}
      </h1>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-4 text-sm">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 space-y-4">
        {(["cin", "nom", "email"] as const).map(field => (
          <div key={field}>
            <label className="block text-sm font-medium text-gray-700 mb-1 capitalize">{field}</label>
            <input type={field === "email" ? "email" : "text"} required value={form[field]}
              onChange={e => setForm(f => ({ ...f, [field]: e.target.value }))}
              className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-400"/>
          </div>
        ))}

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Date de naissance</label>
          <input type="date" required value={form.dateNaissance}
            onChange={e => setForm(f => ({ ...f, dateNaissance: e.target.value }))}
            className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-400"/>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Année d'inscription</label>
          <input type="number" required value={form.anneePremiereInscription}
            onChange={e => setForm(f => ({ ...f, anneePremiereInscription: parseInt(e.target.value) }))}
            className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-400"/>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Département</label>
          <select value={form.departementId ?? ""}
            onChange={e => setForm(f => ({ ...f, departementId: e.target.value ? parseInt(e.target.value) : null }))}
            className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-400">
            <option value="">-- Sélectionner --</option>
            {departements.map(d => (
              <option key={d.id} value={d.id}>{d.nom}</option>
            ))}
          </select>
        </div>

        <div className="flex gap-3 pt-2">
          <button type="submit" disabled={saving}
            className="bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700 transition-colors text-sm font-medium disabled:opacity-50">
            {saving ? "Enregistrement..." : "Enregistrer"}
          </button>
          <button type="button" onClick={() => router.push("/etudiants")}
            className="bg-gray-100 text-gray-700 px-5 py-2 rounded-lg hover:bg-gray-200 transition-colors text-sm font-medium">
            Annuler
          </button>
          {!isNew && (
            <button type="button" onClick={handleDelete}
              className="ml-auto bg-red-50 text-red-600 px-5 py-2 rounded-lg hover:bg-red-100 transition-colors text-sm font-medium">
              Supprimer
            </button>
          )}
        </div>
      </form>
    </div>
  );
}
