"use client";

import { useEffect, useState } from "react";
import axios from "axios";

const API = process.env.NEXT_PUBLIC_API_GATEWAY_URL || "http://localhost:8080";

interface Departement {
  id?: number;
  nom: string;
}

export default function DepartementsPage() {
  const [departements, setDepartements] = useState<Departement[]>([]);
  const [form, setForm] = useState<Departement>({ nom: "" });
  const [editId, setEditId] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  const load = () =>
    axios.get(`${API}/api/departements`)
      .then(r => { setDepartements(r.data); setLoading(false); })
      .catch(() => { setError("Impossible de charger les départements"); setLoading(false); });

  useEffect(() => { load(); }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    try {
      if (editId) {
        await axios.put(`${API}/api/departements/${editId}`, form);
      } else {
        await axios.post(`${API}/api/departements`, form);
      }
      setForm({ nom: "" });
      setEditId(null);
      load();
    } catch (err: unknown) {
      const axiosErr = err as { response?: { data?: { message?: string } } };
      setError(axiosErr.response?.data?.message || "Erreur lors de l'enregistrement");
    }
  };

  const handleEdit = (d: Departement) => {
    setForm({ nom: d.nom });
    setEditId(d.id!);
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Confirmer la suppression ?")) return;
    await axios.delete(`${API}/api/departements/${id}`);
    load();
  };

  return (
    <div>
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Départements</h1>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-4 text-sm">
          {error}
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="md:col-span-1">
          <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-5">
            <h2 className="text-base font-semibold text-gray-700 mb-4">
              {editId ? "Modifier le département" : "Nouveau département"}
            </h2>
            <form onSubmit={handleSubmit} className="space-y-3">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Nom</label>
                <input type="text" required value={form.nom}
                  onChange={e => setForm({ nom: e.target.value })}
                  placeholder="ex. Informatique"
                  className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-400"/>
              </div>
              <div className="flex gap-2">
                <button type="submit"
                  className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors text-sm font-medium">
                  {editId ? "Mettre à jour" : "Créer"}
                </button>
                {editId && (
                  <button type="button" onClick={() => { setForm({ nom: "" }); setEditId(null); }}
                    className="bg-gray-100 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-200 text-sm font-medium">
                    Annuler
                  </button>
                )}
              </div>
            </form>
          </div>
        </div>

        <div className="md:col-span-2">
          <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            {loading ? (
              <div className="py-8 text-center text-gray-400 text-sm">Chargement...</div>
            ) : (
              <table className="w-full">
                <thead className="bg-gray-50 border-b border-gray-100">
                  <tr>
                    <th className="text-left px-4 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">ID</th>
                    <th className="text-left px-4 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">Nom</th>
                    <th className="text-left px-4 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-50">
                  {departements.map(d => (
                    <tr key={d.id} className="hover:bg-gray-50 transition-colors">
                      <td className="px-4 py-3 text-sm text-gray-500">{d.id}</td>
                      <td className="px-4 py-3 text-sm font-medium text-gray-800">{d.nom}</td>
                      <td className="px-4 py-3 flex gap-3">
                        <button onClick={() => handleEdit(d)}
                          className="text-blue-600 hover:text-blue-800 text-sm font-medium">
                          Modifier
                        </button>
                        <button onClick={() => handleDelete(d.id!)}
                          className="text-red-500 hover:text-red-700 text-sm font-medium">
                          Supprimer
                        </button>
                      </td>
                    </tr>
                  ))}
                  {departements.length === 0 && (
                    <tr>
                      <td colSpan={3} className="px-4 py-8 text-center text-gray-400 text-sm">
                        Aucun département.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
