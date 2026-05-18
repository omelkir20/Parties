import type { Metadata } from "next";
import "./globals.css";
import Link from "next/link";

export const metadata: Metadata = {
  title: "Gestion Étudiants",
  description: "Application de gestion des étudiants et des départements",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="fr">
      <body className="bg-gray-50 min-h-screen">
        <nav className="bg-blue-700 text-white shadow-md">
          <div className="max-w-6xl mx-auto px-4 py-3 flex items-center gap-6">
            <span className="font-bold text-lg">EduManager</span>
            <Link href="/etudiants" className="hover:text-blue-200 transition-colors text-sm font-medium">
              Étudiants
            </Link>
            <Link href="/departements" className="hover:text-blue-200 transition-colors text-sm font-medium">
              Départements
            </Link>
          </div>
        </nav>
        <main className="max-w-6xl mx-auto px-4 py-8">{children}</main>
      </body>
    </html>
  );
}
