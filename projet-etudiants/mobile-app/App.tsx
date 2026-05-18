import React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import { StatusBar } from "expo-status-bar";
import EtudiantsScreen from "./src/screens/EtudiantsScreen";

const Stack = createStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <StatusBar style="light" />
      <Stack.Navigator
        screenOptions={{
          headerStyle: { backgroundColor: "#1a73e8" },
          headerTintColor: "#fff",
          headerTitleStyle: { fontWeight: "bold" },
        }}>
        <Stack.Screen
          name="Etudiants"
          component={EtudiantsScreen}
          options={{ title: "Gestion des Étudiants" }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
