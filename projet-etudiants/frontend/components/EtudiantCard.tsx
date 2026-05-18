"use client";

interface EtudiantCardProps {
  id: number;
  cin: string;
  nom: string;
  dateNaissance: string;
  age: number;
  email: string;
  departementNom?: string;
  onEdit?: () => void;
  onDelete?: () => void;
}

export default function EtudiantCard({
  cin, nom, dateNaissance, age, email, departementNom, onEdit, onDelete,
}: EtudiantCardProps) {
  return (
    <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-4 hover:shadow-md transition-shadow">
      <div className="flex justify-between items-start">
        <div>
          <p className="font-semibold text-gray-800 text-base">{nom}</p>
          <p className="text-xs font-mono text-gray-400 mt-0.5">{cin}</p>
        </div>
        {departementNom && (
          <span className="bg-blue-100 text-blue-700 text-xs px-2 py-1 rounded-full font-medium">
            {departementNom}
          </span>
        )}
      </div>
      <div className="mt-3 space-y-1 text-sm text-gray-600">
        <p><span className="text-gray-400">Email :</span> {email}</p>
        <p><span className="text-gray-400">Né le :</span> {dateNaissance} <span className="text-gray-400">({age} ans)</span></p>
      </div>
      {(onEdit || onDelete) && (
        <div className="mt-3 flex gap-2 pt-3 border-t border-gray-50">
          {onEdit && (
            <button onClick={onEdit}
              className="text-blue-600 hover:text-blue-800 text-xs font-medium">
              Modifier
            </button>
          )}
          {onDelete && (
            <button onClick={onDelete}
              className="text-red-500 hover:text-red-700 text-xs font-medium">
              Supprimer
            </button>
          )}
        </div>
      )}
    </div>
  );
}
