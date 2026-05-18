import React, { useEffect, useState } from "react";
import {
  View, Text, FlatList, ActivityIndicator, StyleSheet,
  TouchableOpacity, Alert, RefreshControl,
} from "react-native";
import { Picker } from "@react-native-picker/picker";
import api from "../api";
import { Etudiant, Departement } from "../types";

export default function EtudiantsScreen() {
  const [departements, setDepartements] = useState<Departement[]>([]);
  const [selectedDep, setSelectedDep] = useState<number | null>(null);
  const [etudiants, setEtudiants] = useState<Etudiant[]>([]);
  const [loading, setLoading] = useState(false);
  const [refreshing, setRefreshing] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    chargerDepartements();
    chargerEtudiants(null);
  }, []);

  const chargerDepartements = async () => {
    try {
      const res = await api.get<Departement[]>("/api/departements");
      setDepartements(res.data);
    } catch {
      setError("Impossible de charger les départements");
    }
  };

  const chargerEtudiants = async (depId: number | null) => {
    setLoading(true);
    setError(null);
    try {
      const url = depId
        ? `/api/etudiants/departement/${depId}`
        : "/api/etudiants";
      const res = await api.get<Etudiant[]>(url);
      setEtudiants(res.data);
    } catch {
      setError("Impossible de charger les étudiants");
    } finally {
      setLoading(false);
    }
  };

  const handleDepChange = (depId: number | null) => {
    setSelectedDep(depId);
    chargerEtudiants(depId);
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await chargerEtudiants(selectedDep);
    setRefreshing(false);
  };

  const renderEtudiant = ({ item }: { item: Etudiant }) => (
    <View style={styles.card}>
      <View style={styles.cardHeader}>
        <Text style={styles.nom}>{item.nom}</Text>
        {item.departementNom && (
          <View style={styles.badge}>
            <Text style={styles.badgeText}>{item.departementNom}</Text>
          </View>
        )}
      </View>
      <Text style={styles.cin}>CIN : {item.cin}</Text>
      <Text style={styles.info}>Date de naissance : {item.dateNaissance}</Text>
      <Text style={styles.info}>Âge : {item.age} ans</Text>
      <Text style={styles.info}>Email : {item.email}</Text>
      <Text style={styles.info}>Inscription : {item.anneePremiereInscription}</Text>
    </View>
  );

  return (
    <View style={styles.container}>
      <View style={styles.filterContainer}>
        <Text style={styles.filterLabel}>Filtrer par département :</Text>
        <View style={styles.pickerWrapper}>
          <Picker
            selectedValue={selectedDep}
            onValueChange={handleDepChange}
            style={styles.picker}>
            <Picker.Item label="Tous les départements" value={null} />
            {departements.map(d => (
              <Picker.Item key={d.id} label={d.nom} value={d.id} />
            ))}
          </Picker>
        </View>
      </View>

      {error && (
        <View style={styles.errorBox}>
          <Text style={styles.errorText}>{error}</Text>
        </View>
      )}

      {loading ? (
        <ActivityIndicator size="large" color="#1a73e8" style={{ marginTop: 40 }} />
      ) : (
        <FlatList
          data={etudiants}
          keyExtractor={item => item.id.toString()}
          renderItem={renderEtudiant}
          contentContainerStyle={styles.list}
          refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} />}
          ListEmptyComponent={
            <Text style={styles.empty}>Aucun étudiant trouvé.</Text>
          }
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: "#f0f2f5" },
  filterContainer: { backgroundColor: "#fff", padding: 16, borderBottomWidth: 1, borderBottomColor: "#e0e0e0" },
  filterLabel: { fontSize: 13, color: "#666", fontWeight: "600", marginBottom: 6 },
  pickerWrapper: { borderWidth: 1, borderColor: "#ddd", borderRadius: 8, overflow: "hidden" },
  picker: { height: 44 },
  list: { padding: 16, gap: 12 },
  card: {
    backgroundColor: "#fff", borderRadius: 12, padding: 16,
    shadowColor: "#000", shadowOpacity: 0.08, shadowRadius: 6,
    elevation: 2,
  },
  cardHeader: { flexDirection: "row", justifyContent: "space-between", alignItems: "center", marginBottom: 8 },
  nom: { fontSize: 16, fontWeight: "700", color: "#222", flex: 1 },
  cin: { fontSize: 12, color: "#888", fontFamily: "monospace", marginBottom: 6 },
  info: { fontSize: 13, color: "#555", marginTop: 2 },
  badge: { backgroundColor: "#e8f0fe", borderRadius: 12, paddingHorizontal: 10, paddingVertical: 3 },
  badgeText: { fontSize: 11, color: "#1a73e8", fontWeight: "600" },
  errorBox: { margin: 16, padding: 12, backgroundColor: "#ffebee", borderRadius: 8 },
  errorText: { color: "#d32f2f", fontSize: 13 },
  empty: { textAlign: "center", color: "#aaa", marginTop: 60, fontSize: 14 },
});
