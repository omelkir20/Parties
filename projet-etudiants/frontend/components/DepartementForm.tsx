"use client";

import { useState } from "react";

interface DepartementFormProps {
  initialNom?: string;
  onSubmit: (nom: string) => Promise<void>;
  onCancel?: () => void;
  submitLabel?: string;
}

export default function DepartementForm({
  initialNom = "",
  onSubmit,
  onCancel,
  submitLabel = "Créer",
}: DepartementFormProps) {
  const [nom, setNom] = useState(initialNom);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await onSubmit(nom);
      setNom("");
    } catch (err: unknown) {
      const e = err as Error;
      setError(e.message || "Erreur lors de l'enregistrement");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-3">
      {error && (
        <p className="text-sm text-red-600 bg-red-50 px-3 py-2 rounded-lg">{error}</p>
      )}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Nom du département</label>
        <input
          type="text"
          required
          value={nom}
          onChange={e => setNom(e.target.value)}
          placeholder="ex. Informatique"
          className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
        />
      </div>
      <div className="flex gap-2">
        <button type="submit" disabled={loading}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 text-sm font-medium disabled:opacity-50">
          {loading ? "..." : submitLabel}
        </button>
        {onCancel && (
          <button type="button" onClick={onCancel}
            className="bg-gray-100 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-200 text-sm font-medium">
            Annuler
          </button>
        )}
      </div>
    </form>
  );
}
