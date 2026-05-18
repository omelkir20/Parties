import Link from "next/link";

export default function Home() {
  return (
    <div className="text-center py-20">
      <h1 className="text-4xl font-bold text-gray-800 mb-4">Bienvenue sur EduManager</h1>
      <p className="text-gray-500 mb-8">Gérez vos étudiants et départements facilement.</p>
      <div className="flex justify-center gap-4">
        <Link href="/etudiants"
          className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors font-medium">
          Gérer les étudiants
        </Link>
        <Link href="/departements"
          className="bg-gray-200 text-gray-800 px-6 py-3 rounded-lg hover:bg-gray-300 transition-colors font-medium">
          Gérer les départements
        </Link>
      </div>
    </div>
  );
}
